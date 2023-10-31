package com.example.bookstore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bookstore.databinding.ActivityMainBinding;
import com.example.bookstore.fragment.ExtendedFragment;
import com.example.bookstore.fragment.HomeFragment;
import com.example.bookstore.fragment.HomesFragment;
import com.example.bookstore.fragment.NotificationFragment;
import com.example.bookstore.fragment.SchedulesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new HomeFragment());
            transaction.commit();
            //bottom navigation
            binding.readableBottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    switch (item.getItemId()) {
                        case R.id.home:
                            HomeFragment homeFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, homeFragment)
                                    .commit();
                            return true;
                        case R.id.notification:
                            NotificationFragment notificationFragment = new NotificationFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, notificationFragment)
                                    .commit();
                            return true;
                        case R.id.schedules:
                            SchedulesFragment schedulesFragment = new SchedulesFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, schedulesFragment)
                                    .commit();
                            return true;
                        case R.id.none:
                            ExtendedFragment extendedFragment = new ExtendedFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, extendedFragment)
                                    .commit();
                            return true;
                    }
                    return false;
                }
            });
        }
}