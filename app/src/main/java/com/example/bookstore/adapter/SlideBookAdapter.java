package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemImageAutoSlideBinding;
import com.example.bookstore.model.Book;
import com.example.bookstore.fragment.DetailBookFragment;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;

public class SlideBookAdapter extends SliderViewAdapter<SlideBookAdapter.SliderAdapterViewHolder> {

    private Context context;
    private List<Book> books;
    private Fragment fragment;

    public SlideBookAdapter(Context context, List<Book> books, Fragment fragment) {
        this.context = context;
        this.books = books;
        this.fragment = fragment;
    }


    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        ItemImageAutoSlideBinding itemImageAutoSlideBinding = ItemImageAutoSlideBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SliderAdapterViewHolder(itemImageAutoSlideBinding);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder holder, int position) {
        Book book = books.get(position);
        if (book != null) {
            //set ảnh book
            Glide.with(context).load(book.getBanner()).centerInside().into(holder.binding.imgSlideBook);
            //click item để mở màn chi tiết sách
            holder.binding.getRoot().setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
                fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailBookFragment(book, null)).commit();
            });
        }
    }

    @Override
    public int getCount() {
        return books.size();
    }


    public static class SliderAdapterViewHolder extends ViewHolder {

        ItemImageAutoSlideBinding binding;

        SliderAdapterViewHolder(ItemImageAutoSlideBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
