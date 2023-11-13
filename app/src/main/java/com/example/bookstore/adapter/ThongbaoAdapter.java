package com.example.bookstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.dto.ThongbaoResponseDTO;

import java.util.List;

public class ThongbaoAdapter
        extends RecyclerView.Adapter<ThongbaoAdapter.ViewHolder> {


    private List<ThongbaoResponseDTO>list;

    public ThongbaoAdapter(List<ThongbaoResponseDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thongbao_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_Name
                .setText(list.get(position).getName());
        holder.tv_dola1
                .setText(list.get(position).getPrice1());
        holder.tv_dola2
                .setText(list.get(position).getPrice2());
        holder.tv_day
                .setText(list.get(position).getDay());

        Glide
                .with(holder.itemView.getContext())
                .load(list.get(position).getImage())
                .centerCrop()
                .into(holder.ivAnh);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAnh;
        private TextView tv_Name,tv_dola1,tv_dola2,tv_day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnh=itemView.findViewById(R.id.ivAnh);
            tv_Name=itemView.findViewById(R.id.tv_Name);
            tv_dola1=itemView.findViewById(R.id.tv_dola1);
            tv_dola2=itemView.findViewById(R.id.tv_dola2);
            tv_day=itemView.findViewById(R.id.tv_day);

        }
    }

}
