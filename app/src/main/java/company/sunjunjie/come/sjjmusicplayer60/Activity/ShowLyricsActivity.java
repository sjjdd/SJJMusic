package company.sunjunjie.come.sjjmusicplayer60.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.IOException;

import company.sunjunjie.come.sjjmusicplayer60.DefineItem.LrcView;
import company.sunjunjie.come.sjjmusicplayer60.R;

public class ShowLyricsActivity extends AppCompatActivity {
    private LrcView mLrcView;
    private LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
    private IntentFilter intentFilter;
    private UpdateLyricViewReceiver updateLyricViewReceiver;
    private   Button play_in_lyric,next_in_lyric;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lyrics);
        play_in_lyric=(Button) findViewById(R.id.play_in_lyric);
        next_in_lyric=(Button) findViewById(R.id.next_in_lyric);
        play_in_lyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                intent.putExtra("type",2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        next_in_lyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                intent.putExtra("type",3);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.littleback);
        }
        mLrcView = (LrcView) findViewById(R.id.lrc);
        try {
            if(getIntent().getStringExtra("lyricsPath")!=null)
            //加载歌词
            mLrcView.loadLrc(getIntent().getStringExtra("lyricsPath"));
        }catch (Exception e){
            e.printStackTrace();
        }
        intentFilter=new IntentFilter();
        intentFilter.addAction("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
        updateLyricViewReceiver=new UpdateLyricViewReceiver();
        localBroadcastManager.registerReceiver(updateLyricViewReceiver,intentFilter);
    }
    class UpdateLyricViewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //通过musicService传来音乐的播放位置
            if(intent.getStringExtra("LyricsPath")!=null)
            {
                try {
                    mLrcView.loadLrc(intent.getStringExtra("LyricsPath"));
                    mLrcView.updateTime(intent.getIntExtra("position",-1));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            mLrcView.updateTime(intent.getIntExtra("position",-1));
            if(intent.getIntExtra("playorpause",-1)==1){
                play_in_lyric.setBackgroundResource(R.drawable.playinlyric);
            }else {
                play_in_lyric.setBackgroundResource(R.drawable.pauseinlyric);
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar2,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                ShowLyricsActivity.this.finish();
             break;
            default:
                break;
        }
        return true;
    }
}
