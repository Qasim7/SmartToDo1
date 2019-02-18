package com.example.qasim.smarttodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.qasim.smarttodo.adapter.TaskAdapter;
import com.example.qasim.smarttodo.database.AppDatabase;
import com.example.qasim.smarttodo.model.Task;
import com.example.qasim.smarttodo.util.SwipeToDeleteCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_FOR_TASK = 100;
    private static final String TAG = "mainactivity";

    private List<Task> tasklist;
    public TaskAdapter taskAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab_new_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NewTaskActivity.class), REQUEST_FOR_TASK);

            }
        });

        tasklist = AppDatabase.getDatabase(getApplicationContext()).taskDao().getAllTasks();
        taskAdapter = new TaskAdapter(tasklist, this);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else fab.show();
            }
        });

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new SwipeToDeleteCallback(taskAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity", "OnActivity result " + requestCode + " " + resultCode);
        switch (requestCode) {
            case REQUEST_FOR_TASK:
                if (resultCode == NewTaskActivity.RESULT_CODE_UPDATE) {
                    tasklist = AppDatabase.getDatabase(getApplicationContext()).taskDao().getAllTasks();
//                    Log.e("MainActivity",tasklist.toString());
                    taskAdapter.updateTaskListItems(tasklist);
                }
                break;
        }
    }

}
