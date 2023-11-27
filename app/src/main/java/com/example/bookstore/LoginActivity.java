package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookstore.dto.LoginRequestDTO;
import com.example.bookstore.dto.LoginResponseDTO;
import com.example.bookstore.helpers.HttpRequest;
import com.example.bookstore.helpers.IHttpRequest;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;

    Button btnLogin;

    IHttpRequest iHttpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        iHttpRequest = HttpRequest.createService(IHttpRequest.class);
        edtEmail.setText("t@gmail.com");
        edtPassword.setText("1");
    }

    public void onLoginClick(View view)
    {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        LoginRequestDTO requestDTO = new LoginRequestDTO(email,password);
        iHttpRequest.login(requestDTO).enqueue(login);
    }

    public void onGoRegisterClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this,
                RegisterActivity.class);
        startActivity(intent);
    }

    Callback<LoginResponseDTO> login = new Callback<LoginResponseDTO>() {
        @Override
        public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
            if (response.isSuccessful()){
                LoginResponseDTO loginResponse = response.body();

                if (loginResponse.isStatus()){
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this,
                            " That bai rui kia hmmmmm  ",Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
            Log.d(">>> login", "onFailure: " + t.getMessage());
        }
    };

}