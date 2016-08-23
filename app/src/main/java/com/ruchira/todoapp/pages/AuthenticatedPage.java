package com.ruchira.todoapp.pages;

import android.content.Intent;

public class AuthenticatedPage extends AbstractPage
{
    private String m_userToken = null;

    @Override
    protected void onTokenPresent(String p_token)
    {
        m_userToken = p_token;
    }

    @Override
    protected void onTokenAbsent()
    {
        startActivity(new Intent(this, LoginPage.class));
    }

    protected String getUserToken()
    {
        return m_userToken;
    }
}
