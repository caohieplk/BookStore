package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.adapter.FavoriteAdapter;
import com.example.bookstore.app_util.event_bus.MessageEvent;
import com.example.bookstore.databinding.FragmentFavoriteBookBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.BaseResponse;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.FavoriteBookResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteBookFragment extends Fragment {

    FragmentFavoriteBookBinding binding;
    IHttpRequest iHttpRequest;

    private List<Book> books = new ArrayList<>();
    private Category category;
    private FavoriteAdapter favoriteAdapter;

    public FavoriteBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBookBinding.inflate(inflater, container, false);

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

    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(FavoriteBookFragment.this).commit();
    }

    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        category = new Category("Sách yêu thích");
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Sách Yêu Thích");
        setupFavoriteBookAdapter();
        //call api lấy danh sách banner book
        getFavoritesApi();
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

    private void setupFavoriteBookAdapter() {
        favoriteAdapter = new FavoriteAdapter(requireActivity(), category, books, FavoriteBookFragment.this, new FavoriteAdapter.OnClickItemListener() {
            @Override
            public void onUnFavoriteItem(Book book, int position) {
                unFavoriteBookApi(book, position);
            }
        });
        binding.rvFavorite.setAdapter(favoriteAdapter);
    }


    private void getFavoritesApi() {
        iHttpRequest.getFavorites().enqueue(getFavoritesCallback);
    }


    Callback<FavoriteBookResponse> getFavoritesCallback = new Callback<FavoriteBookResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<FavoriteBookResponse> call, Response<FavoriteBookResponse> response) {
            if (response.isSuccessful()) {
                FavoriteBookResponse favoriteBookResponse = response.body();
                if (favoriteBookResponse != null && favoriteBookResponse.isStatus()) {
                    //xoá sạch data trong list
                    books.clear();
                    //add toàn bộ list từ response vào books
                    books.addAll(favoriteBookResponse.getData());
                    //refresh lại favoriteAdapter
                    favoriteAdapter.notifyDataSetChanged();
                }
            }
            showUI();
        }

        @Override
        public void onFailure(@NonNull Call<FavoriteBookResponse> call, Throwable t) {
            Log.d("getCategoriesCallBack", "Failute" + t.getMessage());
            showUI();
        }
    };


    private void unFavoriteBookApi(Book book, int position) {
        iHttpRequest.unFavorite(new Book(book.getId())).enqueue(new Callback<BaseResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isStatus()) {
                        //xoá thành công 1 transaction
                        Toast.makeText(requireActivity(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        books.remove(position);
                        //refresh lại transactionAdapter
                        favoriteAdapter.notifyDataSetChanged();
                        showUI();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                Log.d("deleteTransCallback", "Failute" + t.getMessage());
                //xoá yêu thích thất bại
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                showUI();
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @SuppressLint("RtlHardcoded")
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        if (event != null && (event.getMsg().equals(MessageEvent.CHANGE_STATE_FAVORITE))) {
            getFavoritesApi();
        }
    }
}