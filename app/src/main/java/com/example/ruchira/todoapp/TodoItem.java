package com.example.ruchira.todoapp;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class TodoItem extends View
{
    private TextView m_textView;

    public TodoItem setText(String p_text)
    {
        m_textView.setText(p_text);
        return this;
    }

    public TodoItem setClickListener(String p_id)
    {
        m_textView.setOnClickListener(view -> {
            System.out.println(p_id);
        });

        return this;
    }


    public TodoItem(Context p_context)
    {
        super(p_context);
        m_textView = new TextView(p_context);
    }
}
