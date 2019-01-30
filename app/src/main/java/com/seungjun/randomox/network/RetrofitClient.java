package com.seungjun.randomox.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.network.data.OxContentInfo;
import com.seungjun.randomox.network.data.UserInfo;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit 라이브러리 싱글톤 클래스
 */
public class RetrofitClient {

    private RetrofitApiService apiService;
    private static Retrofit retrofit;
    private static Context mContext;

    public static final int JSON_MAKE_ERROR = 9999;

    /**
     * 싱글톤 객체 홀더 설정
     */
    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
    }

    /**
     * 생성자
     * @param context
     */
    private RetrofitClient(Context context){

        // retrofit 네트워크 로그를 보기 위한 객체 생성
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // gson lenient 에러 방지용 객체 생성
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://13.209.214.110:6050/randomOX/")
                .client(client)
                .build();
    }


    /**
     * 클라이언트 싱글톤 객체 반환
     * @param context
     * @return
     */
    public static RetrofitClient getInstance(Context context) {

        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }


    /**
     * 클라이언트 api 서비스 객체를 생성한 뒤
     * 클라이언트 객체를 반환한다
     * @return
     */
    public RetrofitClient createBaseApi() {

        apiService = create(RetrofitApiService.class);

        return this;

    }

    /**
     * retrofit api 서비스를 생성하기 위한 함수
     * @param service
     * @param <T>
     * @return
     */
    private <T> T create(final Class<T> service) {

        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }

        return retrofit.create(service);

    }


    /**
     * OX 문제 받아오기 API
     * 방식 : POST
     * @param callback
     */
    public void callPostGetOX(final RetrofitApiCallback callback, int sIndex){

        HashMap<String, Object> body = new HashMap<>();
        body.put("sIndex", sIndex);

        apiService.getOXContent(body).enqueue(new Callback<OxContentInfo>() {
            @Override
            public void onResponse(Call<OxContentInfo> call, Response<OxContentInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<OxContentInfo> call, Throwable t) {
                callback.onError(t);
            }
        });

    }


    /**
     * 로그인 요청 API
     * 방식 : POST
     *
     * @param callback
     * @param user_nick 닉네임
     * @param user_pw 패스워드
     */
    public void callPostLogin(RetrofitApiCallback callback, String user_nick, String user_pw){

        HashMap<String, Object> body = new HashMap<>();
        body.put("user_nick", user_nick);
        body.put("user_pw", user_pw);


        apiService.reqLogin(body).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                callback.onError(t);
            }
        });

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
    public void callPostJoin(RetrofitApiCallback callback, String user_nick, String user_pw, String user_fcm){

        HashMap<String, Object> body = new HashMap<>();
        body.put("user_nick", user_nick);
        body.put("user_pw", user_pw);
        body.put("user_fcm", user_fcm);


        apiService.reqJoin(body).enqueue(new Callback<HeaderInfo>() {
            @Override
            public void onResponse(Call<HeaderInfo> call, Response<HeaderInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<HeaderInfo> call, Throwable t) {
                callback.onError(t);
            }
        });

    }

    /**
     * fcm 키 업데이트용 api
     * @param callback
     * @param user_key
     * @param user_fcm
     */
    public void callPostFcmUpdate(RetrofitApiCallback callback, String user_key, String user_fcm){

        HashMap<String, Object> body = new HashMap<>();
        body.put("user_key", user_key);
        body.put("user_fcm", user_fcm);

        apiService.reqFcmUpdate(body).enqueue(new Callback<HeaderInfo>() {
            @Override
            public void onResponse(Call<HeaderInfo> call, Response<HeaderInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<HeaderInfo> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    /**
     * 사용자 점수랑 인덱스 업뎃 api
     * @param callback
     * @param user_key
     * @param user_point
     * @param user_sIndex
     */
    public void callPostUpdateUserInfo(RetrofitApiCallback callback, String user_key, int user_point, int user_sIndex){

        HashMap<String, Object> body = new HashMap<>();
        body.put("user_key", user_key);
        body.put("user_point", user_point);
        body.put("user_sIndex", user_sIndex);

        apiService.reqUpdateUserInfo(body).enqueue(new Callback<HeaderInfo>() {
            @Override
            public void onResponse(Call<HeaderInfo> call, Response<HeaderInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<HeaderInfo> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    /**
     * 상담소 편지 보내기 api
     * @param callback
     * @param user_key
     * @param user_nick
     * @param letter_text
     */
    public void callPostSendLetter(RetrofitApiCallback callback, String user_key, String user_nick, String letter_text){

        HashMap<String, Object> body = new HashMap<>();
        body.put("user_key", user_key);
        body.put("user_nick", user_nick);
        body.put("letter_text", letter_text);

        apiService.reqSendLetter(body).enqueue(new Callback<HeaderInfo>() {
            @Override
            public void onResponse(Call<HeaderInfo> call, Response<HeaderInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<HeaderInfo> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    /**
     * 탈퇴 요청 하기
     * @param callback
     * @param user_key
     * @param user_nick
     * @param user_pw
     */
    public void callPostDeleteInfo(RetrofitApiCallback callback, String user_key, String user_nick, String user_pw){

        HashMap<String, Object> body = new HashMap<>();
        body.put("user_key", user_key);
        body.put("user_nick", user_nick);
        body.put("user_pw", user_pw);


        apiService.reqDeleteInfo(body).enqueue(new Callback<HeaderInfo>() {
            @Override
            public void onResponse(Call<HeaderInfo> call, Response<HeaderInfo> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.code(), response.body());
                }else{
                    callback.onFailed(response.code());
                }
            }

            @Override
            public void onFailure(Call<HeaderInfo> call, Throwable t) {
                callback.onError(t);
            }
        });

    }
}
