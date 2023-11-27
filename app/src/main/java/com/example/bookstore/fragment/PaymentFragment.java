package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.databinding.FragmentPaymentBinding;
import com.example.bookstore.dto.Book;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class PaymentFragment extends Fragment {

    FragmentPaymentBinding binding;

    private Book book;


    public PaymentFragment() {
        // Required empty public constructor
    }

    public PaymentFragment(Book book) {
        this.book = book;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view2 -> back());
        binding.btnPayment.setOnClickListener(view2 -> {});

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
        binding.toolbar.tvTitle.setText("Thanh Toán");
        //set ảnh book
        Glide.with(requireActivity()).load(book.getImage()).centerCrop().into(binding.imgBook);
        //set tên book
        binding.tvName.setText(book.getName());
        binding.tvAuthor.setText(book.getAuthor());

        //check nếu giá != empty thì show giá ngược lại thì ẩn btnBuyBook đi
        if (book.getPrice().isEmpty()) {
            binding.tvPrice.setVisibility(View.GONE);
        } else {
            binding.tvPrice.setVisibility(View.VISIBLE);
            binding.tvPrice.setText(requireActivity().getString(R.string.book_price_format, book.getPrice()));
        }
    }


    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(PaymentFragment.this).commit();
    }

    private void openReadBookScreen() {
        //truyền đối tượng book sang để hiển thị tên sách, content sách, tác giả
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ReadBookFragment(book)).commit();

    }

}