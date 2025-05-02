package com.example.unittesting;

import android.content.Context;
import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.example.unittesting.Model.Person;
import com.example.unittesting.Util.PreferenceUtil;

// Brenna Pavlinchak
// AD3 - C202504
// PreferenceUtilTest

@RunWith(MockitoJUnitRunner.class)
public class PreferenceUtilTest
{
    @Mock
    private Context context;
    @Mock
    private SharedPreferences prefs;
    @Mock
    private SharedPreferences.Editor editor;

    @Before
    public void setUp()
    {
        when(context.getSharedPreferences("PersonPrefs", Context.MODE_PRIVATE)).thenReturn(prefs);
        when(prefs.edit()).thenReturn(editor);
        when(editor.putString("format_style", "custom")).thenReturn(editor);
        when(editor.putString("first_name", "Jane")).thenReturn(editor);
        when(editor.putString("last_name", "Smith")).thenReturn(editor);
        when(editor.putInt("age", 25)).thenReturn(editor);
    }

    @Test
    public void testSaveFormatStyle()
    {
        PreferenceUtil.saveFormatStyle(context, "custom");
        assertEquals("custom", PreferenceUtil.getFormatStyle(context));
    }

    @Test
    public void testSavePerson()
    {
        Person person = new Person("Jane", "Smith", 25);
        PreferenceUtil.savePerson(context, person);
        Person savedPerson = PreferenceUtil.getPerson(context);
        assertEquals("Jane", savedPerson.getFirstName());
        assertEquals("Smith", savedPerson.getLastName());
        assertEquals(25, savedPerson.getAge());
    }
}