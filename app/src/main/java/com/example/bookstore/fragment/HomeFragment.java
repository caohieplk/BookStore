package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.bookstore.R;
import com.example.bookstore.adapter.CategoryAdapter;
import com.example.bookstore.adapter.SearchBookAdapter;
import com.example.bookstore.adapter.SlideBookAdapter;
import com.example.bookstore.adapter.TopAdapter;
import com.example.bookstore.app_util.AppUtils;
import com.example.bookstore.databinding.FragmentHomeBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.BannerResponse;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.CategoryResponse;
import com.example.bookstore.model.SearchBook;
import com.example.bookstore.model.SearchBookResponse;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    //view binding
    FragmentHomeBinding binding;
    IHttpRequest iHttpRequest;

    //Banner BOOK: 3 banner trong table banner trên mysql
    private List<Book> bannerBooks = new ArrayList<>();
    private SlideBookAdapter slideBookAdapter;


    //lấy tất cả category để xử lý chia tách category theo id category
    List<Category> allCategories = new ArrayList<>();

    //list category có id category từ 1-7
    private List<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    //cho list category bảng xếp hạng top 01, 02, 03, bao gồm category: Sách, Sách nói, Sách thiếu nhi,
    // id lần lượt của 3 category là: 8, 9, 10
    private List<Category> tops = new ArrayList<>();
    private TopAdapter topAdapter;

    //lấy category Sách mới nhất có id là: 11
    private Category categoryNew;


    //search book
    private List<Book> searchBooks = new ArrayList<>();
    private SearchBookAdapter searchBookAdapter;


    //chế độ hiển thị UI trong màn home: UI mặc định, UI show không tìm thấy kết quả, UI show list kết quả tìm kiếm được
    enum ModeScreen {
        DEFAULT,
        NO_RESULT,
        RESULT_BOOK
    }

    //mặc định UI là list sách, ko phải chế độ tìm kiếm
    private ModeScreen modeScreen = ModeScreen.DEFAULT;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initControl();
        initData();
        initView();

        return binding.getRoot();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initControl() {

        //when touch to edit text search => show keyboard
        binding.edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtils.showKeyboard(requireActivity());
                return false;
            }
        });

        //search
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                String nameBook = editable.toString().trim();
                if (nameBook.isEmpty()) {
                    //text trống thì show icon search
                    binding.imgClear.setImageResource(R.drawable.ic_search);
                } else {
                    binding.imgClear.setImageResource(R.drawable.ic_clear);
                    modeScreen = ModeScreen.RESULT_BOOK;
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("NotifyDataSetChanged")
            public void onTextChanged(CharSequence query, int start, int before, int count) {

            }
        });

        binding.imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeScreen == ModeScreen.DEFAULT) { //ban đầu ở mode default (tức là icon search)
                    //icon search clicked => chuyển sang icon clear
                    binding.imgClear.setImageResource(R.drawable.ic_clear);
                } else {
                    //icon clear => clicked to clear text
                    binding.edtSearch.setText("");
                    modeScreen = ModeScreen.DEFAULT;
                    showUIModeScreen();
                }
            }
        });


        //action search
        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    modeScreen = ModeScreen.RESULT_BOOK;
                    AppUtils.hideKeyboard(requireActivity());
                    searchBookApi(binding.edtSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });


        binding.lnSachTruyen.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));
        binding.lnSachNoi.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));
        binding.lnTruyenTranh.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));
        binding.lnSachPhatGiao.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));
        binding.lnSachMoiNhat.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));
        binding.lnFavorite.setOnClickListener(view -> loadFragment(new FavoriteBookFragment()));
        binding.lnHopThu.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));
        binding.lnSachCongGiao.setOnClickListener(view -> loadFragment(new NewBookFragment(categoryNew)));

    }


    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        //call api lấy danh sách banner book
        getBannersApi();
        //call api lấy list category: các thể loại sách: miễn phí, văn học, khoa học...
        getCategoriesApi();
    }

    private void initView() {
        modeScreen = ModeScreen.DEFAULT; //hiển thị toàn bộ bảng xếp hạng, list category...
        showUIModeScreen();

        //setup adapter + recycler view cho list auto slide banner book
        setupSlideBookAdapter();
        //setup adapter + recycler view cho list bảng xếp hạng
        setupTopAdapter();
        //setup adapter + recycler view cho list category
        setupCategoryAdapter();
        //setup adapter + recycler view cho list result search book
        setupSearchBookAdapter();
    }

    private void showUIModeScreen() {
        AppUtils.hideKeyboard(requireActivity());
        switch (modeScreen) {
            case DEFAULT:
                binding.nestedScrollViewDefault.setVisibility(View.VISIBLE);
                binding.rvSearchBook.setVisibility(View.GONE);
                binding.lnNoResult.setVisibility(View.GONE);
                break;
            case NO_RESULT:
                binding.nestedScrollViewDefault.setVisibility(View.GONE);
                binding.rvSearchBook.setVisibility(View.GONE);
                binding.lnNoResult.setVisibility(View.VISIBLE);
                break;
            case RESULT_BOOK:
                binding.nestedScrollViewDefault.setVisibility(View.GONE);
                binding.rvSearchBook.setVisibility(View.VISIBLE);
                binding.lnNoResult.setVisibility(View.GONE);
                break;
        }
    }


    private void setupSlideBookAdapter() {
        slideBookAdapter = new SlideBookAdapter(requireActivity(), bannerBooks, HomeFragment.this);
        binding.imgSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        binding.imgSlider.setSliderAdapter(slideBookAdapter);
        binding.imgSlider.setAutoCycle(true);
        binding.imgSlider.startAutoCycle();
    }

    private void setupCategoryAdapter() {
        categoryAdapter = new CategoryAdapter(requireActivity(), categories, HomeFragment.this);
        binding.rvCategory.setAdapter(categoryAdapter);
    }

    private void setupTopAdapter() {
        topAdapter = new TopAdapter(requireActivity(), tops, HomeFragment.this);
        binding.rvTop.setAdapter(topAdapter);
    }

    private void setupSearchBookAdapter() {
        searchBookAdapter = new SearchBookAdapter(requireActivity(), searchBooks, HomeFragment.this);
        binding.rvSearchBook.setAdapter(searchBookAdapter);
    }

    private void getBannersApi() {
        iHttpRequest.getBannerBook().enqueue(getBannerCallBack);
    }

    private void getCategoriesApi() {
        iHttpRequest.getListCategory().enqueue(getCategoriesCallBack);
    }

    private void getBooksApi() {
        iHttpRequest.getListBook().enqueue(getBooksCallBack);
    }

    private void searchBookApi(String nameBook) {
        iHttpRequest.searchBook(new SearchBook(nameBook)).enqueue(searchBookCallback);
    }


    Callback<BannerResponse> getBannerCallBack = new Callback<BannerResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BannerResponse> call, Response<BannerResponse> response) {
            if (response.isSuccessful()) {
                BannerResponse bannerResponse = response.body();
                if (bannerResponse != null && bannerResponse.isStatus()) {
                    //dọn dẹp list slide trước khi addAll phần tử
                    bannerBooks.clear();
                    bannerBooks.addAll(bannerResponse.getData());
                    //refresh lại adapter
                    slideBookAdapter.notifyDataSetChanged();

                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<BannerResponse> call, Throwable t) {
            Log.d("getBooksCallBack", "Failute" + t.getMessage());
        }
    };

    Callback<CategoryResponse> getCategoriesCallBack = new Callback<CategoryResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<CategoryResponse> call, Response<CategoryResponse> response) {
            if (response.isSuccessful()) {
                CategoryResponse categoryResponse = response.body();
                if (categoryResponse != null && categoryResponse.isStatus()) {
                    //tất cả category
                    allCategories = categoryResponse.getData();

                    //phân chia category cho bảng xếp hạng và category còn lại
                    divCategory();

                    //load xong category thì call api lấy danh sách book
                    getBooksApi();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<CategoryResponse> call, Throwable t) {
            Log.d("getCategoriesCallBack", "Failute" + t.getMessage());
        }
    };


    @SuppressLint("NotifyDataSetChanged")
    private void divCategory() {
        //xoá sạch data trong list category, tránh trường hợp add nhiều lần khi call lại api
        categories.clear();
        tops.clear();

        for (Category category : allCategories) {
            //lọc id category = 8, 9, 10: Sách, sách nói, sách thiếu nhi thì đưa vào list bảng xếp hạng
            if (category.getId() == 8 || category.getId() == 9 || category.getId() == 10) {
                tops.add(category);
            } else if (category.getId() == 11) { //Category Sách mới nhất: id_category = 11
                categoryNew = category;
            } else if (category.getId() >= 12 && category.getId() <= 16) { //5 tab sách trong màn Thư viện có id từ 12- 16
                //ko lấy vào màn home
                //KHÔNG XOÁ DÒNG NÀY
            } //category còn lại id từ 1 -> 7
            else categories.add(category);
        }

        //refresh lại category adapter
        categoryAdapter.notifyDataSetChanged();
        //refresh lại top adapter
        topAdapter.notifyDataSetChanged();
    }

    Callback<BookResponse> getBooksCallBack = new Callback<BookResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BookResponse> call, Response<BookResponse> response) {
            if (response.isSuccessful()) {
                BookResponse bookResponse = response.body();
                if (bookResponse != null && bookResponse.isStatus()) {

                    //lấy toàn bộ list book từ response trả về từ api
                    List<Book> books = bookResponse.getData();


                    for (Category category : allCategories) {
                        category.getBooks().clear();
                        List<Book> bookByCategories = new ArrayList<>();
                        for (Book book : books) {
                            if (book.getId_category() == category.getId()) {
                                bookByCategories.add(book);
                            }
                        }
                        category.setBooks(bookByCategories);
                    }

                    //phân chia category cho bảng xếp hạng và category còn lại
                    divCategory();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<BookResponse> call, Throwable t) {
            Log.d("getBooksCallBack", "Failute" + t.getMessage());
        }
    };


    Callback<SearchBookResponse> searchBookCallback = new Callback<SearchBookResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<SearchBookResponse> call, Response<SearchBookResponse> response) {
            if (response.isSuccessful()) {
                SearchBookResponse searchBookResponse = response.body();
                if (searchBookResponse != null && searchBookResponse.isStatus()) {
                    //xoá sạch data trong list searchBooks, tránh trường hợp add nhiều lần khi call lại api
                    searchBooks.clear();
                    //add toàn bộ list searchBooks từ response vào searchBooks
                    searchBooks.addAll(searchBookResponse.getData());
                    //refresh lại searchBookAdapter
                    searchBookAdapter.notifyDataSetChanged();

                    //empty list search book
                    if (searchBooks.isEmpty()) modeScreen = ModeScreen.NO_RESULT;
                    showUIModeScreen();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<SearchBookResponse> call, Throwable t) {
            Log.d("getCategoriesCallBack", "Failute" + t.getMessage());

            //empty list search book
            if (searchBooks.isEmpty()) modeScreen = ModeScreen.NO_RESULT;
            showUIModeScreen();
        }
    };


    public void loadFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

}
