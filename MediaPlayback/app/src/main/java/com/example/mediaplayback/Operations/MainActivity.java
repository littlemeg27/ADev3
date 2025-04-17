package com.example.mediaplayback.Operations;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediaplayback.Fragments.PlaybackFragment;
import com.example.mediaplayback.R;

// Brenna Pavlinchak
// AD3 - C202504
// MainActivity

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MediaPlayerService.class);
        startService(intent);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PlaybackFragment())
                    .commit();
        }
    }
}