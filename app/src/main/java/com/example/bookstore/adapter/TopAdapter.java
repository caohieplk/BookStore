package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemTopBinding;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.fragment.DetailBookFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {

    private Context context;
    private List<Category> categories;
    private Fragment fragment;

    public TopAdapter(Context context, List<Category> categories, Fragment fragment) {
        this.context = context;
        this.categories = categories;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopBinding itemTopBinding = ItemTopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemTopBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull TopAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);
        if (category != null) {
            //title category
            holder.binding.tvTitle.setText(category.getName());

            if (category.getBooks().size() == 3) {
                //Book 1
                Book book1 = category.getBooks().get(0);
                fillDataToBook(category, book1, holder.binding.imgBook1, holder.binding.tvName1, holder.binding.tvAuthor1, holder.binding.lnBook1);
                //Book 2
                Book book2 = category.getBooks().get(1);
                fillDataToBook(category, book2, holder.binding.imgBook2, holder.binding.tvName2, holder.binding.tvAuthor2, holder.binding.lnBook2);
                //Book 3
                Book book3 = category.getBooks().get(2);
                fillDataToBook(category, book3, holder.binding.imgBook3, holder.binding.tvName3, holder.binding.tvAuthor3, holder.binding.lnBook3);
            }
        }
    }

    private void fillDataToBook(Category category, Book book, ImageView imgBook, TextView tvName, TextView tvAuthor, LinearLayout lnBook) {
        //set ảnh book
        Glide.with(context).load(book.getImage()).centerCrop().into(imgBook);
        //set tên book
        tvName.setText(book.getName());
        //set author book
        //tvAuthor.setText(book.getAuthor());
        //click item để mở màn chi tiết sách
        lnBook.setOnClickListener(view -> {
            //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
            fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailBookFragment(book, category)).commit();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTopBinding binding;

        ViewHolder(ItemTopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
