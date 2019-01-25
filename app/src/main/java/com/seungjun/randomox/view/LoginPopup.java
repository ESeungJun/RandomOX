package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seungjun.randomox.R;
import com.seungjun.randomox.data.PreferenceUtils;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPopup extends Dialog {

    private Context context;

    @BindView(R.id.input_nickname)
    EditText inputNickName;

    @BindView(R.id.input_pw)
    EditText inputPw;

    @BindView(R.id.error_text)
    TextView errorText;

    @BindView(R.id.login_progress)
    AVLoadingIndicatorView loginProgress;

    @BindView(R.id.btn_login)
    TextView btnLogin;


    private RetrofitClient networkClient;


    public LoginPopup(Context context) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_login_dialog);

        this.context = context;

        ButterKnife.bind(this);

        networkClient = RetrofitClient.getInstance(context).createBaseApi();
    }

    @OnClick(R.id.btn_login)
    public void clickLogin(){

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


        inputNickName.setEnabled(false);
        inputNickName.setFocusable(false);

        inputPw.setEnabled(false);
        inputPw.setFocusable(false);

        btnLogin.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        this.setCancelable(false);


        networkClient.callGetTest(new RetrofitApiCallback() {
            @Override
            public void onError(Throwable t) {

                inputNickName.setEnabled(true);
                inputNickName.setFocusable(true);

                inputPw.setEnabled(true);
                inputPw.setFocusable(true);

                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.GONE);
                LoginPopup.this.setCancelable(true);

                errorText.setText(context.getResources().getString(R.string.error_network_unkonw));
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                PreferenceUtils.getInstance(context).setLoginSuccess(true);

                Toast.makeText(context, "로그인 성공! 앞으로 자동 로그인이 됩니다.", Toast.LENGTH_SHORT).show();

                LoginPopup.this.dismiss();
            }

            @Override
            public void onFailed(int code) {


                inputNickName.setEnabled(true);
                inputNickName.setFocusable(true);

                inputPw.setEnabled(true);
                inputPw.setFocusable(true);

                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.GONE);
                LoginPopup.this.setCancelable(true);

            }
        });
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
