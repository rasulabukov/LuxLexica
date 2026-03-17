package com.example.luxlexicaapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.luxlexicaapp.R;
import com.example.luxlexicaapp.data.entities.Language;
import com.example.luxlexicaapp.data.entities.Level;
import com.example.luxlexicaapp.databinding.FragmentLessonsBinding;
import com.example.luxlexicaapp.ui.adapters.ModuleAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class LessonsFragment extends Fragment {

    private FragmentLessonsBinding binding;
    private MainViewModel viewModel;
    private ModuleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLessonsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        adapter = new ModuleAdapter(module -> {
            Bundle args = new Bundle();
            args.putInt("moduleId", module.id);
            Navigation.findNavController(requireView()).navigate(R.id.action_lessonsFragment_to_moduleFragment, args);
        });

        binding.rvModules.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvModules.setAdapter(adapter);

        viewModel.getMainUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvGreeting.setText("Привет, " + user.name);
            }
        });

        viewModel.getLanguages().observe(getViewLifecycleOwner(), this::setupLanguages);
        viewModel.getLevels().observe(getViewLifecycleOwner(), this::setupLevels);
        viewModel.getFilteredModules().observe(getViewLifecycleOwner(), adapter::setModules);
        
        // Observe progress to show checkmarks
        viewModel.getAllUserProgress().observe(getViewLifecycleOwner(), adapter::setProgress);
    }

    private void setupLanguages(List<Language> languages) {
        binding.layoutLanguages.removeAllViews();
        for (Language lang : languages) {
            MaterialButton btn = createChipButton(lang.name, false);
            btn.setOnClickListener(v -> viewModel.selectLanguage(lang.id));
            viewModel.getSelectedLanguageId().observe(getViewLifecycleOwner(), selectedId -> {
                btn.setSelected(selectedId == lang.id);
            });
            binding.layoutLanguages.addView(btn);
        }
    }

    private void setupLevels(List<Level> levels) {
        binding.layoutLevels.removeAllViews();
        for (Level level : levels) {
            MaterialButton btn = createChipButton(level.code, true);
            btn.setOnClickListener(v -> viewModel.selectLevel(level.id));
            viewModel.getSelectedLevelId().observe(getViewLifecycleOwner(), selectedId -> {
                btn.setSelected(selectedId == level.id);
            });
            binding.layoutLevels.addView(btn);
        }
    }

    private MaterialButton createChipButton(String text, boolean isSmall) {
        MaterialButton btn = new MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonStyle);
        btn.setText(text);
        btn.setAllCaps(false);
        btn.setCornerRadius((int) (8 * getResources().getDisplayMetrics().density));
        btn.setBackgroundResource(R.drawable.selector_chip);
        btn.setTextColor(getResources().getColorStateList(R.color.selector_chip_text, null));
        
        int horizontalPadding = (int) ((isSmall ? 12 : 16) * getResources().getDisplayMetrics().density);
        int verticalPadding = (int) ((isSmall ? 4 : 8) * getResources().getDisplayMetrics().density);
        btn.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        btn.setMinWidth(0);
        btn.setMinimumWidth(0);
        btn.setMinHeight(0);
        btn.setMinimumHeight(0);
        
        if (isSmall) {
            btn.setTextSize(12);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, (int) (8 * getResources().getDisplayMetrics().density), 0);
        btn.setLayoutParams(params);
        
        return btn;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
