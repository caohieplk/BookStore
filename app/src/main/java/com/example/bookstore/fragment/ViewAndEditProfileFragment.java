package com.example.bookstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.R;
import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.databinding.FragmentViewAndEditProfileBinding;
import com.example.bookstore.model.EditProfileResponse;
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


public class ViewAndEditProfileFragment extends Fragment {

    FragmentViewAndEditProfileBinding binding;
    IHttpRequest iHttpRequest;

    //mặc định ở chế độ xem profile, khi click icon edit thì thay đổi sang màn edit profile
    private ModeScreen modeScreen = ModeScreen.VIEW_PROFILE;

    enum ModeScreen {
        VIEW_PROFILE,
        EDIT_PROFILE
    }

    private UserInfo userInfo = null;


    public ViewAndEditProfileFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewAndEditProfileBinding.inflate(inflater, container, false);
        initControl();
        initData();
        initView();
        return binding.getRoot();
    }

    private void initControl() {
        binding.toolbar.imgBack.setOnClickListener(view ->
        {
            if (modeScreen == ModeScreen.EDIT_PROFILE) {
                //khi ở chế độ edit profile thì khi nhấn nút back sẽ show lại UI view profile
                modeScreen = ModeScreen.VIEW_PROFILE;
                updateUIByModeScreen();
            } else {
                //ngược lại ở chế độ view profile, khi nhấn nút back thì quay lại màn ProfileFragment
                back();
            }
        });


        //nút chuyển sang chế độ edit profile
        binding.toolbar.imgTool.setOnClickListener(view -> {
            modeScreen = ModeScreen.EDIT_PROFILE;
            updateUIByModeScreen();
        });


        //khi ở chế độ edit profile, click vào nút cập nhật sẽ cập nhật thông tin profile mới lên server
        binding.btnUpdate.setOnClickListener(view -> validEditProfile());

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
        //mặc định ban đầu màn hình ở mode view thông tin cá nhân
        modeScreen = ModeScreen.VIEW_PROFILE;
        updateUIByModeScreen();
    }

    //dựa vào ModeScreen là view hay edit để show UI tương ứng: ví dụ view thì show: Thông tin cá nhân, còn edit thì show: Thay đổi thông tin
    @SuppressLint("SetTextI18n")
    private void updateUIByModeScreen() {
        switch (modeScreen) {
            case VIEW_PROFILE:
                binding.toolbar.tvTitle.setText("Thông tin cá nhân");
                //cho hiện icon edit ở thanh toolbar, khi click vào đó chuyển sang mode edit profile
                binding.toolbar.imgTool.setImageResource(R.drawable.ic_edit_profile);
                binding.toolbar.imgTool.setVisibility(View.VISIBLE);
                //show data name, email lên edit text name, email tương ứng
                binding.edtName.setText(userInfo.getName());
                binding.edtEmail.setText(userInfo.getEmail());
                //khoá các edit email, name không cho nhập text
                binding.edtName.setEnabled(false);
                binding.edtEmail.setEnabled(false);
                //ẩn button lưu lại
                binding.btnUpdate.setVisibility(View.GONE);
                break;
            case EDIT_PROFILE:
                binding.toolbar.tvTitle.setText("Thay đổi thông tin");
                //cho ẩn icon edit ở thanh toolbar
                binding.toolbar.imgTool.setVisibility(View.GONE);
                //mở khoá các edit email, name để nhập text
                binding.edtName.setEnabled(true);
                binding.edtEmail.setEnabled(true);
                //hiện button lưu lại
                binding.btnUpdate.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void validEditProfile() {
        String name = binding.edtName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        //check nếu password reset không trống thì call api reset password
        if (!name.isEmpty() && !email.isEmpty()) {
            //update lại pass vào object user info
            userInfo.setName(name);
            userInfo.setEmail(email);
            //call api reset password
            editProfileApi();
        }
    }

    public void back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(ViewAndEditProfileFragment.this).commit();
    }


    private void editProfileApi() {
        iHttpRequest.editProfile(userInfo).enqueue(editProfileCallback);
    }

    //api reset password
    Callback<EditProfileResponse> editProfileCallback = new Callback<EditProfileResponse>() {
        @Override
        public void onResponse(@NonNull Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
            if (response.isSuccessful()) {
                EditProfileResponse editProfileResponse = response.body();
                if (editProfileResponse != null && editProfileResponse.isStatus()) {
                    //cập nhật lại user info trong Shared preference
                    PreferenceUtils.saveUserInfo(userInfo);
                    //toast reset pass thành công
                    Toast.makeText(requireActivity(), editProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //sau đó back về màn Profile Fragment
                    back();
                    //update lại thông tin màn Profile Fragment vì đã thay đổi name, password ở màn ViewAndEditProfileFragment này rồi
                    loadFragment(new ProfileFragment());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<EditProfileResponse> call, Throwable t) {
            Log.d("editProfileCallback", "Failute" + t.getMessage());
        }
    };

    public void loadFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }


}