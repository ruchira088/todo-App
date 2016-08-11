package com.example.ruchira.todoapp.models;

import java.util.List;

public class TasksList
{
    private String username;

    private List<Task> tasks;

    public List<Task> getTasks()
    {
        return tasks;
    }

    public String getUsername()
    {
        return username;
    }
}
