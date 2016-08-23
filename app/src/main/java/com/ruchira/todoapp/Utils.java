package com.ruchira.todoapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{
    private static SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String createUrl(String p_path)
    {
        return Constants.SERVER_URL + p_path;
    }

    public static Date parseDate(String p_iso8601DateString)
    {
        try
        {
            return ISO_8601_DATE_FORMAT.parse(p_iso8601DateString);

        } catch (ParseException p_parseException)
        {
            p_parseException.printStackTrace();
        }

        return null;
    }

    public static String formatDate(Date p_date)
    {
        return ISO_8601_DATE_FORMAT.format(p_date);
    }
}
