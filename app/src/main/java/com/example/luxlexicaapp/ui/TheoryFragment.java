package com.example.luxlexicaapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.luxlexicaapp.data.AppDatabase;
import com.example.luxlexicaapp.databinding.FragmentModuleTheoryBinding;

public class TheoryFragment extends Fragment {

    private FragmentModuleTheoryBinding binding;
    private int moduleId;
    private MainViewModel viewModel;
    private boolean isTheoryUpdated = false;

    public static TheoryFragment newInstance(int moduleId) {
        TheoryFragment fragment = new TheoryFragment();
        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModuleTheoryBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            moduleId = getArguments().getInt("moduleId");
        }
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        AppDatabase.getDatabase(requireContext()).appDao().getLessonByModule(moduleId)
                .observe(getViewLifecycleOwner(), lesson -> {
                    if (lesson != null) {
                        binding.tvTheoryContent.setText(lesson.content);
                        if (!isTheoryUpdated) {
                            viewModel.updateDailyTaskProgress("THEORY", 1);
                            isTheoryUpdated = true;
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
