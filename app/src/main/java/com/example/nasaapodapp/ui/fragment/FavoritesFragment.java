package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nasaapodapp.R;
import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.ui.adapter.FavoritesAdapter;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;

public class FavoritesFragment extends Fragment {

    private ApodViewModel viewModel;
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Инициализация UI элементов
        recyclerView = view.findViewById(R.id.favorites_recycler_view);
        progressBar = view.findViewById(R.id.favorites_progress_bar);
        emptyView = view.findViewById(R.id.empty_view);

        // Настройка RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FavoritesAdapter();
        recyclerView.setAdapter(adapter);

        // Обработка кликов
        adapter.setOnItemClickListener(favoriteApod -> {
            // Конвертируем ApodEntity в ApodResponse для показа деталей
            ApodResponse apod = new ApodResponse();
            apod.setDate(favoriteApod.getDate());
            apod.setTitle(favoriteApod.getTitle());
            apod.setExplanation(favoriteApod.getExplanation());
            apod.setUrl(favoriteApod.getUrl());
            apod.setHdUrl(favoriteApod.getHdUrl());
            apod.setMediaType(favoriteApod.getMediaType());
            apod.setCopyright(favoriteApod.getCopyright());

            viewModel.setSelectedApod(apod);
            // Навигация к экрану деталей
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new ApodDetailFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Наблюдение за данными
        viewModel.getFavorites().observe(getViewLifecycleOwner(), favorites -> {
            adapter.setFavoritesList(favorites);
            if (favorites != null && favorites.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Загрузка избранных
        viewModel.loadFavorites();
    }
}