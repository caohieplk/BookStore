package com.example.bookstore.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bookstore.fragment.HomeFragment;
import com.example.bookstore.fragment.ItemHome1Fragment;
import com.example.bookstore.fragment.ItemHome2Fragment;

public class AdapterView extends FragmentStatePagerAdapter {

    public AdapterView(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ItemHome1Fragment itemHome1Fragment = new ItemHome1Fragment();
                return itemHome1Fragment;
            case 2:
                ItemHome2Fragment itemHome2Fragment = new ItemHome2Fragment();
                return itemHome2Fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Home";
            case 1:
                return "Sách";
            case 2:
                return "Sách nói";
            default:
                return null;
        }
    }
}
