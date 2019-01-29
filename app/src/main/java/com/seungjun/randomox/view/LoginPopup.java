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
import com.seungjun.randomox.network.data.UserInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.utils.PreferenceUtils;
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

        btnLogin.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        this.setCancelable(false);


        networkClient.callPostLogin(new RetrofitApiCallback() {
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

                UserInfo userInfo = (UserInfo)resultData;

                if(userInfo.reqCode == 0){

                    PreferenceUtils.getInstance(context).setUserSindex(userInfo.user_sIndex);
                    PreferenceUtils.getInstance(context).setUserScore(userInfo.user_point);
                    PreferenceUtils.getInstance(context).setUserId(nickname);
                    PreferenceUtils.getInstance(context).setUserPw(password);
                    PreferenceUtils.getInstance(context).setLoginSuccess(true);

                    Toast.makeText(context, "로그인 성공! 앞으로 자동 로그인이 됩니다.", Toast.LENGTH_SHORT).show();

                    LoginPopup.this.dismiss();
                }else{

                    inputNickName.setEnabled(true);
                    inputNickName.setFocusable(true);

                    inputPw.setEnabled(true);
                    inputPw.setFocusable(true);

                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.GONE);
                    LoginPopup.this.setCancelable(true);


                    errorText.setText(userInfo.reqMsg);
                }


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

                errorText.setText(context.getResources().getString(R.string.error_network_unkonw));

            }
        }, nickname, password);
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
