package com.example.luxlexicaapp.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxlexicaapp.R;
import com.example.luxlexicaapp.data.entities.User;
import com.example.luxlexicaapp.databinding.ItemUserBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();
    private final boolean showRank;

    public UserAdapter(boolean showRank) {
        this.showRank = showRank;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, int rank) {
            binding.tvName.setText(user.name);
            binding.tvExperience.setText(user.experience + " XP");
            
            // Highlight main user (id=1) with yellow border
            if (user.id == 1) {
                binding.getRoot().setStrokeColor(binding.getRoot().getContext().getColor(R.color.accent_gold));
                binding.getRoot().setStrokeWidth(4);
            } else {
                binding.getRoot().setStrokeColor(Color.TRANSPARENT);
                binding.getRoot().setStrokeWidth(0);
            }

            if (binding.tvRank != null) {
                binding.tvRank.setVisibility(View.VISIBLE);
                binding.tvRank.setText("#" + rank);
            }
        }
    }
}
