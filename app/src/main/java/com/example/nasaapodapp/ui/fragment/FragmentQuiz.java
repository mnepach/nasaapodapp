package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nasaapodapp.R;
import com.example.nasaapodapp.data.local.QuizEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.databinding.FragmentQuizBinding;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private ApodViewModel viewModel;
    private List<String> shuffledAnswers;
    private String correctAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Get current APOD data
        ApodResponse apod = viewModel.getSelectedApod().getValue();
        if (apod != null) {
            // Load image
            loadApodImage(apod.getUrl());

            // Load quiz
            viewModel.loadQuizForApod(apod.getDate());
        }

        // Setup observers
        setupObservers();

        // Setup UI listeners
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getQuizData().observe(getViewLifecycleOwner(), quizEntity -> {
            if (quizEntity != null) {
                setupQuiz(quizEntity);
            }
        });
    }

    private void setupQuiz(QuizEntity quiz) {
        // Set quiz question
        binding.quizQuestion.setText(quiz.getQuestion());

        // Prepare and shuffle answers
        shuffledAnswers = new ArrayList<>();
        shuffledAnswers.add(quiz.getCorrectAnswer());
        shuffledAnswers.add(quiz.getWrongAnswer1());
        shuffledAnswers.add(quiz.getWrongAnswer2());
        shuffledAnswers.add(quiz.getWrongAnswer3());

        // Save correct answer for validation
        correctAnswer = quiz.getCorrectAnswer();

        // Randomize order of answers
        Collections.shuffle(shuffledAnswers);

        // Set answer options
        binding.answer1.setText(shuffledAnswers.get(0));
        binding.answer2.setText(shuffledAnswers.get(1));
        binding.answer3.setText(shuffledAnswers.get(2));
        binding.answer4.setText(shuffledAnswers.get(3));

        // Reset UI state
        binding.answerGroup.clearCheck();
        binding.resultText.setVisibility(View.GONE);
        binding.closeButton.setVisibility(View.GONE);
        binding.checkAnswerButton.setVisibility(View.VISIBLE);
    }

    private void setupListeners() {
        binding.checkAnswerButton.setOnClickListener(v -> checkAnswer());
        binding.closeButton.setOnClickListener(v -> navigateBack());
    }

    private void checkAnswer() {
        int selectedId = binding.answerGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            // No answer selected
            Toast.makeText(requireContext(), R.string.select_an_answer, Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected answer text
        RadioButton selectedButton = binding.getRoot().findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();

        // Check if answer is correct
        boolean isCorrect = selectedAnswer.equals(correctAnswer);

        // Show result
        binding.resultText.setText(isCorrect ? R.string.correct : R.string.incorrect);
        binding.resultText.setVisibility(View.VISIBLE);

        // Show close button and hide check answer button
        binding.closeButton.setVisibility(View.VISIBLE);
        binding.checkAnswerButton.setVisibility(View.GONE);
    }

    private void loadApodImage(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.quizImage);
    }

    private void navigateBack() {
        Navigation.findNavController(requireView()).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}