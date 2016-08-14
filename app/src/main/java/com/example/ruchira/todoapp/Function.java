package com.example.ruchira.todoapp;

public interface Function<InputArgType, OutputType>
{
    OutputType apply(InputArgType p_input);
}
