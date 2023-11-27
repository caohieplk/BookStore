package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.app_util.AppUtils;
import com.example.bookstore.app_util.Constant;
import com.example.bookstore.databinding.FragmentDetailBookBinding;
import com.example.bookstore.dto.Book;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class DetailBookFragment extends Fragment {

    FragmentDetailBookBinding binding;

    private Book book;


    public DetailBookFragment() {
        // Required empty public constructor
    }

    public DetailBookFragment(Book book) {
        this.book = book;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBookBinding.inflate(inflater, container, false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view -> back());
        binding.btnReadBook.setOnClickListener(view -> openReadBookScreen());
        binding.btnBuyBook.setOnClickListener(view -> openPaymentScreen());

        //override back button in navigation bar
        final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

    private void initData() {

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Chi Tiết Sách");
        //set ảnh book
        Glide.with(requireActivity()).load(book.getImage()).centerCrop().into(binding.imgBook);
        //set tên book
        binding.tvName.setText(book.getName());
        binding.tvAuthor.setText(book.getAuthor());
        binding.tvContent.setText(book.getContent());
        binding.tvReader.setText(book.getIs_read());
        binding.tvCategory.setText(book.getName_en());
        binding.tvCreateAt.setText(AppUtils.formatDate(book.getCreated_at(), Constant.yyyyMMddHHmmss, Constant.ddMMyyyy));

        //check nếu giá != empty thì show giá ngược lại thì ẩn btnBuyBook đi
        if (book.getPrice().isEmpty()) {
            binding.btnBuyBook.setVisibility(View.GONE);
            binding.tvPrice.setVisibility(View.GONE);
        } else {
            binding.btnBuyBook.setVisibility(View.VISIBLE);
            binding.tvPrice.setVisibility(View.VISIBLE);
            binding.tvPrice.setText(requireActivity().getString(R.string.book_price_format, book.getPrice()));
        }
    }


    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(DetailBookFragment.this).commit();
    }

    private void openReadBookScreen() {
        //truyền đối tượng book sang để hiển thị tên sách, content sách, tác giả
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ReadBookFragment(book)).commit();

    }

    private void openPaymentScreen() {
        //truyền đối tượng book sang để tạo payment
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new PaymentFragment(book)).commit();

    }

}