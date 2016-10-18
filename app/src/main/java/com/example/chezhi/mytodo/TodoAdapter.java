package com.example.chezhi.mytodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chezhi on 16-10-16.
 */

public class TodoAdapter extends ArrayAdapter<TodoItem> {
    private int resourceId;
    public TodoAdapter(Context context, int textViewResourceId, List<TodoItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TodoItem todoItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView todoTitle=(TextView)view.findViewById(R.id.todo_title);
        TextView todoNotes=(TextView)view.findViewById(R.id.todo_notes);
        TextView todoTime=(TextView)view.findViewById(R.id.todo_time);
        todoTitle.setText(todoItem.getTodo_title());
        todoTime.setText(todoItem.getTodo_time());
        todoNotes.setText(todoItem.getTodo_notes());
        return  view;
    }
}
