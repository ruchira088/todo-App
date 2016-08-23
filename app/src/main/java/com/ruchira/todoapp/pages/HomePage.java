package com.ruchira.todoapp.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruchira.todoapp.Constants;
import com.ruchira.todoapp.R;
import com.ruchira.todoapp.StringResources;
import com.ruchira.todoapp.Utils;
import com.ruchira.todoapp.models.Task;
import com.ruchira.todoapp.models.TasksList;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePage extends AuthenticatedPage
{
    final Context m_context = this;

    /**
     * Refreshes the task list displayed in the todo list.
     */
    private void refreshTasks()
    {
        final TextView homePageErrorPanel = (TextView) findViewById(R.id.homePageErrorPanel);
        final LinearLayout todoList = (LinearLayout) findViewById(R.id.todoList);

        // Create a request to fetch the tasks list
        Request request = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.LIST))
                .addHeader(Constants.Keys.TOKEN, getUserToken()).build();

        final OkHttpClient httpClient = new OkHttpClient();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call p_call, IOException p_ioException)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        homePageErrorPanel.setText(StringResources.UNABLE_TO_FETCH_TODO_LIST);
                    }
                });
            }

            @Override
            public void onResponse(Call p_call, Response p_response) throws IOException
            {
                // Remove the existing tasks in the task list
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        todoList.removeAllViews();
                    }
                });

                if (p_response.isSuccessful())
                {
                    TasksList tasksList = new Gson().fromJson(p_response.body().string(), TasksList.class);

                    for (final Task task : tasksList.getTasks())
                    {
                        final TextView textView = new TextView(m_context);
                        textView.setText(task.getTaskName());

                        textView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View p_view)
                            {
                                Intent intent = new Intent(m_context, TaskPage.class);
                                intent.putExtra(Constants.Keys.TASK, new Gson().toJson(task));
                                startActivity(intent);
                            }
                        });

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                todoList.addView(textView);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshTasks();
    }

    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button addTaskButton = (Button) findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                Intent intent = new Intent(m_context, TaskPage.class);
                startActivity(intent);
            }
        });

        Button logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                removeUserToken();
                startActivity(new Intent(m_context, LoginPage.class));
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.refreshBtn);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                refreshTasks();
            }
        });
    }
}
