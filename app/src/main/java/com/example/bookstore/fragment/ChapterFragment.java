package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.adapter.ChapterAdapter;
import com.example.bookstore.databinding.FragmentChapterBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Chapter;
import com.example.bookstore.model.ChapterRequest;
import com.example.bookstore.model.ChapterResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChapterFragment extends Fragment {

    FragmentChapterBinding binding;
    IHttpRequest iHttpRequest;

    private Book book;
    private Author author;

    private List<Chapter> chapters = new ArrayList<>();
    private ChapterAdapter chapterAdapter;



    public ChapterFragment() {
        // Required empty public constructor
    }

    public ChapterFragment(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChapterBinding.inflate(inflater, container, false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view -> back());

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
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        getChaptersApi();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Nội Dung Sách");
        //set ảnh book
        setupChapterAdapter();

    }

    private void showUI(){
        if (chapters.isEmpty()){
            binding.lnNoResult.setVisibility(View.VISIBLE);
            binding.rvChapter.setVisibility(View.GONE);
        } else {
            binding.lnNoResult.setVisibility(View.GONE);
            binding.rvChapter.setVisibility(View.VISIBLE);
        }
    }

    private void setupChapterAdapter() {
        chapterAdapter = new ChapterAdapter(requireActivity(), author,chapters, ChapterFragment.this);
        binding.rvChapter.setAdapter(chapterAdapter);
    }


    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(ChapterFragment.this).commit();
    }


    private void getChaptersApi() {
        iHttpRequest.getChapters(new ChapterRequest(book.getId())).enqueue(getChaptersCallBack);
    }

    Callback<ChapterResponse> getChaptersCallBack = new Callback<ChapterResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<ChapterResponse> call, Response<ChapterResponse> response) {
            if (response.isSuccessful()) {
                ChapterResponse chapterResponse = response.body();
                if (chapterResponse != null && chapterResponse.isStatus()) {
                    //dọn dẹp list slide trước khi addAll phần tử
                    chapters.clear();
                    chapters.addAll(chapterResponse.getData());
                    //refresh lại adapter
                    chapterAdapter.notifyDataSetChanged();
                }
            }
            showUI();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onFailure(@NonNull Call<ChapterResponse> call, Throwable t) {
            Log.d("getChaptersCallBack", "Failute" + t.getMessage());
            chapters.clear();
            chapterAdapter.notifyDataSetChanged();
            showUI();
        }
    };

}