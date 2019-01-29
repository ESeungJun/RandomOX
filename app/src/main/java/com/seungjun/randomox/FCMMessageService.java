package com.seungjun.randomox;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.seungjun.randomox.activity.IntroActivity;
import com.seungjun.randomox.activity.MainActivity;
import com.seungjun.randomox.network.RetrofitApiCallback;
import com.seungjun.randomox.network.RetrofitClient;
import com.seungjun.randomox.network.data.HeaderInfo;
import com.seungjun.randomox.utils.D;
import com.seungjun.randomox.utils.PreferenceUtils;

import static com.seungjun.randomox.RandomOXApplication.getAppContext;

public class FCMMessageService extends FirebaseMessagingService {

    private final String TAG = "FCMMessageService";

    private final int FCM_TYPE_NORMAL = 0;
    private final int FCM_TYPE_LETTER = 1;

    public FCMMessageService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        D.log(TAG, "Message FROM > " + remoteMessage.getFrom());


        if(remoteMessage.getData().size() > 0){

            if(remoteMessage.getData().get("message") != null &&
                    remoteMessage.getData().get("type") != null){

                D.log(TAG, "Message Data Message > " + remoteMessage.getData().get("message"));
                D.log(TAG, "Message Data Type > " + remoteMessage.getData().get("type"));

                sendNotification(remoteMessage.getData().get("message"), Integer.parseInt(remoteMessage.getData().get("type")));
            }


        }


        if (remoteMessage.getNotification() != null) {
            D.log(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        D.log(TAG, "NEW FCM TOKEN > " + s);

        PreferenceUtils.getInstance(getAppContext()).setUserFcmKey(s);

        if(PreferenceUtils.getInstance(getAppContext()).isLoginSuccess()){

            RetrofitClient.getInstance(getAppContext())
                    .createBaseApi()
                    .callPostFcmUpdate(new RetrofitApiCallback() {
                        @Override
                        public void onError(Throwable t) {
                            D.log(TAG, "FCM TOKEN UPDATE FAIL > " +t.getMessage());
                        }

                        @Override
                        public void onSuccess(int code, Object resultData) {

                            if(((HeaderInfo)resultData).reqCode == 0)
                                D.log(TAG, "FCM TOKEN UPDATE");
                            else
                                D.log(TAG, "FCM TOKEN UPDATE FAIL > " + ((HeaderInfo)resultData).reqCode);
                        }

                        @Override
                        public void onFailed(int code) {
                            D.log(TAG, "FCM TOKEN UPDATE FAIL > " + code);
                        }
                    }, PreferenceUtils.getInstance(getAppContext()).getUserKey(), s);

        }

    }


    private void sendNotification(String messageBody, int type) {

        Intent intent = new Intent(this, IntroActivity.class);

        if(type == FCM_TYPE_LETTER)
            intent.putExtra("textData", messageBody);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(type == FCM_TYPE_LETTER ? "개발자의 편지가 도착했습니다 :)" : messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"랜덤OX", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
