package com.example.chezhi.mytodo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.chezhi.mytodo.activity.MainActivity;
import com.example.chezhi.mytodo.receiver.AlarmReceiver;


/**
 * Created by chezhi on 16-10-22.
 */

public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("NotifyService.this","The notificationService is running");
                        }
                    }).start();
                    if(MainActivity.notifySize()==0){
                        Log.d("NotifyService.this","the notify list is empty service will stop");
                        stopSelf();
                    }
                    else{
                        long triggerAtTime=MainActivity.notifyTime(0)-8*3600*1000;
//                    Log.d("NotifyService.this","the Time is "+time);
                        Log.d("NotifyService.this","the triggerAtTime is "+triggerAtTime);
                        Log.d("NotifyService.this","the now time is "+System.currentTimeMillis());
                        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent intent1=new Intent(this,AlarmReceiver.class);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent1,0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
                    }

        return super.onStartCommand(intent,flags,startId);
    }
}
