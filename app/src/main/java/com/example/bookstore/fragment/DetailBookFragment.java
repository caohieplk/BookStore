package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.app_util.AppUtils;
import com.example.bookstore.app_util.Constant;
import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.app_util.event_bus.MessageEvent;
import com.example.bookstore.databinding.FragmentDetailBookBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.AuthorRequest;
import com.example.bookstore.model.AuthorResponse;
import com.example.bookstore.model.BaseResponse;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.Transaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailBookFragment extends Fragment {

    FragmentDetailBookBinding binding;
    IHttpRequest iHttpRequest;

    private Book book;
    private Category category;
    private Author author = null;

    private boolean isLock = true;
    private boolean isFavorite = false;


    public DetailBookFragment() {
        // Required empty public constructor
    }

    public DetailBookFragment(Book book, Category category) {
        this.book = book;
        this.category = category;
    }

    public DetailBookFragment(boolean isLock, Book book, Category category) {
        this.isLock = isLock;
        this.book = book;
        this.category = category;
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
        binding.toolbar.imgTool.setVisibility(View.VISIBLE);
        binding.toolbar.imgTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //khi image đang là favorite => click vào thì chuyển unfavorite
                if (isFavorite){
                    unFavoriteBookApi();
                } else {
                    favoriteBookApi();
                }
            }
        });
        binding.btnReadBook.setOnClickListener(view -> {
            if (isLock && !book.getPrice().isEmpty()) {
                //sách bị khóa, cần thanh toán moi cho đọc
                Toast.makeText(requireActivity(), "Vui lòng thanh toán để mở khóa cuốn sách này", Toast.LENGTH_SHORT).show();
            } else {
                //nếu thanh toán thành công thì cho đọc sách
                openReadBookScreen();
            }
        });
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
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        getAuthorApi();
        checkFavoriteBookApi();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Chi Tiết Sách");
        //set ảnh book
        Glide.with(requireActivity()).load(book.getImage()).centerCrop().into(binding.imgBook);
        //set tên book
        binding.tvName.setText(book.getName());
        binding.tvContent.setText(book.getDescription());
        binding.tvReader.setText(String.valueOf(book.getRead_count()));
        if (category != null) {
            binding.tvCategory.setText(category.getName());
        }
        binding.tvCreateAt.setText(AppUtils.formatDate(book.getCreated_at(), Constant.yyyyMMddHHmmss, Constant.ddMMyyyy));

        //check nếu giá != empty thì show giá ngược lại thì ẩn btnBuyBook đi

        if (book.getPrice().isEmpty()) {
            binding.btnBuyBook.setVisibility(View.GONE);
            binding.tvPrice.setVisibility(View.GONE);
        } else {
            //kiểm tra cuốn sách này đã mua thành công hay chưa, nếu chưa thành công, có thể mua lại
            checkTransactionBookApi();
            binding.btnBuyBook.setVisibility(View.VISIBLE);
            binding.tvPrice.setVisibility(View.VISIBLE);
            //bước 1: convert string "190000" to double 190000.0
            double price = Double.parseDouble(book.getPrice());
            //bước 2: convert double to string format: "190,000 đ"
            String bookPrice = AppUtils.formatMoney(price);
            binding.tvPrice.setText(requireActivity().getString(R.string.book_price_format, bookPrice));
        }
    }

    private void showUIButtonBuyBook(boolean isLock) {
        if (isLock) {
            binding.tvBuyBook.setText(getString(R.string.buy_book));
            binding.btnBuyBook.setEnabled(true);
            binding.btnBuyBook.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_46B0F2));
        } else {
            binding.tvBuyBook.setText(getString(R.string.bought_book));
            binding.btnBuyBook.setEnabled(false);
            binding.btnBuyBook.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.color_8A8A8A));
        }
    }

    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(DetailBookFragment.this).commit();
    }

    public void loadFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void openReadBookScreen() {
        //truyền đối tượng book sang để hiển thị tên sách, content sách, tác giả
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ChapterFragment(book, author)).commit();

    }

    private void openPaymentScreen() {
        //truyền đối tượng book sang để tạo payment
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new PaymentFragment(book, category)).commit();

    }


    private void getAuthorApi() {
        iHttpRequest.getAuthor(new AuthorRequest(book.getId_author())).enqueue(getAuthorCallBack);
    }


    Callback<AuthorResponse> getAuthorCallBack = new Callback<AuthorResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<AuthorResponse> call, Response<AuthorResponse> response) {
            if (response.isSuccessful()) {
                AuthorResponse authorResponse = response.body();
                if (authorResponse != null && authorResponse.isStatus()) {
                    author = authorResponse.getData();
                    //set thông tin author vào text view
                    binding.tvAuthor.setText(author.getName());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<AuthorResponse> call, Throwable t) {
            Log.d("getAuthorCallBack", "Failute" + t.getMessage());
        }
    };


    private void checkTransactionBookApi() {
        iHttpRequest.checkTransaction(new Transaction(book.getId(), Objects.requireNonNull(PreferenceUtils.getUserInfo()).getId())).enqueue(checkTransactionCallBack);
    }

    Callback<BaseResponse> checkTransactionCallBack = new Callback<BaseResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BaseResponse> call, Response<BaseResponse> response) {
            if (response.isSuccessful()) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.isStatus()) {
                    //sách đã được thanh toán
                    showUIButtonBuyBook(false);
                    isLock = false;
                } else showUIButtonBuyBook(true);
            }
        }

        @Override
        public void onFailure(@NonNull Call<BaseResponse> call, Throwable t) {
            showUIButtonBuyBook(true);
            Log.d("checkTransCallBack", "Failute" + t.getMessage());
        }
    };

    private void changeUIButtonFavorite() {
        if (isFavorite) {
            binding.toolbar.imgTool.setImageResource(R.drawable.ic_favorited);
        } else {
            binding.toolbar.imgTool.setImageResource(R.drawable.ic_un_favorite);
        }
    }

    private void checkFavoriteBookApi() {
        iHttpRequest.checkFavorite(new Book(book.getId())).enqueue(checkFavoriteCallBack);
    }

    Callback<BaseResponse> checkFavoriteCallBack = new Callback<BaseResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BaseResponse> call, Response<BaseResponse> response) {
            if (response.isSuccessful()) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null && baseResponse.isStatus()) {
                    //sách đã được thanh toán
                    isFavorite = true;
                } else {
                    isFavorite = false;
                    showUIButtonBuyBook(true);
                }
            }
            changeUIButtonFavorite();
        }

        @Override
        public void onFailure(@NonNull Call<BaseResponse> call, Throwable t) {
            isFavorite = false;
            changeUIButtonFavorite();
            showUIButtonBuyBook(true);
            Log.d("checkTransCallBack", "Failute" + t.getMessage());
        }
    };


    private void unFavoriteBookApi() {
        iHttpRequest.unFavorite(new Book(book.getId())).enqueue(new Callback<BaseResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isStatus()) {
                        //bỏ yêu thích thành công
                        isFavorite = false;
                        changeUIButtonFavorite();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.CHANGE_STATE_FAVORITE));
                    }
                    Toast.makeText(requireActivity(), Objects.requireNonNull(baseResponse).getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                Log.d("deleteTransCallback", "Failute" + t.getMessage());
                //xoá yêu thích thất bại
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                changeUIButtonFavorite();
            }
        });
    }

    private void favoriteBookApi() {
        iHttpRequest.favoriteBook(new Book(book.getId())).enqueue(new Callback<BaseResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isStatus()) {
                        //yêu thích thành công
                        isFavorite = true;
                        changeUIButtonFavorite();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.CHANGE_STATE_FAVORITE));
                    }
                    assert baseResponse != null;
                    Toast.makeText(requireActivity(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                Log.d("favoriteBook", "Failute" + t.getMessage());
                //xoá yêu thích thất bại
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (event != null && (event.getMsg().equals(MessageEvent.PURCHASED_BOOK))) {
            isLock = false;
            //sách đã được thanh toán
            showUIButtonBuyBook(false);
        }
    }

}