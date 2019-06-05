package com.seungjun.randomox

import android.app.Application

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.seungjun.randomox.network.RetrofitClient
import com.seungjun.randomox.utils.D
import com.seungjun.randomox.utils.PreferenceUtils
import com.seungjun.randomox.view.NetworkProgressDialog

class RandomOXApplication : Application() {

    companion object {

        private val TAG = "RandomOXApplication"

        var appContext: RandomOXApplication? = null
    }

    override fun onCreate() {
        super.onCreate()


        if (appContext == null)
            appContext = this

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        D.error(TAG, "getInstanceId failed", task.exception!!)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result!!.token

                    D.log(TAG, "FCM ID > $token")

                    PreferenceUtils.getInstance(appContext!!).userFcmKey = token
                })
    }

}
