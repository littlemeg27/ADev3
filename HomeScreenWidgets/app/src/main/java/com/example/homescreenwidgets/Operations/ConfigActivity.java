package com.example.homescreenwidgets.Operations;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Show the PreferenceFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ConfigPreferenceFragment())
                .commit();

        // Set result to canceled by default
        setResult(RESULT_CANCELED);

        // Get widget ID
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                // Save widget ID for later use
                getSharedPreferences("WidgetPrefs", MODE_PRIVATE)
                        .edit()
                        .putInt("appWidgetId", appWidgetId)
                        .apply();
            }
        }
    }
}