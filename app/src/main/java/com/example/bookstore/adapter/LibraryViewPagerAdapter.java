package com.example.bookstore.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookstore.fragment.library.ItemLibFragment;
import com.example.bookstore.model.Category;

import java.util.ArrayList;
import java.util.List;

public class LibraryViewPagerAdapter extends FragmentStateAdapter {

    private List<ItemLibFragment> fragmentLibraries;

    private List<Category> categories;

    public LibraryViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Category> categories, List<ItemLibFragment> fragmentLibraries) {
        super(fragmentManager, lifecycle);
        this.categories = categories;
        this.fragmentLibraries = fragmentLibraries;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentLibraries.get(position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

}
