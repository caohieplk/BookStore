package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.LoginGoogleRequest;
import com.example.bookstore.model.LoginRequest;
import com.example.bookstore.model.LoginResponse;
import com.example.bookstore.app_util.TypeLogin;
import com.example.bookstore.model.UserInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;

    Button btnLogin;
    ImageView imgGoogle;

    IHttpRequest iHttpRequest;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //khởi tạo google option và google client
        initGoogleSignIn();

        //kiểm tra user đã đăng nhập vào ứng dụng này bằng Google hay chưa
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        //updateUI(googleSignInAccount);
        logoutGoogle();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        imgGoogle = findViewById(R.id.imgGoogle);
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        edtEmail.setText("");
        edtPassword.setText("");
    }

    public void logoutGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);
        mGoogleSignInClient.signOut().addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.v("LOGOUT_GOOGLE_SUCCESS", "__________________");
                    }
                });
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Toast.makeText(this, "Tự động login với Google thành công", Toast.LENGTH_LONG).show();
            gotoMainActivity();
        }
    }

    public void initGoogleSignIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);
    }

    public void onLoginClick(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        LoginRequest requestDTO = new LoginRequest(email, password);
        iHttpRequest.login(requestDTO).enqueue(login);
    }

    public void gotoRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onLoginWithGoogle(View view) {
        //
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //google trả về access token, dùng access token này push lên server thông qua api auth-google để server xác thực
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
            LoginGoogleRequest loginGoogleRequest = new LoginGoogleRequest(googleSignInAccount.getIdToken());
            //call api loginWithGoogle
            iHttpRequest.loginWithGoogle(loginGoogleRequest).enqueue(loginGoogleCallBack);
        } catch (ApiException e) {
            Log.w(">>> handleSignInResult", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    Callback<LoginResponse> login = new Callback<LoginResponse>() {
        @Override
        public void onResponse(@NonNull Call<LoginResponse> call, Response<LoginResponse> response) {
            if (response.isSuccessful()) {
                LoginResponse loginResponse = response.body();
                if (loginResponse != null && loginResponse.isStatus()) {
                    Toast.makeText(LoginActivity.this, "Login thành công", Toast.LENGTH_SHORT).show();
                    //lưu lại user info
                    UserInfo userInfo = loginResponse.getData();
                    userInfo.setTypeLogin(TypeLogin.EMAIL);
                    PreferenceUtils.saveUserInfo(userInfo);
                    //chuyển sang màn MainActivity
                    gotoMainActivity();
                    //dóng màn LoginActivity lại
                    finish();
                    return;
                }
            }
            Toast.makeText(LoginActivity.this, "Login thất bại!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
            Log.d(">>> login", "onFailure: " + t.getMessage());
            Toast.makeText(LoginActivity.this, "Login thất bại!", Toast.LENGTH_SHORT).show();
        }
    };


    Callback<LoginResponse> loginGoogleCallBack = new Callback<LoginResponse>() {
        @Override
        public void onResponse(@NonNull Call<LoginResponse> call, Response<LoginResponse> response) {
            if (response.isSuccessful()) {
                LoginResponse loginResponse = response.body();
                if (loginResponse != null && loginResponse.isStatus()) {
                    Toast.makeText(LoginActivity.this, "Login thành công", Toast.LENGTH_SHORT).show();
                    //lưu lại user info
                    UserInfo userInfo = loginResponse.getData();
                    userInfo.setTypeLogin(TypeLogin.GOOGLE);
                    PreferenceUtils.saveUserInfo(userInfo);
                    //chuyển sang màn MainActivity
                    gotoMainActivity();
                    //dóng màn LoginActivity lại
                    finish();
                    return;
                }
            }
            Log.d(">>> loginGoogleCallBack", "onResponse: " + new Gson().toJson(response));
            Toast.makeText(LoginActivity.this, "Login thất bại!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(@NonNull Call<LoginResponse> call, Throwable t) {
            Log.d(">>> loginGoogleCallBack", "onFailure: " + t.getMessage());
            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

}