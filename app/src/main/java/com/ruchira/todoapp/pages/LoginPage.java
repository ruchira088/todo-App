package com.ruchira.todoapp.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ruchira.todoapp.Constants;
import com.ruchira.todoapp.Function;
import com.ruchira.todoapp.R;
import com.ruchira.todoapp.StringResources;
import com.ruchira.todoapp.Utils;
import com.ruchira.todoapp.models.UserToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This is the login page activity
 */
public class LoginPage extends UnauthenticatedPage
{
    @Override
    protected void onCreate(Bundle p_savedInstanceState)
    {
        super.onCreate(p_savedInstanceState);

        setContentView(R.layout.activity_login_page);
        final LoginPage loginPage = this;

        final TextView errorPanel = (TextView) findViewById(R.id.errorPanel);
        errorPanel.setText(Constants.EMPTY_TEXT);

        TextView newUserLabel = (TextView) findViewById(R.id.newUserLabel_right);
        newUserLabel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p_view)
            {
                startActivity(new Intent(loginPage, RegisterPage.class));
            }
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View p_view)
               {
                   Function<Integer, String> getTextContents = new Function<Integer, String>()
                   {
                       @Override
                       public String apply(Integer p_input)
                       {
                           EditText editText = (EditText) findViewById(p_input);
                           return editText.getText().toString();
                       }
                   };

                   String username = getTextContents.apply(R.id.usernameText);
                   String password = getTextContents.apply(R.id.passwordText);

                   JsonObject requestBodyJson = new JsonObject();
                   requestBodyJson.addProperty(Constants.ParameterNames.USERNAME, username);
                   requestBodyJson.addProperty(Constants.ParameterNames.PASSWORD, password);
                   RequestBody requestBody = RequestBody.create(Constants.JSON, new Gson().toJson(requestBodyJson));

                   OkHttpClient httpClient = new OkHttpClient();

                   Request loginPostRequest = new Request.Builder().url(Utils.createUrl(Constants.ApiEntryPoints.LOGIN))
                           .post(requestBody).build();

                   httpClient.newCall(loginPostRequest).enqueue(new Callback()
                   {
                       @Override
                       public void onFailure(Call p_call, IOException p_ioException)
                       {
                           runOnUiThread(new Runnable()
                           {
                               @Override
                               public void run()
                               {
                                   errorPanel.setText(StringResources.UNABLE_TO_CONNECT_TO_SERVER);
                               }
                           });
                       }

                       @Override
                       public void onResponse(Call p_call, Response p_response) throws IOException
                       {
                           if (p_response.isSuccessful())
                           {
                               UserToken userToken = new Gson().fromJson(p_response.body().string(), UserToken.class);
                               saveUserToken(userToken);

                               Intent intent = new Intent(loginPage, HomePage.class);
                               startActivity(intent);
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
