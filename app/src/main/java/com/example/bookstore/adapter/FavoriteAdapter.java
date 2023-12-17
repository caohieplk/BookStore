package com.example.bookstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemFavoriteBookBinding;
import com.example.bookstore.fragment.DetailBookFragment;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private List<Book> books;
    private Category category;
    private Fragment fragment;

    private OnClickItemListener clickListener;

    public FavoriteAdapter(Context context, Category category, List<Book> books, Fragment fragment, OnClickItemListener clickListener) {
        this.context = context;
        this.category = category;
        this.books = books;
        this.fragment = fragment;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBookBinding itemFavoriteBookBinding = ItemFavoriteBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemFavoriteBookBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Book book = books.get(position);
        if (book != null) {
            //set ảnh book
            Glide.with(holder.itemView.getContext()).load(book.getImage()).centerCrop().into(holder.binding.imgBook);
            //title
            holder.binding.tvTitle.setText(book.getName());

            //Xoá 1 favorite
            holder.binding.imgFavorite.setImageResource(R.drawable.ic_favorited);
            holder.binding.lnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onUnFavoriteItem(book, position);
                }
            });

            //click item để mở màn chi tiết sách
            holder.binding.getRoot().setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
                fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailBookFragment(book, category)).commit();
            });
        }

    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface OnClickItemListener {
        void onUnFavoriteItem(Book book, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFavoriteBookBinding binding;

        ViewHolder(ItemFavoriteBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
