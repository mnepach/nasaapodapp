package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateQuizFragment extends Fragment {

    private static final String TAG = "CreateQuizFragment";
    private ApodViewModel viewModel;
    private ImageView quizImage;
    private TextView quizTitle;
    private TextView quizQuestion;
    private RadioGroup answerGroup;
    private RadioButton answer1, answer2, answer3, answer4;
    private TextView resultText;
    private Button checkAnswerButton;
    private Button nextQuestionButton;
    private Button finishButton;

    private List<ApodResponse.Quiz> quizzes;
    private int currentQuizIndex = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;

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
        quizTitle = view.findViewById(R.id.quiz_title);
        quizQuestion = view.findViewById(R.id.quiz_question);
        answerGroup = view.findViewById(R.id.answer_group);
        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);
        answer3 = view.findViewById(R.id.answer_3);
        answer4 = view.findViewById(R.id.answer_4);
        resultText = view.findViewById(R.id.result_text);
        checkAnswerButton = view.findViewById(R.id.check_answer_button);
        nextQuestionButton = view.findViewById(R.id.next_question_button);
        finishButton = view.findViewById(R.id.finish_button);

        // Reset state
        resultText.setVisibility(View.GONE);
        nextQuestionButton.setVisibility(View.GONE);
        finishButton.setVisibility(View.GONE);
        currentQuizIndex = 0;
        correctAnswers = 0;

        // Load quiz data
        ApodResponse apod = viewModel.getSelectedApod().getValue();
        if (apod == null) {
            Log.e(TAG, "Error: selectedApod is null");
            Toast.makeText(requireContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        // Load APOD image
        Log.d(TAG, "Loading image for APOD: " + apod.getUrl());
        Glide.with(requireContext())
                .load(apod.getUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(quizImage);

        quizTitle.setText(apod.getTitle());

        // Get quizzes from ApodResponse
        quizzes = apod.getQuizzes();
        if (quizzes == null || quizzes.isEmpty()) {
            Log.e(TAG, "Error: no quizzes available for this APOD");
            Toast.makeText(requireContext(), R.string.no_quiz_available, Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        totalQuestions = quizzes.size();
        loadCurrentQuiz();

        // Handle check answer button click
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Handle next question button click
        nextQuestionButton.setOnClickListener(v -> {
            currentQuizIndex++;
            resetQuizUI();
            loadCurrentQuiz();
        });

        // Handle finish button click
        finishButton.setOnClickListener(v -> {
            String resultMessage = getString(R.string.quiz_result, correctAnswers, totalQuestions);
            Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show();
            Navigation.findNavController(view).popBackStack();
        });
    }

    private void loadCurrentQuiz() {
        if (currentQuizIndex >= quizzes.size()) {
            showFinalResults();
            return;
        }

        ApodResponse.Quiz currentQuiz = quizzes.get(currentQuizIndex);
        quizQuestion.setText(currentQuiz.getQuestion());

        // Get all answers and shuffle them
        List<String> answers = new ArrayList<>();
        answers.add(currentQuiz.getCorrectAnswer());
        answers.addAll(currentQuiz.getWrongAnswers());
        Collections.shuffle(answers);

        // Set answers to radio buttons
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));

        // Update question counter
        String questionCounter = getString(R.string.question_counter, currentQuizIndex + 1, totalQuestions);
        quizTitle.setText(questionCounter);
    }

    private void checkAnswer() {
        int selectedId = answerGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(requireContext(), R.string.select_an_answer, Toast.LENGTH_SHORT).show();
            return;
        }

        ApodResponse.Quiz currentQuiz = quizzes.get(currentQuizIndex);
        RadioButton selectedAnswer = answerGroup.findViewById(selectedId);
        String selectedText = selectedAnswer.getText().toString();
        boolean isCorrect = selectedText.equals(currentQuiz.getCorrectAnswer());

        // Show result
        resultText.setVisibility(View.VISIBLE);
        if (isCorrect) {
            correctAnswers++;
            resultText.setText(R.string.correct);
            resultText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
        } else {
            resultText.setText(getString(R.string.incorrect_answer, currentQuiz.getCorrectAnswer()));
            resultText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
        }

        // Disable answer selection
        disableAnswerSelection();

        // Show appropriate navigation button
        checkAnswerButton.setVisibility(View.GONE);
        if (currentQuizIndex < quizzes.size() - 1) {
            nextQuestionButton.setVisibility(View.VISIBLE);
        } else {
            finishButton.setVisibility(View.VISIBLE);
        }
    }

    private void disableAnswerSelection() {
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            answerGroup.getChildAt(i).setEnabled(false);
        }
    }

    private void resetQuizUI() {
        // Reset question UI
        resultText.setVisibility(View.GONE);
        answerGroup.clearCheck();

        // Enable answer selection
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            answerGroup.getChildAt(i).setEnabled(true);
        }

        // Reset buttons
        checkAnswerButton.setVisibility(View.VISIBLE);
        nextQuestionButton.setVisibility(View.GONE);
        finishButton.setVisibility(View.GONE);
    }

    private void showFinalResults() {
        String resultMessage = getString(R.string.quiz_result, correctAnswers, totalQuestions);
        Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show();
        Navigation.findNavController(requireView()).popBackStack();
    }
}