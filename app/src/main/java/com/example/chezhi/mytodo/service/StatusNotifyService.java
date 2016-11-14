package com.example.chezhi.mytodo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.example.chezhi.mytodo.R;
import com.example.chezhi.mytodo.activity.MainActivity;

/**
 * Created by chezhi on 16-11-4.
 */

public class StatusNotifyService extends Service {

    String TAG="StatusNotifyService";

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"The sNotifyService is onCreate");
        Intent intent=new Intent(StatusNotifyService.this, MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new Notification.Builder(this).setSmallIcon(R.mipmap.ic_notify).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).setContentTitle("待完成").setContentText(MainActivity.notifySize()+" 项代办").setContentIntent(pi).build();
        startForeground(2,notification);

    }

    @Override
    public void onDestroy(){
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }
}
