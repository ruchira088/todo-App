package com.ruchira.todoapp.pages;

import android.content.Intent;

public class UnauthenticatedPage extends AbstractPage
{
    @Override
    protected void onTokenPresent(String p_token)
    {
        startActivity(new Intent(this, HomePage.class));
    }

    @Override
    protected void onTokenAbsent()
    {
        // Do nothing
    }
}
