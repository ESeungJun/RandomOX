package com.seungjun.randomox.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.data.OxContentInfo;
import com.seungjun.randomox.utils.D;
import com.seungjun.randomox.view.NormalPopup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OXActivity extends BaseActivity {

    private static final String TAG = "OXActivity";

    @BindView(R.id.btn_o)
    ImageView btnO;

    @BindView(R.id.btn_x)
    ImageView btnX;

    @BindView(R.id.ox_answer_img)
    ImageView answerImg;

    @BindView(R.id.ox_content)
    TextView oxContent;

    @BindView(R.id.ox_answer)
    TextView answerContent;

    @BindView(R.id.ox_next)
    TextView btnNext;

    @BindView(R.id.ox_tag)
    TextView oxTag;

    @BindView(R.id.answer_view)
    RelativeLayout answerView;

    @BindView(R.id.my_score)
    TextView myScoreView;

    private String answerValue = "";

    private ArrayList<OxContentInfo.OxData> oxList = new ArrayList<>();

    private int sIndex = 0;

    private NormalPopup popup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ox);

        ButterKnife.bind(this);


        if (getIntent() != null && getIntent().getParcelableArrayListExtra("oxList") != null) {
            oxList = getIntent().getParcelableArrayListExtra("oxList");
        }

        setNextOX(oxList.get(sIndex));

        myScoreView.setText("내 점수 : " + preferenceUtils.getUserScore() +"점");
    }

    /**
     * 정답일 때
     *
     * @param goodText
     * @param goodImage
     */
    public void visibleGood(String goodText, String goodImage) {

        if (TextUtils.isEmpty(goodText))
            answerContent.setText(Html.fromHtml(getResources().getString(R.string.default_good_text)));
        else
            answerContent.setText(goodText + "\n" + Html.fromHtml(getResources().getString(R.string.default_good_text)));

        if (TextUtils.isEmpty(goodImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.emoji));
        else
            Glide.with(this).load(goodImage).into(answerImg);

        answerView.setVisibility(View.VISIBLE);
        oxContent.setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);

        preferenceUtils.setUserScore(preferenceUtils.getUserScore() + 1 );

        myScoreView.setText("내 점수 : " + preferenceUtils.getUserScore() +"점");
    }

    /**
     * 틀렸을 때
     *
     * @param badText
     * @param badImage
     */
    public void visibleBad(String badText, String badImage) {

        if (TextUtils.isEmpty(badText))
            answerContent.setText(Html.fromHtml(getResources().getString(R.string.default_bad_text)));
        else
            answerContent.setText(badText + "\n" + Html.fromHtml(getResources().getString(R.string.default_bad_text)));

        if (TextUtils.isEmpty(badImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.unhappy));
        else
            Glide.with(this).load(badImage).into(answerImg);

        answerView.setVisibility(View.VISIBLE);
        oxContent.setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.btn_o)
    public void clickO() {

        answerValue = "o";

        btnX.setEnabled(false);
        btnO.setClickable(false);

        checkAnswer();
    }


    @OnClick(R.id.btn_x)
    public void clickX() {

        answerValue = "x";

        btnO.setEnabled(false);
        btnX.setClickable(false);

        checkAnswer();
    }


    public void checkAnswer(){

        if(answerValue.equalsIgnoreCase(oxList.get(sIndex).quiz_ox)){

            visibleGood(oxList.get(sIndex).quiz_coment, oxList.get(sIndex).quiz_img);

        }else{
            visibleBad(oxList.get(sIndex).quiz_coment, oxList.get(sIndex).quiz_img);
        }

    }

    @OnClick(R.id.ox_next)
    public void clickNext() {

        answerValue = "";

        btnNext.setVisibility(View.GONE);

        sIndex++;

        D.log(TAG, "Next sIndex > " + sIndex);


        //다음으로 시작해야할 인덱스가 현재 가지고 있는 것과 동일하거나 크면
        if (sIndex >= oxList.size() - 1) {

            //새롭게 oxList를 요청한다.
            netProgress.setProgressText("문제 요청 중...");
            netProgress.show();

            networkClient.callPostGetOX(new RetrofitApiCallback() {
                @Override
                public void onError(Throwable t) {
                    netProgress.dismiss();

                    popup = new NormalPopup(OXActivity.this);
                    popup.setPopupText(OXActivity.this.getResources().getString(R.string.error_network_unkonw));
                    popup.setPopupTitle("알 수 없는 에러");
                    popup.setOKClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // 뭔가 에러가 난 상황이면
                            // 현재까지 진행된 인덱스 + 원래 있던 인덱스
                            preferenceUtils.setUserSindex(preferenceUtils.getUserSindex() + sIndex);
                            D.log(TAG, "set sIndex > " + preferenceUtils.getUserSindex());

                            popup.dismiss();
                            finish();
                        }
                    });
                    popup.show();
                }

                @Override
                public void onSuccess(int code, Object resultData) {
                    netProgress.dismiss();

                    if (resultData != null) {
                        OxContentInfo oxContentInfo = (OxContentInfo) resultData;

                        if (oxContentInfo.reqCode == 0) {

                            // 정상 상황이면
                            // 현재까지 진행된 인덱스 + 받아온 아이템 총 길이
                            preferenceUtils.setUserSindex(oxContentInfo.oxList.size() + preferenceUtils.getUserSindex());

                            D.log(TAG, "set sIndex > " + preferenceUtils.getUserSindex());

                            oxList.addAll(oxContentInfo.oxList);
                            setNextOX(oxList.get(sIndex));

                        } else {

                            popup = new NormalPopup(OXActivity.this);
                            popup.setPopupText(oxContentInfo.reqMsg);
                            popup.setPopupTitle(oxContentInfo.reqCode == 6000 ? "문제를 다 푸셨군요!" : "서버 에러");
                            popup.setOKClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // 뭔가 에러가 난 상황이면
                                    // 현재까지 진행된 인덱스 + 원래 있던 인덱스
                                    preferenceUtils.setUserSindex(preferenceUtils.getUserSindex() + sIndex);
                                    D.log(TAG, "set sIndex > " + preferenceUtils.getUserSindex());

                                    popup.dismiss();
                                    finish();
                                }
                            });
                            popup.show();
                        }
                    }
                }

                @Override
                public void onFailed(int code) {
                    netProgress.dismiss();

                    popup = new NormalPopup(OXActivity.this);
                    popup.setPopupText(OXActivity.this.getResources().getString(R.string.error_network_unkonw));
                    popup.setPopupTitle("알 수 없는 에러");
                    popup.setOKClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // 뭔가 에러가 난 상황이면
                            // 현재까지 진행된 인덱스 + 원래 있던 인덱스
                            preferenceUtils.setUserSindex(preferenceUtils.getUserSindex() + sIndex);
                            D.log(TAG, "set sIndex > " + preferenceUtils.getUserSindex());

                            popup.dismiss();
                            finish();
                        }
                    });
                    popup.show();
                }
            }, preferenceUtils.getUserSindex());


        }else{
            setNextOX(oxList.get(sIndex));
        }

    }

    @OnClick(R.id.top_back)
    public void back() {
        finish();
    }


    public void setNextOX(OxContentInfo.OxData oxData) {
        oxContent.setText(oxData.quiz_text);
        oxTag.setText(oxData.quiz_tag);

        answerView.setVisibility(View.GONE);
        oxContent.setVisibility(View.VISIBLE);

        btnX.setEnabled(true);
        btnX.setClickable(true);
        btnO.setEnabled(true);
        btnO.setClickable(true);
    }
}
