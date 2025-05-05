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
import com.example.nasaapodapp.data.local.QuizEntity;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";
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

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Инициализация элементов UI
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

        // Загрузка данных теста
        ApodResponse apod = viewModel.getSelectedApod().getValue();
        if (apod == null) {
            Log.e(TAG, "Ошибка: selectedApod равен null");
            Toast.makeText(requireContext(), "Ошибка: изображение не выбрано", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        // Загрузка изображения APOD
        Log.d(TAG, "Загрузка изображения для APOD: " + apod.getUrl());
        Glide.with(requireContext())
                .load(apod.getUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(quizImage);

        // Загрузка теста
        Log.d(TAG, "Загрузка теста для даты: " + apod.getDate());
        viewModel.loadQuizForApod(apod.getDate());

        // Наблюдение за данными теста
        viewModel.getQuizData().observe(getViewLifecycleOwner(), quiz -> {
            Log.d(TAG, "Получены данные теста: " + (quiz != null ? quiz.getQuestion() : "null"));
            bindQuizData(quiz);
        });

        // Наблюдение за ошибками
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e(TAG, "Ошибка: " + errorMessage);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
            }
        });

        // Обработка кнопки проверки ответа
        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        // Обработка кнопки закрытия
        closeButton.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
    }

    private void bindQuizData(QuizEntity quiz) {
        if (quiz == null) {
            Log.e(TAG, "Ошибка: QuizEntity равен null");
            Toast.makeText(requireContext(), "Ошибка: тест не найден", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).popBackStack();
            return;
        }

        quizQuestion.setText(quiz.getQuestion());

        // Перемешивание ответов
        List<String> answers = new ArrayList<>();
        answers.add(quiz.getCorrectAnswer());
        answers.add(quiz.getWrongAnswer1());
        answers.add(quiz.getWrongAnswer2());
        answers.add(quiz.getWrongAnswer3());
        Collections.shuffle(answers);

        // Установка ответов в радио-кнопки
        Log.d(TAG, "Установка ответов: " + answers);
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
        if (quiz == null) {
            Log.e(TAG, "Ошибка: QuizEntity равен null при проверке ответа");
            Toast.makeText(requireContext(), "Ошибка: тест не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedAnswer = answerGroup.findViewById(selectedId);
        String selectedText = selectedAnswer.getText().toString();
        boolean isCorrect = selectedText.equals(quiz.getCorrectAnswer());

        // Показ результата
        resultText.setVisibility(View.VISIBLE);
        if (isCorrect) {
            resultText.setText(R.string.correct);
            resultText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
        } else {
            resultText.setText(R.string.incorrect);
            resultText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
        }

        // Отключение выбора ответа и показ кнопки закрытия
        answerGroup.setEnabled(false);
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            answerGroup.getChildAt(i).setEnabled(false);
        }
        checkAnswerButton.setVisibility(View.GONE);
        closeButton.setVisibility(View.VISIBLE);
    }
}