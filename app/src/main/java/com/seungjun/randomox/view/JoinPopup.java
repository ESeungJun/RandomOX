package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.R;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.RetrofitClient;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.network.data.UserInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.utils.PreferenceUtils;
import com.wang.avi.AVLoadingIndicatorView;

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

    @BindView(R.id.join_progress)
    AVLoadingIndicatorView joinProgress;

    @BindView(R.id.btn_join)
    TextView btnJoin;


    private RetrofitClient networkClient;


    public JoinPopup(Context context) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_join_dialog);

        this.context = context;

        ButterKnife.bind(this);

        networkClient = RetrofitClient.getInstance(context).createBaseApi();
    }

    @OnClick(R.id.btn_join)
    public void clickJoin(){

        String nickname = inputNickName.getText().toString();
        String password = inputPw.getText().toString();

        if(TextUtils.isEmpty(nickname)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(context.getResources().getString(R.string.error_empty_name));

            return;
        }


        if(!CommonUtils.isValidName(nickname)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(context.getResources().getString(R.string.error_nonok_name));

            return;
        }


        if (TextUtils.isEmpty(password)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(context.getResources().getString(R.string.error_empty_pw));

            return;
        }


        if(!CommonUtils.isValidPw(password)){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(context.getResources().getString(R.string.error_nonok_pw));

            return;
        }


        inputNickName.setEnabled(false);
        inputNickName.setFocusable(false);

        inputPw.setEnabled(false);
        inputPw.setFocusable(false);

        btnJoin.setVisibility(View.GONE);
        joinProgress.setVisibility(View.VISIBLE);
        this.setCancelable(false);


        networkClient.callPostJoin(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {

                inputNickName.setEnabled(true);
                inputNickName.setFocusable(true);

                inputPw.setEnabled(true);
                inputPw.setFocusable(true);

                btnJoin.setVisibility(View.VISIBLE);
                joinProgress.setVisibility(View.GONE);
                JoinPopup.this.setCancelable(true);

                errorText.setText(context.getResources().getString(R.string.error_network_unkonw));
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                HeaderInfo result = (HeaderInfo)resultData;

                if(result.reqCode == 0){
                    Toast.makeText(context, "가입 성공! 로그인 해주세요!", Toast.LENGTH_SHORT).show();

                    JoinPopup.this.dismiss();
                }else{

                    inputNickName.setEnabled(true);
                    inputNickName.setFocusable(true);

                    inputPw.setEnabled(true);
                    inputPw.setFocusable(true);

                    btnJoin.setVisibility(View.VISIBLE);
                    joinProgress.setVisibility(View.GONE);
                    JoinPopup.this.setCancelable(true);

                    errorText.setText(result.reqMsg);
                }
            }

            @Override
            public void onFailed(int code) {


                inputNickName.setEnabled(true);
                inputNickName.setFocusable(true);

                inputPw.setEnabled(true);
                inputPw.setFocusable(true);

                btnJoin.setVisibility(View.VISIBLE);
                joinProgress.setVisibility(View.GONE);
                JoinPopup.this.setCancelable(true);

            }
        }, nickname, password, PreferenceUtils.getInstance(context).getUserFcmKey());
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
