package com.seungjun.randomox.network

interface RetrofitApiCallback<T> {

    fun onError(t: Throwable)

    fun onSuccess(code: Int, resultData: T)

    fun onFailed(code: Int)

}
