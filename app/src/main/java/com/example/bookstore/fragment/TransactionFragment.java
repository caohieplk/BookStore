package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.adapter.TransactionAdapter;
import com.example.bookstore.databinding.FragmentTransactionBinding;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.BaseResponse;
import com.example.bookstore.model.Transaction;
import com.example.bookstore.model.TransactionResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

    //view binding
    FragmentTransactionBinding binding;
    IHttpRequest iHttpRequest;


    //search book
    private List<Transaction> transactions = new ArrayList<>();
    private TransactionAdapter transactionAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionBinding.inflate(inflater, container, false);

        initControl();
        initData();
        initView();

        return binding.getRoot();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initControl() {

    }


    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        //call api lấy danh sách banner book
        getTransactionsApi();
    }

    private void initView() {
        binding.toolbar.imgBack.setVisibility(View.GONE);
        binding.toolbar.tvTitle.setText("Giao Dịch Của Bạn");
        //setup transaction adapter
        setupTransactionAdapter();
    }

    private void setupTransactionAdapter() {
        transactionAdapter = new TransactionAdapter(requireActivity(), transactions, TransactionFragment.this, new TransactionAdapter.OnClickItemListener() {
            @Override
            public void onDeleteItem(Transaction transaction, int position) {
                deleteTransactionsApi(transaction, position);
            }
        });
        binding.rvTransaction.setAdapter(transactionAdapter);
    }


    private void showUI() {
        if (transactions.isEmpty()) {
            binding.lnNoResult.setVisibility(View.VISIBLE);
            binding.rvTransaction.setVisibility(View.GONE);
        } else {
            binding.lnNoResult.setVisibility(View.GONE);
            binding.rvTransaction.setVisibility(View.VISIBLE);
        }
    }

    private void getTransactionsApi() {
        iHttpRequest.getTransactions().enqueue(getTransactionsCallback);
    }


    Callback<TransactionResponse> getTransactionsCallback = new Callback<TransactionResponse>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResponse(@NonNull Call<TransactionResponse> call, Response<TransactionResponse> response) {
            if (response.isSuccessful()) {
                TransactionResponse transactionResponse = response.body();
                if (transactionResponse != null && transactionResponse.isStatus()) {
                    //xoá sạch data trong list searchBooks, tránh trường hợp add nhiều lần khi call lại api
                    transactions.clear();
                    //add toàn bộ list searchBooks từ response vào searchBooks
                    transactions.addAll(transactionResponse.getData());
                    //refresh lại searchBookAdapter
                    transactionAdapter.notifyDataSetChanged();
                    showUI();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<TransactionResponse> call, Throwable t) {
            Log.d("getCategoriesCallBack", "Failute" + t.getMessage());
            showUI();
        }
    };


    private void deleteTransactionsApi(Transaction transaction, int position) {
        iHttpRequest.deleteTransaction(new Transaction(transaction.getId())).enqueue(new Callback<BaseResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null && baseResponse.isStatus()) {
                        //xoá thành công 1 transaction
                        Toast.makeText(requireActivity(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        transactions.remove(position);
                        //refresh lại transactionAdapter
                        transactionAdapter.notifyDataSetChanged();
                        showUI();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, Throwable t) {
                Log.d("deleteTransCallback", "Failute" + t.getMessage());
                showUI();
            }
        });
    }

}
