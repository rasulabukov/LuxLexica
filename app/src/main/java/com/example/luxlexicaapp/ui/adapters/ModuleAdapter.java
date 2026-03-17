package com.example.luxlexicaapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxlexicaapp.data.entities.Module;
import com.example.luxlexicaapp.data.entities.UserProgress;
import com.example.luxlexicaapp.databinding.ItemModuleBinding;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Module> modules = new ArrayList<>();
    private List<UserProgress> progressList = new ArrayList<>();
    private final OnModuleClickListener listener;

    public interface OnModuleClickListener {
        void onModuleClick(Module module);
    }

    public ModuleAdapter(OnModuleClickListener listener) {
        this.listener = listener;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
        notifyDataSetChanged();
    }

    public void setProgress(List<UserProgress> progressList) {
        this.progressList = progressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemModuleBinding binding = ItemModuleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ModuleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);
        boolean isCompleted = false;
        if (progressList != null) {
            for (UserProgress p : progressList) {
                if (p.moduleId == module.id && p.isCompleted) {
                    isCompleted = true;
                    break;
                }
            }
        }
        holder.bind(module, isCompleted, listener);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    static class ModuleViewHolder extends RecyclerView.ViewHolder {
        private final ItemModuleBinding binding;

        public ModuleViewHolder(ItemModuleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Module module, boolean isCompleted, OnModuleClickListener listener) {
            binding.tvModuleName.setText(module.name);
            binding.tvModuleDesc.setText(module.description);
            
            if (isCompleted) {
                binding.ivStatus.setImageResource(android.R.drawable.checkbox_on_background);
                binding.ivStatus.setVisibility(View.VISIBLE);
            } else {
                binding.ivStatus.setVisibility(View.GONE);
            }
            
            itemView.setOnClickListener(v -> listener.onModuleClick(module));
        }
    }
}
