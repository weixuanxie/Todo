package com.example.chezhi.mytodo;


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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{
    private static TodoDatabaseHelper dbhelper;
    private static List<TodoItem> todoItemList = new ArrayList<>();
    private static List<TodoItem> todoDoneList = new ArrayList<>();
    private static TodoAdapter adapter1,adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setOnMenuItemClickListener(this);
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
        adapter = new TodoAdapter(MainActivity.this, R.layout.todo_item, todoItemList);
        initTodoItem();
        Log.d("MainActivity.this","the todoItemList is "+todoItemList);
        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todoItem=todoItemList.get(position);
                Toast.makeText(MainActivity.this,"you click the  "+todoItem.getTodo_title(),Toast.LENGTH_SHORT).show();
                Log.d("MainActivity.this","i am here ");
            }
        });
        listView.setAdapter(adapter);
        adapter1=new TodoAdapter(MainActivity.this,R.layout.todo_item,todoDoneList);
        initDoneList();
        Log.d("MainActivity.this","the todoDoneList is "+todoDoneList);
        ListView done_listview=(ListView)findViewById(R.id.done_list);
        done_listview.setDivider(null);
        done_listview.setAdapter(adapter1);
    }
    @Override
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

    protected static void initTodoItem() {
        todoItemList.clear();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select todo_title,todo_notes,datetime(time_todo,'unixepoch') from todoList where todo_done=?",new String[]{"0"});
        if (cursor.moveToFirst()) {
            do {
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                String todoTime1 = cursor.getString(2);
                Log.d("MainActivity.this","the todoTime1 is "+todoTime1);
                String todoTime;
                if (todoTime1.equals("1970-01-01 00:00:00")){
                    TodoItem todoItem = new TodoItem(todoTitle, todoNotes);
                    todoItemList.add(todoItem);
                }
                else
                {
                    todoTime = todoTime1.substring(5,10);
                Log.d("MainActivity.this","the todoTime is "+todoTime);
                Log.d("MainActivity.this","the todoTitle is "+todoTitle);
                Log.d("MainActivity.this","the todoNotes is "+todoNotes);
                TodoItem todoItem = new TodoItem(todoTitle, todoNotes,todoTime);
                todoItemList.add(todoItem);
                Log.d("MainActivity.this","the todoItemListInit is "+todoItemList);
                adapter.notifyDataSetChanged();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    protected static void initDoneList(){
        todoDoneList.clear();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from todoList where todo_done=?", new String[]{"1"});
        if (cursor.moveToFirst()) {
            do {
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                boolean todo_done=true;
                Log.d("MainActivity.this","the done_todoTitle is "+todoTitle);
                Log.d("MainActivity.this","the done_todootes is "+todoNotes);
                TodoItem todoItem = new TodoItem(todoTitle, todoNotes,todo_done);
                todoDoneList.add(todoItem);
                Log.d("MainActivity.this","the todoDoneListInit is "+todoDoneList);
                adapter1.notifyDataSetChanged();

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    protected static void changeTodoItemList(){
        initTodoItem();
        adapter.notifyDataSetChanged();
    }
    protected static void changeDoneList(){
        initDoneList();
        adapter1.notifyDataSetChanged();
    }
}
