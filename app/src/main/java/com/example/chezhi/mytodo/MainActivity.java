package com.example.chezhi.mytodo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private TodoDatabaseHelper dbhelper;
    private List<TodoItem> todoItemList = new ArrayList<TodoItem>();
    private List<TodoItem> todoDoneList = new ArrayList<TodoItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setOnMenuItemClickListener(this);
       /* toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v,"hi i am snackbar",Snackbar.LENGTH_SHORT).setAction(getString(R.string.text_undo),this).show();
                Intent intent = new Intent(MainActivity.this, MyDatePicker.class);
                startActivity(intent);
            }
        });
        dbhelper=new TodoDatabaseHelper(this,"TodoList.db",null,1);
        initTodoItem();
        TodoAdapter adapter = new TodoAdapter(MainActivity.this, R.layout.todo_item, todoItemList);
        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        initDoneList();
        TodoAdapter adapter1=new TodoAdapter(MainActivity.this,R.layout.todo_item,todoDoneList);
        ListView done_listview=(ListView)findViewById(R.id.done_list);
        done_listview.setDivider(null);
        done_listview.setAdapter(adapter1);
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                return true;
        }

        return true;
    }

    private void initTodoItem() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select todo_title,todo_notes,datetime(time_todo,'unixepoch') from todoList ", null);
        if (cursor.moveToFirst()) {
            do {
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
//                int  todoTime1 = cursor.getInt(cursor.getColumnIndex("time_todo"));
                String todoTime1 = cursor.getString(2);
                String todoTime = todoTime1.substring(5,10);
                Log.d("MainActivity.this","the todoTime is "+todoTime);
                Log.d("MainActivity.this","the todotitle is "+todoTitle);
                Log.d("MainActivity.this","the todootes is "+todoNotes);
                TodoItem todoItem = new TodoItem(todoTitle, todoNotes,todoTime);
                todoItemList.add(todoItem);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void initDoneList(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from todoList ", null);
        if (cursor.moveToFirst()) {
            do {
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                Log.d("MainActivity.this","the todotitle is "+todoTitle);
                Log.d("MainActivity.this","the todootes is "+todoNotes);
                TodoItem todoItem = new TodoItem(todoTitle, todoNotes);
                todoDoneList.add(todoItem);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
