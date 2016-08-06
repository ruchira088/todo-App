package com.example.ruchira.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity
{
    private void refreshTasks(Bundle p_extrasBundle)
    {
        TextView todoListPanel = (TextView) findViewById(R.id.todoListPanel);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest getTodoItemsRequest = new JsonObjectRequest(Utils.createUrl("/list"), null,
                response -> {
                    try
                    {
                        JSONArray tasks = response.getJSONArray("tasks");

                        StringBuilder stringBuilder = new StringBuilder();

                        for(int i=0; i< tasks.length(); i++)
                        {
                            stringBuilder.append(tasks.getJSONObject(i).getString("task") + "\n");
                        }

                        todoListPanel.setText(stringBuilder.toString().trim());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<>();
                map.put(Constants.Keys.TOKEN, p_extrasBundle.getString(Constants.Keys.TOKEN));

                return map;
            }
        };

        requestQueue.add(getTodoItemsRequest);
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
