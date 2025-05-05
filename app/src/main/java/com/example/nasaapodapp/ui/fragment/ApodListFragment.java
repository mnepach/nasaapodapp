package com.example.nasaapodapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nasaapodapp.R;
import com.example.nasaapodapp.ui.adapter.ApodAdapter;
import com.example.nasaapodapp.ui.viewmodel.ApodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ApodListFragment extends Fragment {

    private ApodViewModel viewModel;
    private RecyclerView recyclerView;
    private ApodAdapter adapter;
    private ProgressBar progressBar;
    private FloatingActionButton favoritesButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apod_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

        // Initialize UI elements
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        favoritesButton = view.findViewById(R.id.favorites_button);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ApodAdapter();
        recyclerView.setAdapter(adapter);

        // Handle clicks
        adapter.setOnItemClickListener(apod -> {
            viewModel.setSelectedApod(apod);
            Navigation.findNavController(view).navigate(R.id.action_apodListFragment_to_apodDetailFragment);
        });

        favoritesButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_apodListFragment_to_favoritesFragment);
        });

        // Observe data
        viewModel.getApodList().observe(getViewLifecycleOwner(), apodResponses -> {
            adapter.setApodList(apodResponses);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Load data
        if (viewModel.getApodList().getValue() == null || viewModel.getApodList().getValue().isEmpty()) {
            viewModel.loadApodList(20); // Load 20 entries
        }
    }
}