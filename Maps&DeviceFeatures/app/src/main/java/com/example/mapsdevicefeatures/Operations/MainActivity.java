package com.example.mapsdevicefeatures.Operations;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mapsdevicefeatures.Fragments.MapFragment;
import com.example.mapsdevicefeatures.R;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MapFragment())
                    .commit();
        }
    }

    public void switchToCameraFragment()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new com.example.mapsdevicefeatures.Fragments.CameraFragment())
                .addToBackStack(null)
                .commit();
    }

    public void switchToMapFragment()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MapFragment())
                .addToBackStack(null)
                .commit();
    }
}