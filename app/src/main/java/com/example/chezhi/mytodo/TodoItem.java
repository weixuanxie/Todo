package com.example.chezhi.mytodo;

/**
 * Created by chezhi on 16-10-16.
 */

public class TodoItem {
    private int id;
    private String todo_title;
    private String todo_notes;
    private String todo_time;
    private boolean todo_done;
    public TodoItem(int id,String todo_title,String todo_notes){
        this.id=id;
        this.todo_title=todo_title;
        this.todo_notes=todo_notes;
    }
    public TodoItem(int id,String todo_title,String todo_notes,String todo_time){
        this.id=id;
        this.todo_title=todo_title;
        this.todo_notes=todo_notes;
        this.todo_time=todo_time;
    }
    public TodoItem(int id,String todo_title,String todo_notes,boolean todo_done){
        this.id=id;
        this.todo_title=todo_title;
        this.todo_notes=todo_notes;
        this.todo_done=todo_done;
    }
    int getId(){
        return id;
    }
    String getTodo_title(){
        return todo_title;
    }
    String getTodo_notes(){
        return todo_notes;
    }
    String getTodo_time(){
        return todo_time;
    }
    boolean getTodo_done(){
        return todo_done;
    }

}
