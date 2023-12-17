package com.example.bookstore.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookstore.fragment.SchedulesTab1Fragment;
import com.example.bookstore.fragment.SchedulesTab2Fragment;
import com.example.bookstore.fragment.SchedulesTab3Fragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SchedulesTab1Fragment();
            case 1:
                return new SchedulesTab2Fragment();
            case 2:
                return new SchedulesTab3Fragment();
            default:
                return new SchedulesTab1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
