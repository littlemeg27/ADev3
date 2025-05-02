package com.example.unittesting.Util;

import com.example.unittesting.Model.Person;

// Brenna Pavlinchak
// AD3 - C202504
// PersonFormatUtil

public class PersonFormatUtil
{
    public static String formatFullName(Person person)
    {
        if (person == null)
        {
            return "";
        }
        return person.getFirstName() + " " + person.getLastName();
    }

    public static String formatPersonSummary(Person person)
    {
        if (person == null)
        {
            return "";
        }
        return person.getFirstName() + ", " + person.getAge() + " years old";
    }
}