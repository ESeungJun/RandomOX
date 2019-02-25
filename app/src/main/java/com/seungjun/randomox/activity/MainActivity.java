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

    @BindView(R.id.main_join)
    TextView mainJoin;

    @BindView(R.id.main_myScore)
    TextView mainMyScore;

    @BindView(R.id.main_myRank)
    TextView mainMyRank;

    @BindView(R.id.main_rank)
    TextView mainRank;

    @BindView(R.id.main_start_view)
    LinearLayout mainStartView;

    @BindView(R.id.main_myInfoView)
    LinearLayout mainMyInfoView;

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


    private boolean isPause = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        isLogin = preferenceUtils.isLoginSuccess();

        if(isLogin){
            mainLogin.setText("로그아웃");
            mainJoin.setText("탈퇴하기");
            mainRank.setVisibility(View.VISIBLE);

            mainMyInfoView.setVisibility(View.VISIBLE);
            mainMyInfoView.setAlpha(0f);

            mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
            mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
        }else{
            mainLogin.setText("로그인");
            mainJoin.setText("가입하기");

            mainRank.setVisibility(View.GONE);

            mainMyInfoView.setVisibility(View.INVISIBLE);
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

                if(isLogin){
                    mainMyInfoView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadein));
                    mainMyInfoView.setAlpha(1f);
                }
            }
        }, 700);

    }


    @Override
    protected void onResume() {
        super.onResume();

        isLogin = preferenceUtils.isLoginSuccess();

        if (isLogin) {
            mainLogin.setText("로그아웃");
            mainJoin.setText("탈퇴하기");
            mainRank.setVisibility(View.VISIBLE);

            mainMyInfoView.setVisibility(View.VISIBLE);

            if(isPause){

                isPause = false;

                callMyInfo();

            }else{

                mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
                mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
            }


        } else {
            setLogOut();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        isPause = true;
    }


    public void callMyInfo(){

        netProgress.show();
        networkClient.callMyInfo(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {

                netProgress.dismiss();

                Toast.makeText(MainActivity.this, "내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();

                mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
                mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
            }

            @Override
            public void onSuccess(int code, Object resultData) {
                netProgress.dismiss();

                UserInfo userInfo = (UserInfo)resultData;

                if(userInfo.reqCode == 0 ){

                    preferenceUtils.setUserRank(userInfo.rank);
                    preferenceUtils.setUserScore(userInfo.user_point);

                    mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
                    mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
                }else{

                    Toast.makeText(MainActivity.this, "내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();

                    mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
                    mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
                }

            }

            @Override
            public void onFailed(int code) {

                netProgress.dismiss();

                Toast.makeText(MainActivity.this, "내 정보 업데이트 실패했어요. 잠시 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();

                mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
                mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
            }
        }, preferenceUtils.getUserId());
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

        // 로그인 되있는 상태 -> 탈퇴하기 눌림 (탈퇴 시키기)
        if(isLogin) {
            NormalPopup exitPopup = new NormalPopup(this);
            exitPopup.setPopupTitle("탈퇴하기");
            exitPopup.setPopupText("탈퇴 하시겠어요?\n탈퇴 시, 받았던 편지와 모든 데이터가 삭제되며 복구는 불가능해요.");
            exitPopup.setCancelVisible(View.VISIBLE);
            exitPopup.setCancelClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitPopup.dismiss();
                }
            });
            exitPopup.setOKClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    exitPopup.dismiss();

                    netProgress.setProgressText("탈퇴 요청 중");
                    netProgress.show();

                    networkClient.callPostDeleteInfo(new RetrofitApiCallback() {
                        @Override
                        public void onError(Throwable t) {

                            netProgress.dismiss();
                            CommonUtils.showErrorPopup(MainActivity.this, getResources().getString(R.string.error_network_unkonw), false);
                        }

                        @Override
                        public void onSuccess(int code, Object resultData) {

                            netProgress.dismiss();

                            HeaderInfo result = (HeaderInfo) resultData;

                            if(result.reqCode == 0){


                                Toast.makeText(MainActivity.this, "즐거웠어요! 또 놀러오세요 ! :)", Toast.LENGTH_SHORT).show();

                                setLogOut();

                            }else{
                                CommonUtils.showErrorPopup(MainActivity.this, result.reqMsg, false);
                            }


                        }

                        @Override
                        public void onFailed(int code) {

                            netProgress.dismiss();
                            CommonUtils.showErrorPopup(MainActivity.this, getResources().getString(R.string.error_network_unkonw), false);
                        }

                    }, preferenceUtils.getUserKey(), preferenceUtils.getUserId(), preferenceUtils.getUserPw());
                }
            });

            exitPopup.show();

        }else{

            NormalPopup normalPopup = new NormalPopup(this);
            normalPopup.setCancelable(true);
            normalPopup.setCancelText("싫은데요?");
            normalPopup.setCancelVisible(View.VISIBLE);
            normalPopup.setCancelClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    normalPopup.dismiss();
                }
            });
            normalPopup.setPopupTitle("가입 주의사항");
            normalPopup.setOkText("인정합니다");
            normalPopup.setPopupText(getResources().getString(R.string.join_info));
            normalPopup.setOKClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    normalPopup.dismiss();

                    JoinPopup joinPopup = new JoinPopup(MainActivity.this);
                    joinPopup.show();
                }
            });

            normalPopup.show();
        }
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

    @OnClick(R.id.main_rank)
    public void clickRank(){
        startActivity(new Intent(this, RankActivity.class));
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

    @OnClick(R.id.main_app_info)
    public void clickAppInfo(){
        startActivity(new Intent(this, AppInfoActivity.class));
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
        preferenceUtils.setUserRank(-1);
        preferenceUtils.setLoginSuccess(false);

        isLogin = false;

        mainLogin.setText("로그인");
        mainJoin.setText("가입하기");
        mainRank.setVisibility(View.GONE);

        mainMyInfoView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onLogin(boolean isLogin) {

        this.isLogin = isLogin;

        if (this.isLogin) {
            mainLogin.setText("로그아웃");
            mainJoin.setText("탈퇴하기");
            mainRank.setVisibility(View.VISIBLE);

            mainMyInfoView.setVisibility(View.VISIBLE);

            mainMyScore.setText(preferenceUtils.getUserId()+"님의 점수 : " + preferenceUtils.getUserScore() + "점");
            mainMyRank.setText("( " + preferenceUtils.getUserRank() +"위 )");
        } else {
            setLogOut();
        }

    }
}
