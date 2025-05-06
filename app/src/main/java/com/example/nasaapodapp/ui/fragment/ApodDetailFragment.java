package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.nasaapodapp.R;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ApodDetailFragment extends Fragment {

    private static final String TAG = "ApodDetailFragment";
    private ApodViewModel viewModel;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView explanationTextView;
    private TextView copyrightTextView;
    private FloatingActionButton favoriteButton;
    private Button createQuizButton;
    private Button takeQuizButton;
    private Button deleteQuizButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apod_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Инициализация элементов UI
        imageView = view.findViewById(R.id.detail_image_view);
        titleTextView = view.findViewById(R.id.detail_title_text_view);
        dateTextView = view.findViewById(R.id.detail_date_text_view);
        explanationTextView = view.findViewById(R.id.detail_explanation_text_view);
        copyrightTextView = view.findViewById(R.id.detail_copyright_text_view);
        favoriteButton = view.findViewById(R.id.favorite_button);
        createQuizButton = view.findViewById(R.id.create_quiz_button);
        takeQuizButton = view.findViewById(R.id.take_quiz_button);
        deleteQuizButton = view.findViewById(R.id.delete_quiz_button);

        // Наблюдение за выбранным APOD
        viewModel.getSelectedApod().observe(getViewLifecycleOwner(), apod -> {
            Log.d(TAG, "Получен selectedApod: " + (apod != null ? apod.getTitle() : "null"));
            bindApodData(apod);
            if (apod != null) {
                // Проверка наличия теста
                viewModel.checkIfHasQuiz(apod.getDate());
            }
        });

        // Наблюдение за статусом избранного
        viewModel.getIsFavorite().observe(getViewLifecycleOwner(), isFavorite -> {
            Log.d(TAG, "Статус избранного: " + isFavorite);
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        });

        // Наблюдение за наличием теста
        viewModel.getHasQuiz().observe(getViewLifecycleOwner(), hasQuiz -> {
            Log.d(TAG, "Наличие теста: " + hasQuiz);
            createQuizButton.setText(hasQuiz ? R.string.edit_quiz : R.string.create_quiz);
            takeQuizButton.setEnabled(hasQuiz);
            deleteQuizButton.setVisibility(hasQuiz ? View.VISIBLE : View.GONE);
            if (hasQuiz) {
                Log.d(TAG, "Кнопка Take Quiz активирована");
            } else {
                Log.d(TAG, "Кнопка Take Quiz деактивирована");
            }
        });

        // Обработка кнопки избранного
        favoriteButton.setOnClickListener(v -> {
            Log.d(TAG, "Нажата кнопка избранного");
            viewModel.toggleFavorite();
        });

        // Обработка кнопки создания/редактирования теста
        createQuizButton.setOnClickListener(v -> {
            if (viewModel.getSelectedApod().getValue() == null) {
                Log.e(TAG, "Ошибка: selectedApod равен null при создании теста");
                Toast.makeText(requireContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "Навигация в CreateQuizFragment");
            Navigation.findNavController(view).navigate(R.id.action_apodDetailFragment_to_createQuizFragment);
        });

        // Обработка кнопки прохождения теста
        takeQuizButton.setOnClickListener(v -> {
            if (viewModel.getSelectedApod().getValue() == null) {
                Log.e(TAG, "Ошибка: selectedApod равен null при прохождении теста");
                Toast.makeText(requireContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!viewModel.getHasQuiz().getValue()) {
                Log.e(TAG, "Ошибка: тест отсутствует при попытке прохождения");
                Toast.makeText(requireContext(), R.string.no_quiz_available, Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "Навигация в QuizFragment");
            Navigation.findNavController(view).navigate(R.id.action_apodDetailFragment_to_quizFragment);
        });

        // Обработка кнопки удаления теста
        deleteQuizButton.setOnClickListener(v -> {
            Log.d(TAG, "Нажата кнопка удаления теста");
            viewModel.deleteQuiz();
            Toast.makeText(requireContext(), R.string.quiz_deleted, Toast.LENGTH_SHORT).show();
        });

        // Наблюдение за ошибками
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e(TAG, "Ошибка: " + errorMessage);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindApodData(ApodResponse apod) {
        if (apod == null) {
            Log.e(TAG, "Ошибка: apod равен null");
            return;
        }

        titleTextView.setText(apod.getTitle());
        dateTextView.setText(apod.getDate());
        explanationTextView.setText(apod.getExplanation());

        if (apod.getCopyright() != null && !apod.getCopyright().isEmpty()) {
            copyrightTextView.setVisibility(View.VISIBLE);
            copyrightTextView.setText(getString(R.string.copyright, apod.getCopyright()));
        } else {
            copyrightTextView.setVisibility(View.GONE);
        }

        Log.d(TAG, "Загрузка изображения: " + apod.getUrl());
        Glide.with(requireContext())
                .load(apod.getUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);
    }
}