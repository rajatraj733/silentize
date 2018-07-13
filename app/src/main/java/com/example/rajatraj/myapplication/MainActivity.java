package com.example.rajatraj.myapplication;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final int streams[] = {
                AudioManager.STREAM_ALARM,
                AudioManager.STREAM_RING,
                AudioManager.STREAM_MUSIC
        };
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button presetButton = (Button) findViewById(R.id.presetButton);
        presetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int stream: streams) {
                    setPresetValues(sharedPref, audioManager);
                }

            }
        });

        ((Button) findViewById(R.id.minimizeVolButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ringerVol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
                int audioVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int alarmVol = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                storeValues(sharedPref, ringerVol, audioVol, alarmVol);
                for(int stream: streams) {
                    setMinVolumne(audioManager, stream);
                }
            }
        });
//        fab.setOnClickListener(new OnButtonClickListner());

    }

    public void setPresetValues(SharedPreferences sharedPreferences, AudioManager audioManager) {
        int ringVol = sharedPreferences.getInt(getString(R.string.ring_volume), -1);
        if(ringVol == -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to minimize the volume, first");
            builder.create().show();
        } else {
            int alarmVol = sharedPreferences.getInt(getString(R.string.alarm_volume), 0);
            int musicVol = sharedPreferences.getInt(getString(R.string.music_volume), 0);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmVol, 0);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, musicVol, 0);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, ringVol, 0);

        }
    }

    public void setMaxVolume(AudioManager audioManager, int stream) {
        int maxVolume = audioManager.getStreamMaxVolume(stream);
        audioManager.setStreamVolume(stream, maxVolume, 0);
    }
    public void setMinVolumne(AudioManager audioManager, int stream) {
        audioManager.setStreamVolume(stream, 0, 0);
    }
    public void storeValues(SharedPreferences sharedPreferences, int ringerVol, int musicVol, int alarmVol) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.ring_volume), ringerVol);
        editor.putInt(getString(R.string.music_volume), musicVol);
        editor.putInt(getString(R.string.alarm_volume), alarmVol);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class OnButtonClickListner implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}
