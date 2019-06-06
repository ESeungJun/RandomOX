package com.seungjun.randomox.network

import com.seungjun.randomox.network.data.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

/**
 * Retrofit 을 활용하여 요청할 api 목록 인터페이스
 *
 * @GET 인 경우는 () 안에 get url 이 들어가고
 * 만약 경로상에 유동적인 값이 들어가야 한다면 쓰는 게 @Path(경로{} 값)
 * 만약 url에 데이터를 &키=값 형태로 넣어야 한다면 @Query(키값) 를 사용
 */
interface RetrofitApiService {

    @POST("getOX")
    fun getOXContent(@Body body: HashMap<String, Any>): Observable<OxContentInfo>

    @POST("login")
    fun reqLogin(@Body body: HashMap<String, Any>): Observable<UserInfo>

    @POST("join")
    fun reqJoin(@Body body: HashMap<String, Any>): Observable<HeaderInfo>

    @POST("fcmUpdate")
    fun reqFcmUpdate(@Body body: HashMap<String, Any>): Observable<HeaderInfo>

    @POST("updateUserInfo")
    fun reqUpdateUserInfo(@Body body: HashMap<String, Any>): Observable<HeaderInfo>

    @POST("sendLetter")
    fun reqSendLetter(@Body body: HashMap<String, Any>): Observable<HeaderInfo>

    @POST("deleteInfo")
    fun reqDeleteInfo(@Body body: HashMap<String, Any>): Observable<HeaderInfo>

    @POST("getMyInfo")
    fun reqMyInfo(@Body body: HashMap<String, Any>): Observable<UserInfo>

    @GET("getNotices")
    fun reqGetNotices(): Observable<NoticesInfo>

    @GET("getRankInfo")
    fun reqGetRankInfo(): Observable<RankInfo>
}
