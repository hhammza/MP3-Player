package com.progmeleon.musicplayer2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.Manifest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PlaySong extends AppCompatActivity {
    TextView songName;
    private ImageView play;
    private ImageView prev;
    private ImageView next;
    private List<AudioModel> songs;
    MediaPlayer mediaPlayer;
    String curSong;
    int position;
    MainActivity mainActivity;
    private boolean isInitialImage = true;
    SeekBar seekBar;
    Thread updateSeek;
    private Handler handler = new Handler();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        songName = findViewById(R.id.textView3);
        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        mainActivity = new MainActivity();
        seekBar = findViewById(R.id.seekBar2);

        accessData();

        File file = new File(songs.get(position).getPath());
        Uri uri = Uri.parse(file.toString());
        mediaPlayer = MediaPlayer.create(PlaySong.this, uri);
        Log.d("myTag",""+uri.toString());
        mediaPlayer.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitialImage) {
                    play.setImageResource(android.R.drawable.ic_media_play);
                    mediaPlayer.pause();
                    updateSeekBar();

                } else {
                    play.setImageResource(android.R.drawable.ic_media_pause);
                    mediaPlayer.start();
                    handler.removeCallbacks(updateSeekBarRunnable);
                }
                isInitialImage = !isInitialImage;
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position -= 1;
                }
                else{
                    position = songs.size()-1;
                }
                File file = new File(songs.get(position).getPath());
                Uri uri = Uri.parse(file.toString());
                mediaPlayer = MediaPlayer.create(PlaySong.this, uri);
                mediaPlayer.seekTo(0);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                play.setImageResource(android.R.drawable.ic_media_pause);
                seekBar.setMax(mediaPlayer.getDuration());
                curSong = songs.get(position).getTitle();
                songName.setText(curSong);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        loop();
                    }
                });
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position != songs.size()-1){
                    position += 1;
                }
                else{
                    position = 0;
                }
                File file = new File(songs.get(position).getPath());
                Uri uri = Uri.parse(file.toString());
                mediaPlayer = MediaPlayer.create(PlaySong.this, uri);
                mediaPlayer.seekTo(0);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                play.setImageResource(android.R.drawable.ic_media_pause);
                curSong = songs.get(position).getTitle();
                songName.setText(curSong);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        loop();
                    }
                });
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                loop();
            }
        });

        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });


    }
    Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };
    private void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(updateSeekBarRunnable, 500); // Update every second
        }
    }
    private void accessData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getSerializable(("songList"));
        curSong = intent.getStringExtra("currentSong");
        songName.setText(curSong);
        songName.setSelected(true);
        position = intent.getIntExtra("position",0);
    }
    public void loop(){
        play.setImageResource(android.R.drawable.ic_media_play);
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        File file = new File(songs.get(position).getPath());
//        Uri uri = Uri.parse(file.toString());
//        mediaPlayer = MediaPlayer.create(PlaySong.this, uri);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        updateSeekBar();
        play.setImageResource(android.R.drawable.ic_media_pause);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
//        mediaPlayer.release();
    }
}