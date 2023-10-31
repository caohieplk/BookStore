package com.example.bookstore.Adapter;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.dto.ItemSach;
import com.example.bookstore.fragment.DetailSachFragment;
import com.example.bookstore.fragment.HomeFragment;

import java.util.List;

public class AdapterSach extends RecyclerView.Adapter<AdapterSach.ViewHolder> {

    private Context context;
    private List<ItemSach> itemSachList;
    private Fragment fragment;

    public AdapterSach(Context context, List<ItemSach> itemSachList, Fragment fragment) {
        this.context = context;
        this.itemSachList = itemSachList;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public AdapterSach.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSach.ViewHolder holder, int position) {
            ItemSach itemSach = itemSachList.get(position);
            holder.tvName.setText(itemSach.getName());
            holder.imgSach.setImageResource(itemSach.getImage());

            holder.itemView.setOnClickListener(view -> {
                //chuyển màn hình khi click vào item sách sẽ ra lyout rỗng
                fragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new DetailSachFragment()).commit();
            });
    }

    @Override
    public int getItemCount() {
        return itemSachList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgSach;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtSach);
            imgSach = itemView.findViewById(R.id.imgSach);
        }
    }
}
