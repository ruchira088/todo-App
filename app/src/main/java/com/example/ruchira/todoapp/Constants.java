package com.example.ruchira.todoapp;

import okhttp3.MediaType;

public class Constants
{
    public static final String SERVER_URL = "http://192.168.0.29:8000";

    public static final String EMPTY_TEXT = "";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static class Keys
    {
        public static final String TOKEN = "token";
        public static final String TASK = "task";
    }

    public static class ParameterNames
    {
        public static final String USERNAME = "username";

        public static final String PASSWORD = "password";
    }

    public static class ApiEntryPoints
    {
        public static final String LOGIN = "/account/login";

        public static final String TASK = "/task";

        public static final String LIST = "/list";
    }
}
