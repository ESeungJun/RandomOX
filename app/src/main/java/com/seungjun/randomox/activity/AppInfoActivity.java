package com.seungjun.randomox.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.view.WebViewPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppInfoActivity extends BaseActivity {


    @BindView(R.id.info_version)
    TextView appVersion;

    @BindView(R.id.my_score)
    TextView myScore;

    @BindView(R.id.top_title)
    TextView topTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_info);

        ButterKnife.bind(this);

        topTitle.setText("앱 정보");
        myScore.setVisibility(View.INVISIBLE);

        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion.setText("App ver " + i.versionName);

        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            appVersion.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.top_back)
    public void clickBack(){
        finish();
    }


    @OnClick(R.id.info_opensource)
    public void clickOpenLicense(){
        WebViewPopup webViewPopup = new WebViewPopup(this);
        webViewPopup.setPopupText("file:///android_asset/opensource.html");
        webViewPopup.show();
    }


    @OnClick(R.id.info_send_email)
    public void clickSendEmail(){
        Intent it = new Intent(Intent.ACTION_SEND);

        it.putExtra(Intent.EXTRA_EMAIL, new String[]{"6951004@gmail.com"});
        it.putExtra(Intent.EXTRA_TEXT, "문의나 건의사항을 보내주세요");
        it.setType("text/plain");

        try{

            it.setPackage("com.google.android.gm");
            startActivity(it);

        }catch (Exception e){
            e.printStackTrace();
            startActivity(Intent.createChooser(it, "이메일을 보낼 앱을 선택해주세요."));
        }
    }



    @OnClick(R.id.info_send_email_quiz)
    public void clickSendEmailQuiz(){
        Intent it = new Intent(Intent.ACTION_SEND);

        it.putExtra(Intent.EXTRA_EMAIL, new String[]{"6951004@gmail.com"});
        it.putExtra(Intent.EXTRA_TEXT, "여러분의 OX 퀴즈를 보내주세요!\n채택시 여러분의 닉네임과 함께 문제가 출제됩니다!");
        it.setType("text/plain");

        try{

            it.setPackage("com.google.android.gm");
            startActivity(it);

        }catch (Exception e){
            e.printStackTrace();
            startActivity(Intent.createChooser(it, "이메일을 보낼 앱을 선택해주세요."));
        }
    }


}
