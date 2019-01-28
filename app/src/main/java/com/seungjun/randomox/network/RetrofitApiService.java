package com.seungjun.randomox.network;

import com.seungjun.randomox.network.data.OxContentInfo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Retrofit 을 활용하여 요청할 api 목록 인터페이스
 *
 * @GET 인 경우는 () 안에 get url 이 들어가고
 * 만약 경로상에 유동적인 값이 들어가야 한다면 쓰는 게 @Path(경로{} 값)
 * 만약 url에 데이터를 &키=값 형태로 넣어야 한다면 @Query(키값) 를 사용
 *
 *
 */
public interface RetrofitApiService {

    @POST("getOX")
    Call<OxContentInfo> getOXContent(@Body HashMap<String, Object> body);


}
