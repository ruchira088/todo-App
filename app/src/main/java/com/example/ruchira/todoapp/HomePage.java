package com.example.ruchira.todoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePage extends AppCompatActivity
{
    private void refreshTasks(Bundle p_extrasBundle)
    {
        TextView homePageErrorPanel = (TextView) findViewById(R.id.homePageErrorPanel);
        LinearLayout todoList = (LinearLayout) findViewById(R.id.todoList);

        Request request = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.LIST))
                .addHeader(Constants.Keys.TOKEN, p_extrasBundle.getString(Constants.Keys.TOKEN)).build();

        Context context = this;

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call p_call, IOException p_ioException)
            {
                homePageErrorPanel.setText(StringResources.UNABLE_TO_FETCH_TODO_LIST);
            }

            @Override
            public void onResponse(Call p_call, Response p_response) throws IOException
            {
                if (p_response.isSuccessful())
                {
                    try
                    {
                        JSONObject responseBody = new JSONObject(p_response.body().string());
                        JSONArray tasks = responseBody.getJSONArray(Constants.JsonPropertyNames.TASKS);

                        for (int i = 0; i < tasks.length(); i++)
                        {
                            JSONObject task = tasks.getJSONObject(i);

                            TextView textView = new TextView(context);
                            textView.setText(task.getString(Constants.JsonPropertyNames.TASK));
                            textView.setOnClickListener(view -> {
                                try
                                {
                                    System.out.println(task.getString(Constants.JsonPropertyNames.ID));
                                } catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            });


                            todoList.addView(textView);
                        }
                    } catch (JSONException p_jsonException)
                    {
                        p_jsonException.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Bundle extrasBundle = getIntent().getExtras();

        ImageButton imageButton = (ImageButton) findViewById(R.id.refreshBtn);
        imageButton.setOnClickListener(onClickListener -> refreshTasks(extrasBundle));

        refreshTasks(extrasBundle);
    }
}
