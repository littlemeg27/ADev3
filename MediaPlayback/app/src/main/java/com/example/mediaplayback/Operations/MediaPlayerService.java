package com.example.mediaplayback.Operations;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.mediaplayback.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Brenna Pavlinchak
// AD3 - C202504
// MediaPlayerService

public class MediaPlayerService extends Service
{
    private static final String TAG = "MediaPlayerService";
    private final IBinder binder = new MediaPlayerBinder();
    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> songList;
    private int currentSongIndex = 0;
    private boolean isLooping = false;
    private boolean isShuffling = false;
    private static final String CHANNEL_ID = "MediaPlaybackChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final Map<Integer, String> SONG_TITLES = new HashMap<>();

    static
    {
        SONG_TITLES.put(R.raw.song1, "Song 1");
        SONG_TITLES.put(R.raw.song2, "Song 2");
        SONG_TITLES.put(R.raw.song3, "Song 3");
    }

    public class MediaPlayerBinder extends Binder
    {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        songList = new ArrayList<>();
        try
        {
            songList.add(R.raw.song1);
            songList.add(R.raw.song2);
            songList.add(R.raw.song3);
        } catch (Exception e)
        {
            Log.e(TAG, "Failed to initialize song list; ensure MP3 files exist in res/raw/", e);
        }
        if (songList.isEmpty())
        {
            Log.w(TAG, "Song list is empty; add MP3 files to res/raw/");
        }
        createNotificationChannel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    public void playSong()
    {
        if (songList.isEmpty())
        {
            Log.e(TAG, "Song list is empty; cannot play");
            return;
        }
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex));

        if (mediaPlayer != null)
        {
            mediaPlayer.setOnCompletionListener(mp ->
            {
                if (!isLooping)
                {
                    skipNext();
                }
                else
                {
                    playSong();
                }
            });
            mediaPlayer.setLooping(isLooping);
            mediaPlayer.start();
            updateNotification();
        }
        else
        {
            Log.e(TAG, "Failed to create MediaPlayer for song index: " + currentSongIndex);
        }
    }

    public void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updateNotification();
        }
    }

    public void stopSong()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            updateNotification();
        }
    }

    public void skipNext()
    {
        if (songList.isEmpty())
        {
            Log.e(TAG, "Song list is empty; cannot skip next");
            return;
        }
        if (isShuffling)
        {
            Collections.shuffle(songList);
        }
        currentSongIndex = (currentSongIndex + 1) % songList.size();
        playSong();
    }

    public void skipPrevious()
    {
        if (songList.isEmpty())
        {
            Log.e(TAG, "Song list is empty; cannot skip previous");
            return;
        }
        currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
        playSong();
    }

    public void setLooping(boolean looping)
    {
        isLooping = looping;
        if (mediaPlayer != null)
        {
            mediaPlayer.setLooping(looping);
        }
    }

    public void setShuffling(boolean shuffling)
    {
        isShuffling = shuffling;
    }

    public int getCurrentPosition()
    {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration()
    {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    public void seekTo(int position)
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.seekTo(position);
        }
    }

    public boolean isPlaying()
    {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public String getCurrentSongTitle()
    {
        if (songList.isEmpty() || currentSongIndex >= songList.size())
        {
            return "No Song";
        }
        Integer songId = songList.get(currentSongIndex);
        return SONG_TITLES.getOrDefault(songId, "Unknown");
    }

    public Bitmap getCurrentAlbumArt()
    {
        if (songList.isEmpty() || currentSongIndex >= songList.size())
        {
            return null;
        }
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever())
        {
            String uri = "android.resource://" + getPackageName() + "/" + songList.get(currentSongIndex);
            retriever.setDataSource(this, android.net.Uri.parse(uri));
            byte[] art = retriever.getEmbeddedPicture();

            if (art != null)
            {
                return BitmapFactory.decodeByteArray(art, 0, art.length);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error retrieving album art", e);
        }
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopForeground(true);

        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Media Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);

            if (manager != null)
            {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void updateNotification()
    {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(getCurrentSongTitle())
                .setContentText(isPlaying() ? "Playing" : "Paused")
                .setLargeIcon(getCurrentAlbumArt())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new NotificationCompat.BigPictureStyle());

        Intent playPauseIntent = new Intent(this, MediaPlayerService.class).setAction("PLAY_PAUSE");
        PendingIntent playPausePendingIntent = PendingIntent.getService(
                this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.addAction(new NotificationCompat.Action(
                isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play,
                isPlaying() ? "Pause" : "Play",
                playPausePendingIntent
        ));

        Intent skipNextIntent = new Intent(this, MediaPlayerService.class).setAction("SKIP_NEXT");
        PendingIntent skipNextPendingIntent = PendingIntent.getService(
                this, 0, skipNextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.addAction(new NotificationCompat.Action(
                R.drawable.ic_skip_next,
                "Next",
                skipNextPendingIntent
        ));

        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent != null && intent.getAction() != null)
        {
            switch (intent.getAction())
            {
                case "PLAY_PAUSE":
                    if (isPlaying())
                    {
                        pauseSong();
                    }
                    else
                    {
                        playSong();
                    }
                    break;
                case "SKIP_NEXT":
                    skipNext();
                    break;
            }
        }
        return START_STICKY;
    }
}