package com.example.chezhi.mytodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;


/**
 * Created by chezhi on 16-10-16.
 */

class TodoAdapter extends ArrayAdapter<TodoItem> {
    private int resourceId;
    private TodoDatabaseHelper dbhelper;
    private Context mContext;
    TodoAdapter(Context context, int textViewResourceId, List<TodoItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        mContext=context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        final TodoItem todoItem=getItem(position);
        dbhelper=new TodoDatabaseHelper(mContext,"TodoList.db",null,1);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView todoTitle=(TextView)view.findViewById(R.id.todo_title);
        TextView todoNotes=(TextView)view.findViewById(R.id.todo_notes);
        TextView todoTime=(TextView)view.findViewById(R.id.todo_time);
        final CheckBox checkBox=(CheckBox)view.findViewById(R.id.done_check);
        checkBox.setChecked(todoItem.getTodo_done());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Log.d("TodoAdapter.this","i am here ");
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.execSQL("update todoList set todo_done=? where todo_title=?", new String[]{"1",todoItem.getTodo_title()});
                    MainActivity.changeTodoItemList();
                    MainActivity.changeDoneList();
                }
                else{
                    Log.d("TodoAdapter.this","i am here again ");
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.execSQL("update todoList set todo_done=? where todo_title=?", new String[]{"0",todoItem.getTodo_title()});
                    MainActivity.changeDoneList();
                    MainActivity.changeTodoItemList();
                }
            }
        });
        todoTitle.setText(todoItem.getTodo_title());
        todoTime.setText(todoItem.getTodo_time());
        todoNotes.setText(todoItem.getTodo_notes());
        return  view;
    }

}
