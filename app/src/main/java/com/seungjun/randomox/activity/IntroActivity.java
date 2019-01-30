package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.network.data.NoticesInfo;
import com.seungjun.randomox.network.data.UserInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.utils.D;
import com.seungjun.randomox.utils.PreferenceUtils;
import com.seungjun.randomox.view.LoginPopup;
import com.seungjun.randomox.view.NormalPopup;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends BaseActivity {

    private static final String TAG = "IntroActivity";

    @BindView(R.id.intro_title)
    TextView introTitle;

    @BindView(R.id.intro_progress)
    AVLoadingIndicatorView introProgress;

    private String textData = "";

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


                if(getIntent() != null && getIntent().getExtras() != null){
                    textData = getIntent().getStringExtra("textData");
                }


                networkClient.callGetNotices(new RetrofitApiCallback() {
                    @Override
                    public void onError(Throwable t) {

                        // 로그인 되있는 상태
                        // 자동로그인 요청
                        if(preferenceUtils.isLoginSuccess()){
                            callLogin();

                        }else{

                            moveMain();
                        }
                    }

                    @Override
                    public void onSuccess(int code, Object resultData) {

                        NoticesInfo notice = (NoticesInfo)resultData;

                        if(notice.reqCode == 0 &&
                                !TextUtils.isEmpty(notice.noti_text)){

                            NormalPopup notiPoup = new NormalPopup(IntroActivity.this);
                            notiPoup.setPopupText(notice.noti_text + "\n"+ notice.noti_date);
                            notiPoup.setPopupTitle("공지사항");
                            notiPoup.setCancelVisible(View.GONE);

                            // 서버점검이나 강제업뎃 같이
                            // 무조건 무언가를 해야하는 경우
                            if(notice.noti_yn.equals("y")){

                                notiPoup.setOKClick(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        // 업데이트라는 문구가 들어간 경우는 강제 업뎃으로 판단
                                        if(notice.noti_text.contains("업데이트")){


                                        }

                                        notiPoup.dismiss();
                                        finish();
                                    }
                                });
                            }else{

                                notiPoup.setOKClick(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        notiPoup.dismiss();

                                        // 로그인 되있는 상태
                                        // 자동로그인 요청
                                        if(preferenceUtils.isLoginSuccess()){
                                            callLogin();

                                        }else{

                                            moveMain();
                                        }

                                    }
                                });

                            }
                            notiPoup.show();
                        }

                    }

                    @Override
                    public void onFailed(int code) {

                        // 로그인 되있는 상태
                        // 자동로그인 요청
                        if(preferenceUtils.isLoginSuccess()){
                            callLogin();

                        }else{

                            moveMain();
                        }
                    }
                });

            }
        }, 700);

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

                    updateFCM();

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


    public void updateFCM(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            D.error(TAG, "getInstanceId failed", task.getException());

                            moveMain();

                            return;
                        }

                        // 보내기전에 비교해보기
                        if(!preferenceUtils.getUserFcmKey().equals(task.getResult().getToken())){
                            preferenceUtils.setUserFcmKey(task.getResult().getToken());
                        }

                        networkClient.callPostFcmUpdate(new RetrofitApiCallback() {
                            @Override
                            public void onError(Throwable t) {
                                moveMain();
                            }

                            @Override
                            public void onSuccess(int code, Object resultData) {

                                moveMain();

                            }

                            @Override
                            public void onFailed(int code) {
                                moveMain();
                            }
                        }, preferenceUtils.getUserKey(), preferenceUtils.getUserFcmKey());
                    }
                });

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

        moveMain();
    }


    public void moveMain(){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                introProgress.startAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.anim.fadeout));
                introProgress.setAlpha(0f);

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);

                if(!TextUtils.isEmpty(textData))
                    intent.putExtra("textData", textData);

                startActivity(intent);
                overridePendingTransition(0, 0);

                finish();
                overridePendingTransition(0, 0);
            }
        }, 1500);

    }
}
