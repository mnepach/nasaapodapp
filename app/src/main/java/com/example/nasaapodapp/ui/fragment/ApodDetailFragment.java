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
    private Button takeQuizButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apod_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Initialize UI elements
        imageView = view.findViewById(R.id.detail_image_view);
        titleTextView = view.findViewById(R.id.detail_title_text_view);
        dateTextView = view.findViewById(R.id.detail_date_text_view);
        explanationTextView = view.findViewById(R.id.detail_explanation_text_view);
        copyrightTextView = view.findViewById(R.id.detail_copyright_text_view);
        favoriteButton = view.findViewById(R.id.favorite_button);
        takeQuizButton = view.findViewById(R.id.take_quiz_button);

        // Observe selected APOD
        viewModel.getSelectedApod().observe(getViewLifecycleOwner(), apod -> {
            Log.d(TAG, "Received selectedApod: " + (apod != null ? apod.getTitle() : "null"));
            bindApodData(apod);
        });

        // Observe favorite status
        viewModel.getIsFavorite().observe(getViewLifecycleOwner(), isFavorite -> {
            Log.d(TAG, "Favorite status: " + isFavorite);
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        });

        // Handle favorite button click
        favoriteButton.setOnClickListener(v -> {
            Log.d(TAG, "Favorite button clicked");
            viewModel.toggleFavorite();
        });

        // Handle take quiz button click
        takeQuizButton.setOnClickListener(v -> {
            if (viewModel.getSelectedApod().getValue() == null) {
                Log.e(TAG, "Error: selectedApod is null when trying to take quiz");
                Toast.makeText(requireContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
                return;
            }

            ApodResponse apod = viewModel.getSelectedApod().getValue();
            if (apod != null && apod.getQuizzes() != null && !apod.getQuizzes().isEmpty()) {
                Log.d(TAG, "Navigating to QuizFragment");
                Navigation.findNavController(view).navigate(R.id.action_apodDetailFragment_to_quizFragment);
            } else {
                Log.e(TAG, "Error: no quiz available for this APOD");
                Toast.makeText(requireContext(), R.string.no_quiz_available, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe errors
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e(TAG, "Error: " + errorMessage);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindApodData(ApodResponse apod) {
        if (apod == null) {
            Log.e(TAG, "Error: apod is null");
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

        Log.d(TAG, "Loading image: " + apod.getUrl());
        Glide.with(requireContext())
                .load(apod.getUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);

        // Enable take quiz button only if this APOD has quizzes
        boolean hasQuizzes = apod.getQuizzes() != null && !apod.getQuizzes().isEmpty();
        takeQuizButton.setEnabled(hasQuizzes);
    }
}