package com.example.luxlexicaapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.luxlexicaapp.R;
import com.example.luxlexicaapp.data.entities.CommunityPost;
import com.example.luxlexicaapp.databinding.FragmentCommunityBinding;
import com.example.luxlexicaapp.ui.adapters.CommunityAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;
    private CommunityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new CommunityAdapter();
        binding.rvCommunity.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCommunity.setAdapter(adapter);

        // Set dummy data for community posts
        adapter.setPosts(getDummyPosts());
    }

    private List<CommunityPost> getDummyPosts() {
        List<CommunityPost> posts = new ArrayList<>();
        posts.add(new CommunityPost(
                "Мария К.",
                "2 ч назад",
                "Прошла первый модуль по английскому! Рекомендую начинать с приветствий — очень помогает в поездках.",
                R.drawable.ic_avatar_placeholder
        ));
        posts.add(new CommunityPost(
                "Алексей В.",
                "Вчера",
                "Серия из 14 дней! Не бросайте — через неделю уже чувствуется прогресс.",
                R.drawable.ic_avatar_placeholder
        ));
        posts.add(new CommunityPost(
                "Ольга С.",
                "3 дн. назад",
                "Кто учит испанский? Ищу напарника для практики раз в неделю.",
                R.drawable.ic_avatar_placeholder
        ));
        posts.add(new CommunityPost(
                "Иван П.",
                "4 дн. назад",
                "Сегодня выучил 50 новых слов! Метод интервальных повторений реально работает.",
                R.drawable.ic_avatar_placeholder
        ));
        return posts;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
