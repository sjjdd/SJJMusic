package company.sunjunjie.come.sjjmusicplayer60;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by sunjunjie on 2017/12/21.
 */

public class PlayedMusicAdapter extends ArrayAdapter<Music> {
    private int resourdceId;
     ViewHolder holder = null;
    private int getposition=0;
    SJJMusicService sjjmusicService;
    private SJJMusicService.MyBinder musicbinder;
    private LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
    //活动与服务之间绑定
    private boolean ifliked(Music music){
        List<likedMusic> likedMusics=DataSupport.findAll(likedMusic.class);
        for(int i=0;i<likedMusics.size();i++){
            if(music.getMusic_id()==likedMusics.get(i).getMusic_id())
                return true;
        }
        return false;
    }
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

    public PlayedMusicAdapter(Context context, int textViewResourced, List<Music> objects){
        super(context,textViewResourced,objects);
        this.mInflater = LayoutInflater.from(context);
        resourdceId=textViewResourced;
    }
    private LayoutInflater mInflater;
    public View getView(final int position, View convertView, ViewGroup parent){
        /*Music music=getItem(position);//获取当前的Student实例
        View view= LayoutInflater.from(getContext()).inflate(resourdceId,parent,false);
        // List<Student> students= DataSupport.findAll(Student.class);
        TextView music_name=(TextView) view.findViewById(R.id.music_name);
        TextView music_singer_name=(TextView) view.findViewById(R.id.music_singer_name);
        CircleImageView music_image=(CircleImageView) view.findViewById(R.id.song_image);
        music_image.setImageResource(music.getMusic_image());
        music_name.setText(music.getMusic_name().toString());
        music_singer_name.setText(String.valueOf(music.getMusic_singer_name()));

        return view;*/


        Music music=getItem(position);
        if (convertView == null) {

            holder=new ViewHolder();

            //可以理解为从vlist获取view 之后把view返回给ListView
            convertView = mInflater.inflate(R.layout.activity_sjjmain_item, null);
            holder.music_name = (TextView)convertView.findViewById(R.id.music_name);
            holder.music_singer_name = (TextView)convertView.findViewById(R.id.music_singer_name);
            holder.music_image = (CircleImageView) convertView.findViewById(R.id.song_image);
            holder.favorite=(Button) convertView.findViewById(R.id.favorite);
            holder.delete=(Button) convertView.findViewById(R.id.music_delete);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.music_name.setText(music.getMusic_name());
        holder.music_singer_name.setText(music.getMusic_singer_name());
        holder.music_image.setImageResource(music.getMusic_image());
        final Button favorite1=holder.favorite;
        if(ifliked(music))
        {
            favorite1.setBackgroundResource(R.drawable.havelike);
        }
        else {
            favorite1.setBackgroundResource(R.drawable.like);
        }
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            Music music = getItem(position);
            @Override
            public void onClick(View v) {
                List<likedMusic> likedMusics = DataSupport.where("music_id=?", String.valueOf(music.getMusic_id())).find(likedMusic.class);
                if (likedMusics.size() > 0) {//若存在于喜欢的
                    DataSupport.deleteAll(likedMusic.class, "music_id=?", String.valueOf(music.getMusic_id()));
                    favorite1.setBackgroundResource(R.drawable.like);
                    Toast.makeText(getContext(), "取消喜欢" + position, Toast.LENGTH_SHORT).show();
                } else {
                    likedMusic likedMusic = new likedMusic();
                    likedMusic.setMusic_id(music.getMusic_id());
                    likedMusic.save();
                    favorite1.setBackgroundResource(R.drawable.havelike);
                    Toast.makeText(getContext(), "添加喜欢" + position, Toast.LENGTH_SHORT).show();
                }
            }

        });

        holder.delete.setOnClickListener(new MyListener(position));
        Intent getMusicServiceintent = new Intent(getContext(), SJJMusicService.class);
        getContext().bindService(getMusicServiceintent,getmusicService,BIND_AUTO_CREATE);
        convertView.setSelected(true);
        // getposition=position;
        return convertView;

    }
    private class MyListener implements View.OnClickListener {
        int mPosition;

        public MyListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
     /* TODO Auto-generated method stub */
            switch (v.getId()) {
//                case R.id.favorite:
//                    Toast.makeText(getContext(), "点击喜欢按钮" + String.valueOf(mPosition), Toast.LENGTH_SHORT).show();
//                    Music music = getItem(mPosition);
//
//                    //先判断当前点击的歌曲是否存在于喜欢歌曲的表中
//                    List<likedMusic> likedMusics = DataSupport.where("music_id=?", String.valueOf(music.getMusic_id())).find(likedMusic.class);
//                    if (likedMusics.size() > 0) {//若存在于喜欢的
//                        DataSupport.deleteAll(likedMusic.class, "music_id=?", String.valueOf(music.getMusic_id()));
//                        Toast.makeText(getContext(), "取消喜欢" + music.getMusic_name(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        likedMusic likedMusic = new likedMusic();
//                        likedMusic.setMusic_id(music.getMusic_id());
//                        likedMusic.save();
//                        Toast.makeText(getContext(), "添加喜欢" + music.getMusic_name(), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
                case R.id.music_delete:
                    Music music1=getItem(mPosition);
                    DataSupport.deleteAll(PlayedMusic.class,"music_id=?",String.valueOf(music1.getMusic_id()));
                    Toast.makeText(getContext(), "删除歌曲"+music1.getMusic_name(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent("company.sunjunjie.come.sjjmusicplayer60.PlAY_MODE");
                    intent.putExtra("type",1);
                    intent.putExtra("position",music1.getMusic_id());
                    localBroadcastManager.sendBroadcast(intent);
                    break;
                    default:
                        break;
            }
        }
    }
    //提取出来方便点
    public final class ViewHolder {
        public TextView music_name;
        public TextView music_singer_name;
        public CircleImageView music_image;
        public Button favorite;
        public Button delete;
    }

}
