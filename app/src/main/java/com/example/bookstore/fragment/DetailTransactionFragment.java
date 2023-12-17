package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.R;
import com.example.bookstore.app_util.AppUtils;
import com.example.bookstore.app_util.Constant;
import com.example.bookstore.databinding.FragmentDetailTransactionBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.Transaction;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class DetailTransactionFragment extends Fragment {

    FragmentDetailTransactionBinding binding;
    IHttpRequest iHttpRequest;

    private Transaction transaction;


    public DetailTransactionFragment() {
        // Required empty public constructor
    }

    public DetailTransactionFragment(Transaction transaction) {
        this.transaction = transaction;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailTransactionBinding.inflate(inflater, container, false);
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
        //getAuthorApi();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Chi Tiết Giao Dịch");

        binding.tvTitleTransaction.setText(transaction.getTitle());
        binding.tvCreateAt.setText(AppUtils.formatDate(transaction.getCreated_at(), Constant.yyyyMMddHHmmss, Constant.ddMMyyyy));
        if (transaction.getPrice().isEmpty()) {
            binding.tvPrice.setVisibility(View.GONE);
        } else {
            binding.tvPrice.setVisibility(View.VISIBLE);
            //bước 1: convert string "190000" to double 190000.0
            double price = Double.parseDouble(transaction.getPrice());
            //bước 2: convert double to string format: "190,000 đ"
            String bookPrice = AppUtils.formatMoney(price);
            binding.tvPrice.setText(requireActivity().getString(R.string.book_price_format, bookPrice));
        }
        if (transaction.getStatus() == 1){
            binding.tvStatus.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_07864B));
        } else {
            binding.tvStatus.setTextColor(ContextCompat.getColor(requireActivity(), R.color.color_F7395B));
        }
        binding.tvStatus.setText(transaction.getStatusString());
        binding.tvPayment.setText(transaction.getPayment());
    }


    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(DetailTransactionFragment.this).commit();
    }

}