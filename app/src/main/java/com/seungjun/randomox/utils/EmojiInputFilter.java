package com.seungjun.randomox.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.seungjun.randomox.R;

/**
 * 사용 불가능한 이모티콘들을 제한시키기 위한
 * edit text 입력 필터 클래스
 * Created by SeungJun on 2017-11-17.
 */

public class EmojiInputFilter implements InputFilter {

    private Context mContext;

    public EmojiInputFilter(Context context) {
        mContext = context;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 입력된 문자 전체를 하나씩 검사
        for (int index = start; index < end; index++) {
            // 개별로 타입을 받아 온다
            int type = Character.getType(source.charAt(index));

            // 특수 이모티콘이나 별도로 제한시킨 문자열에 걸리면 toast 출력
            if (type == Character.SURROGATE) {
                Toast.makeText(mContext, mContext.getResources().getText(R.string.invalid_character_entered), Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        return null;
    }
}