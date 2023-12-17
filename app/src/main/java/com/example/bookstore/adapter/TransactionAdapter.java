package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.R;
import com.example.bookstore.app_util.AppUtils;
import com.example.bookstore.app_util.Constant;
import com.example.bookstore.databinding.ItemTransactionBinding;
import com.example.bookstore.fragment.DetailTransactionFragment;
import com.example.bookstore.model.Transaction;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactions;
    private Fragment fragment;

    private OnClickItemListener clickListener;

    public TransactionAdapter(Context context,List<Transaction> transactions, Fragment fragment, OnClickItemListener clickListener) {
        this.context = context;
        this.transactions = transactions;
        this.fragment = fragment;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding itemTransactionBinding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemTransactionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        if (transaction != null) {
            holder.binding.tvTitle.setText(transaction.getTitle());
            holder.binding.tvCreateAt.setText(AppUtils.formatDate(transaction.getCreated_at(), Constant.yyyyMMddHHmmss, Constant.ddMMyyyy));
            if (transaction.getPrice().isEmpty()) {
                holder.binding.tvPrice.setVisibility(View.GONE);
            } else {
                holder.binding.tvPrice.setVisibility(View.VISIBLE);
                //bước 1: convert string "190000" to double 190000.0
                double price = Double.parseDouble(transaction.getPrice());
                //bước 2: convert double to string format: "190,000 đ"
                String bookPrice = AppUtils.formatMoney(price);
                holder.binding.tvPrice.setText(context.getString(R.string.book_price_format, bookPrice));
            }
            holder.binding.tvStatus.setText(transaction.getStatusString());
            //set màu: đã thanh toán thì text màu xanh, chưa thanh toán thì text màu đỏ
            if (transaction.getStatus() == 1){
                holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_07864B));
            } else {
                holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_F7395B));
            }

            //Xoá 1 transaction
            holder.binding.lnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onDeleteItem(transaction, position);
                }
            });

            //click item để mở màn chi tiết sách
            holder.binding.getRoot().setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sang màn chi tiết sách: truyền data đối tượng book sang để hiển thị
                fragment.requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailTransactionFragment(transaction)).commit();
            });
        }

    }



    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public interface OnClickItemListener {
        void onDeleteItem(Transaction transaction, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;

        ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
