package com.example.chezhi.mytodo.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.chezhi.mytodo.activity.MainActivity;
import com.example.chezhi.mytodo.service.NotifyService;
import com.example.chezhi.mytodo.R;


/**
 * Created by chezhi on 16-10-22.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("AlarmReceiver.this","receiver is running");
        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        intent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification=new Notification.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(MainActivity.notifyTitle(0)).setContentText(MainActivity.notifyNotes(0)).setContentIntent(pendingIntent).build();
        notification.defaults=Notification.DEFAULT_ALL;
        manager.notify(1,notification);
        MainActivity.removeListItem(0);
        Intent intent_service=new Intent(context,NotifyService.class);
        context.startService(intent_service);
        Log.d("AlarmReceiver.this","server is run again");
    }
}
