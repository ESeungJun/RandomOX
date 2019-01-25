package com.seungjun.randomox.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seungjun.randomox.BaseActivity;
import com.seungjun.randomox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OXActivity extends BaseActivity {

    private static final int VALUE_O = 0;
    private static final int VALUE_X = 1;

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
    LinearLayout answerView;

    private int answerValue = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ox);

        ButterKnife.bind(this);

    }

    /**
     * 정답일 때
     * @param goodText
     * @param goodImage
     */
    public void visibleGood(String goodText, String goodImage){

        answerView.setVisibility(View.VISIBLE);
        oxContent.setVisibility(View.GONE);

        if(TextUtils.isEmpty(goodText))
            answerContent.setText(getResources().getString(R.string.default_good_text));
        else
            answerContent.setText(goodText +"\n+ 5점");

        if(TextUtils.isEmpty(goodImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.emoji));
        else
            Glide.with(this).load(goodImage).into(answerImg);

        btnNext.setVisibility(View.VISIBLE);
    }

    /**
     * 틀렸을 때
     * @param badText
     * @param badImage
     */
    public void visibleBad(String badText, String badImage){

        answerView.setVisibility(View.VISIBLE);
        oxContent.setVisibility(View.GONE);

        if(TextUtils.isEmpty(badText))
            answerContent.setText(getResources().getString(R.string.default_bad_text));
        else
            answerContent.setText(badText +"\n- 5점");

        if(TextUtils.isEmpty(badImage))
            answerImg.setImageDrawable(getResources().getDrawable(R.drawable.unhappy));
        else
            Glide.with(this).load(badImage).into(answerImg);

        btnNext.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.btn_o)
    public void clickO(){

        answerValue = VALUE_O;

        btnX.setEnabled(false);
        btnO.setClickable(false);

        visibleGood(null, null);
    }


    @OnClick(R.id.btn_x)
    public void clickX(){

        answerValue = VALUE_X;

        btnO.setEnabled(false);
        btnX.setClickable(false);

        visibleBad("", "");
    }

    @OnClick(R.id.ox_next)
    public void clickNext(){

        answerValue = -1;

        btnNext.setVisibility(View.GONE);
        answerView.setVisibility(View.GONE);

        oxContent.setVisibility(View.VISIBLE);

        btnX.setEnabled(true);
        btnX.setClickable(true);
        btnO.setEnabled(true);
        btnO.setClickable(true);

    }

    @OnClick(R.id.top_back)
    public void back(){
        finish();
    }

}
