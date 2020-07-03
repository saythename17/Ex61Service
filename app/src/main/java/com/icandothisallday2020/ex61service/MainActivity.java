package com.icandothisallday2020.ex61service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //MediaPlayer mp;//9초이상 음악 플레이에 적합
    //SoundPool sp;//9초이내 효과음에 적합

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view) {
        //백그라운드에서 뮤직을 플레이하는 서비스를 시작
        Intent intent=new Intent(this,MusicService.class);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            startForegroundService(intent);
        //백그라운드작업을 위해 foreground 사용
        else startService(intent);

    }

    public void stop(View view) {
        //백그라운드에서 뮤직을 플레이하는 서비스 종료
        Intent intent=new Intent(this,MusicService.class);
        stopService(intent);
    }
}
