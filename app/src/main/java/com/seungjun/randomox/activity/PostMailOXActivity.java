package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostMailOXActivity extends BaseActivity {

    @BindView(R.id.btn_o)
    ImageView btnO;

    @BindView(R.id.btn_x)
    ImageView btnX;

    @BindView(R.id.my_score)
    TextView myScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_mail);

        ButterKnife.bind(this);

        myScore.setVisibility(View.GONE);
    }



    @OnClick(R.id.btn_o)
    public void clickO(){
        Intent intent = new Intent(this, SendMailActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.btn_x)
    public void clickX(){
        Toast.makeText(this, "하고픈 이야기가 있다면 상담소를 찾아주세요 :)", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick(R.id.top_back)
    public void back(){
        finish();
    }

}
