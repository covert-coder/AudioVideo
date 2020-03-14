package com.example.audiovideo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View .OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
    @Override
    public void onCompletion(MediaPlayer mp) {
        mProgressTimer.cancel();
    }

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

    // implemented onProgressChanged from MainActivity implementation, some controls of progress bar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            Toast.makeText(this,Integer.toString(progress), Toast.LENGTH_LONG).show();
            mMediaPlayer.seekTo(progress);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.pause();
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaPlayer.start();
    }

    // onCreate Method
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
        // mVolumeControl is used to control the music volume.  Video is soundless
        mVolumeControl = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxUserDeviceVolume = mVolumeControl.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSeekBar.setMax(maxUserDeviceVolume);
        // mProgressSeek is a seek bar that displays the audio progress relative to its end
        mProgressSeek.setOnSeekBarChangeListener(MainActivity.this);
        mProgressSeek.setMax(mMediaPlayer.getDuration());

        // anonymous onSeekBarChangeListener for volume control of sound/audio
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // if the user interacts with the seekbar for the volume
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
        mMediaPlayer.setOnCompletionListener(MainActivity.this);
    }

    @Override
    public void onClick(View buttonView) {
        // using a switch statement allows us to use the same onClick (implemented) on multiple
        // buttons
        // getId gets the id of the button that is pressed then the appropriate case is utilized
        switch (buttonView.getId()) {
            // play video button
            case R.id.btnPlayVideo:
                // parse, in this case creates a new object of type uniform resource identifier (uri)
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mining);
                myVideo.setVideoURI(videoUri); // sets the uri for the uers device to our uri
                myVideo.setMediaController(mMediaController); // sets the users media controller to our
                    // media controller
                mMediaController.setAnchorView(myVideo);// anchors the media controller to the video
                    // on the users screen (rewind, forward, pause, stop controls)
                myVideo.start(); // launches the media controller
                break; // exit the case

            // play audio button
            case R.id.btnPlayMusic:

                mMediaPlayer.start(); // starts the mediaPlayer so the sound can play
                // progress bar controls
                mProgressTimer = new Timer(); // assignment of timer variable

                // this timer task is resource intensive and is separated from other UI code
                // by being in this method
                // timer syncs progress bar with audio progress
                mProgressTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        // tie the seekbar to the music progress
                        mProgressSeek.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                }, 0, 1000); // delay is how long to start, period is refresh frequency
                break; // exit the case

            // pause audio button
            case R.id.btnPauseMusic:
                mMediaPlayer.pause(); // pauses player when button is pressed
                mProgressTimer.cancel(); // cancels the timer for progress bar when pause button is pressed
                break; // exit the case
        }
    }
}

