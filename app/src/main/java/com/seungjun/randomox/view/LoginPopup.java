package com.seungjun.randomox.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.seungjun.randomox.R;
import com.seungjun.randomox.activity.MainActivity;
import com.seungjun.randomox.network.data.UserInfo;
import com.seungjun.randomox.utils.CommonUtils;
import com.seungjun.randomox.utils.D;
import com.seungjun.randomox.utils.PreferenceUtils;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.RetrofitClient;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginPopup extends Dialog {

    private static final String TAG = "LoginPopup";

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

    private LoginCallBack callBack;


    public interface LoginCallBack {
        public void onLogin(boolean isLogin);
    }



    public LoginPopup(Context context, LoginCallBack callBack) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_login_dialog);

        this.context = context;

        this.callBack = callBack;

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


        if(!CommonUtils.isValidName(nickname) ||
                nickname.length() < 2 ||
                nickname.length() > 12){
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
                failedLogin(context.getResources().getString(R.string.error_network_unkonw));
            }

            @Override
            public void onSuccess(int code, Object resultData) {

                UserInfo userInfo = (UserInfo)resultData;

                if(userInfo.reqCode == 0){

                    PreferenceUtils.getInstance(context).setUserSindex(userInfo.user_sIndex);
                    PreferenceUtils.getInstance(context).setUserScore(userInfo.user_point);
                    PreferenceUtils.getInstance(context).setUserId(nickname);
                    PreferenceUtils.getInstance(context).setUserPw(CommonUtils.getAES256(context, password));
                    PreferenceUtils.getInstance(context).setUserKey(userInfo.user_key);
                    PreferenceUtils.getInstance(context).setLoginSuccess(true);

                    updateFCM();

                }else{

                    failedLogin(userInfo.reqMsg);
                }


            }

            @Override
            public void onFailed(int code) {

                failedLogin(context.getResources().getString(R.string.error_network_unkonw));

            }
        }, nickname, CommonUtils.getAES256(context, password));
    }


    @Override
    public void show() {

        if(((Activity)context).isFinishing())
            return;

        super.show();
    }

    public void failedLogin(String msg){

        inputNickName.setEnabled(true);
        inputNickName.setFocusableInTouchMode(true);
        inputNickName.setFocusable(true);

        inputPw.setEnabled(true);
        inputPw.setFocusableInTouchMode(true);
        inputPw.setFocusable(true);

        btnLogin.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.GONE);
        LoginPopup.this.setCancelable(true);

        errorText.setVisibility(View.VISIBLE);
        errorText.setText(msg);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }


    public void updateFCM(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            D.error(TAG, "getInstanceId failed", task.getException());

                            failedLogin(context.getResources().getString(R.string.error_network_unkonw));

                            return;
                        }

                        // 보내기전에 비교해보기
                        if(!PreferenceUtils.getInstance(context).getUserFcmKey().equals(task.getResult().getToken())){
                            PreferenceUtils.getInstance(context).setUserFcmKey(task.getResult().getToken());
                        }

                        networkClient.callPostFcmUpdate(new RetrofitApiCallback() {
                            @Override
                            public void onError(Throwable t) {
                                failedLogin(context.getResources().getString(R.string.error_network_unkonw));
                            }

                            @Override
                            public void onSuccess(int code, Object resultData) {

                                Toast.makeText(context, "로그인 성공! 앞으로 자동 로그인이 됩니다.", Toast.LENGTH_SHORT).show();
                                callBack.onLogin(true);
                                LoginPopup.this.dismiss();

                            }

                            @Override
                            public void onFailed(int code) {
                                failedLogin(context.getResources().getString(R.string.error_network_unkonw));;
                            }
                        }, PreferenceUtils.getInstance(context).getUserKey(), PreferenceUtils.getInstance(context).getUserFcmKey());
                    }
                });

    }
}
