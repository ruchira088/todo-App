package com.example.ruchira.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ruchira.todoapp.models.Task;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;

public class TaskPage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.activity_task_page);

        final EditText titleText = (EditText) findViewById(R.id.titleText);
        final SeekBar progressBar = (SeekBar) findViewById(R.id.progressBar);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.dueDatePicker);

        final Function<Integer, Void> setProgressBarValue = new Function<Integer, Void>()
        {
            @Override
            public Void apply(Integer p_value)
            {
                TextView progressAmount = (TextView) findViewById(R.id.progressAmount);
                progressBar.setProgress(p_value);
                progressAmount.setText(p_value + " %");
                return null;
            }
        };

        final Function<Date, Void> setDatePickerValue = new Function<Date, Void>()
        {
            @Override
            public Void apply(Date p_date)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(p_date);

                dueDatePicker.updateDate(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                return null;
            }
        };

        String jsonTask = getIntent().getExtras().getString(Constants.Keys.TASK);
        final Task task = jsonTask == null ? new Task() : new Gson().fromJson(jsonTask, Task.class);

        if(!task.isNew())
        {
            titleText.setText(task.getTaskName());
            setDatePickerValue.apply(task.getDueDate());
            setProgressBarValue.apply(task.getProgress());
        } else
        {
            setProgressBarValue.apply(0);
        }

        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                setProgressBarValue.apply(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final Function<DatePicker, Date> getDateFromDatePicker = new Function<DatePicker, Date>()
        {
            @Override
            public Date apply(DatePicker p_datePicker)
            {
                int dayOfMonth = p_datePicker.getDayOfMonth();
                int month = p_datePicker.getMonth();
                int year = p_datePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                return calendar.getTime();
            }
        };

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                task.setTaskName(titleText.getText().toString())
                        .setDueDate(getDateFromDatePicker.apply(dueDatePicker))
                        .setProgress(progressBar.getProgress());

                String jsonTask = new Gson().toJson(task);

                System.out.println(jsonTask);

            }
        });
    }

    private void saveTask(Task p_task)
    {
        OkHttpClient httpClient = new OkHttpClient();

    }
}
