package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemSearchBookBinding;
import com.example.bookstore.dto.Book;
import com.example.bookstore.fragment.DetailBookFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class SearchBookAdapter extends RecyclerView.Adapter<SearchBookAdapter.ViewHolder> {

    private Context context;
    private List<Book> books;
    private Fragment fragment;

    public SearchBookAdapter(Context context, List<Book> books, Fragment fragment) {
        this.context = context;
        this.books = books;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SearchBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBookBinding itemSearchBookBinding = ItemSearchBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemSearchBookBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchBookAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);
        if (book != null) {
            //set ảnh book
            Glide.with(holder.itemView.getContext()).load(book.getImage()).centerCrop().into(holder.binding.imgBook);
            //set tên book
            holder.binding.tvTitle.setText(book.getName());
            holder.binding.tvAuthor.setText(book.getAuthor());
            holder.binding.tvContent.setText(book.getContent());
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
        ItemSearchBookBinding binding;

        ViewHolder(ItemSearchBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
