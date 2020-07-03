package com.icandothisallday2020.ex61service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    MediaPlayer mp;

    @Override//startService()를 통해 서비스가 실행되면 자동실행되는 메소드
    public int onStartCommand(Intent intent, int flags, int startId) {
        //ver.Oreo~ 운영체제에서 백그라운드작업 및 브로드캐스트수신작업에 제약
        //(배터리 소모량을 줄이기 위함)
        //만약 백그라운드에서 계속 동작하고 싶다면→foregroundService()로 실행
        //사용자가 서비스가 가동중임을 인지할 수 있도록 Notification 설정 필수
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel=new NotificationChannel
                    ("music","MusicService",NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder=new NotificationCompat.Builder
                    (this,"music");
            //알림 설정
            builder.setSmallIcon(R.drawable.ic_stat_name);
            builder.setContentTitle("Music Service");
            builder.setContentText("Playing Music...");
            //알림창을 클릭했을때 뮤직제어화면(MainActivity)으로 전환되도록
            Intent i=new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            //기존 액티비티를 위로올리도록(중복 화면을 생성하지 않도록)
            PendingIntent pi=PendingIntent.getActivity
                    (this,17,i,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            builder.setAutoCancel(true);
            //└클릭하면 자동취소되야하지만 foregroundService 에서는 적용X 이기 때문에
            Notification notification=builder.build();
            startForeground(7,notification);//이 서비스를 포어그라운드에서 돌아가도록 명령
        }

        //미디어플레이어 객체 생성 및 시작
        if(mp==null) {
            mp=MediaPlayer.create(this,R.raw.gang);
            mp.setLooping(true);
            mp.setVolume(1.0f,1.0f);//0.0f~1.0f
        }
        mp.start();
        return START_STICKY;
        //└운영체제가 메모리 문제로 인해 서비스를 강제로 kill 했을때
        //메모리문제가 해결되면 자동으로 서비스를 실행--주로 다운로드시 설정
    }

    @Override//stopService()를 통해 서비스가 종료되면 자동실행"
    public void onDestroy() {
        if(mp!=null && mp.isPlaying()) {
            mp.stop();
            mp. release(); //MediaPlayer 를 메모리에서 삭제하도록
            mp=null;//※※※:GC에 의해 메모리에서 지워짐
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //start service 에선 사용X- bind Service()용
        return null;
    }
}
