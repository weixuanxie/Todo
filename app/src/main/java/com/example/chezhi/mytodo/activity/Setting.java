package com.example.chezhi.mytodo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.example.chezhi.mytodo.R;

/**
 * Created by chezhi on 16-10-28.
 */

public class Setting extends AppCompatActivity {
    CheckBox checkBox;
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

        editor=pref.edit();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    editor.putBoolean("time_desc",true);
                }
                else {
                    editor.clear();
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

    }
}
