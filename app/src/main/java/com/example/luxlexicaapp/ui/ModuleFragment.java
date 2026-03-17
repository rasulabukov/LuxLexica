package com.example.luxlexicaapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.luxlexicaapp.databinding.FragmentModuleBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class ModuleFragment extends Fragment {

    private FragmentModuleBinding binding;
    private int moduleId;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentModuleBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            moduleId = getArguments().getInt("moduleId");
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        setupToolbar();

        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0: return TheoryFragment.newInstance(moduleId);
                    case 1: return PracticeFragment.newInstance(moduleId);
                    default: return TestFragment.newInstance(moduleId);
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Теория"); break;
                case 1: tab.setText("Практика"); break;
                case 2: tab.setText("Тест"); break;
            }
        }).attach();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> 
            Navigation.findNavController(v).navigateUp());

        viewModel.getFilteredModules().observe(getViewLifecycleOwner(), modules -> {
            if (modules != null) {
                for (com.example.luxlexicaapp.data.entities.Module m : modules) {
                    if (m.id == moduleId) {
                        binding.toolbar.setTitle(m.name);
                        break;
                    }
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
