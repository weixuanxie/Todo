package com.example.chezhi.mytodo.model;

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

import com.example.chezhi.mytodo.R;
import com.example.chezhi.mytodo.activity.MainActivity;
import com.example.chezhi.mytodo.db.TodoDatabaseHelper;

import java.util.List;


/**
 * Created by chezhi on 16-10-16.
 */

public class TodoAdapter extends ArrayAdapter<TodoItem> {
    private int resourceId;
    private TodoDatabaseHelper dbHelper;
    private Context mContext;
    public TodoAdapter(Context context, int textViewResourceId, List<TodoItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        mContext=context;
    }
    @NonNull
    @Override
    /**
     * if use convertview the donelist and todolist will confuse,so once i know how to fix it,I will change it .
     * and  i use viewholder because i want to less warning.
     */
    public View getView(int position,View convertView, @NonNull ViewGroup parent){
        View view;
        final TodoItem todoItem=getItem(position);
        /*TextView todoTitle=(TextView)view.findViewById(R.id.todo_title);
        TextView todoNotes=(TextView)view.findViewById(R.id.todo_notes);
        TextView todoTime=(TextView)view.findViewById(R.id.todo_time);
        final CheckBox checkBox=(CheckBox)view.findViewById(R.id.done_check);*/
        dbHelper=new TodoDatabaseHelper(mContext,"TodoList.db",null,1);
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder.v_todoTitle=(TextView)view.findViewById(R.id.todo_title);
            viewHolder.v_todoNotes=(TextView)view.findViewById(R.id.todo_notes);
            viewHolder.v_todoTime=(TextView)view.findViewById(R.id.todo_time);
            viewHolder.v_checkBox=(CheckBox)view.findViewById(R.id.done_check);
            viewHolder.v_checkBox.setChecked(todoItem.getTodo_done());
            viewHolder.v_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.v_checkBox.isChecked()){
                        Log.d("TodoAdapter.this","i am checked "+todoItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update todoList set todo_done=? where id=?", new String[]{"1",String.valueOf(todoItem.getId())});
//                        viewHolder.v_checkBox.setChecked(true);
                        MainActivity.changeTodoItemList();
                        MainActivity.changeDoneList();
                    }
                    else{
                        Log.d("TodoAdapter.this","i am not checked "+todoItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update todoList set todo_done=? where id=?", new String[]{"0",String.valueOf(todoItem.getId())});
//                        viewHolder.v_checkBox.setChecked(false);
                        MainActivity.changeDoneList();
                        MainActivity.changeTodoItemList();
                    }
                }
            });
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
            viewHolder.v_checkBox.setChecked(todoItem.getTodo_done());
            viewHolder.v_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.v_checkBox.isChecked()){
                        Log.d("TodoAdapter.this","i am checked "+todoItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update todoList set todo_done=? where id=?", new String[]{"1",String.valueOf(todoItem.getId())});
//                        viewHolder.v_checkBox.setChecked(true);
                        MainActivity.changeTodoItemList();
                        MainActivity.changeDoneList();
                    }
                    else{
                        Log.d("TodoAdapter.this","i am not checked "+todoItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update todoList set todo_done=? where id=?", new String[]{"0",String.valueOf(todoItem.getId())});
//                        viewHolder.v_checkBox.setChecked(false);
                        MainActivity.changeDoneList();
                        MainActivity.changeTodoItemList();
                    }
                }
            });

        }
        viewHolder.v_todoTitle.setText(todoItem.getTodo_title());
        viewHolder.v_todoTime.setText(todoItem.getTodo_time());
        viewHolder.v_todoNotes.setText(todoItem.getTodo_notes());
        return  view;
    }
    private  class ViewHolder{
        CheckBox v_checkBox;
        TextView v_todoTitle;
        TextView v_todoTime;
        TextView v_todoNotes;
    }

}
