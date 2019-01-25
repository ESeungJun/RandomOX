package com.seungjun.randomox.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private static PreferenceUtils instance;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor preEditor;


    private static final String LOGIN_SUCCESS = "login_success";


    private PreferenceUtils(Context context){

        if(preferences == null)
            preferences = context.getSharedPreferences("RandomOXSetting", Context.MODE_PRIVATE);

        if(preEditor == null)
            preEditor = preferences.edit();
    }


    public static PreferenceUtils getInstance(Context context){

        if(instance == null)
            instance = new PreferenceUtils(context);


        return instance;
    }

    /**
     * 로그인 성공 여부 저장
     * @param isLogin
     */
    public void setLoginSuccess(boolean isLogin){

        if(preEditor != null){

            preEditor.putBoolean(LOGIN_SUCCESS, isLogin);
            preEditor.commit();
        }

    }

    /**
     * 로그인 성공 여부 반환
     * @return
     */
    public boolean isLoginSuccess(){
        if(preferences != null){
            return preferences.getBoolean(LOGIN_SUCCESS, false);
        }

        return false;
    }
}
