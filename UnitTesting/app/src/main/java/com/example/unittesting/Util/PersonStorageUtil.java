package com.example.unittesting.Util;

import android.content.Context;
import android.util.Log;
import com.example.unittesting.Model.Person;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

// Brenna Pavlinchak
// AD3 - C202504
// PersonStorageUtil

public class PersonStorageUtil
{
    private static final String FILE_NAME = "person_data.txt";
    private static final String TAG = "PersonStorageUtil";

    public static void savePerson(Context context, Person person) throws IOException
    {
        String data = PersonConversionUtil.toJson(person);
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))
        {
            fos.write(data.getBytes());
        }
    }

    public static Person loadPerson(Context context) throws IOException
    {
        StringBuilder data = new StringBuilder();
        try
        {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            int c;
            while ((c = fis.read()) != -1)
            {
                data.append((char) c);
            }
            fis.close();
        }
        catch (IOException e)
        {
            Log.e(TAG, "Failed to load person data: " + e.getMessage());
            throw e;
        }
        return PersonConversionUtil.fromJson(data.toString());
    }
}