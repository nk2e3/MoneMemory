package com.monemobility.monememory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {


    MediaPlayer mediaPlayer;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("musicPause")){
                mediaPlayer.pause();
            }
            else if(action.equals("musicPlay")){
                mediaPlayer.start();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mylog", "On Create Service");
        mediaPlayer = MediaPlayer.create(this, R.raw.power);

        IntentFilter filter = new IntentFilter();
        filter.addAction("musicPause");
        filter.addAction("musicPlay");
        registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
           // Log.d("mylog", "Starting playing");
            mediaPlayer.setLooping(true);
        //} else {

        //}


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
        mediaPlayer.release();
        unregisterReceiver(receiver);
    }
}