package com.ruchira.todoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

public class TodoItem extends View
{
    private TextView m_textView;

    public TodoItem(Context p_context)
    {
        super(p_context);
        m_textView = new TextView(p_context);
    }

    public TodoItem setText(String p_text)
    {
        m_textView.setText(p_text);
        return this;
    }

    public TodoItem setClickListener(final String p_id)
    {
        m_textView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println(p_id);
            }
        });

        return this;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        m_textView.draw(canvas);
    }
}
