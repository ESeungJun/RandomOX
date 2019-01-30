package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.network.data.OxContentInfo;
import com.seungjun.randomox.network.data.UserInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.utils.PreferenceUtils;
import com.seungjun.randomox.view.JoinPopup;
import com.seungjun.randomox.view.LoginPopup;
import com.seungjun.randomox.view.NormalPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LoginPopup.LoginCallBack {

    @BindView(R.id.main_start)
    TextView btnStart;

    @BindView(R.id.main_title)
    TextView mainTitle;

    @BindView(R.id.main_login)
    TextView mainLogin;

    @BindView(R.id.main_start_view)
    LinearLayout mainStartView;

    @BindView(R.id.hide_postbox)
    ImageView hidePostbox;

    @BindView(R.id.hide_view1)
    View hideView1;

    @BindView(R.id.hide_view2)
    View hideView2;

    @BindView(R.id.hide_view3)
    View hideView3;

    private boolean isLogin = false;

    private String textData = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        isLogin = preferenceUtils.isLoginSuccess();

        if(isLogin){
            mainLogin.setText("로그아웃");
        }else{
            mainLogin.setText("로그인");
        }


        if(getIntent() != null && getIntent().getExtras() != null){
            textData = getIntent().getStringExtra("textData");

            hideView1.setVisibility(View.INVISIBLE);
            hideView2.setVisibility(View.INVISIBLE);
            hideView3.setVisibility(View.INVISIBLE);

            hidePostbox.setImageDrawable(getResources().getDrawable(R.drawable.mail));
        }

        mainStartView.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainStartView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein));
                mainStartView.setAlpha(1f);
            }
        }, 700);

    }


    @OnClick(R.id.main_login)
    public void clickLogin(){

        // 로그인 되있는 상태 -> 로그아웃이 눌림 (로그아웃 시키기)
        if(isLogin){

            setLogOut();

            Toast.makeText(this, "로그아웃 되었어요.", Toast.LENGTH_SHORT).show();

        }
        // 로그아웃 되어있는 상태 -> 로그인이 눌림 (로그인 팝업)
        else{
            LoginPopup loginPopup = new LoginPopup(this, this);
            loginPopup.show();
        }

    }


    @OnClick(R.id.main_join)
    public void clickJoin(){
        JoinPopup joinPopup = new JoinPopup(this);
        joinPopup.show();
    }


    @OnClick(R.id.main_start)
    public void clickStart(){

        if(preferenceUtils.isLoginSuccess()){
            callOxData();
        }
        else{
            Toast.makeText(this, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.main_title)
    public void clickMainTitle(){

        if(hideView3.getVisibility() == View.VISIBLE){
            hideView3.setVisibility(View.INVISIBLE);

            return;
        }

        if(hideView2.getVisibility() == View.VISIBLE){
            hideView2.setVisibility(View.INVISIBLE);

            return;
        }

        if(hideView1.getVisibility() == View.VISIBLE){
            hideView1.setVisibility(View.INVISIBLE);

            return;
        }

    }

    @OnClick(R.id.hide_postbox)
    public void clickPostbox(){

        if(hideView1.getVisibility() == View.VISIBLE ||
                hideView2.getVisibility() == View.VISIBLE ||
                hideView3.getVisibility() == View.VISIBLE)
            return;

        if(!isLogin){
            Toast.makeText(this, "로그인을 하면 이용 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = null;

        if(!TextUtils.isEmpty(textData)){
            intent = new Intent(this, ReqMailActivity.class);
            intent.putExtra("textData", textData);
        }else{
            intent = new Intent(this, PostMailOXActivity.class);
        }

        startActivity(intent);
    }


    /**
     * 문제 요청 후 화면 진입하는 함수
     */
    public void callOxData(){
        netProgress.show();

        networkClient.callPostGetOX(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {

                netProgress.dismiss();

                CommonUtils.showErrorPopup(MainActivity.this, getResources().getString(R.string.error_network_unkonw), false);
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                netProgress.dismiss();

                if(resultData != null){
                    OxContentInfo oxContentInfo = (OxContentInfo) resultData;

                    if(oxContentInfo.reqCode == 0){

                        //for test
                        Intent intent = new Intent(MainActivity.this, OXActivity.class);
                        intent.putParcelableArrayListExtra("oxList", oxContentInfo.oxList);
                        startActivity(intent);

                    }else{

                        CommonUtils.showErrorPopup(MainActivity.this, oxContentInfo.reqMsg, false);
                    }
                }
            }
            @Override
            public void onFailed(int code) {

                netProgress.dismiss();

                CommonUtils.showErrorPopup(MainActivity.this, getResources().getString(R.string.error_network_unkonw), false);
            }
        }, preferenceUtils.getUserSindex());
    }

    /**
     * 로그아웃 함수
     */
    public void setLogOut(){

        // 데이터 초기화
        preferenceUtils.setUserPw("");
        preferenceUtils.setUserId("");
        preferenceUtils.setUserScore(0);
        preferenceUtils.setUserSindex(1);
        preferenceUtils.setUserFcmKey("");
        preferenceUtils.setUserKey("");
        preferenceUtils.setLoginSuccess(false);

        isLogin = false;

        mainLogin.setText("로그인");
    }


    @Override
    public void onLogin(boolean isLogin) {

        this.isLogin = isLogin;

        if (this.isLogin) {
            mainLogin.setText("로그아웃");

        } else {
            setLogOut();
        }

    }
}
