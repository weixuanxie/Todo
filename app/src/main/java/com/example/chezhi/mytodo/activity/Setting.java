package com.example.chezhi.mytodo.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.example.chezhi.mytodo.R;
import com.example.chezhi.mytodo.service.StatusNotifyService;

/**
 * Created by chezhi on 16-10-28.
 */

public class Setting extends AppCompatActivity {
    CheckBox checkBox,ck_notify;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.setting);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_about);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        checkBox=(CheckBox)findViewById(R.id.cb_time_desc);
        ck_notify=(CheckBox)findViewById(R.id.cb_status_notify);

        editor=pref.edit();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    editor.putBoolean("time_desc",true);
                }
                else {
                    editor.putBoolean("time_desc",false);
                }
                editor.commit();
            }
        });

        ck_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ck_notify.isChecked()){
                    editor.putBoolean("status_notify",true);
                }
                else{
                    editor.putBoolean("status_notify",false);
                }
                editor.commit();
            }
        });

        boolean time_desc=pref.getBoolean("time_desc",false);
        if (time_desc){
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }

        boolean status_notify=pref.getBoolean("status_notify",false);
        if (status_notify){
            ck_notify.setChecked(true);
        }
        else {
            ck_notify.setChecked(false);
        }

    }
    @Override
    public void onBackPressed(){
        boolean status_notify=pref.getBoolean("status_notify",false);
        if (status_notify){
            Intent intentService=new Intent(Setting.this, StatusNotifyService.class);
            startService(intentService);
        }
        else {
            Intent stopIntent=new Intent(Setting.this,StatusNotifyService.class);
            stopService(stopIntent);
        }
        super.onBackPressed();
    }
}
