package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemChapterBinding;
import com.example.bookstore.fragment.ReadBookFragment;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Chapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private Context context;
    private List<Chapter> chapters;
    private Author author;
    private Fragment fragment;

    public ChapterAdapter(Context context, Author author, List<Chapter> chapters, Fragment fragment) {
        this.context = context;
        this.author = author;
        this.chapters = chapters;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterBinding itemChapterBinding = ItemChapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemChapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        if (chapter != null) {
            holder.binding.tvTitle.setText(chapter.getTitle());
            //click item để mở màn chi tiết sách
            holder.binding.getRoot().setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
                fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ReadBookFragment(chapter, author)).commit();
            });
        }

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemChapterBinding binding;

        ViewHolder(ItemChapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
