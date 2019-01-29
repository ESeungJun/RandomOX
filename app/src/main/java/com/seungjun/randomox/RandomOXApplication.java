package com.seungjun.randomox;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.seungjun.randomox.network.RetrofitClient;
import com.seungjun.randomox.utils.D;
import com.seungjun.randomox.utils.PreferenceUtils;
import com.seungjun.randomox.view.NetworkProgressDialog;

public class RandomOXApplication extends Application {

    private static final String TAG = "RandomOXApplication";

    private static RandomOXApplication context = null;


    @Override
    public void onCreate() {
        super.onCreate();


        if(context == null)
            context = this;

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            D.error(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        D.log(TAG, "FCM ID > " + token);

                        PreferenceUtils.getInstance(context).setUserFcmKey(token);
                    }
                });
    }


    public static RandomOXApplication getAppContext(){
        return context;
    }
}
