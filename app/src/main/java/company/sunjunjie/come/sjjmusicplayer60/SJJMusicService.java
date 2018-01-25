package company.sunjunjie.come.sjjmusicplayer60;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class SJJMusicService extends Service {
    public MediaPlayer mediaPlayer;
    private LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
    public SJJMusicService() {
        mediaPlayer=new MediaPlayer();
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    //与活动通信
    private MyBinder myBinder=new MyBinder();
    public class MyBinder extends Binder {
        public SJJMusicService getService(){
            return SJJMusicService.this;
        }
    }

    public void play(String musicPath){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicPath);
            mediaPlayer.prepare();
            mediaPlayer.start();;
            mediaPlayer.seekTo(0);
            //监听音频播放完的代码，实现音频的播放模式切换
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                    intent.putExtra("type",0);
                    localBroadcastManager.sendBroadcast(intent);
                }
            });
            //发送歌的当前位置
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mediaPlayer.isPlaying()) {
                      //  mLrcView.updateTime(mMediaPlayer.getCurrentPosition());
                        Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
                        intent.putExtra("position",mediaPlayer.getCurrentPosition());
                        localBroadcastManager.sendBroadcast(intent);
                        //每隔100ms刷新一次
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        } else {
            mediaPlayer.start();
            //发送歌的当前位置
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mediaPlayer.isPlaying()) {
                        //  mLrcView.updateTime(mMediaPlayer.getCurrentPosition());
                        Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
                        intent.putExtra("position",mediaPlayer.getCurrentPosition());
                        localBroadcastManager.sendBroadcast(intent);
                        //每隔100ms刷新一次
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
