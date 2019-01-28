package com.seungjun.randomox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    @BindView(R.id.ox_content)
    TextView oxContent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_mail);

        ButterKnife.bind(this);

        myScore.setVisibility(View.GONE);

        oxContent.setAlpha(0f);

        btnO.setAlpha(0f);
        btnX.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                oxContent.startAnimation(AnimationUtils.loadAnimation(PostMailOXActivity.this, R.anim.fadein));
                oxContent.setAlpha(1f);
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnO.startAnimation(AnimationUtils.loadAnimation(PostMailOXActivity.this, R.anim.fadein));
                btnX.startAnimation(AnimationUtils.loadAnimation(PostMailOXActivity.this, R.anim.fadein));

                btnO.setAlpha(1f);
                btnX.setAlpha(1f);

            }
        }, 2000);
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
