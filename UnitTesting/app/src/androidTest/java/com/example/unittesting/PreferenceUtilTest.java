package com.example.unittesting;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.unittesting.Util.PreferenceUtil;

@RunWith(AndroidJUnit4.class)
public class PreferenceUtilTest {
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Before
    public void setUp() {
        context = Mockito.mock(Context.class);
        prefs = Mockito.mock(SharedPreferences.class);
        editor = Mockito.mock(SharedPreferences.Editor.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(prefs);
        when(prefs.edit()).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
    }

    @Test
    public void testSaveAndGetFormatStyle() {
        // Arrange
        when(prefs.getString("format_style", "default")).thenReturn("custom");

        // Act
        PreferenceUtil.saveFormatStyle(context, "custom");
        String style = PreferenceUtil.getFormatStyle(context);

        // Assert
        assertEquals("custom", style);
        verify(editor).putString("format_style", "custom");
        verify(editor).apply();
    }
}