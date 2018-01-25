package company.sunjunjie.come.sjjmusicplayer60;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalMusicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalMusicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Music music=new Music();
   // List<File> list=new ArrayList<File>();//定义一个list待会儿用于存放寻找的文件
    private ListView listView;
    private List<Music> musicList=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private TextView showMusicName,showSingerName;
    List<PlayedMusic> playedMusics;
    public LocalMusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalMusicFragment newInstance(String param1, String param2) {
        LocalMusicFragment fragment = new LocalMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_local_music, container, false);
      return view;
    }
    //加这个函数是为了避免将同样的歌曲重复添加到已经播放过的
    public boolean NoRepeateInPlayed(int music_id){
        List<PlayedMusic> music=DataSupport.findAll(PlayedMusic.class);
        if(music.size()>1) {
            for (int i = 0; i < music.size(); i++) {
                if (music_id==music.get(i).getMusic_id()) {
                    return false;
                }

            }
        }return true;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // initMusic();
        musicList= DataSupport.findAll(Music.class);
        MusicAdapter adapter=new MusicAdapter(getActivity(),R.layout.activity_sjjnodelete_item,musicList);
       /* showMusicName=(TextView)getActivity().findViewById(R.id.music_info_textView);
        showSingerName=(TextView)getActivity().findViewById(R.id.singer_info_textView);*/
        listView=(ListView) getActivity().findViewById(R.id.list_view_reposity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               // Toast.makeText(getActivity(),String.valueOf(musicList.size()), Toast.LENGTH_SHORT).show();//打印出所有本地歌曲数量
                Music music=musicList.get(position);
                List<PlayedMusic> Played=DataSupport.findAll(PlayedMusic.class);
               //将已经播放过的列表与本地音乐库中的音乐数比较，若小于其数量，则删除当前数据重新导入一遍
               //if(Played.size()<musicList.size()) {
                   PlayedMusic.deleteAll(PlayedMusic.class);
                   for (int i = 0; i < musicList.size(); i++) {
                       PlayedMusic playedMusic = new PlayedMusic();
                       playedMusic.setPlayed_id( i);
                       playedMusic.setMusic_id(musicList.get(i).getMusic_id());
                       playedMusic.save();

                   }
             //  }
               playedMusics=DataSupport.where("music_id=?",String.valueOf(music.getMusic_id())).find(PlayedMusic.class);
                if(playedMusics.size()>0)
                mListener.playMusic(music,playedMusics.get(0).getPlayed_id());//接口回调
                /*Button play=(Button) getActivity().findViewById(R.id.play_button);
                play.setBackgroundResource(R.drawable.play);*/
            }
        });
        Button backTomain=(Button) getActivity().findViewById(R.id.backTomain);
        backTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.changeToMain();
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.playMusic(music,(int)playedMusics.get(0).getPlayed_id());
            mListener.changeToMain();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void playMusic(Music music,int id);
        void changeToMain();
    }
}
