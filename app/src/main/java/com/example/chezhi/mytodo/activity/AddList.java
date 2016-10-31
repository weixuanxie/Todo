package com.example.chezhi.mytodo.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.example.chezhi.mytodo.R;
import com.example.chezhi.mytodo.db.TodoDatabaseHelper;

/**
 * Created by chezhi on 16-10-28.
 */

public class AddList extends AppCompatActivity{
    TodoDatabaseHelper dbHelper;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_list);
        dbHelper=new TodoDatabaseHelper(this,"TodoList.db",null,1);
        editText = (EditText) findViewById(R.id.item_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_addList);
        toolbar.inflateMenu(R.menu.item_details);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_done:
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String title = editText.getText().toString().trim();
                        db.execSQL("insert into list(title)values(?)", new String[]{title});
                        db.close();
                        /*MainActivity.changeTodoItemList();
                        MainActivity.changeDoneList();*/
                        MainActivity.changeDrawerList();
                        onBackPressed();
                }
                return true;
            }

        });
    }
}
