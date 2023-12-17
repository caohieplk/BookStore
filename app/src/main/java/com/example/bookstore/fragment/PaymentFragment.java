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
import com.example.bookstore.adapter.BankInfoAdapter;
import com.example.bookstore.app_util.AppUtils;
import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.app_util.event_bus.MessageEvent;
import com.example.bookstore.databinding.FragmentPaymentBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.BankInfo;
import com.example.bookstore.model.BankInfoResponse;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.Transaction;
import com.example.bookstore.model.TransactionResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentFragment extends Fragment {

    FragmentPaymentBinding binding;
    IHttpRequest iHttpRequest;

    private Book book;
    private Category category;

    //for calculator book price
    //giá gốc 1 cuốn sách
    private double originalPrice = 0.0;
    //mặc định số lượng 1 cuốn sách
    private int quantity = 1;

    //list thông tin chuyển khoản
    private List<BankInfo> bankInfos = new ArrayList<>();
    private BankInfoAdapter bankInfoAdapter;


    public PaymentFragment() {
        // Required empty public constructor
    }

    public PaymentFragment(Book book, Category category) {
        this.book = book;
        this.category = category;
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

    @SuppressLint("ClickableViewAccessibility")
    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view2 -> back());

        //click nút thanh toán, bộ phận sell sẽ liên hệ với user để xác nhận đơn hàng dựa vào thông tin user profile
        binding.btnPayment.setOnClickListener(view2 -> {
            //check user đã nhập tên chủ tài khoản, ngân hàng, số tài khoản chưa thì thông báo thanh toán thành công
            validPayment();
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

    private void validPayment() {
        String userName = binding.edtUserName.getText().toString().trim();
        String bankName = binding.edtBankName.getText().toString().trim();
        String bankNumber = binding.edtBankNumber.getText().toString().trim();

        //nếu tất cả các editext chủ tài khoản, tên ngân hàng, số tài khoản không trống thì thông báo thanh toán thành công
        if (!userName.isEmpty() && !bankName.isEmpty() && !bankNumber.isEmpty()) {
            //ẩn bản phím
            AppUtils.hideKeyboard(requireActivity());
            //show thông báo
            makeTransactionApi();
        } else {
            //ngược lại thì thông báo vui lòng nhập đủ thông tin chuyển khoản
            Toast.makeText(requireActivity(), getString(R.string.please_enter_bank_info), Toast.LENGTH_SHORT).show();

        }
    }

    public void loadFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        //call api lấy danh sách thông tin chuyển khoản
        getBankInfoApi();
        //bước 1: convert string "190000" to double 190000.0
        originalPrice = Double.parseDouble(book.getPrice());
        //default 1 book
        quantity = 1;
    }

    //tính số tiền tạm tính (trước thuế)
    private double calSubTotal() {
        return quantity * originalPrice;
    }

    //tổng số tiền
    private double total() {
        return calSubTotal();
    }

    //đưa giá trị show lên view: text view số lượng, tiền tạm tính, thuế, tổng tiền
    private void updateUIMoney() {
        //fill data to view
        //tính tiền
        double subTotal = calSubTotal();
        //sub total = quantity x original price, ví dụ: 2 x 190.000 vnđ
        binding.tvSubTotal.setText(getString(R.string.price_format_quantity, quantity, AppUtils.formatMoney(originalPrice)));
        double total = total();
        binding.tvTotal.setText(getString(R.string.price_format_total, AppUtils.formatMoney(total)));
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Thanh Toán");
        //set ảnh book
        Glide.with(requireActivity()).load(book.getImage()).centerCrop().into(binding.imgBook);
        //set tên book
        binding.tvName.setText(book.getName());
        //binding.tvAuthor.setText(book.getAuthor());

        //check nếu giá != empty thì show giá ngược lại thì ẩn btnBuyBook đi
        if (book.getPrice().isEmpty()) {
            binding.tvPrice.setVisibility(View.GONE);
        } else {
            binding.tvPrice.setVisibility(View.VISIBLE);
            //bước 1: convert string "190000" to double 190000.0
            double price = Double.parseDouble(book.getPrice());
            //bước 2: convert double to string format: "190,000 đ"
            String bookPrice = AppUtils.formatMoney(price);
            binding.tvPrice.setText(requireActivity().getString(R.string.book_price_format, bookPrice));
        }

        //show tiền
        updateUIMoney();
        //setup adapter thông tin ngân hàng
        setupBankInfoAdapter();
    }


    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(PaymentFragment.this).commit();
    }


    private void setupBankInfoAdapter() {
        bankInfoAdapter = new BankInfoAdapter(requireActivity(), bankInfos, PaymentFragment.this);
        binding.rvBankInfo.setAdapter(bankInfoAdapter);
    }

    private void getBankInfoApi() {
        iHttpRequest.getBankInfos().enqueue(bankInfoCallback);
    }

    //api lấy thông tin ngân hàng để user tự chuyển khoản
    Callback<BankInfoResponse> bankInfoCallback = new Callback<BankInfoResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<BankInfoResponse> call, Response<BankInfoResponse> response) {
            if (response.isSuccessful()) {
                BankInfoResponse bankInfoResponse = response.body();
                if (bankInfoResponse != null && bankInfoResponse.isStatus()) {
                    //xoá sạch data trong list bankInfos, tránh trường hợp add nhiều lần khi call lại api
                    bankInfos.clear();
                    //add toàn bộ list bankInfos từ bankInfoResponse vào bankInfos
                    bankInfos.addAll(bankInfoResponse.getData());
                    //refresh lại bankInfoAdapter
                    bankInfoAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<BankInfoResponse> call, Throwable t) {
            Log.d("bankInfoCallback", "Failute" + t.getMessage());
        }
    };


    private void makeTransactionApi() {
        int id_book = book.getId();
        int id_user = Objects.requireNonNull(PreferenceUtils.getUserInfo()).getId();
        int status = 1; //0: Thanh toán thất bại, 1: Đã thanh toán
        String title = "Thanh toán mở khoá sách: " + book.getName();
        String created_at = AppUtils.getNowDate();
        double total = total();
        String price = String.valueOf(total);
        String payment = "Chuyển khoản ngân hàng";
        iHttpRequest.makeTransaction(new Transaction(id_book, id_user, status, title, created_at, price, payment)).enqueue(makeTransactionCallback);
    }

    Callback<TransactionResponse> makeTransactionCallback = new Callback<TransactionResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<TransactionResponse> call, Response<TransactionResponse> response) {
            if (response.isSuccessful()) {
                TransactionResponse transactionResponse = response.body();
                if (transactionResponse != null && transactionResponse.isStatus()) {
                    Toast.makeText(requireActivity(), transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //back về màn trước
                    back();
                    //bắn event đã mua sách để mở khoá đọc sách
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.PURCHASED_BOOK));

                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<TransactionResponse> call, Throwable t) {
            Log.d("bankInfoCallback", "Failute" + t.getMessage());
            Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

}