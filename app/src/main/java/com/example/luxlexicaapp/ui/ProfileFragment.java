package com.example.luxlexicaapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.luxlexicaapp.databinding.FragmentProfileBinding;
import com.example.luxlexicaapp.ui.adapters.AchievementAdapter;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private MainViewModel viewModel;
    private AchievementAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        adapter = new AchievementAdapter();
        binding.rvAchievements.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rvAchievements.setAdapter(adapter);

        viewModel.getMainUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.etName.setText(user.name);
                binding.tvStreakCount.setText(user.streak + " дней");
                binding.tvStatsContent.setText(
                        "Пройдено уроков: " + user.lessonsCompleted + 
                        "\nОбщий опыт: " + user.experience + " XP"
                );
                adapter.setUserExperience(user.experience);
            }
        });

        viewModel.getAllAchievements().observe(getViewLifecycleOwner(), achievements -> {
            if (achievements != null) {
                adapter.setAchievements(achievements);
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            String newName = binding.etName.getText().toString().trim();
            if (!newName.isEmpty()) {
                viewModel.updateUserName(newName);
                Toast.makeText(getContext(), "Имя сохранено", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
