package com.example.mediaplayback.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mediaplayback.Operations.MediaPlayerService;
import com.example.mediaplayback.R;

// Brenna Pavlinchak
// AD3 - C202504
// PlaybackFragment

public class PlaybackFragment extends Fragment
{
    private MediaPlayerService mediaPlayerService;
    private boolean isBound = false;
    private final Handler handler = new Handler();
    private Button playPauseButton;
    private Button stopButton;
    private Button prevButton;
    private Button nextButton;
    private CheckBox loopToggle;
    private CheckBox shuffleToggle;
    private SeekBar seekBar;
    private TextView songTitle;
    private ImageView albumArt;

    private final ServiceConnection connection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mediaPlayerService = binder.getService();
            isBound = true;
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            isBound = false;
        }
    };

    private final Runnable updateSeekBar = new Runnable()
    {
        @Override
        public void run()
        {
            if (isBound && mediaPlayerService != null)
            {
                seekBar.setProgress(mediaPlayerService.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_playback, container, false);

        playPauseButton = view.findViewById(R.id.playPauseButton);
        stopButton = view.findViewById(R.id.stopButton);
        prevButton = view.findViewById(R.id.prevButton);
        nextButton = view.findViewById(R.id.nextButton);
        loopToggle = view.findViewById(R.id.loopToggle);
        shuffleToggle = view.findViewById(R.id.shuffleToggle);
        seekBar = view.findViewById(R.id.seekBar);
        songTitle = view.findViewById(R.id.songTitle);
        albumArt = view.findViewById(R.id.albumArt);


        playPauseButton.setOnClickListener(v ->
        {
            if (isBound && mediaPlayerService != null)
            {
                if (mediaPlayerService.isPlaying())
                {
                    mediaPlayerService.pauseSong();
                    playPauseButton.setText(R.string.play_button);
                }
                else
                {
                    mediaPlayerService.playSong();
                    playPauseButton.setText(R.string.pause_button);
                }
                updateUI();
            }
        });

        stopButton.setOnClickListener(v ->
        {
            if (isBound && mediaPlayerService != null)
            {
                mediaPlayerService.stopSong();
                playPauseButton.setText(R.string.play_button);
                updateUI();
            }
        });

        prevButton.setOnClickListener(v ->
        {
            if (isBound && mediaPlayerService != null)
            {
                mediaPlayerService.skipPrevious();
                updateUI();
            }
        });

        nextButton.setOnClickListener(v ->
        {
            if (isBound && mediaPlayerService != null)
            {
                mediaPlayerService.skipNext();
                updateUI();
            }
        });

        loopToggle.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isBound && mediaPlayerService != null)
            {
                mediaPlayerService.setLooping(isChecked);
            }
        });

        shuffleToggle.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isBound && mediaPlayerService != null) {
                mediaPlayerService.setShuffling(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser && isBound && mediaPlayerService != null)
                {
                    mediaPlayerService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Bind to service
        Intent intent = new Intent(requireContext(), MediaPlayerService.class);
        requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        return view;
    }

    private void updateUI()
    {
        if (isBound && mediaPlayerService != null)
        {
            songTitle.setText(mediaPlayerService.getCurrentSongTitle());
            playPauseButton.setText(mediaPlayerService.isPlaying() ? R.string.pause_button : R.string.play_button);
            seekBar.setMax(mediaPlayerService.getDuration());
            Bitmap albumArtBitmap = mediaPlayerService.getCurrentAlbumArt();

            if (albumArtBitmap != null)
            {
                albumArt.setImageBitmap(albumArtBitmap);
            }
            else
            {
                albumArt.setImageResource(R.drawable.ic_music);
            }
            handler.post(updateSeekBar);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Intent intent = new Intent(requireContext(), MediaPlayerService.class);
        requireContext().startService(intent);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (isBound)
        {
            requireContext().unbindService(connection);
            isBound = false;
        }
        handler.removeCallbacks(updateSeekBar);
    }
}