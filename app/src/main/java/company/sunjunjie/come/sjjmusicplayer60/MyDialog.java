package company.sunjunjie.come.sjjmusicplayer60;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sunjunjie on 2017/12/14.
 */

public class MyDialog extends Dialog {
    Context mContext;
      /**TextView*/
      private TextView tvTest;
      /**dialog点击回调*/
      private DialogClickCallBack  dialogClickCallBack;
      private List<PlayedMusic> PlayedmusicList=new ArrayList<>();
      private List<Music>  musicList=new ArrayList<>();
    public MyDialog(Context context){
        super(context,R.style.MyDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clearpan);
        /*tvTest = (TextView) findViewById(R.id.tv_test);
        tvTest.setOnClickListener(new OnTvClickListener());
        Button test_item = (Button) findViewById(R.id.test_item);
        test_item.setOnClickListener(new OnTvClickListener());*/
        TextView bottomPlayer=(TextView) findViewById(R.id.bottomPlayer);
        bottomPlayer.setOnClickListener(new OnTvClickListener());
    }
    private class OnTvClickListener implements View.OnClickListener{
@Override
        public void onClick(View arg0) {
          /**把点击的view Id传出去*/
           dialogClickCallBack.viewClick(R.id.played_music_list);
           dialogClickCallBack.viewClick(R.id.bottomPlayer);
          //退出dialog
         //cancel();
         }
      }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Activity activity=getOwnerActivity();

        PlayedmusicList = DataSupport.findAll(PlayedMusic.class);//查询已播放过的数据库中内容
        musicList=new ArrayList<>();//因为每点一次就要调用，为了显示一致先清空。
        musicList.clear();
        //这里保证了PlayedmusicList里的数据与当前显示一致,要保证将播放过的歌存放到Played的库中
        if (PlayedmusicList.size() > 0 ) {
            for (int i = 0; i < PlayedmusicList.size(); i++) {
                List<Music> music = DataSupport.where("music_id=?", String.valueOf(PlayedmusicList.get(i).getMusic_id())).find(Music.class);
                musicList.add(music.get(0));
            }
            PlayedMusicAdapter adapter = new PlayedMusicAdapter(mContext, R.layout.activity_sjjmain_item, musicList);
            ListView listView = (ListView) findViewById(R.id.played_music_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Music musicinlist=musicList.get(position);

                }
            });
        }
        else {
            PlayedMusicAdapter adapter = new PlayedMusicAdapter(mContext, R.layout.activity_sjjmain_item, musicList);
            ListView listView = (ListView) findViewById(R.id.played_music_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Music musicinlist=musicList.get(position);

                }
            });
        }

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.height=400;
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }

    //绑定回调
    public void setDialogClickCallBack(DialogClickCallBack callBack){
        this.dialogClickCallBack = callBack;
    }
}
