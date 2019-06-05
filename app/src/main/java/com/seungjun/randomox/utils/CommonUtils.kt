package com.seungjun.randomox.utils

import android.app.Activity
import android.content.Context
import android.text.InputFilter
import android.view.View

import com.seungjun.randomox.view.NormalPopup

import java.util.regex.Pattern

object CommonUtils {

    private val TAG = "CommonUtils"

    /**
     * 비밀번호 유효성 체크
     * 영어 + 숫자 + 특수문자 조합 4 ~ 10자
     *
     * @param pw
     * 체크할 비밀번호
     * @return 유효성 여부
     */
    fun isValidPw(pw: String): Boolean {
        val stricterFilterString = "^((?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^*+=-])).{4,10}$"
        val p = java.util.regex.Pattern.compile(stricterFilterString)
        val m = p.matcher(pw)
        return m.matches()
    }

    /**
     * 닉네임 유효성 체크
     * 한글 2 ~ 12자
     *
     * @param nickname 체크할 이름
     * @return 유효성 여부
     */
    fun isValidName(nickname: String): Boolean {
        val stricterFilterString = "^[ㄱ-ㅣ가-힣\\s]*$"
        val p = java.util.regex.Pattern.compile(stricterFilterString)
        val m = p.matcher(nickname)
        return m.matches()
    }

    //    public InputFilter textSetFilter(String lang){
    //        Pattern ps = null;
    //
    //        if(lang.equals("kor")){
    //            ps = Pattern.compile("^[ㄱ-ㅣ가-힣\\s]*$"); //한글 및 공백문자만 허용
    //        }
    //
    //        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
    //            if (!ps.matcher(source).matches()) {
    //                return "";
    //            }
    //            return null;
    //        };
    //
    //        return filter;
    //    }

    /**
     * 에러 팝업 노출
     * 버튼은 확인 하나만 나옴
     * @param context
     * @param text
     */
    fun showErrorPopup(context: Context, text: String, isFinish: Boolean) {

        NormalPopup(context).apply {
            setPopupText(text)
            setOKClick(View.OnClickListener{
                dismiss()

                if (isFinish)
                    (context as Activity).finish()
            })
            setCancelVisible(View.GONE)
            show()
        }
    }

    /**
     * AES256 암호화
     * @param text 암호화
     * @return
     */
    fun getAES256(context: Context, text: String): String {
        var aes: AESUtils? = null
        try {
            aes = AESUtils(context)
            return aes.encrypt(text)
        } catch (e: Exception) {
            D.log(TAG, "AES256 e : $e")
            return ""
        }

    }

    /**
     * AES256 복호화
     * @param text 복호화
     * @return
     */
    fun setAES256(context: Context, text: String): String {
        var aes: AESUtils? = null
        try {
            aes = AESUtils(context)
            return aes.decrypt(text)
        } catch (e: Exception) {
            D.log(TAG, "AES256 e : $e")
            return ""
        }

    }
}
