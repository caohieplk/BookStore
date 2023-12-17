package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bookstore.app_util.PreferenceUtils;
import com.example.bookstore.databinding.ActivityMainBinding;
import com.example.bookstore.fragment.HomeFragment;
import com.example.bookstore.fragment.NotificationFragment;
import com.example.bookstore.fragment.ProfileFragment;
import com.example.bookstore.fragment.TransactionFragment;
import com.example.bookstore.fragment.library.LibraryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        checkLogin();
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();

        //bottom navigation
        binding.readableBottomBar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentTransaction homeTransaction = getSupportFragmentManager().beginTransaction();
                        homeTransaction.replace(R.id.container, homeFragment);
                        homeTransaction.commit();
                        return true;
                    case R.id.transaction:
                        TransactionFragment transactionFragment = new TransactionFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, transactionFragment).commit();
                        return true;
                    case R.id.notification:
                        NotificationFragment notificationFragment = new NotificationFragment();
                        transaction.replace(R.id.container, notificationFragment);
                        transaction.commit();
                        return true;
                    case R.id.library:
                        LibraryFragment libraryFragment = new LibraryFragment();
                        transaction.replace(R.id.container, libraryFragment);
                        transaction.commit();
                        return true;
                    case R.id.none:
                        ProfileFragment profileFragment = new ProfileFragment();
                        transaction.replace(R.id.container, profileFragment);
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void checkLogin(){
        boolean isLogin = PreferenceUtils.isLogin();
        //nếu userinfo = null nghĩa là chưa login => thì mở UI màn Login để điền user & password
        if (!isLogin){
            gotoLoginActivity();
            finish();
        }
    }

    public void gotoLoginActivity()
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
