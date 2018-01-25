package company.sunjunjie.come.sjjmusicplayer60;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.ColorSpace;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import company.sunjunjie.come.sjjmusicplayer60.Activity.SJJOnlineActivity;
import company.sunjunjie.come.sjjmusicplayer60.Activity.SJJSettingActivity;
import company.sunjunjie.come.sjjmusicplayer60.Activity.ShowLyricsActivity;
import company.sunjunjie.come.sjjmusicplayer60.DefineItem.SJJSkinSettingManager;
import company.sunjunjie.come.sjjmusicplayer60.DefineItem.SetTimeStopDialog;
import company.sunjunjie.come.sjjmusicplayer60.Service.LockService;

public class SJJMainActivity extends AppCompatActivity implements View.OnClickListener,SJJMainFragment.OnFragmentInteractionListener,SJJMusicReposityFragment.OnFragmentInteractionListener
,LocalMusicFragment.OnFragmentInteractionListener,likedMusicFragment.OnFragmentInteractionListener,RecentPlayedFragment.OnFragmentInteractionListener{
    private SJJMusicService.MyBinder musicbinder;
    //活动与服务之间绑定
    private ServiceConnection getmusicService=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicbinder=(SJJMusicService.MyBinder)service;
            sjjmusicService=musicbinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sjjmusicService=null;
        }
    };
    //给广播标志位
    private static final int changemode=0;
    private static  final int deletesong=1;
    private static final  int play=2;
    private static  final int next=3;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SJJMainFragment sjjMainFragment;
    private  LocalMusicFragment localMusicFragment;
    private SJJMusicReposityFragment sjjMusicReposityFragment;
    private RecentPlayedFragment recentPlayedFragment;
    private likedMusicFragment likedMusicFragment;
    private Button play_btn,next_song_btn,current_playing_btn,play_mode_btn;
    private SJJMusicService sjjmusicService;
    private TextView showMusicName,showSingerName,exit_app_item,about_app_item,musicTime,musicTotal;
    private TextView set_time_stop_play_item,night_mode_item,settings_item;
    private  List<Music> musicList;
    private  CircleImageView imageOfSong;
    private  Toolbar toolbar;
    private SeekBar seekBar;
    private  String songpath;
    private DrawerLayout mDrawerLayout;
    private MyDialog myDialog;
    private SetTimeStopDialog setTimeStopDialog;
    private int currentId,modeflag=0;
    List<File> list=new ArrayList<File>();//定义一个list待会儿用于存放寻找的文件
    private List<PlayedMusic> PlayedmusicList=new ArrayList<>();
    //定义与广播有关的组件，用于实现循环，单曲还是顺序
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private Animation rotate;
    private  String locksongname,locksingername;
    String LyricsPath = null;
    private SJJSkinSettingManager mSettingManager;
    /*
    这个方法是activity和fragment通信的一种方法
    在MainFragment中调用这个方法,可以在activity中做出相应的反应
     */
    public void changetoMusicReposity(){
        sjjMusicReposityFragment=new SJJMusicReposityFragment();
        fragmentManager = getSupportFragmentManager();      //得到FragmentManager
        fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
        fragmentTransaction.replace(R.id.fragment_layout, sjjMusicReposityFragment).commit();
    }
    //跳转到喜欢音乐，在MainFragment中
    public void changetoLikedMusic(){
        likedMusicFragment=new likedMusicFragment();
        fragmentManager = getSupportFragmentManager();      //得到FragmentManager
        fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
        fragmentTransaction.replace(R.id.fragment_layout,likedMusicFragment).commit();
    }
    //跳转到最近播放音乐
    public void changetoRecentMusic(){
        recentPlayedFragment=new RecentPlayedFragment();
        fragmentManager = getSupportFragmentManager();      //得到FragmentManager
        fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
        fragmentTransaction.replace(R.id.fragment_layout,recentPlayedFragment).commit();

    }
    //跳转到本地音乐，在MainFragment中
    public void changetoLocalMusic()
    {
        localMusicFragment=new LocalMusicFragment();
        fragmentManager = getSupportFragmentManager();      //得到FragmentManager
        fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
        fragmentTransaction.replace(R.id.fragment_layout, localMusicFragment).commit();
    }
    public void initView(TextView amountOfLocal,TextView amountOfLiked,TextView amountOfRecent){
  //      List<Music> amount=DataSupport.findAll(Music.class);
        List<likedMusic> likedMusics=DataSupport.findAll(likedMusic.class);
        List<RecentPlayedMusic> recentPlayedMusics=DataSupport.findAll(RecentPlayedMusic.class);
        amountOfLocal.setText(String.valueOf(musicList.size()));
        amountOfLiked.setText(String.valueOf(likedMusics.size()));
        amountOfRecent.setText(String.valueOf(recentPlayedMusics.size()));
    }
 public  void onFragmentInteraction2(){

 }
 //在recentPlayed中有重复则返回false
 public boolean isInRecentPlayedMusic(Music music){
     List<RecentPlayedMusic> recentPlayedMusics=DataSupport.findAll(RecentPlayedMusic.class);
     if(recentPlayedMusics.size()>0) {
         for (int i = 0; i < recentPlayedMusics.size(); i++) {
             if (recentPlayedMusics.get(i).getMusic_id() == music.getMusic_id())
                 return false;
         }
     }
     return true;
 }
 public   void playMusic(Music music,int NowId){
      sjjmusicService.play(music.getMusic_path());
      currentId=NowId;//让currentId等于当前播放列表中正在播放的那首歌的id。
     // Toast.makeText(SJJMainActivity.this,"当前正在播放的id"+String.valueOf(currentId), Toast.LENGTH_SHORT).show();
     /* if(modeflag==0)//单曲循环
          sjjmusicService.mediaPlayer.setLooping(true);*/

      seekBar.setProgress(sjjmusicService.mediaPlayer.getCurrentPosition());
      seekBar.setMax(sjjmusicService.mediaPlayer.getDuration());
      showMusicName.setText(music.getMusic_name());
      showSingerName.setText(music.getMusic_singer_name());
      imageOfSong.setImageResource(music.getMusic_image());

      //通知锁屏界面更新歌名、歌手信息
     Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_LOCK_UI");
     locksingername=music.getMusic_singer_name();
     locksongname=music.getMusic_name();
     intent.putExtra("showsongname",locksongname);
     intent.putExtra("showsingername",locksingername);
     localBroadcastManager.sendBroadcast(intent);

      //音乐播放图片旋转
     rotate = AnimationUtils.loadAnimation(this, R.anim.tip);
     if(rotate!=null) {
         LinearInterpolator lin = new LinearInterpolator();//匀速
         rotate.setInterpolator(lin);
         rotate.setDuration(sjjmusicService.mediaPlayer.getDuration());
         imageOfSong.setAnimation(rotate);
     }
      play_btn.setBackgroundResource(R.drawable.play);
      //每次要播放的歌都按序存放到最近播放列表中，不重复添加
     List<RecentPlayedMusic> recentPlayedMusics=DataSupport.findAll(RecentPlayedMusic.class);
     if(recentPlayedMusics.size()==0){
         RecentPlayedMusic recentPlayedMusic=new RecentPlayedMusic();
         recentPlayedMusic.setRecent_id(0);//从0开始
         recentPlayedMusic.setMusic_id(music.getMusic_id());
         recentPlayedMusic.save();
     }
     else if(recentPlayedMusics.size()>0&&isInRecentPlayedMusic(music)) {
         RecentPlayedMusic recentPlayedMusic=new RecentPlayedMusic();
         recentPlayedMusic.setRecent_id(recentPlayedMusics.size() - 1);//从0开始
         recentPlayedMusic.setMusic_id(music.getMusic_id());
         recentPlayedMusic.save();
     }
     handler.post(runnable);
     LyricsPath=music.getMusic_lyrics_path();//获取歌词文件路径
     Intent intent2=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
     intent2.putExtra("LyricsPath",LyricsPath);
     localBroadcastManager.sendBroadcast(intent2);
  }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide();//隐藏工程名那个蓝条
        setContentView(R.layout.activity_sjjmain);
        Intent getMusicServiceintent = new Intent(SJJMainActivity.this, SJJMusicService.class);
        bindService(getMusicServiceintent,getmusicService,BIND_AUTO_CREATE);
        startService(new Intent(this, LockService.class));
        //获取广播实例
        localBroadcastManager=LocalBroadcastManager.getInstance(SJJMainActivity.this);
        intentFilter=new IntentFilter();
        intentFilter.addAction("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
        play_btn=(Button)findViewById(R.id.play_button);
        next_song_btn=(Button)findViewById(R.id.next_song_button);
        current_playing_btn=(Button) findViewById(R.id.current_playing_button);
        play_mode_btn=(Button) findViewById(R.id.play_mode_button);
        showMusicName=(TextView) findViewById(R.id.music_info_textView);
        showSingerName=(TextView) findViewById(R.id.singer_info_textView);
        exit_app_item=(TextView) findViewById(R.id.exit_app);
        about_app_item=(TextView) findViewById(R.id.about_app);
        set_time_stop_play_item=(TextView) findViewById(R.id.set_time_stop_play);
        night_mode_item=(TextView) findViewById(R.id.night_mode);
        settings_item=(TextView) findViewById(R.id.setting);
        musicTime=(TextView) findViewById(R.id.musictime);
        musicTotal=(TextView) findViewById(R.id.musictotal);
        seekBar=(SeekBar) findViewById(R.id.process_bar);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        imageOfSong=(CircleImageView) findViewById(R.id.imageOfSong);

        //定义点击显示歌词的地方
        RelativeLayout showLyrics=(RelativeLayout) findViewById(R.id.showlyrics);
        showLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* List<PlayedMusic> now = DataSupport.where("played_id=?", String.valueOf(currentId )).find(PlayedMusic.class);
                if(now.size()>0) {
                    List<Music> nowMusic = DataSupport.where("music_id=?", String.valueOf(now.get(0).getMusic_id())).find(Music.class);
                    LyricsPath=getLyricsPath(nowMusic.get(0).getMusic_name());

                }
                else {
                    LyricsPath="";
                }*/
                Intent intent=new Intent(SJJMainActivity.this, ShowLyricsActivity.class);
                intent.putExtra("lyricsPath",LyricsPath);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.slider);
        }
        myDialog = new MyDialog(this);//弹出的已播放的歌曲
        setTimeStopDialog=new SetTimeStopDialog(this);//定时关闭
        //实现侧滑
       /* sm=(SlideMenu)findViewById(R.id.sm);
        findViewById(R.id.ib_back).setOnClickListener(this);*/
        exit_app_item.setOnClickListener(this);
        about_app_item.setOnClickListener(this);
        set_time_stop_play_item.setOnClickListener(this);
        night_mode_item.setOnClickListener(this);
        settings_item.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        next_song_btn.setOnClickListener(this);
        current_playing_btn.setOnClickListener(this);
        play_mode_btn.setOnClickListener(this);
        sjjMainFragment = new SJJMainFragment();           //创建了刚才定义的MainFragment实例
        fragmentManager = getSupportFragmentManager();      //得到FragmentManager
        fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
        fragmentTransaction.replace(R.id.fragment_layout, sjjMainFragment).commit();//将MainActivity里的布局模块fragment_layout替换为mainFragment*/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {//可以滑动那个进度条
                    sjjmusicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 初始化皮肤
        mSettingManager = new SJJSkinSettingManager(this);
        mSettingManager.initSkins();
        //创建数据库
        Connector.getDatabase();
        initMusic();
    }
   public void changeToMain(){
       sjjMainFragment = new SJJMainFragment();           //创建了刚才定义的MainFragment实例
       fragmentManager = getSupportFragmentManager();      //得到FragmentManager
       fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
       fragmentTransaction.replace(R.id.fragment_layout, sjjMainFragment).commit();//将MainActivity里的布局模块fragment_layout替换为mainFragment

   }
   public void play(){
     //  Toast.makeText(this, "sjj", Toast.LENGTH_SHORT).show();
       if(sjjmusicService.mediaPlayer.isPlaying()){
           play_btn.setBackgroundResource(R.drawable.pause);
           //通知锁屏界面播放暂停按钮
           Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_LOCK_UI");
           intent.putExtra("showsongname",locksongname);
           intent.putExtra("showsingername",locksingername);
           intent.putExtra("playorpause",1);
           localBroadcastManager.sendBroadcast(intent);
           Intent intent2=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
           intent2.putExtra("playorpause",1);
           localBroadcastManager.sendBroadcast(intent2);
           imageOfSong.clearAnimation();
       }else {
           play_btn.setBackgroundResource(R.drawable.play);
           //通知锁屏界面更新歌名、歌手信息
           Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_LOCK_UI");
           intent.putExtra("showsongname",locksongname);
           intent.putExtra("showsingername",locksingername);
           intent.putExtra("playorpause",0);
           localBroadcastManager.sendBroadcast(intent);
           Intent intent2=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
           intent2.putExtra("LyricsPath",LyricsPath);
           intent2.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
           localBroadcastManager.sendBroadcast(intent2);
           //音乐播放图片旋转
           rotate = AnimationUtils.loadAnimation(this, R.anim.tip);
           if(rotate!=null) {
               LinearInterpolator lin = new LinearInterpolator();//匀速
               rotate.setInterpolator(lin);
               rotate.setDuration(sjjmusicService.mediaPlayer.getDuration());
               imageOfSong.setAnimation(rotate);
           }
       }
       sjjmusicService.playOrPause();

   }

   public void next(){
       List<PlayedMusic> totalPlayed=DataSupport.findAll(PlayedMusic.class);
       if(currentId+1<totalPlayed.size()){
           currentId=currentId+1;
       }else {
           currentId=0;
       }
       List<PlayedMusic> next = DataSupport.where("played_id=?", String.valueOf(currentId )).find(PlayedMusic.class);
       if(next.size()>0) {
           List<Music> nextMusic = DataSupport.where("music_id=?", String.valueOf(next.get(0).getMusic_id())).find(Music.class);
           playMusic(nextMusic.get(0),currentId);
         //  Toast.makeText(this, "hhhh", Toast.LENGTH_SHORT).show();
       }
       Intent intent2=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
       intent2.putExtra("LyricsPath",LyricsPath);
       intent2.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
       localBroadcastManager.sendBroadcast(intent2);
   }
    @Override
    //对底部播放栏进行操作
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.play_button:
            play();
             break;
         case R.id.next_song_button:
            next();
             /*sjjmusicService.play(nextMusic.get(0).getMusic_path());
             seekBar.setProgress(sjjmusicService.mediaPlayer.getCurrentPosition());
             seekBar.setMax(sjjmusicService.mediaPlayer.getDuration());
             showMusicName.setText(nextMusic.get(0).getMusic_name());
             showSingerName.setText(nextMusic.get(0).getMusic_singer_name());
             play_btn.setBackgroundResource(R.drawable.play);
             handler.post(runnable);*/

             break;
         case R.id.exit_app:
             AlertDialog.Builder dialog=new AlertDialog.Builder(SJJMainActivity.this);
             dialog.setTitle("退出程序");
             dialog.setMessage("小主，您真的要退出本程序吗？");
             dialog.setCancelable(false);
             dialog.setPositiveButton("当然喽", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     System.exit(0);
                 }
             });
             dialog.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
                 }
             });
             dialog.show();
             break;
         case R.id.about_app:
             Intent intent=new Intent(SJJMainActivity.this,SJJAboutAppActivity.class);
             startActivity(intent);
             break;
         case R.id.set_time_stop_play://定时关闭功能
             setTimeStopDialog.setDialogClickCallBack(new DialogClickListener());
             setTimeStopDialog.show();
             RelativeLayout set_1_stop=(RelativeLayout) setTimeStopDialog.findViewById(R.id.set_1_stop);
             RelativeLayout set_10_stop=(RelativeLayout) setTimeStopDialog.findViewById(R.id.set_10_stop);
             RelativeLayout set_15_stop=(RelativeLayout) setTimeStopDialog.findViewById(R.id.set_15_stop);
             set_1_stop.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                   SetTimeToExit(1);
                 }
             });
             set_10_stop.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     SetTimeToExit(10);
                 }
             });
             set_15_stop.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     SetTimeToExit(15);
                 }
             });
             break;
         case R.id.night_mode:
             if(!sjjmusicService.mediaPlayer.isPlaying()) {
                 int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                 if (mode == Configuration.UI_MODE_NIGHT_YES) {
                     getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                 } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
                     getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                 }

                 recreate();
             }
             else {
                 Toast.makeText(this, "需要先关闭音乐播放再切换夜间模式", Toast.LENGTH_SHORT).show();
             }
             break;
         case R.id.setting:
             Toast.makeText(this, "功能开发中", Toast.LENGTH_SHORT).show();
             Intent intent1=new Intent(SJJMainActivity.this, SJJSettingActivity.class);
             startActivity(intent1);
             break;
         case R.id.current_playing_button:
             myDialog.show();
             myDialog.setDialogClickCallBack(new DialogClickListener());
             final List<PlayedMusic> playedList=DataSupport.findAll(PlayedMusic.class);
             final ListView playedmusic= (ListView) myDialog.findViewById(R.id.played_music_list);
             playedmusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     final PlayedMusic music=playedList.get(position);
                     final List<Music> musicInMusic=DataSupport.where("music_id=?",String.valueOf(music.getMusic_id())).find(Music.class);
                     /*showMusicName.setText(musicInMusic.get(0).getMusic_name());
                     showSingerName.setText(musicInMusic.get(0).getMusic_singer_name());
                     sjjmusicService.play(musicInMusic.get(0).getMusic_path());*/
                   /*  Button delete=(Button) view.findViewById(R.id.music_delete);
                     delete.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             DataSupport.deleteAll(PlayedMusic.class,"music_id=?",String.valueOf(music.getMusic_id()));
                             Toast.makeText(SJJMainActivity.this, "删除歌曲"+musicInMusic.get(0).getMusic_name(), Toast.LENGTH_SHORT).show();
                             if(sjjmusicService.mediaPlayer!=null)
                             sjjmusicService.mediaPlayer.stop();
                         }
                     });*/
                     currentId=(int)music.getPlayed_id();//让currentId等于当前播放列表中正在播放的那首歌的id。
                     if(musicInMusic.size()>0)
                     playMusic(musicInMusic.get(0),currentId);
                 }
             });
             break;
         case R.id.play_mode_button:
             //利用modeflag做0-2的循环代表三种模式0：单曲，1：顺序2：随机
             if(modeflag<3)
                 modeflag++;
             else
                 modeflag=0;
             if(modeflag==0){
                 play_mode_btn.setBackgroundResource(R.drawable.singlecycle);
                // sjjmusicService.mediaPlayer.setLooping(true);
                 Toast.makeText(SJJMainActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
             }else if(modeflag==1)
             {
                 play_mode_btn.setBackgroundResource(R.drawable.orderplay);
                 //sjjmusicService.mediaPlayer.setLooping(false);
                 //orderPlay();
                 Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
             }else{
                 play_mode_btn.setBackgroundResource(R.drawable.randomplay);
                // sjjmusicService.mediaPlayer.setLooping(false);
                 Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
             }
             break;
         default:
                 break;
     }

    }
    public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.toolbar,menu);
       return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ib_search:
               // Toast.makeText(sjjmusicService, "sjj", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SJJMainActivity.this, SJJOnlineActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
        return true;
    }
  //本函数用于定时关闭音乐播放，是暂停，接收的参数为分钟
    public  void SetTimeToExit(int minute){
        Toast.makeText(SJJMainActivity.this, String.valueOf(minute)+"分钟后关闭音乐播放", Toast.LENGTH_SHORT).show();
        Timer timer=new Timer();
        //创建TimerTask对象
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
              /*  Connector.getDatabase();
                initMusic();*/
                sjjmusicService.mediaPlayer.pause();
                Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_LOCK_UI");
                intent.putExtra("showsongname",locksongname);
                intent.putExtra("showsingername",locksingername);
                intent.putExtra("playorpause",1);
                localBroadcastManager.sendBroadcast(intent);
                Intent intent2=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
                intent2.putExtra("playorpause",1);
                localBroadcastManager.sendBroadcast(intent2);
                Intent intent3=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                intent3.putExtra("timepause",1);
                localBroadcastManager.sendBroadcast(intent3);
            }

        };
        //使用timer.schedule()方法调用timerTak,定时minute分钟后执行run

        timer.schedule(timerTask,minute*60000);
    }
  //在指定目录下查找所有的.mp3和.m4a文件
    public void findAllLyrics(File file, List<File> list) {
        File[] subFiles = file.listFiles();
        if (subFiles != null)
            for (File subFile : subFiles) {
                if (subFile.isFile() && (subFile.getName().endsWith(".lrc")))
                    list.add(subFile);
                else if (subFile.isDirectory())//如果是目录
                    findAllLyrics(subFile, list); //递归
            }
    }
    //寻找对应歌名的歌词文件
    public String getLyricsPath(String musicinformation){
        List<File> Lyricslist=new ArrayList<File>();
        File path=new File(Environment.getExternalStorageDirectory(),"./kuwoMusic/lyrics");//定义了要查找的文件目录
        findAllLyrics(path,Lyricslist);
        for(File file:Lyricslist){//根据歌名来找歌词文件
         //   Toast.makeText(this,file.getName(), Toast.LENGTH_SHORT).show();
            String musicinformation2=musicinformation.replaceAll(" ","");
            if((String.valueOf(file.getName()).indexOf(musicinformation2))!=-1){
                return file.getPath();
            }
        }
        return null;
    }

    //在指定目录下查找所有的.mp3和.m4a文件
    public void findAll(File file, List<File> list) {
        File[] subFiles = file.listFiles();
        if (subFiles != null)
            for (File subFile : subFiles) {
                if (subFile.isFile() && (subFile.getName().endsWith(".mp3")||subFile.getName().endsWith(".m4a")))
                    list.add(subFile);
                else if (subFile.isDirectory())//如果是目录
                    findAll(subFile, list); //递归
            }
    }

    //定义一个函数将上面file.getName分割成歌名以及歌手名
    public String[] divideFilename(String fileName){
       String[] divided=new String[2];
       if(fileName.indexOf("-")!=-1) {//文件名为“阿兰-千古”这样格式的
           divided = fileName.split("-");
           if (divided[1].endsWith(".mp3")) {
               divided[1] = divided[1].replace(".mp3", " ");//因为是临时对象需再赋值一次
           } else if (divided[1].endsWith(".m4a")) {
               divided[1] = divided[1].replace(".m4a", " ");
           }
       }else {
           divided[1]=fileName;divided[0]="未知";
           if (divided[1].endsWith(".mp3")) {
               divided[1] = divided[1].replace(".mp3", " ");//因为是临时对象需再赋值一次
           } else if (divided[1].endsWith(".m4a")) {
               divided[1] = divided[1].replace(".m4a", " ");
           }

       }
        return divided;
    }
    //定义一个函数用于查询数据库中的MUsic表，使得music_id不会重复创建
    public boolean NoRepeateMusicId(int id){
      musicList=DataSupport.findAll(Music.class);
        if(musicList.size()>1) {
          for (int i = 0; i < musicList.size(); i++) {
              if (id == musicList.get(i).getMusic_id()) {
                  return false;//有重复则不再添加
              }
          }
      }
        return true;
    }
    public void  initMusic(){
        // File file=new File(Environment.getExternalStorageDirectory(),"./kugoumusic/");//定义了要查找的文件目录
        findAll(Environment.getExternalStorageDirectory(),list);
        int i=0;
        for (File file1 : list) {
            String[] divideFileName=divideFilename(file1.getName());//按照那个“-”进行分割歌名和歌手名
            String lyricspath;
            if(file1.getName().endsWith(".mp3"))
            lyricspath=getLyricsPath(file1.getName().replaceAll(".mp3",""));
            else
            {
                lyricspath=getLyricsPath(file1.getName().replaceAll(".m4a",""));
            }
            Music music=new Music(i,R.drawable.shamo,divideFileName[1],divideFileName[0],file1.getPath(),lyricspath);
            if(NoRepeateMusicId(i)) {
                music.save();//将乐库歌单保存到数据库
            }
            i++;
        }
    }
    //  通过 Handler 更新 UI 上的组件状态
    private Handler handler=new Handler();
    public Runnable runnable=new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(sjjmusicService.mediaPlayer.getCurrentPosition());
            seekBar.setMax(sjjmusicService.mediaPlayer.getDuration());
            musicTime.setText(time.format(sjjmusicService.mediaPlayer.getCurrentPosition()));
            musicTotal.setText(time.format(sjjmusicService.mediaPlayer.getDuration()));
            handler.postDelayed(runnable, 200);
        }
    };
   //当前播放列表的操作
    private class DialogClickListener implements DialogClickCallBack {
        @Override
        public void viewClick(int viewId) {
            // TODO Auto-generated method stub 做一些你需要做的事情
           switch (viewId){
                case R.id.bottomPlayer:
                    myDialog.cancel();
                    break;
               case R.id.close_set_time:
                   setTimeStopDialog.cancel();
            }
        }
    }

    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", -1);
            switch (type) {
                case changemode://音乐放完了也要发一遍
                  //  Toast.makeText(context, String.valueOf(modeflag), Toast.LENGTH_SHORT).show();
                    Intent intent2=new Intent("company.sunjunjie.come.sjjmusicplayer60.UPDATE_Lyrics");
                    intent2.putExtra("LyricsPath",LyricsPath);
                    intent2.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    localBroadcastManager.sendBroadcast(intent2);
                switch (modeflag) {
                    case 0://单曲循环
                        sjjmusicService.mediaPlayer.start();
                        sjjmusicService.mediaPlayer.setLooping(true);

                        break;
                    case 1://顺序播放
                        sjjmusicService.mediaPlayer.start();
                        sjjmusicService.mediaPlayer.setLooping(false);
                        List<PlayedMusic> totalPlayed = DataSupport.findAll(PlayedMusic.class);
                        if (currentId + 1 < totalPlayed.size()) {
                            currentId = currentId + 1;
                        } else {
                            currentId = 0;
                        }
                        List<PlayedMusic> next = DataSupport.where("played_id=?", String.valueOf(currentId)).find(PlayedMusic.class);
                        List<Music> nextMusic = DataSupport.where("music_id=?", String.valueOf(next.get(0).getMusic_id())).find(Music.class);
                        if (nextMusic.size() > 0)
                            playMusic(nextMusic.get(0), currentId);
                        break;
                    case 2://随机播放
                        sjjmusicService.mediaPlayer.start();
                        sjjmusicService.mediaPlayer.setLooping(false);
                        List<PlayedMusic> totalPlayed2 = DataSupport.findAll(PlayedMusic.class);
                        currentId = (int) (Math.random() * (totalPlayed2.size()));//生成一个0到播放列表音乐总数量的随机数
                       // Toast.makeText(context, "随机数" + String.valueOf(currentId), Toast.LENGTH_SHORT).show();
                        List<PlayedMusic> randomMusic = DataSupport.where("played_id=?", String.valueOf(currentId)).find(PlayedMusic.class);
                        if (randomMusic.size() > 0) {
                            List<Music> randomMusicINmusic = DataSupport.where("music_id=?", String.valueOf(randomMusic.get(0).getMusic_id())).find(Music.class);
                            playMusic(randomMusicINmusic.get(0), currentId);
                        }
                        break;
                    default:
                        break;
                }break;
                case deletesong:
                     int getposition=intent.getIntExtra("position",-1);
                     if(getposition==currentId)
                     {
                         //删除的歌曲如果正好是当前播放的音乐的话自动播放之后的
                         if(sjjmusicService.mediaPlayer.isPlaying());
                         {
                             sjjmusicService.mediaPlayer.stop();
                             List<PlayedMusic> playedMusics = DataSupport.where("played_id >?", String.valueOf(currentId)).find(PlayedMusic.class);
                             List<Music> music = DataSupport.where("music_id=?", String.valueOf(playedMusics.get(0).getMusic_id())).find(Music.class);
                             currentId = playedMusics.get(0).getPlayed_id();
                             playMusic(music.get(0), currentId);
                         }
                     }
                     myDialog.cancel();
                    break;
                case play:
                    play();
                    break;
                case next:
                    next();
                    break;
                    default:
                        break;

            }
            if(intent.getIntExtra("changebackground",-1)>=0) {
                //换背景
                int changebackground = intent.getIntExtra("changebackground", -1);
                mSettingManager.changeSkin(changebackground);
            }
            if(intent.getIntExtra("timepause",-1)==1){
                play_btn.setBackgroundResource(R.drawable.pause);
            }
        }
    }
  /*  public void onDestroyView ()
    {
        try{
            sjjMainFragment = new SJJMainFragment();           //创建了刚才定义的MainFragment实例
            fragmentManager = getSupportFragmentManager();      //得到FragmentManager
            fragmentTransaction = fragmentManager.beginTransaction();   //得到fragmentTransaction,用于管理fragment的切换
            fragmentTransaction.remove(sjjMainFragment);
            fragmentTransaction.commit();

        }catch(Exception e){
        }

    }*/

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy(){
        super.onDestroy();
       // onDestroyView();
        handler.removeCallbacks(runnable);
        unbindService(getmusicService);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }
}
