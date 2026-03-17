package com.example.luxlexicaapp.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.luxlexicaapp.R;
import com.example.luxlexicaapp.data.AppDatabase;
import com.example.luxlexicaapp.data.entities.PracticeQuestion;
import com.example.luxlexicaapp.data.entities.UserProgress;
import com.example.luxlexicaapp.databinding.FragmentPracticeBinding;
import com.example.luxlexicaapp.databinding.ItemTestOptionBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PracticeFragment extends Fragment {

    private FragmentPracticeBinding binding;
    private int moduleId;
    private List<PracticeQuestion> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int selectedOptionIndex = -1;
    private MainViewModel viewModel;

    public static PracticeFragment newInstance(int moduleId) {
        PracticeFragment fragment = new PracticeFragment();
        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            moduleId = getArguments().getInt("moduleId");
        }
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        AppDatabase db = AppDatabase.getDatabase(requireContext());

        // Check progress to disable if already completed
        db.appDao().getUserProgress(1, moduleId).observe(getViewLifecycleOwner(), progress -> {
            if (progress != null && progress.isPracticeCompleted) {
                binding.btnPracticeNext.setEnabled(false);
                binding.btnPracticeNext.setText("Практика пройдена");
                binding.btnPracticeNext.setVisibility(View.VISIBLE);
                // Optionally disable options selection too
                binding.layoutPracticeOptions.setEnabled(false);
            }
        });

        db.appDao().getPracticeByModule(moduleId).observe(getViewLifecycleOwner(), qList -> {
            if (qList != null && !qList.isEmpty()) {
                questions = qList;
                displayPractice();
            }
        });

        binding.btnPracticeNext.setOnClickListener(v -> {
            if (selectedOptionIndex == -1) {
                Snackbar.make(binding.getRoot(), "Выберите вариант", Snackbar.LENGTH_SHORT).show();
                return;
            }
            
            if (selectedOptionIndex == questions.get(currentQuestionIndex).correctAnswerIndex) {
                currentQuestionIndex++;
                selectedOptionIndex = -1;
                if (currentQuestionIndex < questions.size()) {
                    displayPractice();
                } else {
                    savePracticeProgress();
                    viewModel.updateDailyTaskProgress("PRACTICE", 1);
                    Snackbar.make(binding.getRoot(), "Правильно! Переходим к тесту.", Snackbar.LENGTH_SHORT).show();
                    ViewPager2 viewPager = getActivity().findViewById(R.id.view_pager);
                    if (viewPager != null) {
                        viewPager.setCurrentItem(2, true);
                    }
                }
            } else {
                Snackbar.make(binding.getRoot(), "Неверно, попробуйте еще раз", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void savePracticeProgress() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProgress progress = db.appDao().getUserProgressSync(1, moduleId);
            if (progress == null) {
                progress = new UserProgress(1, moduleId);
            }
            progress.isPracticeCompleted = true;
            if (progress.isPracticeCompleted && progress.isTestCompleted) {
                progress.isCompleted = true;
            }
            db.appDao().insertUserProgress(progress);
        });
    }

    private void displayPractice() {
        PracticeQuestion q = questions.get(currentQuestionIndex);
        binding.tvPracticeQuestion.setText(q.questionText);
        binding.layoutPracticeOptions.removeAllViews();
        binding.btnPracticeNext.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        List<String> options = gson.fromJson(q.optionsJson, new TypeToken<List<String>>(){}.getType());

        for (int i = 0; i < options.size(); i++) {
            ItemTestOptionBinding optionBinding = ItemTestOptionBinding.inflate(getLayoutInflater(), binding.layoutPracticeOptions, false);
            optionBinding.btnOption.setText(options.get(i));
            int index = i;
            optionBinding.btnOption.setOnClickListener(v -> {
                selectedOptionIndex = index;
                updateOptionsUI();
            });
            binding.layoutPracticeOptions.addView(optionBinding.getRoot());
        }
        updateOptionsUI();
    }

    private void updateOptionsUI() {
        for (int i = 0; i < binding.layoutPracticeOptions.getChildCount(); i++) {
            MaterialButton btn = (MaterialButton) binding.layoutPracticeOptions.getChildAt(i);
            if (i == selectedOptionIndex) {
                btn.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.accent_gold)));
                btn.setStrokeWidth(4);
                btn.setBackgroundColor(getResources().getColor(R.color.dark_blue_surface));
            } else {
                btn.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.button_bg)));
                btn.setStrokeWidth(2);
                btn.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
