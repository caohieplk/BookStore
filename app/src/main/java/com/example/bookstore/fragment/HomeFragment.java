package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.bookstore.R;
import com.example.bookstore.adapter.CategoryAdapter;
import com.example.bookstore.adapter.SearchBookAdapter;
import com.example.bookstore.adapter.SlideBookAdapter;
import com.example.bookstore.adapter.TopAdapter;
import com.example.bookstore.databinding.FragmentHomeBinding;
import com.example.bookstore.dto.BannerResponse;
import com.example.bookstore.dto.Book;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.Category;
import com.example.bookstore.dto.CategoryResponse;
import com.example.bookstore.dto.SearchBook;
import com.example.bookstore.dto.SearchBookResponse;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    //lấy list book normal (không có giá)
    private List<Book> normalBooks = new ArrayList<>();

    //SLIDE BOOK
    private List<Book> slideBooks = new ArrayList<>();
    private SlideBookAdapter slideBookAdapter;

    //cho list bảng xếp hạng top 01, 02, 03
    private List<Category> tops = new ArrayList<>();
    private TopAdapter topAdapter;

    //cho list category
    private List<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    //search book
    private List<Book> searchBooks = new ArrayList<>();
    private SearchBookAdapter searchBookAdapter;


    //chế độ hiển thị UI trong màn home
    enum ModeScreen {
        DEFAULT,
        NO_RESULT,
        RESULT_BOOK
    }

    private ModeScreen modeScreen = ModeScreen.DEFAULT;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                showKeyboard();
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
                if (modeScreen == ModeScreen.DEFAULT){ //ban đầu ở mode default (tức là icon search)
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
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    modeScreen = ModeScreen.RESULT_BOOK;
                    hideKeyboard();
                    searchBookApi(binding.edtSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });




        binding.lnSachTruyen.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnSachNoi.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnTruyenTranh.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnSachPhatGiao.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnSachMoiNhat.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnSachChinhTri.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnHopThu.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));
        binding.lnSachCongGiao.setOnClickListener(view -> loadFragment(new NewBookFragment(normalBooks)));

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

    private void showUIModeScreen(){
        hideKeyboard();
        switch (modeScreen){
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

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireActivity().findViewById(android.R.id.content).getWindowToken(), 0);
            View current = requireActivity().getCurrentFocus();
            if (current != null) current.clearFocus();
        }
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }
    }

    private void setupSlideBookAdapter() {
        slideBookAdapter = new SlideBookAdapter(requireActivity(), slideBooks, HomeFragment.this);
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
        //hard code 3 loại danh sách category top trong bảng xếp hạng
        tops.add(new Category("Sách"));
        tops.add(new Category("Sách nói"));
        tops.add(new Category("Sách thiếu nhi"));
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
                    slideBooks.clear();
                    slideBooks.addAll(bannerResponse.getData());
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
                    //xoá sạch data trong list category, tránh trường hợp add nhiều lần khi call lại api
                    categories.clear();
                    //add toàn bộ list category từ response vào categories
                    categories.addAll(categoryResponse.getData());
                    //refresh lại category adapter
                    categoryAdapter.notifyDataSetChanged();
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


    Callback<BookResponse> getBooksCallBack = new Callback<BookResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BookResponse> call, Response<BookResponse> response) {
            if (response.isSuccessful()) {
                BookResponse bookResponse = response.body();
                if (bookResponse != null && bookResponse.isStatus()) {
                    //lấy toàn bộ list book từ response trả về từ api
                    List<Book> books = bookResponse.getData();

                    //CHO BẢNG XẾP HẠNG
                    //tạo list book của tops
                    List<Book> topBooks = new ArrayList<>();
                    //sau khi lấy được toàn bộ book thì lấy ra các book có top 01, 02, 03 tương ứng đứng top đưa vào top để show lên bảng xếp hạng

                    for (Book book : books) {
                        if (!book.getTop().isEmpty()) {
                            topBooks.add(book);
                        }
                    }
                    //sau khi foreach thì topBooks xếp lộn xộn không theo top 01, 02, 03, lúc này ta cần sort để topBooks theo thứ tự
                    Collections.sort(topBooks, new Comparator<Book>() {
                        @Override
                        public int compare(Book book1, Book book2) {
                            return book1.getTop().compareTo(book2.getTop());
                            //sort chuỗi top "02", "03" ,"01" => thu được "01", "02", "03"
                        }
                    });

                    //add topBooks vùa sort vào list tops
                    for (Category top : tops) {
                        top.setBooks(topBooks);
                    }
                    //cập nhật lại adapter Bảng xếp hạng
                    topAdapter.notifyDataSetChanged();


                    //nếu giá != empty thì dưa vào list category sách bán chạy
                    List<Book> topSellBooks = new ArrayList<>();
                    //làm sạch list trước khi add phần tử
                    normalBooks.clear();
                    for (Book book : books) {
                        if (!book.getPrice().isEmpty()) {
                            topSellBooks.add(book);
                        } else {
                            //nếu giá == empty thì đưa vào list category thường
                            normalBooks.add(book);
                        }
                    }
                    //CHO CATEGORY
                    //đưa toàn bộ book vào các category dưới bảng xếp hạng
                    //dùng foreach, với mỗi category thì add
                    for (Category catergory : categories) {
                        //đối với category: "Sách bán chạy" chỉ lấy sách có giá không bị trống ngược lại thì đưa các sách có giá trị trống vào category khác
                        if (catergory.getName().equals("Sách bán chạy")) {
                            catergory.setBooks(topSellBooks);
                        } else catergory.setBooks(normalBooks);
                    }
                    //refresh lại category adapter
                    categoryAdapter.notifyDataSetChanged();


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
