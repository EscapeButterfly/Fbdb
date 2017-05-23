package com.gorovoii.vitalii.fbdb.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceToDevice extends Service {

    private static final String TAG = "SENDNOTIF";
    IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public DeviceToDevice getServerInstance() {
            return DeviceToDevice.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private static final String LEGACY_SERVER_KEY =
            "AAAASKprTh8:APA91bG2Q6DhRf6vNLZ7FRQ6cJyAIu7OsPRau8BsO8pbSQk6WphOqmGU_MfRLLmUK15g50iu4S2mY96SmIOgCS9kTpBJHuAGLRDiUDlCfMON19IGSzlbztqWrAwFL8FQf478YxIJgQ-B";


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void sendNotification(final String regToken) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body","Hi this is sent from device to device");
                    dataJson.put("title","dummy title");
                    json.put("notification",dataJson);
                    json.put("to",regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();
    }


}
