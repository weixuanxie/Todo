package com.example.chezhi.mytodo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;



/**
 * Created by chezhi on 16-10-22.
 */

public class NotifyService extends Service {
    private  TodoDatabaseHelper dbhelper;
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
                    long triggerAtTime=NotifyActivity.notifyTime(0);
                    Log.d("NotifyService.this","the triggerAtTime is "+triggerAtTime);
                    Log.d("NotifyService.this","the now time is "+System.currentTimeMillis());
                    AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent1=new Intent(this,AlarmReceiver.class);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent1,0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent,flags,startId);
    }
}
