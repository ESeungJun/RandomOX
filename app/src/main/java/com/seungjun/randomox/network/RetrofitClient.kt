package com.seungjun.randomox.network

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.network.data.NoticesInfo
import com.seungjun.randomox.network.data.OxContentInfo
import com.seungjun.randomox.network.data.RankInfo
import com.seungjun.randomox.network.data.UserInfo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import org.json.JSONObject

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


/**
 * Retrofit 라이브러리 싱글톤 클래스
 */
object RetrofitClient{

    
    val retrofit: Retrofit by lazy {

        val client = OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }.build()

        val gson = GsonBuilder().apply {
            setLenient()
        }.create()
        
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(gson))
            addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            baseUrl("http://54.180.6.192:8080/randomOX/")
            client(client)
        }.build()
    }
    
    private var apiService: RetrofitApiService = retrofit.create(RetrofitApiService::class.java)
    


    /**
     * OX 문제 받아오기 API
     * 방식 : POST
     * @param callback
     */
    fun callPostGetOX(callback: RetrofitApiCallback<OxContentInfo>, sIndex: Int) {

        val body = HashMap<String, Any>()
        body["sIndex"] = sIndex

        apiService.getOXContent(body).enqueue(object : Callback<OxContentInfo> {
            override fun onResponse(call: Call<OxContentInfo>, response: Response<OxContentInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as OxContentInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<OxContentInfo>, t: Throwable) {
                callback.onError(t)
            }
        })

    }


    /**
     * 로그인 요청 API
     * 방식 : POST
     *
     * @param callback
     * @param user_nick 닉네임
     * @param user_pw 패스워드
     */
    fun callPostLogin(callback: RetrofitApiCallback<UserInfo>, user_nick: String, user_pw: String) {

        val body = HashMap<String, Any>()
        body["user_nick"] = user_nick
        body["user_pw"] = user_pw


        apiService.reqLogin(body).enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as UserInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                callback.onError(t)
            }
        })

    }


    /**
     * 가입 요청 API
     * 방식 : POST
     *
     * @param callback
     * @param user_nick 닉네임
     * @param user_pw 패스워드
     * @param user_fcm fcm 키 값
     */
    fun callPostJoin(callback: RetrofitApiCallback<HeaderInfo>, user_nick: String, user_pw: String, user_fcm: String) {

        val body = HashMap<String, Any>()
        body["user_nick"] = user_nick
        body["user_pw"] = user_pw
        body["user_fcm"] = user_fcm


        apiService.reqJoin(body).enqueue(object : Callback<HeaderInfo> {
            override fun onResponse(call: Call<HeaderInfo>, response: Response<HeaderInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as HeaderInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<HeaderInfo>, t: Throwable) {
                callback.onError(t)
            }
        })

    }

    /**
     * fcm 키 업데이트용 api
     * @param callback
     * @param user_key
     * @param user_fcm
     */
    fun callPostFcmUpdate(callback: RetrofitApiCallback<HeaderInfo>, user_key: String, user_fcm: String) {

        val body = hashMapOf(Pair("user_key", user_key as Any), Pair("user_fcm", user_fcm as Any))

        apiService.reqFcmUpdate(body).enqueue(object : Callback<HeaderInfo> {
            override fun onResponse(call: Call<HeaderInfo>, response: Response<HeaderInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as HeaderInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<HeaderInfo>, t: Throwable) {
                callback.onError(t)
            }
        })
    }


    /**
     * 사용자 점수랑 인덱스 업뎃 api
     * @param callback
     * @param user_key
     * @param user_point
     * @param user_sIndex
     */
    fun callPostUpdateUserInfo(callback: RetrofitApiCallback<HeaderInfo>, user_key: String, user_point: Int, user_sIndex: Int) {

        val body = HashMap<String, Any>()
        body["user_key"] = user_key
        body["user_point"] = user_point
        body["user_sIndex"] = user_sIndex

        apiService.reqUpdateUserInfo(body).enqueue(object : Callback<HeaderInfo> {
            override fun onResponse(call: Call<HeaderInfo>, response: Response<HeaderInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as HeaderInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<HeaderInfo>, t: Throwable) {
                callback.onError(t)
            }
        })
    }


    /**
     * 상담소 편지 보내기 api
     * @param callback
     * @param user_key
     * @param user_nick
     * @param letter_text
     */
    fun callPostSendLetter(callback: RetrofitApiCallback<HeaderInfo>, user_key: String, user_nick: String, letter_text: String) {

        val body = HashMap<String, Any>()
        body["user_key"] = user_key
        body["user_nick"] = user_nick
        body["letter_text"] = letter_text

        apiService.reqSendLetter(body).enqueue(object : Callback<HeaderInfo> {
            override fun onResponse(call: Call<HeaderInfo>, response: Response<HeaderInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as HeaderInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<HeaderInfo>, t: Throwable) {
                callback.onError(t)
            }
        })
    }


    /**
     * 탈퇴 요청 하기
     * @param callback
     * @param user_key
     * @param user_nick
     * @param user_pw
     */
    fun callPostDeleteInfo(callback: RetrofitApiCallback<HeaderInfo>, user_key: String, user_nick: String, user_pw: String) {

        val body = HashMap<String, Any>()
        body["user_key"] = user_key
        body["user_nick"] = user_nick
        body["user_pw"] = user_pw


        apiService.reqDeleteInfo(body).enqueue(object : Callback<HeaderInfo> {
            override fun onResponse(call: Call<HeaderInfo>, response: Response<HeaderInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as HeaderInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<HeaderInfo>, t: Throwable) {
                callback.onError(t)
            }
        })

    }

    /**
     * 공지 정보 받아오기
     * @param callback
     */
    fun callGetNotices(callback: Observer<NoticesInfo>) {

        apiService.reqGetNotices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback)

    }


    /**
     * 랭킹 정보 받아오기
     * @param callback
     */
    fun callGetRankInfo(callback: RetrofitApiCallback<RankInfo>) {


        apiService.reqGetRankInfo().enqueue(object : Callback<RankInfo> {
            override fun onResponse(call: Call<RankInfo>, response: Response<RankInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as RankInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<RankInfo>, t: Throwable) {

                callback.onError(t)
            }
        })

    }


    /**
     * 내정보 받아오기
     * @param callback
     * @param user_nick
     */
    fun callMyInfo(callback: RetrofitApiCallback<UserInfo>, user_nick: String) {

        val body = HashMap<String, Any>()
        body["user_nick"] = user_nick

        apiService.reqMyInfo(body).enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.code(), response.body() as UserInfo)
                } else {
                    callback.onFailed(response.code())
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {

                callback.onError(t)
            }
        })
    }
    
}
