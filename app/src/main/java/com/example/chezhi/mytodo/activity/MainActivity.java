package com.example.chezhi.mytodo.activity;



import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chezhi.mytodo.R;
import com.example.chezhi.mytodo.model.ListItem;
import com.example.chezhi.mytodo.model.MyFunction;
import com.example.chezhi.mytodo.model.MyListAdapter2;
import com.example.chezhi.mytodo.service.NotifyService;
import com.example.chezhi.mytodo.model.TodoAdapter;
import com.example.chezhi.mytodo.db.TodoDatabaseHelper;
import com.example.chezhi.mytodo.model.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{
    private static TodoDatabaseHelper dbhelper;
    private static List<TodoItem> todoItemList = new ArrayList<>();
    private static List<TodoItem> todoDoneList = new ArrayList<>();
    private static List<MyFunction> notifyList = new ArrayList<>();
    private static List<ListItem> lists = new ArrayList<>();
    private static TodoAdapter adapter,adapter1;
    private static MyListAdapter2 adapter2;
    DrawerLayout drawer;
    TextView addList;
    SharedPreferences pref;
    static boolean ck_state=false;

    @Override
    protected void onStart(){
        super.onStart();
        Intent sIntent=new Intent(MainActivity.this,NotifyService.class);
        startService(sIntent);
        Log.d("MainActivity.this","the MainActivity is start...");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(this);

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setupDrawerContent(navigationView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v,"hi i am snackbar",Snackbar.LENGTH_SHORT).setAction(getString(R.string.text_undo),this).show();
                Intent intent = new Intent(MainActivity.this, MyDatePicker.class);
                startActivity(intent);
            }
        });
        addList=(TextView)findViewById(R.id.todoList_add);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity.this","your click the todoList_add");
                Intent intent=new Intent(MainActivity.this,AddList.class);
                startActivity(intent);
            }
        });

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean time_desc=pref.getBoolean("time_desc",false);
        if (time_desc){
            ck_state=true;
        }
        Log.d("MainActivity.this","the ck_state is "+ck_state);
        Log.d("MainActivity.this","the time desc is "+time_desc);
        dbhelper=new TodoDatabaseHelper(this,"TodoList.db",null,1);
        sqlFunction();
        adapter = new TodoAdapter(MainActivity.this, R.layout.todo_item, todoItemList);
        initTodoItem();

        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单");
                menu.add(0,1,0,"编辑");
                menu.add(0,2,0,"删除");
            }
        });

        adapter1=new TodoAdapter(MainActivity.this,R.layout.todo_item,todoDoneList);
        initDoneList();

        ListView done_listView=(ListView)findViewById(R.id.done_list);
        done_listView.setDivider(null);
        done_listView.setAdapter(adapter1);
        done_listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单");
                menu.add(0,3,0,"编辑");
                menu.add(0,4,0,"删除");
            }
        });

        adapter2=new MyListAdapter2(MainActivity.this,R.layout.list_item,lists);
        initDrawerList();
        final ListView drawer_list=(ListView)findViewById(R.id.drawer_list);
        drawer_list.setDivider(null);
        drawer_list.setAdapter(adapter2);
        drawer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long listId=lists.get(position).getId();
                String stringId=String.valueOf(listId);
                String title=lists.get(position).getTitle();
                Intent intent=new Intent(MainActivity.this,ListDetail.class);
                Log.d("MainActivity.this","the listId is "+stringId);
                Log.d("MainActivity.this","the title is "+title);
                intent.putExtra("id",stringId);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

    }

    public void setupDrawerContent(NavigationView navigationView){
        Log.d("MainActivity.this","i am here");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.todoList_add:
                       /* Intent intent=new Intent(MainActivity.this,AboutThis.class);
                        startActivity(intent);*/
                        Log.d("MainActivity.this","your click the todoList_add");
                        Intent intent=new Intent(MainActivity.this,AddList.class);
                        startActivity(intent);
                        break;
                }
                item.setChecked(true);
                drawer.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
