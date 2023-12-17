package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.adapter.NewBookAdapter;
import com.example.bookstore.databinding.FragmentNewBookBinding;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;

import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class NewBookFragment extends Fragment {

    FragmentNewBookBinding binding;

    private List<Book> books;
    private Category category;
    private NewBookAdapter newBookAdapter;

    public NewBookFragment() {
        // Required empty public constructor
    }

    public NewBookFragment(Category category) {
        this.category = category;
        this.books = category.getBooks();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewBookBinding.inflate(inflater, container, false);

        initControl();
        initData();
        initView();

        return binding.getRoot();
    }

    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view2 -> back());

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
        binding.toolbar.tvTitle.setText("Sách Mới Nhất");
        setupNewBookAdapter();
    }

    private void setupNewBookAdapter(){
        newBookAdapter = new NewBookAdapter(requireActivity(), books, NewBookFragment.this);
        binding.rvNewBook.setAdapter(newBookAdapter);
    }

    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(NewBookFragment.this).commit();
    }


}