package com.example.chezhi.mytodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chezhi on 16-10-15.
 *
 */

public class TodoDatabaseHelper extends SQLiteOpenHelper{
    private static final int Version=1;
    private static final  String CREATE_TODOLIST="Create table todoList(id integer primary key autoincrement,time_todo integer,todo_title text,todo_notes text,todo_done integer DEFAULT '0',delete_flag integer DEFAULT '0',tag text)";
    private static final  String CREATE_LIST="Create table list(id integer primary key autoincrement,title text,done integer DEFAULT '0',delete_flag integer DEFAULT '0',parent_id integer DEFAULT '0')";
    Context mContext;
    public TodoDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory    factory, int version){
        super(context,name,factory,Version);
        mContext=context;
    }
    @Override
    public  void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODOLIST);
        db.execSQL(CREATE_LIST);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        switch (oldVersion){
            case  1:

            case  2:
                db.execSQL("alter table todoList add column delete_flag integer DEFAULT '0'");
            case  3:
                db.execSQL(CREATE_LIST);
            case  4:
                db.execSQL("drop table list");
                db.execSQL(CREATE_LIST);
            default:

        }
    }
}
