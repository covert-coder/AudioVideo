package com.example.audiovideo;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.CaseMap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity implements View .OnClickListener, SeekBar.OnSeekBarChangeListener {

    // UI components
    private VideoView myVideo;
    private Button playButton;
    private MediaController mMediaController;
    private Button mMusicPlay, mMusicPause;
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private AudioManager mVolumeControl;
    private SeekBar mProgressSeek;
    private Timer mProgressTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideo = findViewById(R.id.myVideoView);
        playButton = findViewById(R.id.btnPlayVideo);
        playButton.setOnClickListener(MainActivity.this);
        mMediaController = new MediaController(MainActivity.this);
        mMusicPause = findViewById(R.id.btnPauseMusic);
        mMusicPlay = findViewById(R.id.btnPlayMusic);
        mMusicPlay.setOnClickListener(MainActivity.this);
        mMusicPause.setOnClickListener(MainActivity.this);
        mMediaPlayer = MediaPlayer.create(this, R.raw.miningsound);
        mSeekBar = findViewById(R.id.seekBarVolume);
        mProgressSeek = findViewById(R.id.seekProgress);

        mVolumeControl = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxUserDeviceVolume = mVolumeControl.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int currentUserDeviceVolume = mVolumeControl.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSeekBar.setMax(maxUserDeviceVolume);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Toast.makeText(MainActivity.this, "volume = " + Integer.toString(progress),
                            Toast.LENGTH_LONG).show();
                    mVolumeControl.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mProgressSeek.setOnSeekBarChangeListener(MainActivity.this);

    }

    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()) {
            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mining);
                myVideo.setVideoURI(videoUri);
                myVideo.setMediaController(mMediaController);
                mMediaController.setAnchorView(myVideo);
                myVideo.start();
                break;

            case R.id.btnPlayMusic:
                mMediaPlayer.start();
                mProgressTimer = new Timer(); // assignment of timer variable

                break;

            case R.id.btnPauseMusic:
                mMediaPlayer.pause();
                break;
        }
    }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                Toast.makeText(MainActivity.this, "progress is = " + Integer.toString(progress),
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onStartTrackingTouch (SeekBar seekBar){

        }
        @Override
        public void onStopTrackingTouch (SeekBar seekBar){

        }
    }

