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
import com.example.bookstore.adapter.ThongbaoAdapter;
import com.example.bookstore.databinding.FragmentHomeBinding;
import com.example.bookstore.databinding.FragmentNotificationBinding;
import com.example.bookstore.dto.ListNotificationResponeDTO;
import com.example.bookstore.dto.ListThongbaoResponeDTO;
import com.example.bookstore.dto.NotificationResponeDTO;
import com.example.bookstore.dto.ThongbaoResponseDTO;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    IHttpRequest iHttpRequest;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.listThongBao.setLayoutManager(linearLayoutManager);
        iHttpRequest.getThongBao().enqueue(getThongBaoCallback);

        return binding.getRoot();
    }


    Callback<ListThongbaoResponeDTO> getThongBaoCallback = new Callback<ListThongbaoResponeDTO>() {
        @Override
        public void onResponse(Call<ListThongbaoResponeDTO> call, Response<ListThongbaoResponeDTO> response) {
            if(response.isSuccessful()){
                ListThongbaoResponeDTO responeDTO = response.body();
                if(responeDTO.isStatus()){
                    List<ThongbaoResponseDTO> thongbao = responeDTO.getData();
                    ThongbaoAdapter adapter = new ThongbaoAdapter(thongbao);
                    binding.listThongBao.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(Call<ListThongbaoResponeDTO> call, Throwable t) {
            Log.d("login","Failute" + t.getMessage());
        }
    };

}
