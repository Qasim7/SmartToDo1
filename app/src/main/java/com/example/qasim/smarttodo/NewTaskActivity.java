package com.example.qasim.smarttodo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qasim.smarttodo.database.AppDatabase;
import com.example.qasim.smarttodo.model.Task;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity implements WeekdaysDataSource.Callback {

    public static final int RESULT_CODE_UPDATE = 100;
    private static final int NOT_UPDATE = 101;
    private static final String TAG = "NewTaskActivity";
    private TextView txtStartTime;
    private TextView txtFinishTime;
    //    private View viewColor;
    private Calendar date;
    private EditText ed_title;
    private EditText ed_description;
    private ColorSeekBar colorSeekBar;
    private Task task;
    private FloatingActionButton fab_task;
    private PendingIntent pendingIntent;
    private Switch aSwitch;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed_title = findViewById(R.id.edt_title);
        ed_description = findViewById(R.id.edt_desc);
        txtStartTime = findViewById(R.id.txt_start_time);
        txtFinishTime = findViewById(R.id.txt_finish_time);
//        viewColor = findViewById(R.id.view_color);
        fab_task = findViewById(R.id.fab_new_task);
        aSwitch = findViewById(R.id.switchReminder);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(NewTaskActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(NewTaskActivity.this, 0, alarmIntent, 0);

        setupColorSeekBar();
        fabClicked();

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartTimeClick();

            }
        });

        txtFinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishTimeClick();
            }
        });


    }

    private void fabClicked() {
        final int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {

            fab_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertNewTask();
                }
            });
        } else {

            ed_title.setText(getIntent().getStringExtra("title"));
            ed_description.setText(getIntent().getStringExtra("description"));
            txtStartTime.setText(getIntent().getStringExtra("startTime"));
            txtFinishTime.setText(getIntent().getStringExtra("finishTime"));
            colorSeekBar.setColor(getIntent().getIntExtra("color", 0));
//            final int position = getIntent().getIntExtra("position", -1);
//            Log.e(TAG, "onCreate: "+position);   // bu positionda olan update olunmush taski gostermek ucun
            fab_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTask(id);
                }
            });
        }
    }

    private void setupColorSeekBar() {
        colorSeekBar = findViewById(R.id.colorSlider);
        colorSeekBar.setMaxPosition(100);
        colorSeekBar.setColorSeeds(R.array.material_colors); // material_colors is defalut included in res/color,just use it.
        colorSeekBar.setColorBarPosition(10); //0 - maxValue
        colorSeekBar.setBarHeight(2); //5dpi
        colorSeekBar.setThumbHeight(50); //30dpi

        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
//                viewColor.setbackgroundColor(color);
            }
        });
    }

    private void onFinishTimeClick() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                // Log.v(TAG, "The choosen one " + date.getTime());
                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                txtFinishTime.setText(new SimpleDateFormat("HH:mm").format(date.getTime()));

            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
    }

    @SuppressLint("ShortAlarm")
    private void onStartTimeClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                // Log.v(TAG, "The choosen one " + date.getTime());
                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                txtStartTime.setText(new SimpleDateFormat("HH:mm").format(date.getTime()));

            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    manager.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
                }
                Toast.makeText(getApplicationContext(), "Alarm is set", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateTask(int id) {
        task = new Task();
        String title = ed_title.getText().toString();
        String description = ed_description.getText().toString();
        String startTime = txtStartTime.getText().toString();
        String finishTime = txtFinishTime.getText().toString();
        int color = colorSeekBar.getColor();

        if (title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Title is empty", Toast.LENGTH_SHORT).show();
        } else {
            task.setId(id);
            task.setTitle(title);
            task.setDescription(description);
            task.setColour(color);
            task.setStartTime(startTime);
            task.setFinishTime(finishTime);
            AppDatabase.getDatabase(getApplicationContext()).taskDao().update(task);
            setResult(RESULT_CODE_UPDATE);
            finish();
        }
    }

    private void insertNewTask() {
        task = new Task();
        String title = ed_title.getText().toString();
        String description = ed_description.getText().toString();
        String startTime = txtStartTime.getText().toString();
        String finishTime = txtFinishTime.getText().toString();
        int color = colorSeekBar.getColor();

        if (title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Title is empty", Toast.LENGTH_SHORT).show();
        } else {
            task.setTitle(title);
            task.setDescription(description);
            task.setColour(color);
            task.setStartTime(startTime);
            task.setFinishTime(finishTime);
            AppDatabase.getDatabase(getApplicationContext()).taskDao().insert(task);
            setResult(RESULT_CODE_UPDATE);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(NOT_UPDATE);
        super.onBackPressed();
    }

    @Override
    public void onWeekdaysItemClicked(int i, WeekdaysDataItem weekdaysDataItem) {

    }

    @Override
    public void onWeekdaysSelected(int i, ArrayList<WeekdaysDataItem> arrayList) {

    }
}
