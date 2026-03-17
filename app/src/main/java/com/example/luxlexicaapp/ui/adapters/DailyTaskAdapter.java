package com.example.luxlexicaapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxlexicaapp.data.entities.DailyTask;
import com.example.luxlexicaapp.databinding.ItemDailyTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class DailyTaskAdapter extends RecyclerView.Adapter<DailyTaskAdapter.TaskViewHolder> {

    private List<DailyTask> tasks = new ArrayList<>();

    public void setTasks(List<DailyTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDailyTaskBinding binding = ItemDailyTaskBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemDailyTaskBinding binding;

        public TaskViewHolder(ItemDailyTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DailyTask task) {
            binding.tvTaskDesc.setText(task.description);
            binding.tvReward.setText("+" + task.rewardExperience + " XP");
            binding.pbProgress.setMax(task.targetValue);
            binding.pbProgress.setProgress(task.currentValue);
            
            if (task.isCompleted) {
                binding.ivCheck.setImageResource(android.R.drawable.checkbox_on_background);
            } else {
                binding.ivCheck.setImageResource(android.R.drawable.checkbox_off_background);
            }
        }
    }
}
