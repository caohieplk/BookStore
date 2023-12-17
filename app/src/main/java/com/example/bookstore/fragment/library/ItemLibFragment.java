package com.example.bookstore.fragment.library;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.adapter.FavoriteAdapter;
import com.example.bookstore.adapter.LibraryBookAdapter;
import com.example.bookstore.app_util.event_bus.MessageEvent;
import com.example.bookstore.databinding.FragmentItemLibBinding;
import com.example.bookstore.fragment.NewBookFragment;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.BaseResponse;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.CategoryRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemLibFragment extends Fragment {

    FragmentItemLibBinding binding;
    IHttpRequest iHttpRequest;

    private List<Book> books = new ArrayList<>();
    private Category category;
    private LibraryBookAdapter libraryBookAdapter;

    public ItemLibFragment() {
        // Required empty public constructor
    }

    public ItemLibFragment(Category category) {
        this.category = category;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemLibBinding.inflate(inflater, container, false);

        initControl();
        initData();
        initView();

        return binding.getRoot();
    }

    private void initControl() {

    }

    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        setupLibraryBookAdapter();
        //call api lấy list book dựa vào id category
        getBooksByIdCategoryApi();
    }

    private void showUI() {
        if (books.isEmpty()) {
            binding.lnNoResult.setVisibility(View.VISIBLE);
            binding.rvFavorite.setVisibility(View.GONE);
        } else {
            binding.lnNoResult.setVisibility(View.GONE);
            binding.rvFavorite.setVisibility(View.VISIBLE);
        }
    }

    private void setupLibraryBookAdapter() {
        libraryBookAdapter = new LibraryBookAdapter(requireActivity(), books, ItemLibFragment.this);
        binding.rvFavorite.setAdapter(libraryBookAdapter);
    }


    private void getBooksByIdCategoryApi() {
        iHttpRequest.getListBookByIdCategory(new CategoryRequest(category.getId())).enqueue(getListBookByIdCategorCallback);
    }


    Callback<BookResponse> getListBookByIdCategorCallback = new Callback<BookResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BookResponse> call, Response<BookResponse> response) {
            if (response.isSuccessful()) {
                BookResponse bookResponse = response.body();
                if (bookResponse != null && bookResponse.isStatus()) {
                    //xoá sạch data trong list
                    books.clear();
                    //add toàn bộ list từ response vào books
                    books.addAll(bookResponse.getData());
                    //refresh lại favoriteAdapter
                    libraryBookAdapter.notifyDataSetChanged();
                }
            }
            showUI();
        }

        @Override
        public void onFailure(@NonNull Call<BookResponse> call, Throwable t) {
            Log.d("getBookByIdCategory", "Failute" + t.getMessage());
            showUI();
        }
    };

}