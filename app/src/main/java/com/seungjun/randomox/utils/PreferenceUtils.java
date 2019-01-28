package com.seungjun.randomox.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static PreferenceUtils instance;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor preEditor;


    private static final String LOGIN_SUCCESS = "login_success";
    private static final String USER_ID = "user_id";
    private static final String USER_PW = "user_pw";
    private static final String USER_SINDEX = "user_sindex";
    private static final String USER_SCORE = "user_score";


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

    /**
     * 유저 아이디 저장
     * @param userId
     */
    public void setUserId(String userId){
        if(preEditor != null){
            preEditor.putString(USER_ID, userId);
            preEditor.commit();
        }
    }

    /**
     * 유저 아이디 반환
     * @return
     */
    public String getUserId(){

        if(preferences != null)
            return preferences.getString(USER_ID, "");

        return "";
    }


    /**
     * 유저 비번 저장
     * @param userPw
     */
    public void setUserPw(String userPw){
        if(preEditor != null){
            preEditor.putString(USER_PW, userPw);
            preEditor.commit();
        }
    }

    /**
     * 유저 비번 반환
     * @return
     */
    public String getUserPw(){

        if(preferences != null)
            return preferences.getString(USER_PW, "");

        return "";
    }


    public void setUserSindex(int sIndex){
        if(preEditor != null){
            preEditor.putInt(USER_SINDEX, sIndex);
            preEditor.commit();

        }
    }


    public int getUserSindex(){
        if(preferences != null)
            return preferences.getInt(USER_SINDEX, 0);

        return 0;
    }


    public void setUserScore(int score){
        if(preEditor != null){
            preEditor.putInt(USER_SCORE, score);
            preEditor.commit();

        }
    }


    public int getUserScore(){
        if(preferences != null)
            return preferences.getInt(USER_SCORE, 0);

        return 0;
    }
}