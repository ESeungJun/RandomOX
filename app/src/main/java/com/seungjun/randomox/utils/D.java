package com.seungjun.randomox.utils;

import android.util.Log;

/**
 * 로그를 쉽게 관리하기위한 클래스
 *
 */
public class D
{
    //	private static final boolean isDebug = true;
    private static boolean isDebug = false;

    public static void log(String tag, String message)
    {
        if (isDebug)
            Log.d("[" + tag + "] ", message);
    }

    public static void error(String tag, String message)
    {
        if (isDebug)
            Log.e("[" + tag + "] ", message);
    }

    public static void error(String tag, String message, Throwable tr)
    {
        if (isDebug)
            Log.e("[" + tag + "] ", message, tr);
    }

}
