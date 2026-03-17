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

import com.example.luxlexicaapp.R;
import com.example.luxlexicaapp.data.AppDatabase;
import com.example.luxlexicaapp.data.entities.Question;
import com.example.luxlexicaapp.data.entities.Test;
import com.example.luxlexicaapp.data.entities.User;
import com.example.luxlexicaapp.data.entities.UserProgress;
import com.example.luxlexicaapp.databinding.FragmentTestBinding;
import com.example.luxlexicaapp.databinding.ItemTestOptionBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {

    private FragmentTestBinding binding;
    private int moduleId;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int selectedOptionIndex = -1;
    private MainViewModel viewModel;

    public static TestFragment newInstance(int moduleId) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
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
        db.appDao().getTestByModule(moduleId).observe(getViewLifecycleOwner(), test -> {
            if (test != null) {
                db.appDao().getQuestionsByTest(test.id).observe(getViewLifecycleOwner(), qList -> {
                    if (qList != null && !qList.isEmpty()) {
                        questions = qList;
                        displayQuestion();
                    }
                });
            }
        });

        binding.btnNext.setOnClickListener(v -> {
            if (selectedOptionIndex == -1) {
                Snackbar.make(binding.getRoot(), "Выберите ответ", Snackbar.LENGTH_SHORT).show();
                return;
            }
            checkAnswer();
            currentQuestionIndex++;
            selectedOptionIndex = -1;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                finishTest();
            }
        });
    }

    private void displayQuestion() {
        Question q = questions.get(currentQuestionIndex);
        binding.tvQuestion.setText(q.questionText);
        binding.layoutOptions.removeAllViews();
        binding.btnNext.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        List<String> options = gson.fromJson(q.optionsJson, new TypeToken<List<String>>(){}.getType());

        for (int i = 0; i < options.size(); i++) {
            ItemTestOptionBinding optionBinding = ItemTestOptionBinding.inflate(getLayoutInflater(), binding.layoutOptions, false);
            optionBinding.btnOption.setText(options.get(i));
            int index = i;
            optionBinding.btnOption.setOnClickListener(v -> {
                selectedOptionIndex = index;
                updateOptionsUI();
            });
            binding.layoutOptions.addView(optionBinding.getRoot());
        }
        updateOptionsUI();
    }

    private void updateOptionsUI() {
        for (int i = 0; i < binding.layoutOptions.getChildCount(); i++) {
            MaterialButton btn = (MaterialButton) binding.layoutOptions.getChildAt(i);
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

    private void checkAnswer() {
        if (selectedOptionIndex == questions.get(currentQuestionIndex).correctAnswerIndex) {
            correctAnswers++;
        }
    }

    private void finishTest() {
        double score = (double) correctAnswers / questions.size();
        boolean passed = score >= 0.8;

        AppDatabase db = AppDatabase.getDatabase(requireContext());
        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProgress progress = db.appDao().getUserProgressSync(1, moduleId);
            if (progress == null) {
                progress = new UserProgress(1, moduleId);
            }
            
            if (passed) {
                progress.isTestCompleted = true;
                if (progress.isPracticeCompleted && progress.isTestCompleted) {
                    progress.isCompleted = true;
                    
                    User user = db.userDao().getMainUserSync();
                    if (user != null) {
                        user.experience += 100;
                        user.lessonsCompleted += 1;
                        db.userDao().update(user);
                    }
                }
                viewModel.updateDailyTaskProgress("TEST", 1);
            }
            db.appDao().insertUserProgress(progress);
        });

        String message = passed ? "Тест пройден! +100 XP" : "Тест не пройден. Попробуйте еще раз.";
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        requireActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
