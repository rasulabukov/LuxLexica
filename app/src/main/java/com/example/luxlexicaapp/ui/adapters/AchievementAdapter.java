package com.example.luxlexicaapp.ui.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxlexicaapp.R;
import com.example.luxlexicaapp.data.entities.Achievement;
import com.example.luxlexicaapp.databinding.ItemAchievementBinding;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<Achievement> achievements = new ArrayList<>();
    private int userExperience = 0;

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
        notifyDataSetChanged();
    }

    public void setUserExperience(int experience) {
        this.userExperience = experience;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAchievementBinding binding = ItemAchievementBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AchievementViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        holder.bind(achievements.get(position), userExperience);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {
        private final ItemAchievementBinding binding;

        public AchievementViewHolder(ItemAchievementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Achievement achievement, int xp) {
            binding.tvAchievementTitle.setText(achievement.title);
            
            String rangeText = "";
            boolean isAchieved = false;
            
            // Logic based on titles as requested
            if (achievement.title.equals("Новичок")) {
                rangeText = "0 - 1000 XP";
                isAchieved = true; // Always unlocked or if xp >= 0
            } else if (achievement.title.equals("Эрудит")) {
                rangeText = "1000 - 2000 XP";
                isAchieved = xp >= 1000;
            } else if (achievement.title.equals("Постоянство")) {
                rangeText = "2000+ XP";
                isAchieved = xp >= 2000;
            }
            
            binding.tvAchievementRange.setText(rangeText);
            
            if (isAchieved) {
                binding.ivAchievementIcon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.getContext(), R.color.accent_gold)));
            } else {
                binding.ivAchievementIcon.setImageTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.getContext(), R.color.text_secondary)));
            }
        }
    }
}
