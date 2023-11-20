package com.example.bookstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;



import com.example.bookstore.NotificationDetailActivity;

import com.example.bookstore.RecycleViewInterface;
import com.example.bookstore.adapter.NotificationAdapter;

import com.example.bookstore.databinding.FragmentNotificationBinding;
import com.example.bookstore.dto.ListNotificationResponeDTO;
import com.example.bookstore.dto.NotificationResponeDTO;

import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;



import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements RecycleViewInterface {

    FragmentNotificationBinding binding;

    IHttpRequest iHttpRequest;

    List<NotificationResponeDTO> notification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listNotification.setLayoutManager(linearLayoutManager);
        iHttpRequest.getNotification().enqueue(getNotifiCallback);

        return binding.getRoot();
    }

    Callback<ListNotificationResponeDTO> getNotifiCallback = new Callback<ListNotificationResponeDTO>() {
        @Override
        public void onResponse(Call<ListNotificationResponeDTO> call, Response<ListNotificationResponeDTO> response) {
            if(response.isSuccessful()){
                ListNotificationResponeDTO responeDTO = response.body();
                if(responeDTO.isStatus()){

//                    List<NotificationResponeDTO> notifi = responeDTO.getData();
//                    NotificationAdapter adapter = new NotificationAdapter(notifi);
//                    binding.listNotification.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();

                    notification = responeDTO.getData();
                    if(notification==null){
                        Toast.makeText(getContext(),
                                "Trống không", Toast.LENGTH_LONG).show();
                    }
                    else{
                        NotificationAdapter adapter = new NotificationAdapter(notification, NotificationFragment.this::onItemClick);
                        binding.listNotification.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        }

        @Override
        public void onFailure(Call<ListNotificationResponeDTO> call, Throwable t) {
            Log.d("login","Failute" + t.getMessage());
        }
    };



    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
//        name, price1, price2, day,

        intent.putExtra("NAME",notification.get(position).getName());
        intent.putExtra("PRICE1",notification.get(position).getPrice1());
        intent.putExtra("PRICE2",notification.get(position).getPrice2());
        intent.putExtra("DAY",notification.get(position).getDay());



//        intent.putExtra("PRICE1",thongbao.get(position).getPrice1());
//        intent.putExtra("DAY", thongbao.get(position).getDay());

        startActivity(intent);
    }


}
