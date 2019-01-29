package com.seungjun.randomox;

import android.app.Application;

import com.seungjun.randomox.network.RetrofitClient;
import com.seungjun.randomox.utils.PreferenceUtils;
import com.seungjun.randomox.view.NetworkProgressDialog;

public class RandomOXApplication extends Application {

    public static PreferenceUtils preferenceUtils;


    @Override
    public void onCreate() {
        super.onCreate();


    }
}
