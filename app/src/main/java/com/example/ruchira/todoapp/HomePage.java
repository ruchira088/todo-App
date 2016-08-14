package com.example.ruchira.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ruchira.todoapp.models.Task;
import com.example.ruchira.todoapp.models.TasksList;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomePage extends AppCompatActivity
{
    /**
     * Refreshes the task list displayed in the todo list.
     *
     * @param p_extrasBundle The extras {@link Bundle} which contains the authentication token.
     */
    private void refreshTasks(final Bundle p_extrasBundle)
    {
        final TextView homePageErrorPanel = (TextView) findViewById(R.id.homePageErrorPanel);
        final LinearLayout todoList = (LinearLayout) findViewById(R.id.todoList);

        // Create a request to fetch the tasks list
        Request request = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.LIST))
                .addHeader(Constants.Keys.TOKEN, p_extrasBundle.getString(Constants.Keys.TOKEN)).build();

        final Context context = this;

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
                        final TextView textView = new TextView(context);
                        textView.setText(task.getTaskName());

                        textView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View p_view)
                            {
                                Intent intent = new Intent(context, TaskPage.class);
                                intent.putExtra(Constants.Keys.TOKEN, p_extrasBundle.getString(Constants.Keys.TOKEN));
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

//                    try
//                    {
//
//                        JSONObject responseBody = new JSONObject(p_response.body().string());
//                        JSONArray tasks = responseBody.getJSONArray(Constants.JsonPropertyNames.TASKS);
//
//                        // Create a TextView for each of the fetched tasks
//                        for (int i = 0; i < tasks.length(); i++)
//                        {
//                            final JSONObject task = tasks.getJSONObject(i);
//                            /*TodoItem todoItem = new TodoItem(context);
//                            todoItem.setText(task.getString(Constants.JsonPropertyNames.TASK))
//                                    .setClickListener(task.getString(Constants.JsonPropertyNames.ID));*/
//
//                            final TextView textView = new TextView(context);
//                            textView.setText(task.getString(Constants.JsonPropertyNames.TASK));
//                            textView.setOnClickListener(new View.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(View p_view)
//                                {
//                                    try
//                                    {
//                                        final RequestBody requestBody = RequestBody.create(Constants.JSON,
//                                                new JSONObject()
//                                                        .put(Constants.JsonPropertyNames.ID, task.getString(Constants.JsonPropertyNames.ID))
//                                                        .put("task", "Clean the room")
//                                                        .toString());
//
//                                        Request request = new Request.Builder().patch(requestBody)
//                                                .url(Utils.createUrl(Constants.ApiEntryPoints.TASK))
//                                                .addHeader(Constants.Keys.TOKEN, p_extrasBundle.getString(Constants.Keys.TOKEN))
//                                                .build();
//
//                                        httpClient.newCall(request).enqueue(new Callback()
//                                        {
//                                            @Override
//                                            public void onFailure(Call p_call, IOException p_ioException)
//                                            {
//                                                p_ioException.printStackTrace();
//                                            }
//
//                                            @Override
//                                            public void onResponse(Call p_call, Response p_response) throws IOException
//                                            {
//                                                refreshTasks(p_extrasBundle);
//                                            }
//                                        });
//
//                                    } catch (JSONException p_jsonException)
//                                    {
//                                        p_jsonException.printStackTrace();
//                                    }
//                                }
//                            });
//
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    todoList.addView(textView);
//                                }
//                            });
//
//                        }
//                    } catch (JSONException p_jsonException)
//                    {
//                        p_jsonException.printStackTrace();
//                    }
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshTasks(getIntent().getExtras());
    }

    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        System.out.println("onCreate");
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.activity_home_page);
        final Bundle extrasBundle = getIntent().getExtras();

        ImageButton imageButton = (ImageButton) findViewById(R.id.refreshBtn);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                refreshTasks(extrasBundle);
            }
        });
    }
}
