package com.example.bookstore.fragment.library;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.adapter.LibraryViewPagerAdapter;
import com.example.bookstore.databinding.FragmentLibraryBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.CategoryResponse;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryFragment extends Fragment {
    FragmentLibraryBinding binding;
    IHttpRequest iHttpRequest;
    private LibraryViewPagerAdapter libraryViewPagerAdapter;

    //list 5 fragment: Sách hay mỗi ngày, Tủ sách thanh niên, Nội dung giáo dục, Bộ sưu tập tiểu thuyết văn học, Những cuốn sách ly kì và rùng rợn
    private List<ItemLibFragment> fragmentLibraries = new ArrayList<>();

    //cho list category cho thư viện có 5 tab bao gồm: : Sách hay mỗi ngày, Tủ sách thanh niên, Nội dung giáo dục, Bộ sưu tập tiểu thuyết văn học, Những cuốn sách ly kì và rùng rợn
    // 5 tab này: id từ 12 - 16:
    private List<Category> categories = new ArrayList<>();

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);

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
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        getCategoriesApi();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Thư Viện Của Bạn");
        setupLibraryViewPager();
        setTab();
    }

    private void initListTab(){
        //truyền từng category vào từng ItemLibFragment
        fragmentLibraries.clear();
        for (Category category: categories){
            fragmentLibraries.add(new ItemLibFragment(category));
        }
    }

    private void setTab() {
        new TabLayoutMediator(binding.tablayout, binding.viewPager,
                (tab, position) -> {
                    tab.setText(categories.get(position).getName());
                }
        ).attach();
    }

    private void setupLibraryViewPager(){
        libraryViewPagerAdapter = new LibraryViewPagerAdapter(requireActivity().getSupportFragmentManager(), requireActivity().getLifecycle(),categories, fragmentLibraries);
        binding.viewPager.setAdapter(libraryViewPagerAdapter);
        binding.viewPager.setCurrentItem(0, false);
    }

    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(LibraryFragment.this).commit();
    }


    private void getCategoriesApi() {
        iHttpRequest.getListCategory().enqueue(getCategoriesCallBack);
    }
    Callback<CategoryResponse> getCategoriesCallBack = new Callback<CategoryResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<CategoryResponse> call, Response<CategoryResponse> response) {
            if (response.isSuccessful()) {
                CategoryResponse categoryResponse = response.body();
                if (categoryResponse != null && categoryResponse.isStatus()) {
                    List<Category> allCategories = categoryResponse.getData();
                    //xoá sạch data trong list category, tránh trường hợp add nhiều lần khi call lại api
                    categories.clear();

                    for (Category category : allCategories) {
                        //lọc id category = 8, 9, 10: Sách, sách nói, sách thiếu nhi thì đưa vào list bảng xếp hạng
                        if (category.getId() >= 12 && category.getId() <= 16) { //5 tab sách trong màn Thư viện có id từ 12- 16
                            categories.add(category);
                        }

                    }
                    initListTab();
                    //refresh lại category adapter
                    libraryViewPagerAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<CategoryResponse> call, Throwable t) {
            Log.d("getCategoriesCallBack", "Failute" + t.getMessage());
        }
    };
}