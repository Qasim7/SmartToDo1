package com.example.qasim.smarttodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qasim.smarttodo.adapter.TaskAdapter;
import com.example.qasim.smarttodo.database.AppDatabase;
import com.example.qasim.smarttodo.model.Task;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    public static final int RESULT_CODE_UPDATE = 100;
    private static final int NOT_UPDATE = 101;
    private TextView txtDateTime;
    private TextView txtColor;
    private Calendar date;
    private EditText ed_title;
    private EditText ed_description;
    private ColorSeekBar colorSeekBar;
    private Task task;
    private TaskAdapter.TaskView taskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ed_title = findViewById(R.id.edt_title);
        ed_description = findViewById(R.id.edt_desc);
        txtDateTime = findViewById(R.id.txt_datetime);
        txtColor = findViewById(R.id.txtColor);

        colorSeekBar = findViewById(R.id.colorSlider);
        colorSeekBar.setMaxPosition(100);
        colorSeekBar.setColorSeeds(R.array.material_colors); // material_colors is defalut included in res/color,just use it.
        colorSeekBar.setColorBarPosition(10); //0 - maxValue
        colorSeekBar.setBarHeight(2); //5dpi
        colorSeekBar.setThumbHeight(50); //30dpi

        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int colorBarPosition, int alphaBarPosition, int color) {
                txtColor.setTextColor(color);
            }
        });


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new Task();

                String title = ed_title.getText().toString();
                String description = ed_description.getText().toString();
                String startTime = txtDateTime.getText().toString();
                int color = colorSeekBar.getColor();

                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Title is empty", Toast.LENGTH_SHORT).show();
                } else {
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setColour(color);
                    if (startTime.equals("start time")) {
                        task.setStartTime("");
                    } else {
                        task.setStartTime(startTime);
                    }
                    AppDatabase.getDatabase(getApplicationContext()).taskDao().insert(task);
                    setResult(RESULT_CODE_UPDATE);
                    finish();
                }


            }
        });


        txtDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                final Calendar currentDate = Calendar.getInstance();
                date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(NewTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                // Log.v(TAG, "The choosen one " + date.getTime());
                                // Toast.makeText(getContext(),"The choosen one " + date.getTime(),Toast.LENGTH_SHORT).show();
                                txtDateTime.setText(new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date.getTime()));

                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(NOT_UPDATE);
        super.onBackPressed();
    }
}
