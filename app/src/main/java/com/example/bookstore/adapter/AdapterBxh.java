package com.example.bookstore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterBxh extends RecyclerView.Adapter<AdapterBxh.ViewHolder>{

    private List<String> list;

    public AdapterBxh(List<String> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public AdapterBxh.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bxh,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBxh.ViewHolder holder, int position) {
        String item = list.get(position);
        holder.tv.setText(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.txtSach);
        }
    }
}
