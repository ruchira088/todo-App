package com.ruchira.todoapp.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ruchira.todoapp.Constants;
import com.ruchira.todoapp.Function;
import com.ruchira.todoapp.R;
import com.ruchira.todoapp.Utils;
import com.ruchira.todoapp.models.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaskPage extends AuthenticatedPage
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

        if (!task.isNew())
        {
            titleText.setText(task.getTaskName());
            setDatePickerValue.apply(task.getDueDate());
            setProgressBarValue.apply(task.getProgress());
        } else
        {
            setProgressBarValue.apply(0);
        }


        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                finish();
            }
        });

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                setProgressBarValue.apply(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
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

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            private void saveTask(Task p_task)
            {
                OkHttpClient httpClient = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(Constants.JSON, new Gson().toJson(p_task));

                Request.Builder requestBuilder = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.TASK)).addHeader(Constants.Keys.TOKEN, getUserToken());
                Request request = null;

                if (p_task.isNew())
                {
                    request = requestBuilder.post(requestBody).build();
                } else
                {
                    request = requestBuilder.put(requestBody).build();
                }

                httpClient.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call p_call, IOException p_ioException)
                    {

                    }

                    @Override
                    public void onResponse(Call p_call, Response p_response) throws IOException
                    {
                        finish();
                    }
                });
            }

            @Override
            public void onClick(View p_view)
            {
                task.setTaskName(titleText.getText().toString())
                        .setDueDate(getDateFromDatePicker.apply(dueDatePicker))
                        .setProgress(progressBar.getProgress());

                saveTask(task);
            }
        });
    }
}
