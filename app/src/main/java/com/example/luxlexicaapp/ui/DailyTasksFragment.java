package com.example.luxlexicaapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.luxlexicaapp.databinding.FragmentDailyTasksBinding;
import com.example.luxlexicaapp.ui.adapters.DailyTaskAdapter;

public class DailyTasksFragment extends Fragment {

    private FragmentDailyTasksBinding binding;
    private MainViewModel viewModel;
    private DailyTaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDailyTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        adapter = new DailyTaskAdapter();
        binding.rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTasks.setAdapter(adapter);

        viewModel.getDailyTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                adapter.setTasks(tasks);
            }
        });

        viewModel.updateDailyTaskProgress("LOGIN", 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
