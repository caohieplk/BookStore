package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemLibraryBookBinding;
import com.example.bookstore.databinding.ItemNewBookBinding;
import com.example.bookstore.fragment.DetailBookFragment;
import com.example.bookstore.model.Book;

import java.util.List;

public class LibraryBookAdapter extends RecyclerView.Adapter<LibraryBookAdapter.ViewHolder> {

    private Context context;
    private List<Book> books;
    private Fragment fragment;

    public LibraryBookAdapter(Context context, List<Book> books, Fragment fragment) {
        this.context = context;
        this.books = books;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public LibraryBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLibraryBookBinding itemLibraryBookBinding = ItemLibraryBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemLibraryBookBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull LibraryBookAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        if (book != null) {
            //set ảnh book
            Glide.with(holder.itemView.getContext()).load(book.getImage()).centerCrop().into(holder.binding.imgBook);
            //set tên book
            holder.binding.tvTitle.setText(book.getName());
            //click item để mở màn chi tiết sách
            holder.binding.getRoot().setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
                fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailBookFragment(book, null)).commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLibraryBookBinding binding;

        ViewHolder(ItemLibraryBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
