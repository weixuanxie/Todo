package com.example.chezhi.mytodo.model;


/**
 * Created by chezhi on 16-10-23.
 */

public class MyFunction {
    private String todo_title;
    private String todo_notes;
    private long   todo_time;
    public MyFunction(String todo_title,String todo_notes,long todo_time){
        this.todo_title=todo_title;
        this.todo_notes=todo_notes;
        this.todo_time=todo_time;
    }
    public long getTodo_time(){
        return todo_time;
    }
    public String getTodo_title(){
        return todo_title;
    }
    public String getTodo_notes(){
        return todo_notes;
    }
}





