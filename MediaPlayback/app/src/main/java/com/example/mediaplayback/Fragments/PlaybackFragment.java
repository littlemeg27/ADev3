package com.example.mediaplayback.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mediaplayback.Operations.MediaPlayerService;
import com.example.mediaplayback.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start service
        Intent intent = new Intent(this, MediaPlayerService.class);
        startService(intent);

        // Load PlaybackFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PlaybackFragment())
                    .commit();
        }
    }
}