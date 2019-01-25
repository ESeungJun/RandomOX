package com.seungjun.randomox.network;

public interface RetrofitApiCallback<T> {

    void onError(Throwable t);

    void onSuccess(int code, T resultData);

    void onFailed(int code);

}
