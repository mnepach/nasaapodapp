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
import androidx.navigation.Navigation;
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

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Initialize UI elements
        recyclerView = view.findViewById(R.id.favorites_recycler_view);
        progressBar = view.findViewById(R.id.favorites_progress_bar);
        emptyView = view.findViewById(R.id.empty_view);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FavoritesAdapter();
        recyclerView.setAdapter(adapter);

        // Handle clicks
        adapter.setOnItemClickListener(favoriteApod -> {
            // Convert ApodEntity to ApodResponse to show details
            ApodResponse apod = new ApodResponse();
            apod.setDate(favoriteApod.getDate());
            apod.setTitle(favoriteApod.getTitle());
            apod.setExplanation(favoriteApod.getExplanation());
            apod.setUrl(favoriteApod.getUrl());
            apod.setHdUrl(favoriteApod.getHdUrl());
            apod.setMediaType(favoriteApod.getMediaType());
            apod.setCopyright(favoriteApod.getCopyright());

            viewModel.setSelectedApod(apod);
            // Navigate to detail screen
            Navigation.findNavController(view).navigate(R.id.action_favoritesFragment_to_apodDetailFragment);
        });

        // Observe data
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

        // Load favorites
        viewModel.loadFavorites();
    }
}