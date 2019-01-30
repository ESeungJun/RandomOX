package com.seungjun.randomox.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.seungjun.randomox.R;
import com.seungjun.randomox.view.NormalPopup;

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    /**
     * 비밀번호 유효성 체크
     * 영어 + 숫자 + 특수문자 조합 4 ~ 10자
     *
     * @param pw
     *         체크할 비밀번호
     * @return 유효성 여부
     */
    public static boolean isValidPw(String pw) {
        String stricterFilterString = "^((?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^*+=-])).{4,10}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(stricterFilterString);
        java.util.regex.Matcher m = p.matcher(pw);
        return m.matches();
    }

    /**
     * 닉네임 유효성 체크
     * 한글 2 ~ 12자
     *
     * @param nickname 체크할 이름
     * @return 유효성 여부
     */
    public static boolean isValidName(String nickname) {
        String stricterFilterString = "^[ㄱ-힣\\s]*.{2,12}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(stricterFilterString);
        java.util.regex.Matcher m = p.matcher(nickname);
        return m.matches();
    }

    /**
     * 에러 팝업 노출
     * 버튼은 확인 하나만 나옴
     * @param context
     * @param text
     */
    public static void showErrorPopup(Context context, String text, boolean isFinish){

        NormalPopup popup = new NormalPopup(context);
        popup.setPopupText(text);

        popup.setOKClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();

                if(isFinish)
                    ((Activity)context).finish();
            }
        });

        popup.setCancelVisible(View.GONE);
        popup.show();

    }

    /**
     * AES256 암호화
     * @param text 암호화
     * @return
     */
    public static String getAES256(Context context, String text) {
        AESUtils aes = null;
        try {
            aes = new AESUtils(context);
            return aes.encrypt(text);
        } catch (Exception e) {
            D.log(TAG, "AES256 e : "+ e.toString());
            return "";
        }
    }

    /**
     * AES256 복호화
     * @param text 복호화
     * @return
     */
    public static String setAES256(Context context, String text) {
        AESUtils aes = null;
        try {
            aes = new AESUtils(context);
            return aes.decrypt(text);
        } catch (Exception e) {
            D.log(TAG, "AES256 e : "+ e.toString());
            return "";
        }
    }
}
