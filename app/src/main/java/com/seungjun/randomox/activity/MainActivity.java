package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.data.PreferenceUtils;
import com.seungjun.randomox.view.JoinPopup;
import com.seungjun.randomox.view.LoginPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

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

        if(isLogin){

        }else{
            LoginPopup loginPopup = new LoginPopup(this);
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

        //for test
        Intent intent = new Intent(this, OXActivity.class);
        startActivity(intent);

//        if(preferenceUtils.isLoginSuccess()){
//            Intent intent = new Intent(this, OXActivity.class);
//            startActivity(intent);
//        }
//        else{
//            Toast.makeText(this, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
//        }

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
        Intent intent = new Intent(this, PostMailOXActivity.class);
        startActivity(intent);
    }
}
