package com.example.mediaplayback.Operations;

import android.app.Notification;
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
import androidx.core.app.NotificationCompat;

import com.example.mediaplayback.R;

import java.util.ArrayList;
import java.util.Collections;

public class MediaPlayerService extends Service {
    private static final String TAG = "MediaPlayerService";
    private final IBinder binder = new MediaPlayerBinder();
    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> songList;
    private int currentSongIndex = 0;
    private boolean isLooping = false;
    private boolean isShuffling = false;
    private static final String CHANNEL_ID = "MediaPlaybackChannel";
    private static final int NOTIFICATION_ID = 1;

    public class MediaPlayerBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songList = new ArrayList<>();
        songList.add(R.raw.song1);
        songList.add(R.raw.song2);
        songList.add(R.raw.song3);
        createNotificationChannel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void playSong() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex));
        mediaPlayer.setOnCompletionListener(mp -> {
            if (!isLooping) {
                skipNext();
            } else {
                playSong();
            }
        });
        mediaPlayer.start();
        updateNotification();
    }

    public void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updateNotification();
        }
    }

    public void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            updateNotification();
        }
    }

    public void skipNext() {
        if (isShuffling) {
            Collections.shuffle(songList);
        }
        currentSongIndex = (currentSongIndex + 1) % songList.size();
        playSong();
    }

    public void skipPrevious() {
        currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
        playSong();
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
        }
    }

    public void setShuffling(boolean shuffling) {
        isShuffling = shuffling;
    }

    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public String getCurrentSongTitle() {
        int songId = songList.get(currentSongIndex);
        if (songId == R.raw.song1) {
            return "Song 1";
        } else if (songId == R.raw.song2) {
            return "Song 2";
        } else if (songId == R.raw.song3) {
            return "Song 3";
        } else {
            return "Unknown";
        }
    }

    public Bitmap getCurrentAlbumArt() {
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            String uri = "android.resource://" + getPackageName() + "/" + songList.get(currentSongIndex);
            retriever.setDataSource(this, android.net.Uri.parse(uri));
            byte[] art = retriever.getEmbeddedPicture();
            if (art != null) {
                return BitmapFactory.decodeByteArray(art, 0, art.length);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting album art", e);
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Media Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void updateNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(getCurrentSongTitle())
                .setContentText(isPlaying() ? "Playing" : "Paused")
                .setLargeIcon(getCurrentAlbumArt())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new NotificationCompat.MediaStyle());

        Intent playPauseIntent = new Intent(this, MediaPlayerService.class).setAction("PLAY_PAUSE");
        PendingIntent playPausePendingIntent = PendingIntent.getService(
                this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.addAction(new NotificationCompat.Action(
                isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play,
                isPlaying() ? "Pause" : "Play",
                playPausePendingIntent
        ));

        Intent skipNextIntent = new Intent(this, MediaPlayerService.class).setAction("SKIP_NEXT");
        PendingIntent skipNextPendingIntent = PendingIntent.getService(
                this, 0, skipNextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.addAction(new NotificationCompat.Action(
                R.drawable.ic_skip_next,
                "Next",
                skipNextPendingIntent
        ));

        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "PLAY_PAUSE":
                    if (isPlaying()) {
                        pauseSong();
                    } else {
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