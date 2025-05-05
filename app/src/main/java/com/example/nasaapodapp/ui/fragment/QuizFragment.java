package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nasaapodapp.R;
import com.example.nasaapodapp.data.local.QuizEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    private ApodViewModel viewModel;
    private ImageView quizImage;
    private TextView quizQuestion;
    private RadioGroup answerGroup;
    private RadioButton answer1, answer2, answer3, answer4;
    private TextView resultText;
    private Button checkAnswerButton;
    private Button closeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Initialize UI elements
        quizImage = view.findViewById(R.id.quiz_image);
        quizQuestion = view.findViewById(R.id.quiz_question);
        answerGroup = view.findViewById(R.id.answer_group);
        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);
        answer3 = view.findViewById(R.id.answer_3);
        answer4 = view.findViewById(R.id.answer_4);
        resultText = view.findViewById(R.id.result_text);
        checkAnswerButton = view.findViewById(R.id.check_answer_button);
        closeButton = view.findViewById(R.id.close_button);

        // Load quiz data
        ApodResponse apod = viewModel.getSelectedApod().getValue();
        if (apod != null) {
            // Load APOD image
            Glide.with(requireContext())
                    .load(apod.getUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(quizImage);

            // Load quiz
            viewModel.loadQuizForApod(apod.getDate());
        }

        // Observe quiz data
        viewModel.getQuizData().observe(getViewLifecycleOwner(), this::bindQuizData);

        // Observe errors
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle check answer button
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Handle close button
        closeButton.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
    }

    private void bindQuizData(QuizEntity quiz) {
        if (quiz == null) return;

        quizQuestion.setText(quiz.getQuestion());

        // Shuffle answers
        List<String> answers = new ArrayList<>();
        answers.add(quiz.getCorrectAnswer());
        answers.add(quiz.getWrongAnswer1());
        answers.add(quiz.getWrongAnswer2());
        answers.add(quiz.getWrongAnswer3());
        Collections.shuffle(answers);

        // Set answers to radio buttons
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
    }

    private void checkAnswer() {
        int selectedId = answerGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(requireContext(), R.string.select_an_answer, Toast.LENGTH_SHORT).show();
            return;
        }

        QuizEntity quiz = viewModel.getQuizData().getValue();
        if (quiz == null) return;

        RadioButton selectedAnswer = answerGroup.findViewById(selectedId);
        String selectedText = selectedAnswer.getText().toString();
        boolean isCorrect = selectedText.equals(quiz.getCorrectAnswer());

        // Show result
        resultText.setVisibility(View.VISIBLE);
        if (isCorrect) {
            resultText.setText(R.string.correct);
            resultText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
        } else {
            resultText.setText(R.string.incorrect);
            resultText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
        }

        // Disable answer selection and show close button
        answerGroup.setEnabled(false);
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            answerGroup.getChildAt(i).setEnabled(false);
        }
        checkAnswerButton.setVisibility(View.GONE);
        closeButton.setVisibility(View.VISIBLE);
    }
}