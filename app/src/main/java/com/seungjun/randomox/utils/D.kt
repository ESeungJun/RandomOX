package com.seungjun.randomox.utils

import android.util.Log

/**
 * 로그를 쉽게 관리하기위한 클래스
 *
 */
object D {
    //	private static final boolean isDebug = true;
    private val isDebug = false

    fun log(tag: String, message: String) {
        if (isDebug)
            Log.d("[$tag] ", message)
    }

    fun error(tag: String, message: String) {
        if (isDebug)
            Log.e("[$tag] ", message)
    }

    fun error(tag: String, message: String, tr: Throwable) {
        if (isDebug)
            Log.e("[$tag] ", message, tr)
    }

}
