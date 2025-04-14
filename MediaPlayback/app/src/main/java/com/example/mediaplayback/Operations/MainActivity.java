package com.example.mediaplayback.Operations;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediaplayback.R;

public class MainActivity extends AppCompatActivity {
    private MediaPlayerService mediaPlayerService;
    private boolean isBound = false;
    private final Handler handler = new Handler();
    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (isBound && mediaPlayerService != null) {
                seekBar.setProgress(mediaPlayerService.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        }
    };
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mediaPlayerService = binder.getService();
            isBound = true;
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private Button playPauseButton;
    private Button stopButton;
    private Button prevButton;
    private Button nextButton;
    private CheckBox loopToggle;
    private CheckBox shuffleToggle;
    private SeekBar seekBar;
    private TextView songTitle;
    private ImageView albumArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        playPauseButton = findViewById(R.id.playPauseButton);
        stopButton = findViewById(R.id.stopButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        loopToggle = findViewById(R.id.loopToggle);
        shuffleToggle = findViewById(R.id.shuffleToggle);
        seekBar = findViewById(R.id.seekBar);
        songTitle = findViewById(R.id.songTitle);
        albumArt = findViewById(R.id.albumArt);

        Intent intent = new Intent(this, MediaPlayerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startService(intent);

        playPauseButton.setOnClickListener(v -> {
            if (isBound && mediaPlayerService != null) {
                if (mediaPlayerService.isPlaying()) {
                    mediaPlayerService.pauseSong();
                    playPauseButton.setText(R.string.play_button);
                } else {
                    mediaPlayerService.playSong();
                    playPauseButton.setText(R.string.pause_button);
                }
                updateUI();
            }
        });

        stopButton.setOnClickListener(v -> {
            if (isBound && mediaPlayerService != null) {
                mediaPlayerService.stopSong();
                playPauseButton.setText(R.string.play_button);
                updateUI();
            }
        });

        prevButton.setOnClickListener(v -> {
            if (isBound && mediaPlayerService != null) {
                mediaPlayerService.skipPrevious();
                updateUI();
            }
        });

        nextButton.setOnClickListener(v -> {
            if (isBound && mediaPlayerService != null) {
                mediaPlayerService.skipNext();
                updateUI();
            }
        });

        loopToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBound && mediaPlayerService != null) {
                mediaPlayerService.setLooping(isChecked);
            }
        });

        shuffleToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBound && mediaPlayerService != null) {
                mediaPlayerService.setShuffling(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isBound && mediaPlayerService != null) {
                    mediaPlayerService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateUI()
    {
        if (isBound && mediaPlayerService != null)
        {
            songTitle.setText(mediaPlayerService.getCurrentSongTitle());
            playPauseButton.setText(mediaPlayerService.isPlaying() ? R.string.pause_button : R.string.play_button);
            seekBar.setMax(mediaPlayerService.getDuration());
            Bitmap albumArtBitmap = mediaPlayerService.getCurrentAlbumArt();
            if (albumArtBitmap != null) {
                albumArt.setImageBitmap(albumArtBitmap);
            } else {
                albumArt.setImageResource(R.drawable.ic_music); // Fallback
            }
            handler.post(updateSeekBar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        handler.removeCallbacks(updateSeekBar);
    }
}