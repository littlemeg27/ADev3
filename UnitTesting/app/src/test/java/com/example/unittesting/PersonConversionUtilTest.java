package com.example.unittesting;

import com.example.unittesting.Model.Person;
import com.example.unittesting.Util.PersonConversionUtil;

import org.junit.Test;
import static org.junit.Assert.*;

public class PersonConversionUtilTest
{
    @Test
    public void testToJson()
    {
        Person person = new Person("John", "Doe", 30);
        String json = PersonConversionUtil.toJson(person);
        assertTrue(json.contains("\"firstName\":\"John\""));
        assertTrue(json.contains("\"lastName\":\"Doe\""));
        assertTrue(json.contains("\"age\":30"));
    }

    @Test
    public void testFromJson()
    {
        String json = "{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"age\":25}";
        Person person = PersonConversionUtil.fromJson(json);
        assertEquals("Jane", person.getFirstName());
        assertEquals("Smith", person.getLastName());
        assertEquals(25, person.getAge());
    }

    @Test
    public void testNullInput()
    {
        String json = PersonConversionUtil.toJson(null);
        assertEquals("null", json);
    }
}
