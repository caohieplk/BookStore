package com.example.bookstore;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.example.bookstore.app_util.PreferenceUtils;

public class App extends MultiDexApplication {
    private static App instance = null;
    public static App getInstance(){
        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance(this);
        MultiDex.install(this);
        PreferenceUtils.init();
    }
    public static synchronized void getInstance(App app){
        if (instance == null) instance = app;
    }
}
