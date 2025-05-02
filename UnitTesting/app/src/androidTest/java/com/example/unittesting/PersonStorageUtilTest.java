package com.example.unittesting;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.unittesting.Model.Person;
import com.example.unittesting.Util.PersonStorageUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import static org.junit.Assert.*;

// Brenna Pavlinchak
// AD3 - C202504
// PersonStorageUtilTest

@RunWith(AndroidJUnit4.class)
public class PersonStorageUtilTest
{
    @Test
    public void testSaveAndLoadPerson() throws IOException
    {
        Context context = ApplicationProvider.getApplicationContext();
        Person person = new Person("John", "Doe", 30);
        PersonStorageUtil.savePerson(context, person);
        Person loaded = PersonStorageUtil.loadPerson(context);
        assertEquals("John", loaded.getFirstName());
        assertEquals("Doe", loaded.getLastName());
        assertEquals(30, loaded.getAge());
    }
}