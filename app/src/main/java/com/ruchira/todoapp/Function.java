package com.ruchira.todoapp;

public interface Function<InputArgType, OutputType>
{
    OutputType apply(InputArgType p_input);
}
