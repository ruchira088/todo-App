package com.example.ruchira.todoapp.models;

import com.example.ruchira.todoapp.Utils;

import java.util.Date;

public class Task
{
    private String id;

    private String task;

    private int progress;

    private String dueDate;

    public String getId()
    {
        return id;
    }

    public Task setId(String p_id)
    {
        id = p_id;
        return this;
    }

    public String getTaskName()
    {
        return task;
    }

    public Task setTaskName(String p_taskName)
    {
        task = p_taskName;
        return this;
    }

    public int getProgress()
    {
        return  progress;
    }

    public Task setProgress(Integer p_progress)
    {
        progress = p_progress;
        return this;
    }

    public Date getDueDate()
    {
        return Utils.parseDate(dueDate);
    }

    public Task setDueDate(Date p_date)
    {
        dueDate = Utils.formatDate(p_date);

        return this;
    }

    public boolean isNew()
    {
        return id == null;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id='").append(id).append('\'');
        sb.append(", task='").append(task).append('\'');
        sb.append(", progress=").append(progress);
        sb.append(", dueDate='").append(dueDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
