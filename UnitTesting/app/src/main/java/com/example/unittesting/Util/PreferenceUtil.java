package com.example.unittesting.Util;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.unittesting.Model.Person;

// Brenna Pavlinchak
// AD3 - C202504
// PreferenceUtil

public class PreferenceUtil
{
    private static final String PREF_NAME = "PersonPrefs";
    private static final String KEY_FORMAT_STYLE = "format_style";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_AGE = "age";

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

    public static void savePerson(Context context, Person person)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_FIRST_NAME, person.getFirstName())
                .putString(KEY_LAST_NAME, person.getLastName())
                .putInt(KEY_AGE, person.getAge())
                .apply();
    }

    public static Person getPerson(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String firstName = prefs.getString(KEY_FIRST_NAME, null);
        String lastName = prefs.getString(KEY_LAST_NAME, null);
        int age = prefs.getInt(KEY_AGE, -1);

        if (firstName != null && lastName != null && age != -1)
        {
            return new Person(firstName, lastName, age);
        }
        return null;
    }
}