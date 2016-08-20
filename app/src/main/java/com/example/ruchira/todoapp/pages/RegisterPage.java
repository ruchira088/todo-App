package com.example.ruchira.todoapp.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ruchira.todoapp.Constants;
import com.example.ruchira.todoapp.R;
import com.example.ruchira.todoapp.Utils;
import com.example.ruchira.todoapp.models.Message;
import com.example.ruchira.todoapp.models.UserToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterPage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.activity_register_page);

        final EditText usernameEditText = (EditText) findViewById(R.id.usernameText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordText);
        final EditText confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordText);

        final TextView messagePanel = (TextView) findViewById(R.id.messagePanel);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if(password.equals(confirmPassword))
                {
                    JsonObject body = new JsonObject();
                    body.addProperty(Constants.ParameterNames.USERNAME, username);
                    body.addProperty(Constants.ParameterNames.PASSWORD, password);
                    System.out.println(body.toString());

                    RequestBody requestBody = RequestBody.create(Constants.JSON, body.toString());

                    Request request = new Request.Builder()
                            .url(Utils.createUrl(Constants.ApiEntryPoints.REGISTER))
                            .post(requestBody).build();

                    OkHttpClient httpClient = new OkHttpClient();

                    httpClient.newCall(request).enqueue(new Callback()
                    {
                        @Override
                        public void onFailure(Call p_call, IOException p_e)
                        {
                            p_e.printStackTrace();

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    messagePanel.setText("Unable to register account.");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call p_call, Response p_response) throws IOException
                        {
                            final Gson gson = new Gson();
                            final String responseBody = p_response.body().string();

                            if(p_response.isSuccessful())
                            {
                                UserToken userToken = gson.fromJson(responseBody, UserToken.class);
                            } else
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        messagePanel.setText(gson.fromJson(responseBody, Message.class).getMessage());
                                    }
                                });
                            }
                        }
                    });


                } else
                {
                    messagePanel.setText("Passwords do NOT match.");
                }
            }
        });

    }
}