//        ContextMenu.ContextMenuInfo info=item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position=menuInfo.position;
        int id=todoItemList.get(position).getId();
        Log.d("MainActivity.this","the id is "+id);
        String stringId=String.valueOf(id);
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        switch (item.getItemId()){
            case 1:
                Cursor cursor=db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch') from todoList where id=?",new String[]{stringId});
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
                db.execSQL("update todoList set delete_flag=? where id=?",new String[]{"1",stringId});
                changeDoneList();
                changeTodoItemList();
                break;
            case 3:
                id=todoDoneList.get(position).getId();
                Log.d("MainActivity.this","the id is "+id);
                stringId=String.valueOf(id);
                cursor=db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch') from todoList where id=?",new String[]{stringId});
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
                db.execSQL("update todoList set delete_flag=? where id=?",new String[]{"1",stringId});
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
                Intent intent_se=new Intent(MainActivity.this,Setting.class);
                startActivity(intent_se);
                break;

            case R.id.item2:
                Intent intent=new Intent(MainActivity.this,AboutThis.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    protected static void initDrawerList() {
        lists.clear();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,title from list where  delete_flag=? and parent_id=?",new String[]{"0","0"});
        if (cursor.moveToFirst()) {
            do {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                ListItem listItem=new ListItem(id,title);
                lists.add(listItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    protected static void initTodoItem() {
        todoItemList.clear();
        Cursor cursor;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        if(ck_state){
            cursor = db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch')  from todoList where todo_done=? AND delete_flag=? order by datetime(time_todo) desc",new String[]{"0","0"});
        }else {
        cursor = db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch')  from todoList where todo_done=? AND delete_flag=?",new String[]{"0","0"});
        }
        if (cursor.moveToFirst()) {
            do {
                int todoId=cursor.getInt(cursor.getColumnIndex("id"));
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                String todoTime1 = cursor.getString(3);
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
        Cursor cursor;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        cursor = db.rawQuery("select * from todoList where todo_done=? AND delete_flag=?", new String[]{"1","0"});
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

    public static void changeTodoItemList(){
        initTodoItem();
        adapter.notifyDataSetChanged();
    }

    public static void changeDoneList(){
        initDoneList();
        adapter1.notifyDataSetChanged();
    }
    public static void changeDrawerList(){
        initDrawerList();
        adapter2.notifyDataSetChanged();
    }
    protected static void sqlFunction(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long time=System.currentTimeMillis()+8*3600*1000;
        Cursor cursor = db.rawQuery("select id,todo_title,todo_notes,datetime(time_todo,'unixepoch'),time_todo  from todoList where todo_done=? AND delete_flag=? order by datetime(time_todo) asc",new String[]{"0","0"});
        if (cursor.moveToFirst()) {
            do {
//                int todoId=cursor.getInt(cursor.getColumnIndex("id"));
                String todoTitle = cursor.getString(cursor.getColumnIndex("todo_title"));
                String todoNotes = cursor.getString(cursor.getColumnIndex("todo_notes"));
                long    triggerTime = cursor.getInt(4);
                triggerTime=triggerTime*1000;
                if (triggerTime>=time){
                    MyFunction myFunction= new MyFunction(todoTitle, todoNotes,triggerTime);
                    notifyList.add(myFunction);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public static int notifySize(){
        sqlFunction();
        return notifyList.size();
    }

    public static long notifyTime(int position){
        sqlFunction();
        return notifyList.get(position).getTodo_time();
    }

    public static String notifyTitle(int position){
        sqlFunction();
        return notifyList.get(position).getTodo_title();
    }

    public static String notifyNotes(int position) {
        sqlFunction();
        return notifyList.get(position).getTodo_notes();
    }

    public static void removeListItem(int position){
        notifyList.remove(position);
    }

}
