package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.databinding.FragmentResetPasswordBinding;
import com.example.bookstore.model.ResetPasswordResponse;
import com.example.bookstore.model.UserInfo;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPasswordFragment extends Fragment {

    FragmentResetPasswordBinding binding;
    IHttpRequest iHttpRequest;
    private UserInfo userInfo = null;
    private String resetPassword = "";



    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view -> back());

        //button đặt lại mật khẩu
        binding.btnReset.setOnClickListener(view -> validResetPassword());

        //override back button in navigation bar
        final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                back();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        userInfo = PreferenceUtils.getUserInfo();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.toolbar.tvTitle.setText("Đặt lại mật khẩu");

    }

    private void validResetPassword(){
        String resetPassword = binding.edtPassword.getText().toString().trim();
        //check nếu password reset không trống thì call api reset password
          if (!resetPassword.isEmpty()){
              //update lại pass vào object user info
              userInfo.setPassword(resetPassword);
              //call api reset password
              resetPasswordApi();
          }
    }


    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(ResetPasswordFragment.this).commit();
    }


    private void resetPasswordApi() {
        iHttpRequest.resetPassword(userInfo).enqueue(resetPasswordCallback);
    }

    //api reset password
    Callback<ResetPasswordResponse> resetPasswordCallback = new Callback<ResetPasswordResponse>() {
        @Override
        public void onResponse(@NonNull Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
            if (response.isSuccessful()) {
                ResetPasswordResponse resetPasswordResponse = response.body();
                if (resetPasswordResponse != null && resetPasswordResponse.isStatus()) {
                    //cập nhật lại user info trong Shared preference
                    PreferenceUtils.saveUserInfo(userInfo);
                    //toast reset pass thành công
                    Toast.makeText(requireActivity(), resetPasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //sau đó back về màn Profile Fragment
                    back();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResetPasswordResponse> call, Throwable t) {
            Log.d("resetPasswordCallback", "Failute" + t.getMessage());
        }
    };


}