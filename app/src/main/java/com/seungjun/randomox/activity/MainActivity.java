package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.OxContentInfo;
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

    @BindView(R.id.hide_postbox)
    ImageView hidePostbox;

    @BindView(R.id.hide_view1)
    View hideView1;

    @BindView(R.id.hide_view2)
    View hideView2;

    @BindView(R.id.hide_view3)
    View hideView3;


    private boolean isLogin = false;

    private NormalPopup popup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


        isLogin = preferenceUtils.isLoginSuccess();

        if(isLogin){
            mainLogin.setText("로그아웃");

        }else{
            mainLogin.setText("로그인");
        }

    }

    @OnClick(R.id.main_login)
    public void clickLogin(){

        // 로그인 되있는 상태 -> 로그아웃이 눌림 (로그아웃 시키기)
        if(isLogin){

            // 데이터 초기화
            preferenceUtils.setUserPw("");
            preferenceUtils.setUserId("");
            preferenceUtils.setUserScore(0);
            preferenceUtils.setUserSindex(1);
            preferenceUtils.setUserFcmKey("");
            preferenceUtils.setLoginSuccess(false);

            isLogin = false;

            Toast.makeText(this, "로그아웃 되었어요.", Toast.LENGTH_SHORT).show();

            mainLogin.setText("로그인");

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

        Intent intent = new Intent(this, PostMailOXActivity.class);
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

                popup = new NormalPopup(MainActivity.this);
                popup.setPopupText(MainActivity.this.getResources().getString(R.string.error_network_unkonw));
                popup.setOKClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });
                popup.show();
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

                        popup = new NormalPopup(MainActivity.this);
                        popup.setPopupText(oxContentInfo.reqMsg);
                        popup.setOKClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popup.dismiss();
                            }
                        });
                        popup.show();
                    }
                }
            }
            @Override
            public void onFailed(int code) {

                netProgress.dismiss();

                popup = new NormalPopup(MainActivity.this);
                popup.setPopupText(MainActivity.this.getResources().getString(R.string.error_network_unkonw));
                popup.setOKClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });
                popup.show();
            }
        }, preferenceUtils.getUserSindex());
    }

    @Override
    public void onLogin(boolean isLogin) {


        this.isLogin = isLogin;

        if (this.isLogin) {
            mainLogin.setText("로그아웃");

        } else {
            mainLogin.setText("로그인");
        }


    }
}
