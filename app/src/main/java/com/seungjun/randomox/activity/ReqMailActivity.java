package com.seungjun.randomox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.seungjun.randomox.BaseActivity;

import butterknife.ButterKnife;

public class ReqMailActivity extends BaseActivity {

    private static final String TAG = "ReqMailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        ButterKnife.bind(this);


    }
}
