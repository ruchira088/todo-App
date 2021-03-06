package com.ruchira.todoapp;

import okhttp3.MediaType;

public class Constants
{
    public static final String APP_NAME = "com.ruchira.todoapp";

    public static final String SERVER_URL = "http://192.168.0.18:8000";

    public static final String EMPTY_TEXT = "";

    public static final MediaType JPG = MediaType.parse("image/jpg");

    public static final String JPG_EXTENSION = ".jpg";

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

        public static final String FIRST_NAME = "firstName";

        public static final String LAST_NAME = "lastName";

        public static final String IMAGE_FILE = "imageFile";
    }

    public static class ApiEntryPoints
    {
        public static final String LOGIN = "/account/login";

        public static final String REGISTER = "/account/register";

        public static final String PROFILE = "/account/register";

        public static final String TASK = "/task";

        public static final String LIST = "/list";
    }
}
