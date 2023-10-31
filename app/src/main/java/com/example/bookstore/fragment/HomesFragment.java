package com.example.bookstore.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstore.Adapter.AdapterView;
import com.example.bookstore.R;
import com.google.android.material.tabs.TabLayout;


public class HomesFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    public HomesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout=view.findViewById(R.id.tabLayout);
        viewPager=view.findViewById(R.id.viewPager);

        AdapterView adapterView=new AdapterView(getChildFragmentManager());
        viewPager.setAdapter(adapterView);
        tabLayout.setupWithViewPager(viewPager);
    }
}