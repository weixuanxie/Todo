package com.example.chezhi.mytodo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chezhi.mytodo.R;
import com.example.chezhi.mytodo.db.TodoDatabaseHelper;
import com.example.chezhi.mytodo.model.ListItem;
import com.example.chezhi.mytodo.model.MyListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chezhi on 16-10-29.
 */

public class ListDetail extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{
    private static TodoDatabaseHelper dbHelper;
    private static List<ListItem> itemList = new ArrayList<>();
    private static List<ListItem> doneList = new ArrayList<>();
    private static MyListAdapter adapter,adapter1;
    static String id;
    static String de_title;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        dbHelper=new TodoDatabaseHelper(this,"TodoList.db",null,1);
        db=dbHelper.getWritableDatabase();
        Intent intent=getIntent();
        final String parent_id=intent.getStringExtra("id");
        id=parent_id;
        String title=intent.getStringExtra("title");
        de_title=title;

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_list);
        toolbar.inflateMenu(R.menu.list_item_menu);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=LayoutInflater.from(ListDetail.this);
                View view=inflater.inflate(R.layout.dialog_layout,null);
                final EditText text=(EditText)view.findViewById(R.id.dialog_edit);
                AlertDialog.Builder builder=new AlertDialog.Builder(ListDetail.this);
                builder.setView(view);
                builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(text.getText().toString().trim())){
                            SQLiteDatabase db=dbHelper.getWritableDatabase();
                            db.execSQL("insert into list(title,parent_id) values(?,?)",new String[]{text.getText().toString().trim(),parent_id});
                            changeTodoItemList();
                        }
                        else{
                            Toast.makeText(ListDetail.this,"the text is empty",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });

        adapter = new MyListAdapter(ListDetail.this, R.layout.list_detail_item, itemList);
        initTodoItem();
        Log.d("ListDetail.this","the todoItemList is "+itemList);
        ListView listView = (ListView) findViewById(R.id.item_list);
//        listView.setDivider(null);
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单");
                menu.add(0,1,0,"删除");
            }
        });

        adapter1=new MyListAdapter(ListDetail.this, R.layout.list_detail_item,doneList);
        initDoneList();
        Log.d("ListDetail.this","the todoDoneList is "+doneList);
        ListView done_listView=(ListView)findViewById(R.id.done_list);
//        done_listView.setDivider(null);
        done_listView.setAdapter(adapter1);
        done_listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单");
                menu.add(0,2,0,"删除");
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.list_delete:
                AlertDialog.Builder builder=new AlertDialog.Builder(ListDetail.this);
                builder.setTitle(de_title);
                builder.setMessage(R.string.delete_message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.execSQL("update list set delete_flag=? where id=?",new String[]{"1",id});
                        MainActivity.changeDrawerList();
                        onBackPressed();
                    }
                });
                builder.setNegativeButton("CANCEL",null);
                AlertDialog dialog=builder.create();
                dialog.show();

        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
//        ContextMenu.ContextMenuInfo info=item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position=menuInfo.position;
        int id=itemList.get(position).getId();
        Log.d("ListDetail.this","the id is "+id);
        String stringId=String.valueOf(id);
        switch (item.getItemId()){
            case 1:
                db.execSQL("update list set delete_flag=? where id=?",new String[]{"1",stringId});
                changeDoneList();
                changeTodoItemList();
                break;
            case 2:
                id=doneList.get(position).getId();
                Log.d("ListDetail.this","the id is "+id);
                stringId=String.valueOf(id);
                db.execSQL("update list set delete_flag=? where id=?",new String[]{"1",stringId});
                changeDoneList();
                changeTodoItemList();
                break;

        }
        return super.onContextItemSelected(item);
    }

    protected static void initTodoItem() {
        itemList.clear();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id,title from list where delete_flag=? and parent_id=? and done=?",new String[]{"0",id,"0"});
        if (cursor.moveToFirst()) {
            do {
                int Id=cursor.getInt(cursor.getColumnIndex("id"));
                String Title = cursor.getString(cursor.getColumnIndex("title"));
                    ListItem listItem= new ListItem(Id,Title);
                    itemList.add(listItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    protected static void initDoneList(){
        doneList.clear();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from list where done=? AND delete_flag=? and parent_id=?", new String[]{"1","0",id});
        if (cursor.moveToFirst()) {
            do {
                int Id=cursor.getInt(cursor.getColumnIndex("id"));
                String Title = cursor.getString(cursor.getColumnIndex("title"));
                boolean todo_done=true;
                ListItem listItem= new ListItem(Id,Title,todo_done);
                doneList.add(listItem);
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
}
