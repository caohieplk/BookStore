package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.databinding.ItemCategoryBinding;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> categories;
    private Fragment fragment;

    //list book
    private List<Book> bookList;
    private BookAdapter bookAdapter;

    public CategoryAdapter(Context context, List<Category> categories, Fragment fragment) {
        this.context = context;
        this.categories = categories;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemCategoryBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        if (category != null) {
            //ảnh icon
            Glide.with(holder.itemView.getContext()).load(category.getImage()).centerCrop().into(holder.binding.imgIcon);
            //title
            holder.binding.tvCategory.setText(category.getName());
            //setup book adapter
            bookAdapter = new BookAdapter(context, category, category.getBooks(), fragment);
            //set book adapter vào recycler view book
            holder.binding.rvBook.setAdapter(bookAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;

        ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
