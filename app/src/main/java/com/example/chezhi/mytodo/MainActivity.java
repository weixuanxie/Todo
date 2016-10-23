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
import android.view.ContextMenu;
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
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单");
                menu.add(0,1,0,"编辑");
                menu.add(0,2,0,"删除");
            }
        });
        listView.setAdapter(adapter);
        adapter1=new TodoAdapter(MainActivity.this,R.layout.todo_item,todoDoneList);
        initDoneList();
        Log.d("MainActivity.this","the todoDoneList is "+todoDoneList);
        ListView done_listView=(ListView)findViewById(R.id.done_list);
        done_listView.setDivider(null);
        done_listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单");
                menu.add(0,3,0,"编辑");
                menu.add(0,4,0,"删除");
            }
        });
        done_listView.setAdapter(adapter1);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        ContextMenu.ContextMenuInfo info=item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position=menuInfo.position;
        int id=todoItemList.get(position).getId();
        Log.d("MainActivity.this","the id is "+id);
        String stringId=String.valueOf(id);
        SQLiteDatabase dp=dbhelper.getWritableDatabase();
        switch (item.getItemId()){
            case 1:
                Cursor cursor=dp.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch') from todoList where id=?",new String[]{stringId});
                if(cursor.moveToFirst()){
                    do{
                        String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                        String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                        String todoTime = cursor.getString(3);
                        String todoTime1,todoTime2;
                        Intent intent=new Intent(MainActivity.this,MyDatePicker.class);
                        intent.putExtra("todoId",stringId);
                        intent.putExtra("todoTitle",todoTitle);
                        intent.putExtra("todoNotes",todoNotes);
                        if (!todoTime.equals("1970-01-01 00:00:00")){
                            todoTime1 = todoTime.substring(0,10);
                            todoTime2 = todoTime.substring(11);
                            intent.putExtra("todoDate",todoTime1);
                            intent.putExtra("todoTime",todoTime2);
                        }
                        startActivity(intent);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                /*Intent intent=new Intent(MainActivity.this,TodoAdapter.class);
                startActivity(intent);*/
                break;
            case 2:
                dp.execSQL("update todoList set delete_flag=? where id=?",new String[]{"1",stringId});
                changeDoneList();
                changeTodoItemList();
                break;
            case 3:
                id=todoDoneList.get(position).getId();
                Log.d("MainActivity.this","the id is "+id);
                stringId=String.valueOf(id);
                cursor=dp.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch') from todoList where id=?",new String[]{stringId});
                if(cursor.moveToFirst()){
                    do{
                        String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                        String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                        String todoTime = cursor.getString(3);
                        String todoTime1,todoTime2;
                        Intent intent=new Intent(MainActivity.this,MyDatePicker.class);
                        intent.putExtra("todoId",stringId);
                        intent.putExtra("todoTitle",todoTitle);
                        intent.putExtra("todoNotes",todoNotes);
                        if (!todoTime.equals("1970-01-01 00:00:00")){
                            todoTime1 = todoTime.substring(0,10);
                            todoTime2 = todoTime.substring(11);
                            intent.putExtra("todoDate",todoTime1);
                            intent.putExtra("todoTime",todoTime2);
                        }
                        startActivity(intent);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                break;
            case 4:
                id=todoDoneList.get(position).getId();
                Log.d("MainActivity.this","the id is "+id);
                stringId=String.valueOf(id);
                dp.execSQL("update todoList set delete_flag=? where id=?",new String[]{"1",stringId});
                changeDoneList();
                changeTodoItemList();
                break;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                return true;

            case R.id.item2:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,NotifyActivity.class);
                startActivity(intent);
                return true;
        }

        return true;
    }

    protected static void initTodoItem() {
        todoItemList.clear();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch'),datetime(time_todo)  from todoList where todo_done=? AND delete_flag=?",new String[]{"0","0"});
        if (cursor.moveToFirst()) {
            do {
                int todoId=cursor.getInt(cursor.getColumnIndex("id"));
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                String todoTime1 = cursor.getString(3);
                int    time = cursor.getInt(4);
                String todoTime;
                if (todoTime1.equals("1970-01-01 00:00:00")){
                    TodoItem todoItem = new TodoItem(todoId,todoTitle, todoNotes);
                    todoItemList.add(todoItem);
                }
                else
                {
                    todoTime = todoTime1.substring(5,10);
                    TodoItem todoItem = new TodoItem(todoId,todoTitle, todoNotes,todoTime);
                    todoItemList.add(todoItem);
                    adapter.notifyDataSetChanged();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    protected static void initDoneList(){
        todoDoneList.clear();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from todoList where todo_done=? AND delete_flag=?", new String[]{"1","0"});
        if (cursor.moveToFirst()) {
            do {
                int todoId=cursor.getInt(cursor.getColumnIndex("id"));
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                boolean todo_done=true;
                TodoItem todoItem = new TodoItem(todoId,todoTitle, todoNotes,todo_done);
                todoDoneList.add(todoItem);
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
