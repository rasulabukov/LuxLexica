package com.example.luxlexicaapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxlexicaapp.data.entities.CommunityPost;
import com.example.luxlexicaapp.databinding.ItemCommunityPostBinding;

import java.util.ArrayList;
import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private List<CommunityPost> posts = new ArrayList<>();

    public void setPosts(List<CommunityPost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommunityPostBinding binding = ItemCommunityPostBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CommunityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class CommunityViewHolder extends RecyclerView.ViewHolder {
        private final ItemCommunityPostBinding binding;

        public CommunityViewHolder(ItemCommunityPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CommunityPost post) {
            binding.tvUserName.setText(post.getUserName());
            binding.tvTimeAgo.setText(post.getTimeAgo());
            binding.tvContent.setText(post.getContent());
            binding.ivAvatar.setImageResource(post.getAvatarResId());
        }
    }
}
