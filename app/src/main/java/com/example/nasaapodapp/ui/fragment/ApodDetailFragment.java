package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.nasaapodapp.R;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ApodDetailFragment extends Fragment {

    private ApodViewModel viewModel;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView explanationTextView;
    private TextView copyrightTextView;
    private FloatingActionButton favoriteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apod_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // инициализация UI элементов
        imageView = view.findViewById(R.id.detail_image_view);
        titleTextView = view.findViewById(R.id.detail_title_text_view);
        dateTextView = view.findViewById(R.id.detail_date_text_view);
        explanationTextView = view.findViewById(R.id.detail_explanation_text_view);
        copyrightTextView = view.findViewById(R.id.detail_copyright_text_view);
        favoriteButton = view.findViewById(R.id.favorite_button);

        // наблюдение за выбранным элементом
        viewModel.getSelectedApod().observe(getViewLifecycleOwner(), this::bindApodData);

        // наблюдение за статусом избранного
        viewModel.getIsFavorite().observe(getViewLifecycleOwner(), isFavorite -> {
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        });

        // обработка клика по кнопке избранное
        favoriteButton.setOnClickListener(v -> {
            viewModel.toggleFavorite();
        });

        // наблюдение за ошибками
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindApodData(ApodResponse apod) {
        if (apod == null) return;

        titleTextView.setText(apod.getTitle());
        dateTextView.setText(apod.getDate());
        explanationTextView.setText(apod.getExplanation());

        if (apod.getCopyright() != null && !apod.getCopyright().isEmpty()) {
            copyrightTextView.setVisibility(View.VISIBLE);
            copyrightTextView.setText(getString(R.string.copyright, apod.getCopyright()));
        } else {
            copyrightTextView.setVisibility(View.GONE);
        }

        Glide.with(requireContext())
                .load(apod.getUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);
    }
}