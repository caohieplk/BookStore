package com.example.bookstore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;
import com.example.bookstore.model.RegisterRequest;
import com.example.bookstore.model.RegisterResponse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtRegisterEmail ,edtRegisterPassword ,edtRegisterName;
    Button btnRegister;
    TextView tvGoSignIn;
    IHttpRequest iHttpRequest;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoSignIn = findViewById(R.id.tvGoSignIn);
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
    }

    public void onGoLoginClick(View view)
    {
        finish();
    }

    public void onRegisterClick(View view)
    {
        String email = edtRegisterEmail.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        String name = edtRegisterName.getText().toString();

        // bat loi
        RegisterRequest requestDTO = new RegisterRequest(name,email,password ,"", 1);
        iHttpRequest.register(requestDTO).enqueue(registerCallback);
    }

    Callback<RegisterResponse> registerCallback = new Callback<RegisterResponse>()
    {
        @Override
        public void onResponse(@NonNull Call<RegisterResponse> call, Response<RegisterResponse> response) {
            if (response.isSuccessful()){
                RegisterResponse responseDTO = response.body();
                if (responseDTO != null && responseDTO.isStatus()) {
                    Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            Toast.makeText(RegisterActivity.this, "Đăng kí thất bại",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(@NonNull Call<RegisterResponse> call, Throwable t) {
            Toast.makeText(RegisterActivity.this, "Đăng kí thất bại",Toast.LENGTH_LONG).show();
            Log.d(">>> login", "onFailure: " + t.getMessage());
        }
    };
}