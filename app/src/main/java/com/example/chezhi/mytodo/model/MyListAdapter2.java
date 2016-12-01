package com.example.chezhi.mytodo.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chezhi.mytodo.R;

import java.util.List;

/**
 * Created by chezhi on 16-10-29.
 */

public class MyListAdapter2 extends ArrayAdapter<ListItem> {
    private int resourceId;
    private Context mContext;
    public MyListAdapter2(Context context, int textViewResourceId, List<ListItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        mContext=context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view;
        final ListItem listItem=getItem(position);
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new MyListAdapter2.ViewHolder();
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder.title=(TextView)view.findViewById(R.id.list_title);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.title.setText(listItem.getTitle());
        return  view;
    }
    private class ViewHolder{
        TextView title;
    }
}
