package company.sunjunjie.come.sjjmusicplayer60.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import company.sunjunjie.come.sjjmusicplayer60.Activity.MyLockScreenActivity;

public class LockService extends Service {
    private String TAG = this.getClass().getSimpleName();
    private LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
    private IntentFilter intentFilter;
    private LockReceiver lockReceriver;
    private String showsongname,showsingername;
    private int playorpause;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        intentFilter=new IntentFilter();
        intentFilter.addAction("company.sunjunjie.come.sjjmusicplayer60.UPDATE_LOCK_UI");
        lockReceriver=new LockReceiver();
        localBroadcastManager.registerReceiver(lockReceriver,intentFilter);

        IntentFilter mScreenOnFilter = new IntentFilter();
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_ON);
        LockService.this.registerReceiver(mScreenActionReceiver, mScreenOnFilter);

    }

    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mScreenActionReceiver);
        // 在此重新启动,使服务常驻内存
        startService(new Intent(this, LockService.class));
    }


    private BroadcastReceiver mScreenActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Intent LockIntent = new Intent(LockService.this,MyLockScreenActivity.class);
                LockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LockIntent.putExtra("showsongname_lock",showsongname);
                LockIntent.putExtra("showsingername_lock",showsingername);
                LockIntent.putExtra("playorpause_lock",playorpause);
                startActivity(LockIntent);
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e(TAG, "screen off");
            }
        }
    };

    class LockReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            showsongname=intent.getStringExtra("showsongname");
            showsingername=intent.getStringExtra("showsingername");
            playorpause=intent.getIntExtra("playorpause",-1);
        }
    }
}
