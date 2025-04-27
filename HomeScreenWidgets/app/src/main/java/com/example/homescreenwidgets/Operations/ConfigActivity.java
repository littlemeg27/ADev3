package com.example.homescreenwidgets.Operations;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homescreenwidgets.Fragments.ConfigPreferenceFragment;
import com.example.homescreenwidgets.R;

// Brenna Pavlinchak
// AD3 - C202504
// ConfigActivity

public class ConfigActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ConfigPreferenceFragment())
                .commit();

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
            {

                getSharedPreferences("WidgetPrefs", MODE_PRIVATE)
                        .edit()
                        .putInt("appWidgetId", appWidgetId)
                        .apply();
            }
        }
    }
}