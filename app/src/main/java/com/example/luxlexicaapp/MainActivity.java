package com.example.luxlexicaapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.luxlexicaapp.data.AppDatabase;
import com.example.luxlexicaapp.data.DataInitializer;
import com.example.luxlexicaapp.data.entities.User;
import com.example.luxlexicaapp.databinding.ActivityMainBinding;
import com.example.luxlexicaapp.ui.MainViewModel;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        AppDatabase db = AppDatabase.getDatabase(this);
        DataInitializer.initializeData(this, db);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.startFragment || destination.getId() == R.id.aboutAuthorFragment) {
                binding.bottomNav.setVisibility(View.GONE);
            } else {
                binding.bottomNav.setVisibility(View.VISIBLE);
            }
        });

        updateStreak(db);
        viewModel.updateDailyTaskProgress("LOGIN", 1);
    }

    private void updateStreak(AppDatabase db) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = db.userDao().getMainUserSync();
            if (user == null) return;

            long currentTime = System.currentTimeMillis();
            Calendar lastLogin = Calendar.getInstance();
            lastLogin.setTimeInMillis(user.lastLoginDate);
            
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(currentTime);

            if (lastLogin.get(Calendar.YEAR) != today.get(Calendar.YEAR) ||
                lastLogin.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)) {
                
                Calendar yesterday = Calendar.getInstance();
                yesterday.setTimeInMillis(currentTime);
                yesterday.add(Calendar.DAY_OF_YEAR, -1);

                if (lastLogin.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                    lastLogin.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
                    user.streak += 1;
                } else {
                    user.streak = 1;
                }
                user.lastLoginDate = currentTime;
                db.userDao().update(user);
            }
        });
    }
}
