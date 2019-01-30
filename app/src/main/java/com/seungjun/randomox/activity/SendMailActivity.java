package com.seungjun.randomox.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.utils.EmojiInputFilter;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendmail);

        ButterKnife.bind(this);

        topTitle.setText("랜덤OX 상담소");
        myScore.setVisibility(View.GONE);

        inputStory.setFilters(new InputFilter[]{new EmojiInputFilter(this)});

    }


    @OnClick(R.id.top_back)
    public void back(){
        finish();
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

                CommonUtils.showErrorPopup(SendMailActivity.this, getResources().getString(R.string.error_network_unkonw), false);
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                netProgress.dismiss();

                HeaderInfo result = (HeaderInfo) resultData;

                if(result.reqCode != 6001)
                    CommonUtils.showErrorPopup(SendMailActivity.this, getResources().getString(R.string.error_send_mail), false);
                else
                    CommonUtils.showErrorPopup(SendMailActivity.this, result.reqMsg, true);
            }

            @Override
            public void onFailed(int code) {

                netProgress.dismiss();

                CommonUtils.showErrorPopup(SendMailActivity.this, getResources().getString(R.string.error_network_unkonw), false);

            }

        }, preferenceUtils.getUserKey(), preferenceUtils.getUserId(), story);
    }
}
