package com.example.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bookstore.R;
import com.example.bookstore.databinding.ItemBankInfoBinding;
import com.example.bookstore.model.BankInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BankInfoAdapter extends RecyclerView.Adapter<BankInfoAdapter.ViewHolder> {

    private Context context;
    private List<BankInfo> bankInfos;
    private Fragment fragment;

    public BankInfoAdapter(Context context, List<BankInfo> bankInfos, Fragment fragment) {
        this.context = context;
        this.bankInfos = bankInfos;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public BankInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBankInfoBinding itemBankInfoBinding = ItemBankInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBankInfoBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull BankInfoAdapter.ViewHolder holder, int position) {
        BankInfo bankInfo = bankInfos.get(position);
        if (bankInfo != null) {
            holder.binding.tvTitle.setText(context.getString(R.string.bank_name_format, bankInfo.getBank_name()));
            holder.binding.tvUserName.setText(bankInfo.getUser_name());
            holder.binding.tvBankName.setText(bankInfo.getBank_name());
            holder.binding.tvBankDetail.setText(bankInfo.getBank_detail());
            holder.binding.tvBankNumber.setText(bankInfo.getBank_number());
        }
    }

    @Override
    public int getItemCount() {
        return bankInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBankInfoBinding binding;

        ViewHolder(ItemBankInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
