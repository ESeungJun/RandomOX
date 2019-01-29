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

    @BindView(R.id.ox_answer_check)
    TextView answerCheckView;

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

    // 사용자가 몇문제나 풀었는지 계산하는 카운트
    private int count = 0;

    // 서버에 요청할때 쓰이는 sIndex
    private int callSIndex = 0;

    private NormalPopup popup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ox);

        ButterKnife.bind(this);


        if (getIntent() != null && getIntent().getParcelableArrayListExtra("oxList") != null) {
            oxList = getIntent().getParcelableArrayListExtra("oxList");
        }

        setNextOX(oxList.get(count));

        myScoreView.setText("내 점수 : " + preferenceUtils.getUserScore() +"점");

        callSIndex = preferenceUtils.getUserSindex();
    }

    /**
     * 정답일 때
     *
     * @param goodText
     * @param goodImage
     */
    public void visibleGood(String goodText, String goodImage) {

        if (TextUtils.isEmpty(goodText))
            answerContent.setText("");
        else
            answerContent.setText(goodText);

        if (TextUtils.isEmpty(goodImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.emoji));
        else
            Glide.with(this).load(goodImage).into(answerImg);

        answerCheckView.setText(getResources().getString(R.string.default_good_text));
        answerCheckView.setTextColor(getResources().getColor(R.color.color_4482ff));

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
            answerContent.setText("");
        else
            answerContent.setText(badText);

        if (TextUtils.isEmpty(badImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.unhappy));
        else
            Glide.with(this).load(badImage).into(answerImg);

        answerCheckView.setText(getResources().getString(R.string.default_bad_text));
        answerCheckView.setTextColor(getResources().getColor(R.color.color_ff4f60));

        answerView.setVisibility(View.VISIBLE);
        oxContent.setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);
    }


    /**
     * 스페셜 문제 전용 view 처리
     * @param goodText
     * @param goodImage
     */
    public void visibleSpecial(String goodText, String goodImage) {

        if (TextUtils.isEmpty(goodText))
            answerContent.setText("");
        else
            answerContent.setText(goodText);

        if (TextUtils.isEmpty(goodImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.emoji));
        else
            Glide.with(this).load(goodImage).into(answerImg);

        answerCheckView.setText(getResources().getString(R.string.default_special_text));
        answerCheckView.setTextColor(getResources().getColor(R.color.color_8cba23));

        answerView.setVisibility(View.VISIBLE);
        oxContent.setVisibility(View.GONE);

        btnNext.setVisibility(View.VISIBLE);

        preferenceUtils.setUserScore(preferenceUtils.getUserScore() + 1 );

        myScoreView.setText("내 점수 : " + preferenceUtils.getUserScore() +"점");
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

        // 스페셜 타입의 퀴즈인 경우
        // 뭘 선택하던 정답으로 처리
        if(oxList.get(count).quiz_special == 1){
            visibleSpecial(oxList.get(count).quiz_g_coment, oxList.get(count).quiz_g_img);
        }
        // 일반 문제는 정답 가르기
        else{
            if(answerValue.equalsIgnoreCase(oxList.get(count).quiz_ox)){

                visibleGood(oxList.get(count).quiz_g_coment, oxList.get(count).quiz_g_img);

            }else{
                visibleBad(oxList.get(count).quiz_coment, oxList.get(count).quiz_img);
            }
        }



        // 사용자가 정답을 체크하면 다음 문제 인덱스 대기
        count++;
        D.log(TAG, "Next count > " + count);

        // sIndex도 증가시킨다
        callSIndex += 1;

        preferenceUtils.setUserSindex(callSIndex);
        D.log(TAG, "set sIndex > " + preferenceUtils.getUserSindex());
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @OnClick(R.id.ox_next)
    public void clickNext() {

        answerValue = "";

        btnNext.setVisibility(View.GONE);

        // 현재 가지고 있는 문제를 다 풀어서 더이상 가져올 게 없으면
        if (count == oxList.size()) {

            //새롭게 oxList를 요청한다.
            netProgress.setProgressText("문제 요청 중...");
            netProgress.show();

            networkClient.callPostGetOX(new RetrofitApiCallback() {
                @Override
                public void onError(Throwable t) {
                    netProgress.dismiss();

                    popup = new NormalPopup(OXActivity.this);
                    popup.setPopupText(OXActivity.this.getResources().getString(R.string.error_network_unkonw));
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

                    if (resultData != null) {
                        OxContentInfo oxContentInfo = (OxContentInfo) resultData;

                        if (oxContentInfo.reqCode == 0) {
                            oxList.addAll(oxContentInfo.oxList);
                            setNextOX(oxList.get(count));

                        } else {

                            popup = new NormalPopup(OXActivity.this);
                            popup.setPopupText(oxContentInfo.reqMsg);
                            popup.setOKClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

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
                    popup.setOKClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.dismiss();
                            finish();
                        }
                    });
                    popup.show();
                }
            }, callSIndex);


        }else{
            setNextOX(oxList.get(count));
        }

    }

    @OnClick(R.id.top_back)
    public void back() {
        finish();
    }


    public void setNextOX(OxContentInfo.OxData oxData) {

        //문제를 셋팅할 때마다 callSIndex에 문제의 인덱스 셋팅
        callSIndex = oxData.quiz_index;

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
