package com.seungjun.randomox.network

import com.seungjun.randomox.network.data.HeaderInfo
import com.seungjun.randomox.network.data.NoticesInfo
import com.seungjun.randomox.network.data.OxContentInfo
import com.seungjun.randomox.network.data.RankInfo
import com.seungjun.randomox.network.data.UserInfo

import java.util.HashMap

import butterknife.BindView
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit 을 활용하여 요청할 api 목록 인터페이스
 *
 * @GET 인 경우는 () 안에 get url 이 들어가고
 * 만약 경로상에 유동적인 값이 들어가야 한다면 쓰는 게 @Path(경로{} 값)
 * 만약 url에 데이터를 &키=값 형태로 넣어야 한다면 @Query(키값) 를 사용
 */
interface RetrofitApiService {

    @POST("getOX")
    fun getOXContent(@Body body: HashMap<String, Any>): Call<OxContentInfo>

    @POST("login")
    fun reqLogin(@Body body: HashMap<String, Any>): Call<UserInfo>

    @POST("join")
    fun reqJoin(@Body body: HashMap<String, Any>): Call<HeaderInfo>

    @POST("fcmUpdate")
    fun reqFcmUpdate(@Body body: HashMap<String, Any>): Call<HeaderInfo>

    @POST("updateUserInfo")
    fun reqUpdateUserInfo(@Body body: HashMap<String, Any>): Call<HeaderInfo>

    @POST("sendLetter")
    fun reqSendLetter(@Body body: HashMap<String, Any>): Call<HeaderInfo>

    @POST("deleteInfo")
    fun reqDeleteInfo(@Body body: HashMap<String, Any>): Call<HeaderInfo>

    @POST("getMyInfo")
    fun reqMyInfo(@Body body: HashMap<String, Any>): Call<UserInfo>

    @GET("getNotices")
    fun reqGetNotices(): Call<NoticesInfo>

    @GET("getRankInfo")
    fun reqGetRankInfo(): Call<RankInfo>
}
