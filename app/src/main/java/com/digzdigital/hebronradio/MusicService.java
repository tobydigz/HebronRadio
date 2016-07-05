package com.digzdigital.hebronradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private static final int NOTIFY_ID = 1;
    private final IBinder musicBind = new MusicBinder();
    SharedPreferences mPrefs;
    private MediaPlayer player;
    private String songTitle = "Hebron FM Radio Stream";
    private boolean pausedon = false;
    private boolean avserv = true;
    private boolean streamOn = false;
    private Uri trackUri , trackUri2 ;
    private OnAudioFocusChangeListener focusChangeListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            switch (focusChange) {
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                    // Lower the volume while ducking.
                    player.setVolume(0.2f, 0.2f);
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                    pausePlayer();
                    break;
                case (AudioManager.AUDIOFOCUS_LOSS):
                    stpPly();
                    ComponentName component =
                            new ComponentName(MusicService.this,
                                    MediaControlReceiver.class);
                    am.unregisterMediaButtonEventReceiver(component);
                    break;
                case (AudioManager.AUDIOFOCUS_GAIN):
                    // Return the volume to normal and resume if paused.
                    player.setVolume(1f, 1f);
                    if (pausedon) {
                        player.start();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    private void sendCommand(String data) {
        Intent intent = new Intent("digz-event");
        // add data
        intent.putExtra("message", data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();


        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set music player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        /*player.setVolumeControlStream(AudioManager.STREAM_MUSIC);*/
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();

        if (!streamOn) {
            streamOn = true;
            playSong();
        } else {
            Toast.makeText(this, "Something's wrong", Toast.LENGTH_LONG).show();
            sendCommand("uncheck");
            streamOn = false;
        }

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.icon)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
    }

    public void playSong() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ComponentName component = new ComponentName(this, MediaControlReceiver.class);
        am.registerMediaButtonEventReceiver(component);
        ActivityMediaControlReceiver activityMediaControlReceiver = new ActivityMediaControlReceiver();
        IntentFilter filter = new IntentFilter(MediaControlReceiver.ACTION_MEDIA_BUTTON);
        registerReceiver(activityMediaControlReceiver, filter);

        final int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            if (!pausedon) {



                //play a song
                player.reset();
                if (!streamOn) {
                    Log.d("Digz","I'm Here");
                    Context mContext = this.getApplicationContext();
                    mPrefs = mContext.getSharedPreferences("hebronfm", 0);

                    trackUri = Uri.parse(mPrefs.getString("Link1", "http://10.0.3.21:8000/;stream.mp3"));
                    trackUri2 = Uri.parse(mPrefs.getString("Link2", "http://80.248.0.231:8000/;stream.mp3"));

                    Log.d("Digz", String.valueOf(trackUri));
                    Log.d("Digz", String.valueOf(trackUri2));
                } else {
                    trackUri2 = trackUri;
                }

                try {
                    player.setDataSource(getApplicationContext(), trackUri2);
                    Log.d("digz", "Try block still executing");
                } catch (Exception e) {
                    Log.d("digz", "Control handed over to catch block");
                    Log.e("MUSIC SERVICE", "Error setting data source", e);
                }


                player.prepareAsync();
            } else {
                go();
            }
        }
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
        pausedon = true;
    }

    public void stpPly() {
        player.stop();
        lsfoc();
        pausedon = false;
        avserv = false;

    }


    public void lsfoc() {
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(focusChangeListener);
    }

    public void go() {
        player.start();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public class ActivityMediaControlReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MediaControlReceiver.ACTION_MEDIA_BUTTON.equals(
                    intent.getAction())) {
                KeyEvent event =
                        (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                switch (event.getKeyCode()) {
                    case (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE):
                        if (player.isPlaying())
                            pausePlayer();
                        else
                            playSong();
                        break;
                    case (KeyEvent.KEYCODE_MEDIA_PLAY):
                        playSong();
                        break;
                    case (KeyEvent.KEYCODE_MEDIA_PAUSE):
                        pausePlayer();
                        break;
                    default:
                        break;
                }
            }


        }
    }

    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals
                    (intent.getAction())) {
                pausePlayer();
            }
        }
    }
}


