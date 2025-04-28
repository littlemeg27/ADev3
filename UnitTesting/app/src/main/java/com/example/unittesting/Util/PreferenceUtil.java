package com.example.unittesting.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil
{
    private static final String PREF_NAME = "person_prefs";
    private static final String KEY_FORMAT_STYLE = "format_style";

    public static void saveFormatStyle(Context context, String style)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_FORMAT_STYLE, style).apply();
    }

    public static String getFormatStyle(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_FORMAT_STYLE, "default");
    }
}