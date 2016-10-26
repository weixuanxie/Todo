package com.example.chezhi.mytodo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.chezhi.mytodo.R;

/**
 * Created by chezhi on 16-10-21.
 */

public class AboutThis extends AppCompatActivity {
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_about);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
