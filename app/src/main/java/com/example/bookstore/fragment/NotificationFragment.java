package com.example.bookstore.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.adapter.NotificationAdapter;
import com.example.bookstore.databinding.FragmentNotificationBinding;
import com.example.bookstore.dto.ListNotificationResponeDTO;
import com.example.bookstore.dto.NotificationResponeDTO;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.Notification;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;

    IHttpRequest iHttpRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_notification, container, false);
//    }

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
                    List<NotificationResponeDTO> notifi = responeDTO.getData();
                    NotificationAdapter adapter = new NotificationAdapter(notifi);
                    binding.listNotification.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(Call<ListNotificationResponeDTO> call, Throwable t) {
            Log.d("login","Failute" + t.getMessage());
        }
    };


}
