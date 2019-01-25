package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.seungjun.randomox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinPopup extends Dialog {

    private Context context;

    @BindView(R.id.input_nickname)
    EditText inputNickName;

    @BindView(R.id.input_pw)
    EditText inputPw;

    @BindView(R.id.error_text)
    TextView errorText;


    public JoinPopup(Context context) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_join_dialog);

        this.context = context;

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_join)
    public void clickJoin(){

        if(TextUtils.isEmpty(inputNickName.getText().toString())){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(context.getResources().getString(R.string.error_empty_name));

            return;
        }

        if (TextUtils.isEmpty(inputPw.getText().toString())){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(context.getResources().getString(R.string.error_empty_pw));

            return;
        }
    }


    @Override
    public void show() {

        if(((Activity)context).isFinishing())
            return;

        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
