package com.example.ruchira.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
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
        TextView todoListPanel = (TextView) findViewById(R.id.todoListPanel);
        TextView homePageErrorPanel = (TextView) findViewById(R.id.homePageErrorPanel);

        Request request = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.LIST))
                .addHeader(Constants.Keys.TOKEN, p_extrasBundle.getString(Constants.Keys.TOKEN)).build();

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call p_call, IOException p_ioException)
            {
                runOnUiThread(() -> homePageErrorPanel.setText(StringResources.UNABLE_TO_FETCH_TODO_LIST));
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

                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < tasks.length(); i++)
                        {
                            stringBuilder.append(tasks.getJSONObject(i).getString(Constants.JsonPropertyNames.TASK) + "\n");
                        }

                        runOnUiThread(() -> todoListPanel.setText(stringBuilder.toString().trim()));
                    }
                    catch (JSONException p_jsonException)
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
