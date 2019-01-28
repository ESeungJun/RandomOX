package com.seungjun.randomox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.seungjun.randomox.utils.PreferenceUtils;
import com.seungjun.randomox.network.RetrofitClient;
import com.seungjun.randomox.view.NetworkProgressDialog;

/**
 * 모든 액티비티에 부모가 될 액티비티
 * 화면에서 공통으로 처리해야하는 작업을 여기서 한다
 * ex) 권한 질의, 네트워크 체크 등등
 */
public class BaseActivity extends AppCompatActivity {


    protected NetworkProgressDialog netProgress;
    protected RetrofitClient networkClient;

    protected static PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        netProgress = new NetworkProgressDialog(this);

        networkClient = RetrofitClient.getInstance(this).createBaseApi();

        if(preferenceUtils == null)
            preferenceUtils = PreferenceUtils.getInstance(this);

    }
}
