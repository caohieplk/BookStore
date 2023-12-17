package com.example.bookstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.LoginActivity;
import com.example.bookstore.R;
import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.databinding.FragmentProfileBinding;
import com.example.bookstore.model.LogoutResponse;
import com.example.bookstore.app_util.TypeLogin;
import com.example.bookstore.model.UserInfo;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    IHttpRequest iHttpRequest;
    private UserInfo userInfo = null;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {

        //button xem thông tin cá nhân
        binding.lnProfile.setOnClickListener(view -> openViewAndEditProfileScreen());

        //button reset password
        binding.lnResetPassword.setOnClickListener(view -> openResetPasswordScreen());

        //button log out app
        binding.lnLogout.setOnClickListener(view -> logoutApi());

    }

    private void openViewAndEditProfileScreen() {
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ViewAndEditProfileFragment()).commit();
    }

    private void openResetPasswordScreen() {
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ResetPasswordFragment()).commit();
    }

    private void initData() {
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        userInfo = PreferenceUtils.getUserInfo();
    }

    private void initView() {
        fillUserInfoDataToView();

    }

    private void fillUserInfoDataToView() {
        binding.tvName.setText(userInfo.getName());
        binding.tvEmail.setText(userInfo.getEmail());
    }


    private void logoutApi() {
        //call api để logout khỏi seasion trên server
        iHttpRequest.logout().enqueue(logoutCallback);
        //nếu login bằng google thì cần thêm bước logout google
        if (userInfo.getTypeLogin() == TypeLogin.GOOGLE){
            logoutGoogle();
        }
    }



    //api log out app
    Callback<LogoutResponse> logoutCallback = new Callback<LogoutResponse>() {
        @Override
        public void onResponse(@NonNull Call<LogoutResponse> call, Response<LogoutResponse> response) {
            if (response.isSuccessful()) {
                LogoutResponse logoutResponse = response.body();
                if (logoutResponse != null && logoutResponse.isStatus()) {
                    //xoá sạch data
                    PreferenceUtils.saveUserInfo(null);
                    //toast logout thành công
                    Toast.makeText(requireActivity(), logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //sau đó back về màn login
                    gotoLoginScreen();
                    //finish màn hình MainActivity đi
                    requireActivity().finish();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<LogoutResponse> call, Throwable t) {
            Log.d("logoutCallback", "Failute" + t.getMessage());
        }
    };

    public void logoutGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions);
        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.v("LOGOUT_GOOGLE_SUCCESS", "__________________");
                    }
                });
    }


    public void gotoLoginScreen() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
