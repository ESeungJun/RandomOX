package com.seungjun.randomox.utils

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.widget.Toast

import com.seungjun.randomox.R

/**
 * 사용 불가능한 이모티콘들을 제한시키기 위한
 * edit text 입력 필터 클래스
 * Created by SeungJun on 2017-11-17.
 */

class EmojiInputFilter(private val mContext: Context) : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        // 입력된 문자 전체를 하나씩 검사
        for (index in start until end) {
            // 개별로 타입을 받아 온다
            val type = Character.getType(source[index])

            // 특수 이모티콘이나 별도로 제한시킨 문자열에 걸리면 toast 출력
            if (type == Character.SURROGATE.toInt()) {
                Toast.makeText(mContext, mContext.resources.getText(R.string.invalid_character_entered), Toast.LENGTH_SHORT).show()
                return ""
            }
        }
        return null
    }
}