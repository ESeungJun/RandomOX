package com.seungjun.randomox.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtils constructor(context: Context) {

    init {

        if (preferences == null)
            preferences = context.getSharedPreferences("RandomOXSetting", Context.MODE_PRIVATE)

        if (preEditor == null)
            preEditor = preferences!!.edit()
    }

    companion object {

        private var instance: PreferenceUtils? = null

        private var preferences: SharedPreferences? = null
        private var preEditor: SharedPreferences.Editor? = null


        private val LOGIN_SUCCESS = "login_success"
        private val USER_KEY = "user_key"
        private val USER_ID = "user_id"
        private val USER_PW = "user_pw"
        private val USER_SINDEX = "user_sindex"
        private val USER_SCORE = "user_score"
        private val USER_FCM_KEY = "user_fcm_key"
        private val USER_RANK = "user_rank"

        private val NOTI_NO_SHOW = "noti_no_show"
        private val NOTI_DATE = ""


        fun getInstance(context: Context): PreferenceUtils {

            if (instance == null)
                instance = PreferenceUtils(context)


            return instance!!
        }
    }

    /**
     * 로그인 성공 여부 반환
     * @return
     */
    /**
     * 로그인 성공 여부 저장
     * @param isLogin
     */
    var isLoginSuccess: Boolean
        get() = if (preferences != null) {
            preferences!!.getBoolean(LOGIN_SUCCESS, false)
        } else false
        set(isLogin) {

            if (preEditor != null) {

                preEditor!!.putBoolean(LOGIN_SUCCESS, isLogin)
                preEditor!!.commit()
            }

        }

    /**
     * 유저 아이디 반환
     * @return
     */
    /**
     * 유저 아이디 저장
     * @param userId
     */
    var userId: String?
        get() = if (preferences != null) preferences!!.getString(USER_ID, "") else ""
        set(userId) {
            if (preEditor != null) {
                preEditor!!.putString(USER_ID, userId)
                preEditor!!.commit()
            }
        }

    /**
     * 유저 암호화된 비번 반환
     * @return
     */
    /**
     * 유저 암호화된 비번 저장
     * @param userPw
     */
    var userPw: String?
        get() = if (preferences != null) preferences!!.getString(USER_PW, "") else ""
        set(userPw) {
            if (preEditor != null) {
                preEditor!!.putString(USER_PW, userPw)
                preEditor!!.commit()
            }
        }


    /**
     * 유저 문제 시작 인덱스 반환
     * @return
     */
    /**
     * 유저 문제 시작 인덱스 저장
     * @param sIndex
     */
    var userSindex: Int
        get() = if (preferences != null) preferences!!.getInt(USER_SINDEX, 1) else 1
        set(sIndex) {
            if (preEditor != null) {
                preEditor!!.putInt(USER_SINDEX, sIndex)
                preEditor!!.commit()

            }
        }


    /**
     * 유저 점수 반환
     * @return
     */
    /**
     * 유저 점수 저장
     * @param score
     */
    var userScore: Int
        get() = if (preferences != null) preferences!!.getInt(USER_SCORE, 0) else 0
        set(score) {
            if (preEditor != null) {
                preEditor!!.putInt(USER_SCORE, score)
                preEditor!!.commit()

            }
        }


    /**
     * fcm 키 반환
     * @return
     */
    /**
     * fcm 키 저장
     * @param fcmKey
     */
    var userFcmKey: String?
        get() = if (preferences != null) preferences!!.getString(USER_FCM_KEY, "") else ""
        set(fcmKey) {

            if (preEditor != null) {
                preEditor!!.putString(USER_FCM_KEY, fcmKey)
                preEditor!!.commit()
            }

        }


    /**
     * 유저 키 반환
     * @return
     */
    /**
     * 유저 키 저장
     * @param key
     */
    var userKey: String?
        get() = if (preferences != null) preferences!!.getString(USER_KEY, "") else ""
        set(key) {

            if (preEditor != null) {
                preEditor!!.putString(USER_KEY, key)
                preEditor!!.commit()
            }

        }

    /**
     * 공지 다시보기 여부 반환
     */
    /**
     * 공지 다시 보기 여부 저장
     * @param isShow
     */
    var isNotiNoShow: Boolean
        get() = if (preferences != null) preferences!!.getBoolean(NOTI_NO_SHOW, false) else false
        set(isShow) {
            if (preEditor != null) {
                preEditor!!.putBoolean(NOTI_NO_SHOW, isShow)
                preEditor!!.commit()
            }
        }

    /**
     * 공지 날짜 반환
     * @return
     */
    /**
     * 공지 날짜
     * @param date
     */
    var notiDate: String?
        get() = if (preferences != null) preferences!!.getString(NOTI_DATE, "") else ""
        set(date) {

            if (preEditor != null) {
                preEditor!!.putString(NOTI_DATE, date)
                preEditor!!.commit()

            }

        }


    var userRank: Int
        get() = if (preferences != null) preferences!!.getInt(USER_RANK, -1) else -1
        set(rank) {

            if (preEditor != null) {
                preEditor!!.putInt(USER_RANK, rank)
                preEditor!!.commit()
            }

        }



}
