package com.example.bookstore;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.databinding.ActivityNotificationDetailBinding;

public class NotificationDetailActivity extends AppCompatActivity {

    ActivityNotificationDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //        name, price1, price2, day
        String name = getIntent().getStringExtra("NAME");
        String price1 = getIntent().getStringExtra("PRICE1");
        String price2 = getIntent().getStringExtra("PRICE2");
        String day = getIntent().getStringExtra("DAY");

        //int price1 = getIntent().getIntExtra("PRICE1",0);
//        String price2 = getIntent().getStringExtra("PRICE2");
//        String day = getIntent().getStringExtra("DAY");


        binding.name.setText(name);
        binding.price1.setText(price1);
        binding.price2.setText(price2);
        binding.day.setText(day);
    }
}
