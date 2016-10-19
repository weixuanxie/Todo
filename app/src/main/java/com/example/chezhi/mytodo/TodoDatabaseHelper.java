package com.example.chezhi.mytodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by chezhi on 16-10-15.
 */

public class TodoDatabaseHelper extends SQLiteOpenHelper{
    public static final  String CREATE_TODOLIST="Create table todoList("+"id integer primary key autoincrement,"+"time_todo integer,"+"todo_title text,"+"todo_notes text,"+"todo_done integer DEFAULT '0',"+"tag text)";
    private Context mContext;
    public TodoDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory    factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public  void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODOLIST);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
