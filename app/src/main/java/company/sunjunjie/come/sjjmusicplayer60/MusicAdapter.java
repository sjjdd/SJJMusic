package company.sunjunjie.come.sjjmusicplayer60;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by sunjunjie on 2017/11/17.
 */

public class MusicAdapter extends ArrayAdapter<Music> {
    private int resourdceId;
    ViewHolder holder = null;

    private int getposition=0;
    public MusicAdapter(Context context, int textViewResourced, List<Music> objects){
        super(context,textViewResourced,objects);
        this.mInflater = LayoutInflater.from(context);
        resourdceId=textViewResourced;
    }
    private boolean ifliked(Music music){
        List<likedMusic> likedMusics=DataSupport.findAll(likedMusic.class);
        for(int i=0;i<likedMusics.size();i++){
            if(music.getMusic_id()==likedMusics.get(i).getMusic_id())
                return true;
        }
        return false;
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


        final Music music=getItem(position);
        if (convertView == null) {

      holder=new ViewHolder();

      //可以理解为从vlist获取view 之后把view返回给ListView

        convertView = mInflater.inflate(R.layout.activity_sjjnodelete_item, null);
      holder.music_name = (TextView)convertView.findViewById(R.id.music_name);
      holder.music_singer_name = (TextView)convertView.findViewById(R.id.music_singer_name);
      holder.music_image = (CircleImageView) convertView.findViewById(R.id.song_image);
      holder.favorite=(Button) convertView.findViewById(R.id.favorite);

      convertView.setTag(holder);
     }else {
      holder = (ViewHolder)convertView.getTag();
    }

     holder.music_name.setText(music.getMusic_name());
     holder.music_singer_name.setText(music.getMusic_singer_name());
     holder.music_image.setImageResource(music.getMusic_image());
     final Button favorite1=holder.favorite;
     //判断当前的音乐是否存在于数据库中，若存在，则显示已添加喜欢
        if(ifliked(music))
        {
            favorite1.setBackgroundResource(R.drawable.havelike);
        }
        else {
            favorite1.setBackgroundResource(R.drawable.like);
        }
        holder.favorite.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // Toast.makeText(getContext(), "点击喜欢按钮"+String.valueOf(position), Toast.LENGTH_SHORT).show();
             //先判断当前点击的歌曲是否存在于喜欢歌曲的表中
             List<likedMusic> likedMusics=DataSupport.where("music_id=?",String.valueOf(music.getMusic_id())).find(likedMusic.class);
             if(likedMusics.size()>0){//若存在于喜欢的
                 DataSupport.deleteAll(likedMusic.class,"music_id=?",String.valueOf(music.getMusic_id()));
                 favorite1.setBackgroundResource(R.drawable.like);
                 Toast.makeText(getContext(), "取消喜欢"+music.getMusic_name(), Toast.LENGTH_SHORT).show();
             }
             else{
                 likedMusic likedMusic=new likedMusic();
                 likedMusic.setMusic_id(music.getMusic_id());
                 likedMusic.save();
                 favorite1.setBackgroundResource(R.drawable.havelike);
                 Toast.makeText(getContext(), "添加喜欢"+music.getMusic_name(), Toast.LENGTH_SHORT).show();
             }
         }
     });
     convertView.setSelected(true);
    // getposition=position;
   return convertView;

    }

    //提取出来方便点
    public final class ViewHolder {
        public TextView music_name;
        public TextView music_singer_name;
        public CircleImageView music_image;
       public Button favorite;
    }
}
