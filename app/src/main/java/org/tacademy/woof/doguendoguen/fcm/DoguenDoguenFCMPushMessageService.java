package org.tacademy.woof.doguendoguen.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.home.HomeActivity;

import java.net.URLDecoder;
import java.util.Map;

/*
  실제 FCM을 통한 푸쉬메세지를 받는 곳
 */
public class DoguenDoguenFCMPushMessageService extends FirebaseMessagingService {
    private static final String TAG = "FCMPushMessageService";
    /*
     실제 푸쉬 메세지가 도착할 경우 자동 실행될 콜백메소드!!!!!
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        //실제 푸쉬로 넘어온 데이터
        Map<String, String> receiveData = remoteMessage.getData();  //키 벨류, 자동 파싱해서 들어감!!!서버:한글로보낼때utf8인코딩
        try {
            //한글은 반드시 디코딩 해준다.
            sendPushNotification(URLDecoder.decode(receiveData.get("pushMessage"), "UTF-8"));
        } catch (Exception e) {
            Log.e("decodingError", e.toString());
        }
    }
    private void sendPushNotification(String pushMessage) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("fcmExtra", pushMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM 푸쉬 메세지")
                .setContentText(pushMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}