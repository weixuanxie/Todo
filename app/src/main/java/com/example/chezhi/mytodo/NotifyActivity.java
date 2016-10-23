package com.example.chezhi.mytodo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chezhi on 16-10-22.
 */

public class NotifyActivity extends AppCompatActivity {
    private static TodoDatabaseHelper dbhelper;
    private static List<MyFunction> notifyList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        dbhelper=new TodoDatabaseHelper(this,"TodoList.db",null,1);
        sqlFunction();
        Button button=(Button) findViewById(R.id.test_11111);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NotifyActivity.this,NotifyService.class);
                startService(intent);
            }
        });


    }
    protected static void sqlFunction(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long time=System.currentTimeMillis();
        Cursor cursor = db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch'),time_todo  from todoList where todo_done=? AND delete_flag=? order by datetime(time_todo) asc",new String[]{"0","0"});
        if (cursor.moveToFirst()) {
            do {
                int todoId=cursor.getInt(cursor.getColumnIndex("id"));
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                long    triggerTime = cursor.getInt(4);
                triggerTime=triggerTime*1000;
                if (triggerTime>=time){
                    Log.d("MyFunction.this","i find it");
                    MyFunction myFunction= new MyFunction(todoTitle, todoNotes,triggerTime);
                    notifyList.add(myFunction);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    protected static int notifySize(){
        sqlFunction();
        return notifyList.size();
    }
    protected static long notifyTime(int posiion){
        sqlFunction();
        return notifyList.get(posiion).getTodo_time()+891;
    }
    protected static String notifyTitle(int posiion){
        sqlFunction();
        return notifyList.get(posiion).getTodo_title();
    }
    protected static String notifyNotes(int posiion) {
        sqlFunction();
        return notifyList.get(posiion).getTodo_notes();
    }
    protected static void removeListitem(int position){
        notifyList.remove(position);
    }
}
