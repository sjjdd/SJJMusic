package company.sunjunjie.come.sjjmusicplayer60.Activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import company.sunjunjie.come.sjjmusicplayer60.R;
import company.sunjunjie.come.sjjmusicplayer60.SJJMainActivity;
import company.sunjunjie.come.sjjmusicplayer60.SJJMusicService;
import company.sunjunjie.come.sjjmusicplayer60.Service.LockService;

public class MyLockScreenActivity extends AppCompatActivity {
   // private SJJMusicService sjjmusicService;
    private LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
    private IntentFilter intentFilter;
    private LockReceiver lockReceriver;
    private TextView showsongname,showsingername;
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    Button play;
   // private SJJMusicService.MyBinder musicbinder;
    //活动与服务之间绑定
   /* private ServiceConnection getmusicService=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicbinder=(SJJMusicService.MyBinder)service;
            sjjmusicService=musicbinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sjjmusicService=null;
        }
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        setContentView(R.layout.activity_my_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
       /* Intent getMusicServiceintent = new Intent(MyLockScreenActivity.this, SJJMusicService.class);
        bindService(getMusicServiceintent,getmusicService,BIND_AUTO_CREATE);*/
        startService(new Intent(this, LockService.class));
        Button lock=(Button) findViewById(R.id.close);
        play=(Button) findViewById(R.id.play);
        Button next=(Button) findViewById(R.id.next);
        showsongname=(TextView) findViewById(R.id.show_song_name);
        showsingername=(TextView) findViewById(R.id.show_singer_name);
        showsongname.setText(getIntent().getStringExtra("showsongname_lock"));
        showsingername.setText(getIntent().getStringExtra("showsingername_lock"));
        if(getIntent().getIntExtra("playorpause_lock",-1)==1){
            play.setBackgroundResource(R.drawable.pause);
        }else{
            play.setBackgroundResource(R.drawable.play);
        }
        intentFilter=new IntentFilter();
        intentFilter.addAction("company.sunjunjie.come.sjjmusicplayer60.UPDATE_LOCK_UI");
        lockReceriver=new LockReceiver();
        localBroadcastManager.registerReceiver(lockReceriver,intentFilter);
      /*  if(sjjmusicService.mediaPlayer!=null) {
            if (sjjmusicService.mediaPlayer.isPlaying()) {
                play.setBackgroundResource(R.drawable.play);
            } else {
                play.setBackgroundResource(R.drawable.pause);
            }
        }*/
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLockScreenActivity.this.finish();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                intent.putExtra("type",2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                intent.putExtra("type",3);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }
   class LockReceiver extends BroadcastReceiver{
       @Override
       public void onReceive(Context context, Intent intent) {
           showsongname.setText(intent.getStringExtra("showsongname"));
           showsingername.setText(intent.getStringExtra("showsingername"));
           if(intent.getIntExtra("playorpause",-1)==1){
               play.setBackgroundResource(R.drawable.pause);
           }else {
               play.setBackgroundResource(R.drawable.play);
           }
       }
   }
    public void onBackPressed() {
        // do nothing
    }
    public boolean onKeyDown( int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event. KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
