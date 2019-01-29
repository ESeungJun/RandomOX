package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.UserInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.view.LoginPopup;
import com.seungjun.randomox.view.NormalPopup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends BaseActivity {

    @BindView(R.id.intro_title)
    TextView introTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        ButterKnife.bind(this);

        introTitle.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                introTitle.startAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.anim.fadein));
                introTitle.setAlpha(1f);
            }
        }, 1000);

        // 로그인 되있는 상태
        // 자동로그인 요청
        if(preferenceUtils.isLoginSuccess()){
            callLogin();
        }else{
            moveMain();
        }
    }


    /**
     * 로그인 요청 함수
     */
    public void callLogin(){
        networkClient.callPostLogin(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {
                setLogOut();
            }

            @Override
            public void onSuccess(int code, Object resultData) {
                UserInfo userInfo = (UserInfo)resultData;

                if(userInfo.reqCode == 0){
                    preferenceUtils.setLoginSuccess(true);
                    preferenceUtils.setUserSindex(userInfo.user_sIndex);
                    preferenceUtils.setUserScore(userInfo.user_point);
                    preferenceUtils.setUserKey(userInfo.user_key);

                    moveMain();

                }else{
                    setLogOut();
                }


            }

            @Override
            public void onFailed(int code) {
                setLogOut();
            }
        }, preferenceUtils.getUserId(), preferenceUtils.getUserPw());
    }


    /**
     * 로그아웃 함수
     */
    public void setLogOut(){


        Toast.makeText(IntroActivity.this, getResources().getString(R.string.auto_login_failed), Toast.LENGTH_LONG).show();

        // 데이터 초기화
        preferenceUtils.setUserPw("");
        preferenceUtils.setUserId("");
        preferenceUtils.setUserScore(0);
        preferenceUtils.setUserSindex(1);
        preferenceUtils.setUserFcmKey("");
        preferenceUtils.setUserKey("");
        preferenceUtils.setLoginSuccess(false);
    }


    public void moveMain(){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
}
