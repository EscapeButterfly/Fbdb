package com.gorovoii.vitalii.fbdb.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.gorovoii.vitalii.fbdb.R;
import com.gorovoii.vitalii.fbdb.activity.MainActivity;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FMS extends com.google.firebase.messaging.FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMsgService";

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        Log.e("FMS", "onCreate");
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Log.i(TAG, "onMessageReceived: title : "+title);

        Intent intent = new Intent("MyData");
        intent.putExtra("title", title);
        intent.putExtra("body", body);
     /*   for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "key, " + key + " value " + value);
        }*/
        broadcaster.sendBroadcast(intent);
    }
}
