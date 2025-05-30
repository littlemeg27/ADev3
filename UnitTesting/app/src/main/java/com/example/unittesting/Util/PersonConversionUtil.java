package com.example.unittesting.Util;

import com.example.unittesting.Model.Person;
import com.google.gson.Gson;

// Brenna Pavlinchak
// AD3 - C202504
// PersonConversionUtil

public class PersonConversionUtil
{
    private static final Gson gson = new Gson();

    public static String toJson(Person person)
    {
        return gson.toJson(person);
    }

    public static Person fromJson(String json)
    {
        return gson.fromJson(json, Person.class);
    }
}