package com.ruchira.todoapp.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ruchira.todoapp.Constants;
import com.ruchira.todoapp.models.UserToken;

public abstract class AbstractPage extends AppCompatActivity
{
    private String m_userToken = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        m_userToken = sharedPreferences.getString(Constants.Keys.TOKEN, null);

        if (m_userToken == null)
        {
            onTokenAbsent();
        } else
        {
            onTokenPresent(m_userToken);
        }
    }

    protected void saveUserToken(UserToken p_userToken)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.Keys.TOKEN, p_userToken.getToken());
        editor.commit();

        m_userToken = p_userToken.getToken();
    }

    protected void removeUserToken()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.Keys.TOKEN);
        editor.commit();

        m_userToken = null;
    }

    protected abstract void onTokenPresent(String p_token);

    protected abstract void onTokenAbsent();
}
