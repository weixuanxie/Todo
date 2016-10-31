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
import com.example.chezhi.mytodo.activity.ListDetail;
import com.example.chezhi.mytodo.db.TodoDatabaseHelper;

import java.util.List;

/**
 * Created by chezhi on 16-10-29.
 */

public class MyListAdapter extends ArrayAdapter<ListItem> {
    private int resourceId;
    private TodoDatabaseHelper dbHelper;
    private Context mContext;
    public MyListAdapter(Context context, int textViewResourceId, List<ListItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        mContext=context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view;
        final ListItem listItem=getItem(position);
        /*TextView todoTitle=(TextView)view.findViewById(R.id.todo_title);
        TextView todoNotes=(TextView)view.findViewById(R.id.todo_notes);
        TextView todoTime=(TextView)view.findViewById(R.id.todo_time);
        final CheckBox checkBox=(CheckBox)view.findViewById(R.id.done_check);*/
        dbHelper=new TodoDatabaseHelper(mContext,"TodoList.db",null,1);
        final MyListAdapter.ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new MyListAdapter.ViewHolder();
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder.v_title=(TextView)view.findViewById(R.id.text_listItem);
            viewHolder.v_checkBox=(CheckBox)view.findViewById(R.id.checkbox_listItem);
            viewHolder.v_checkBox.setChecked(listItem.getTodo_done());
            viewHolder.v_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.v_checkBox.isChecked()){
                        Log.d("MyListAdapter.this","i am checked "+listItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update list set done=? where id=?", new String[]{"1",String.valueOf(listItem.getId())});
//                        viewHolder.v_checkBox.setChecked(true);
                        ListDetail.changeTodoItemList();
                        ListDetail.changeDoneList();
                    }
                    else{
                        Log.d("MyListAdapter.this","i am not checked "+listItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update list set done=? where id=?", new String[]{"0",String.valueOf(listItem.getId())});
//                        viewHolder.v_checkBox.setChecked(false);
                        ListDetail.changeDoneList();
                        ListDetail.changeTodoItemList();
                    }
                }
            });
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(MyListAdapter.ViewHolder)view.getTag();
            viewHolder.v_checkBox.setChecked(listItem.getTodo_done());
            viewHolder.v_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.v_checkBox.isChecked()){
                        Log.d("MyListAdapter.this","i am checked "+listItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update list set done=? where id=?", new String[]{"1",String.valueOf(listItem.getId())});
//                        viewHolder.v_checkBox.setChecked(true);
                        ListDetail.changeTodoItemList();
                        ListDetail.changeDoneList();
                    }
                    else{
                        Log.d("MyListAdapter.this","i am not checked "+listItem.getId());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("update list set done=? where id=?", new String[]{"0",String.valueOf(listItem.getId())});
//                        viewHolder.v_checkBox.setChecked(false);
                        ListDetail.changeDoneList();
                        ListDetail.changeTodoItemList();
                    }
                }
            });

        }
        viewHolder.v_title.setText(listItem.getTitle());
        return  view;
    }
    private  class ViewHolder{
        CheckBox v_checkBox;
        TextView v_title;
    }

}
