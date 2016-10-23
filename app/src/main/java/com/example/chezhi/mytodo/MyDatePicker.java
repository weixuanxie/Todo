package com.example.chezhi.mytodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.icu.util.Calendar.*;

/**
 * Created by chezhi on 16-10-6.
 */

public class MyDatePicker extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int intDate,intTime;
    private Calendar calendar;
    private TextView textView;
    private TextView timetext;
    private EditText item_title;
    private EditText item_details;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private TodoDatabaseHelper dbHelper;
    private  String date_text=null;
    private  String time_text=null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.datepicker);
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        datebutton = (Button) findViewById(R.id.date_button);
        dbHelper=new TodoDatabaseHelper(this,"TodoList.db",null,1);
        textView = (TextView) findViewById(R.id.date_textview);
        item_title=(EditText)findViewById(R.id.item_title);
        item_details=(EditText)findViewById(R.id.item_details);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_swipe);
        toolbar.inflateMenu(R.menu.item_details);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("MyDatePicker", "datepicker is in use ...");
                showDialog(0);
                datePickerDialog.updateDate(mYear, mMonth, mDay);
            }
        });
        timetext = (TextView) findViewById(R.id.time_textview);
        Intent intent=getIntent();
        final String   todo_id=intent.getStringExtra("todoId");
        Log.d("MyDatePicker.this","the todo id is "+todo_id);
        String todo_title=intent.getStringExtra("todoTitle");
        String todo_details=intent.getStringExtra("todoNotes");
        date_text=intent.getStringExtra("todoDate");
        time_text=intent.getStringExtra("todoTime");
        Log.d("MyDatePicker.this","the todo date_text is "+date_text+"the todo time_text is "+time_text);
        if(!TextUtils.isEmpty(todo_title)){
            item_title.setText(todo_title);
        }
        if(!TextUtils.isEmpty(todo_details)){
            item_details.setText(todo_details);
        }
        if(!TextUtils.isEmpty(date_text)){
            textView.setText(date_text);
        }
        if(!TextUtils.isEmpty(time_text)){
            timetext.setText(time_text);
        }
        timetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("MyDatePicker", "timepicker is in use ...");
                showDialog(1);
                timePickerDialog.updateTime(mHour,mMinute);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_done:
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        String title=item_title.getText().toString().trim();
                        String notes=item_details.getText().toString().trim();
                        long time=intTime+intDate;
                        String time2=String.valueOf(time);
                        if(!TextUtils.isEmpty(todo_id)){
                            db.execSQL("update todoList set todo_title=?,todo_notes=?,time_todo=? where id=?",new String[]{title,notes,String.valueOf(getTime()),todo_id});
                        }
                        else {
                            db.execSQL("insert into todoList(todo_title,todo_notes,time_todo)values(?,?,?)",new String[]{title,notes,time2});
                        }
                        MainActivity.changeTodoItemList();
                        MainActivity.changeDoneList();
                        onBackPressed();
                }
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    intDate=((year-1970)*365+mDay-1+ArrayMonth(year,month)+leapYear(year,month))*3600*24;
                    Log.d("MyDatePicker.this","intDate is "+intDate);
                    textView.setText("你设定的日期为:" + setDateFormat(year, month, dayOfMonth));
                }
            }, mYear, mMonth, mDay);
            return datePickerDialog;

        } else if (id == 1) {
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    intTime=mHour*3600+mMinute*60;
                    Log.d("MyDatePicker.this","intTime is "+intTime);
                    timetext.setText("你设定的时间为:" + setTimeFormat(hourOfDay, minute));
                }
            }, mHour, mMinute, true);
            return timePickerDialog;
        }
        return null;
//        return timePickerDialog;
    }
    private int leapYear(int year,int month){
        int counts=0;
        for (int i=1970;i<year;i++){
            if(i%4==0&&i%100!=0||i%400==0&&month>2){
                counts++;
            }
        }
        Log.d("MyDatePicker.this","the counts year is "+counts);
        return counts;
    }
    private int ArrayMonth(int year,int month){
        int arrayMonth[]={31,28,31,30,31,30,31,31,30,31,30,31};
        int i,counts=0;
        for(i=0;i<month;i++){
            counts=counts+arrayMonth[i];
        }
        if(year%4==0&&year%100!=0||year%400==0){
            counts=counts+1;
        }
        Log.d("MyDatePicker.this","the counts month is "+counts);
        return counts;
    }
    protected long getTime(){
        int year=Integer.parseInt(date_text.substring(0,4));
        Log.d("MyDatePicker.this","the year is "+year);
        int month=Integer.parseInt(date_text.substring(5,7));
        Log.d("MyDatePicker.this"," the month is"+month);
        int day=Integer.parseInt(date_text.substring(8));
        Log.d("MyDatePicker.this"," the day is "+day);
        int hour=Integer.parseInt(time_text.substring(0,2));
        Log.d("MyDatePicker.this"," the hour is"+hour);
        int minute=Integer.parseInt(time_text.substring(3,5));
        Log.d("MyDatePicker.this"," the minute is "+minute);
        if (textView.getText().equals(date_text)&&timetext.getText().equals(time_text)){
           return initDate(year,month,day)+initTime(hour,minute);
        }
        if(!textView.getText().equals(date_text)&&timetext.getText().equals(time_text)){
            return intDate+initTime(hour,minute);
        }
        if(textView.getText().equals(date_text)&&!timetext.getText().equals(time_text)){
            return initDate(year,month,day)+intTime;
        }
        if (!textView.getText().equals(date_text)&&!timetext.getText().equals(time_text)){
            return intTime+initDate(year,month-1,day);
        }
        return 1;
    }
    protected long initDate(int year,int month,int day){
        return ((year-1970)*365+day-1+ArrayMonth(year,month)+leapYear(year,month))*3600*24;
    }
    protected long initTime(int hour,int minute){
        return hour*3600+minute*60;
    }

    private String setDateFormat(int year, int month, int date) {
        return String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(date);
    }

    private String setTimeFormat(int hour, int minute) {
        return String.valueOf(hour) + ":" + String.valueOf(minute);
    }

}
