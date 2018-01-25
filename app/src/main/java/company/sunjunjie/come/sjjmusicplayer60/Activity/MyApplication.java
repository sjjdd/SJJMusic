package company.sunjunjie.come.sjjmusicplayer60.Activity;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by sunjunjie on 2018/1/3.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();

        /**
         * 默认设置一直使用夜间模式
         *
         * 这里AppCompatDelegate.setDefaultNightMode()方法可以接受的参数值有4个：
         * MODE_NIGHT_NO. Always use the day (light) theme(一直应用日间(light)主题).
         * MODE_NIGHT_YES. Always use the night (dark) theme(一直使用夜间(dark)主题).
         * MODE_NIGHT_AUTO. Changes between day/night based on the time of day(根据当前时间在day/night主题间切换).
         * MODE_NIGHT_FOLLOW_SYSTEM(默认选项). This setting follows the system's setting, which is essentially MODE_NIGHT_NO(跟随系统，通常为MODE_NIGHT_NO).
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
}
