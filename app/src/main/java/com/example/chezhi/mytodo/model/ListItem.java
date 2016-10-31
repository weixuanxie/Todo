package com.example.chezhi.mytodo.model;

/**
 * Created by chezhi on 16-10-29.
 */

public class ListItem {
    private int id;
    private boolean todo_done;
    private String title;
    public ListItem(int id,String title){
        this.id=id;
        this.title=title;
    }
    public ListItem(int id,String title,boolean todo_done){
        this.id=id;
        this.title=title;
        this.todo_done=todo_done;
    }
    boolean getTodo_done(){
        return todo_done;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
