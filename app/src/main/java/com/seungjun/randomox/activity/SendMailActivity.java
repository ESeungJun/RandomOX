package com.seungjun.randomox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendMailActivity extends BaseActivity{

    @BindView(R.id.top_title)
    TextView topTitle;

    @BindView(R.id.my_score)
    TextView myScore;

    @BindView(R.id.input_story)
    EditText inputStory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendmail);

        ButterKnife.bind(this);

        topTitle.setText("랜덤OX 상담소");
        myScore.setVisibility(View.GONE);


    }


    @OnClick(R.id.send_mail)
    public void clickSendMail(){
        //서버에 보내는 로직

        Toast.makeText(this, "접수 되었습니다! 곧 답장 보내줄게요 :)", Toast.LENGTH_SHORT).show();
        finish();
    }
}
