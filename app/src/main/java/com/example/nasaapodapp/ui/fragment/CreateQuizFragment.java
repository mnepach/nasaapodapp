package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.nasaapodapp.R;
import com.example.nasaapodapp.data.local.QuizEntity;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class CreateQuizFragment extends Fragment {

    private ApodViewModel viewModel;
    private TextInputEditText questionEditText;
    private TextInputEditText correctAnswerEditText;
    private TextInputEditText wrongAnswer1EditText;
    private TextInputEditText wrongAnswer2EditText;
    private TextInputEditText wrongAnswer3EditText;
    private Button saveButton;
    private Button cancelButton;
    private boolean isEditing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Инициализация UI элементов
        questionEditText = view.findViewById(R.id.edit_question);
        correctAnswerEditText = view.findViewById(R.id.edit_correct_answer);
        wrongAnswer1EditText = view.findViewById(R.id.edit_wrong_answer_1);
        wrongAnswer2EditText = view.findViewById(R.id.edit_wrong_answer_2);
        wrongAnswer3EditText = view.findViewById(R.id.edit_wrong_answer_3);
        saveButton = view.findViewById(R.id.save_quiz_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        // Проверяем, существует ли уже викторина для этого APOD
        viewModel.getHasQuiz().observe(getViewLifecycleOwner(), hasQuiz -> {
            isEditing = hasQuiz;
            if (hasQuiz) {
                // Если викторина существует, загружаем ее данные
                viewModel.loadQuizForApod(viewModel.getSelectedApod().getValue().getDate());
            }
        });

        // Загружаем данные викторины, если она существует
        viewModel.getQuizData().observe(getViewLifecycleOwner(), quizEntity -> {
            if (quizEntity != null) {
                questionEditText.setText(quizEntity.getQuestion());
                correctAnswerEditText.setText(quizEntity.getCorrectAnswer());
                wrongAnswer1EditText.setText(quizEntity.getWrongAnswer1());
                wrongAnswer2EditText.setText(quizEntity.getWrongAnswer2());
                wrongAnswer3EditText.setText(quizEntity.getWrongAnswer3());
            }
        });

        // Обработка нажатия на кнопку сохранения
        saveButton.setOnClickListener(v -> {
            String question = questionEditText.getText().toString().trim();
            String correctAnswer = correctAnswerEditText.getText().toString().trim();
            String wrongAnswer1 = wrongAnswer1EditText.getText().toString().trim();
            String wrongAnswer2 = wrongAnswer2EditText.getText().toString().trim();
            String wrongAnswer3 = wrongAnswer3EditText.getText().toString().trim();

            // Проверка на заполнение всех полей
            if (question.isEmpty() || correctAnswer.isEmpty() ||
                    wrongAnswer1.isEmpty() || wrongAnswer2.isEmpty() || wrongAnswer3.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем викторину
            viewModel.createOrUpdateQuiz(question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3);
            Toast.makeText(requireContext(), R.string.quiz_created, Toast.LENGTH_SHORT).show();

            // Возвращаемся назад
            Navigation.findNavController(requireView()).popBackStack();
        });

        // Обработка нажатия на кнопку отмены
        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).popBackStack();
        });

        // Наблюдаем за ошибками
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}