package company.sunjunjie.come.sjjmusicplayer60;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//欢迎界面
public class SplashActivity extends AppCompatActivity {
    private TextView welcome_title;
    private  List<Music> musicList;
    List<File> list=new ArrayList<File>();//定义一个list待会儿用于存放寻找的文件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        //设置为无标题栏
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
      // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       //z getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        //获取组件
        LinearLayout r1_splash=(LinearLayout) findViewById(R.id.r1_splash);
        welcome_title=(TextView) findViewById(R.id.welcome);
        //背景透明度变化5秒内从0.5变到1.0
        AlphaAnimation aa=new AlphaAnimation(0.5f,1.0f);
        aa.setDuration(5000);
        r1_splash.startAnimation(aa);
        //创建Timer对象
      /*  Connector.getDatabase();
        initMusic();*/
        Timer timer=new Timer();
        //创建TimerTask对象
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
              /*  Connector.getDatabase();
                initMusic();*/
                Intent intent=new Intent(SplashActivity.this,SJJMainActivity.class);
                startActivity(intent);
                finish();
            }

        };
        //使用timer.schedule()方法调用timerTak,定时5秒后执行run

        timer.schedule(timerTask,5000);
    }

}
