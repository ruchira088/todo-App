package com.example.ruchira.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);
        setContentView(R.layout.activity_login_page);

        final LoginPage loginPage = this;

        final TextView errorPanel = (TextView) findViewById(R.id.errorPanel);
        errorPanel.setText(Constants.EMPTY_TEXT);

        final EditText usernameField = (EditText) findViewById(R.id.usernameText);
        final EditText passwordField = (EditText) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View p_view)
               {

                   final String username = usernameField.getText().toString();
                   String password = passwordField.getText().toString();

                   RequestBody requestBody = null;

                   try
                   {
                       requestBody = RequestBody.create(Constants.JSON,
                               new JSONObject().put(Constants.ParameterNames.USERNAME, username).put(Constants.ParameterNames.PASSWORD, password).toString());
                   } catch (JSONException jsonException)
                   {
                       // The program should NEVER reach here.
                       jsonException.printStackTrace();
                       return;
                   }

                   OkHttpClient httpClient = new OkHttpClient();

                   Request loginPostRequest = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.LOGIN)).post(requestBody).build();

                   httpClient.newCall(loginPostRequest).enqueue(new Callback()
                   {
                       @Override
                       public void onFailure(Call p_call, IOException p_ioException)
                       {
                           errorPanel.setText(StringResources.UNABLE_TO_CONNECT_TO_SERVER);
                       }

                       @Override
                       public void onResponse(Call p_call, Response p_response) throws IOException
                       {
                           if (p_response.isSuccessful())
                           {
                               try
                               {
                                   JSONObject response = new JSONObject(p_response.body().string());
                                   Intent intent = new Intent(loginPage, HomePage.class);
                                   intent.putExtra(Constants.Keys.TOKEN, response.get(Constants.Keys.TOKEN).toString());
                                   startActivity(intent);

                               } catch (JSONException p_jsonException)
                               {
                                   p_jsonException.printStackTrace();
                               }
                           } else
                           {
                               runOnUiThread(new Runnable()
                               {
                                   @Override
                                   public void run()
                                   {
                                       errorPanel.setText(StringResources.INVALID_CREDENTIALS);
                                   }
                               });
                           }
                       }
                   });
               }
           }

        );

    }
}
