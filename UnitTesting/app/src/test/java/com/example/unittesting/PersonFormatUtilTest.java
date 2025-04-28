package com.example.unittesting;

import com.example.unittesting.Model.Person;
import com.example.unittesting.Util.PersonFormatUtil;

import org.junit.Test;
import static org.junit.Assert.*;

public class PersonFormatUtilTest
{
    @Test
    public void testFormatFullName()
    {
        Person person = new Person("John", "Doe", 30);
        String result = PersonFormatUtil.formatFullName(person);
        assertEquals("John Doe", result);
    }

    @Test
    public void testFormatPersonSummary()
    {
        Person person = new Person("Jane", "Smith", 25);
        String result = PersonFormatUtil.formatPersonSummary(person);
        assertEquals("Jane, 25 years old", result);
    }

    @Test
    public void testNullPerson()
    {
        assertEquals("", PersonFormatUtil.formatFullName(null));
        assertEquals("", PersonFormatUtil.formatPersonSummary(null));
    }
}
