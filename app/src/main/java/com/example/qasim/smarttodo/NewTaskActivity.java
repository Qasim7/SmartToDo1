package com.example.qasim.smarttodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.qasim.smarttodo.database.AppDatabase;
import com.example.qasim.smarttodo.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTaskActivity extends AppCompatActivity {

    public static final int RESULT_CODE_UPDATE = 100;
    private static final int NOT_UPDATE = 101;
    TextView txtDateTime;
    ImageButton buttonDateTimePicker;
    Calendar date;
    EditText ed_title, ed_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        ed_title = findViewById(R.id.edt_title);
        ed_description = findViewById(R.id.edt_desc);
        txtDateTime = findViewById(R.id.txt_datetime);
        buttonDateTimePicker = findViewById(R.id.btn_datetime);


        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ed_title.getText().toString();
                String description = ed_description.getText().toString();
                Task task = new Task();
                task.setTitle(title);
                task.setDescription(description);

                AppDatabase.getDatabase(getApplicationContext()).taskDao().insert(task);
                setResult(RESULT_CODE_UPDATE);
                finish();

            }
        });


        buttonDateTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                txtDateTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date.getTime()));
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
