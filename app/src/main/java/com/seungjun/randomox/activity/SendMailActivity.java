package com.seungjun.randomox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.view.NormalPopup;

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

    private NormalPopup popup;


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

        String story = inputStory.getText().toString();

        if(TextUtils.isEmpty(story)){
            Toast.makeText(this, "이야기를 적어주시면 접수가 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        netProgress.setProgressText("편지를 보내는 중");
        netProgress.show();

        networkClient.callPostSendLetter(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {

                netProgress.dismiss();

                popup = new NormalPopup(SendMailActivity.this);
                popup.setPopupText(SendMailActivity.this.getResources().getString(R.string.error_network_unkonw));
                popup.setOKClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                        finish();
                    }
                });
                popup.show();
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                netProgress.dismiss();

                HeaderInfo result = (HeaderInfo) resultData;

                popup = new NormalPopup(SendMailActivity.this);
                popup.setPopupText(result.reqMsg);
                popup.setOKClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                        finish();
                    }
                });

                popup.show();
            }

            @Override
            public void onFailed(int code) {

                netProgress.dismiss();

                popup = new NormalPopup(SendMailActivity.this);
                popup.setPopupText(SendMailActivity.this.getResources().getString(R.string.error_network_unkonw));
                popup.setOKClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                        finish();
                    }
                });
                popup.show();

            }

        }, preferenceUtils.getUserKey(), preferenceUtils.getUserId(), story);
    }
}
