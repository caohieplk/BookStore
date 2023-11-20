package com.example.bookstore.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.R;
import com.example.bookstore.RecycleViewInterface;
import com.example.bookstore.databinding.NotificationItemBinding;

import com.example.bookstore.dto.NotificationResponeDTO;

import java.util.List;

public class NotificationAdapter extends
        RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    //List<NotificationResponeDTO> list;

    private final RecycleViewInterface recycleViewInterface;
    private List<NotificationResponeDTO>list;



    public NotificationAdapter(List<NotificationResponeDTO> list , RecycleViewInterface recycleViewInterface){
        this.list = list;
        this.recycleViewInterface = recycleViewInterface;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.tv_Name
//                .setText(list.get(position).getName());
//        holder.tv_dola1
//                .setText(list.get(position).getPrice1());
//        holder.tv_dola2
//                .setText(list.get(position).getPrice2());
//        holder.tv_day
//                .setText(list.get(position).getDay());
//
//        Glide
//                .with(holder.itemView.getContext())
//                .load(list.get(position).getImage())
//                .centerCrop()
//                .into(holder.ivAnh);


        holder.binding.tvName
                .setText(list.get(position).getName());
        holder.binding.tvDola1
                .setText(String.valueOf(list.get(position).getPrice1()));
        holder.binding.tvDola2
                .setText(String.valueOf(list.get(position).getPrice2()));
        holder.binding.tvDay
                .setText(String.valueOf(list.get(position).getDay()));
        Glide
                .with(holder.itemView.getContext())
                .load(list.get(position).getImage())
                .centerCrop()
                .into(holder.binding.ivAnh);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view, recycleViewInterface);


    }



    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView  tv_Name,tv_dola1,tv_dola2,tv_day,btnMuaSach;
//        ImageView ivAnh;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            ivAnh = itemView.findViewById(R.id.ivAnh);
//            tv_Name = itemView.findViewById(R.id.tv_Name);
//            tv_dola1 = itemView.findViewById(R.id.tv_dola1);
//            tv_dola2 = itemView.findViewById(R.id.tv_dola2);
//            tv_day = itemView.findViewById(R.id.tv_day);
//            btnMuaSach = itemView.findViewById(R.id.btnMuaSach);
//        }
//        private ImageView ivAnh;
//        private TextView tv_Name,tv_dola1,tv_dola2,tv_day;
        NotificationItemBinding binding;
        public ViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);

//            ivAnh=itemView.findViewById(R.id.ivAnh);
//            tv_Name=itemView.findViewById(R.id.tv_Name);
//            tv_dola1=itemView.findViewById(R.id.tv_dola1);
//            tv_dola2=itemView.findViewById(R.id.tv_dola2);
//            tv_day=itemView.findViewById(R.id.tv_day);

            binding = NotificationItemBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            try{
                                recycleViewInterface.onItemClick(pos);
                                Toast.makeText(view.getContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Log.d("lord",">>>>" + e);
                            }

                        }
                    }
                }
            });

        }
    }
}
