package com.example.chezhi.mytodo;

/**
 * Created by chezhi on 16-10-16.
 */

public class TodoItem {
    private String todo_title;
    private String todo_notes;
    private String todo_time;
    public TodoItem(String todo_title,String todo_notes){
        this.todo_title=todo_title;
        this.todo_notes=todo_notes;
    }
    public TodoItem(String todo_title,String todo_notes,String todo_time){
        this.todo_title=todo_title;
        this.todo_notes=todo_notes;
        this.todo_time=todo_time;
    }
    protected String getTodo_title(){
        return todo_title;
    }
    protected String getTodo_notes(){
        return todo_notes;
    }
    protected String getTodo_time(){
        return todo_time;
    }
}
