package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemBookBinding;
import com.example.bookstore.dto.Book;
import com.example.bookstore.fragment.DetailBookFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private Context context;
    private List<Book> books;
    private Fragment fragment;

    public BookAdapter(Context context, List<Book> books, Fragment fragment) {
        this.context = context;
        this.books = books;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding itemBookBinding = ItemBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBookBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        if (book != null) {
            //set ảnh book
            Glide.with(holder.itemView.getContext()).load(book.getImage()).centerCrop().into(holder.binding.imgBook);
            //set tên book
            holder.binding.tvTitle.setText(book.getName());
            //check nếu giá != empty thì show giá ngược lại thì ẩn tvPrice đi
            if (book.getPrice().isEmpty()) {
                holder.binding.tvPrice.setVisibility(View.GONE);
            } else {
                holder.binding.tvPrice.setVisibility(View.VISIBLE);
                holder.binding.tvPrice.setText(context.getString(R.string.book_price_format, book.getPrice()));
            }
            //click item để mở màn chi tiết sách
            holder.binding.getRoot().setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
                fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailBookFragment(book)).commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBookBinding binding;

        ViewHolder(ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
