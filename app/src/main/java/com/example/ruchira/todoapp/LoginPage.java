package com.example.ruchira.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        TextView errorPanel = (TextView) findViewById(R.id.errorPanel);
        errorPanel.setText("");

        EditText usernameField = (EditText) findViewById(R.id.usernameText);
        EditText passwordField = (EditText) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(onClickListener ->
        {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            JSONObject requestBody = null;

            try
            {
                requestBody = new JSONObject().put("username", username).put("password", password);
            }
            catch (JSONException jsonException)
            {
                // The program should NEVER reach here.
                jsonException.printStackTrace();
                return;
            }

            requestQueue.add(new JsonObjectRequest(Request.Method.POST,
                    Utils.createUrl("/login"),
                    requestBody,
                    response ->
                    {
                        try
                        {
                            Intent intent = new Intent(this, HomePage.class);
                            intent.putExtra(Constants.Keys.TOKEN, response.get(Constants.Keys.TOKEN).toString());
                            startActivity(intent);
                        }
                        catch (JSONException jsonException)
                        {
                            jsonException.printStackTrace();
                        }
                    },
                    error ->
                    {
                        errorPanel.setText("Unable to authenticate.");
                    }
            ));

        });

    }
}
