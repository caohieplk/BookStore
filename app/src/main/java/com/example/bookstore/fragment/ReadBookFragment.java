package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.databinding.FragmentReadBookBinding;
import com.example.bookstore.dto.Book;
import com.example.bookstore.helpers.IHttpRequest;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

public class ReadBookFragment extends Fragment {


    FragmentReadBookBinding binding;
    IHttpRequest iHttpRequest;
    private Book book;



    public ReadBookFragment() {
        // Required empty public constructor
    }

    public ReadBookFragment(Book book) {
        this.book = book;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReadBookBinding.inflate(inflater,container,false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {
        //press back button
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
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
        binding.tvName.setText(book.getName());
        binding.tvContent.setText(book.getContent());
        binding.tvAuthor.setText(book.getAuthor());
    }

    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(ReadBookFragment.this).commit();
    }


}
