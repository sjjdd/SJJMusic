package company.sunjunjie.come.sjjmusicplayer60;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentPlayedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentPlayedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentPlayedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private List<Music> musicList=new ArrayList<>();
    List<PlayedMusic> playedMusics;
    Music music=new Music();
    private OnFragmentInteractionListener mListener;

    public RecentPlayedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentPlayedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentPlayedFragment newInstance(String param1, String param2) {
        RecentPlayedFragment fragment = new RecentPlayedFragment();
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
        View view=inflater.inflate(R.layout.fragment_recent_played, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
           // mListener.playMusic(music,(int)playedMusics.get(0).getPlayed_id());
            mListener.changeToMain();
            mListener.playMusic(music,(int)playedMusics.get(0).getPlayed_id());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<RecentPlayedMusic> recentPlayedMusics= DataSupport.findAll(RecentPlayedMusic.class);
        //Toast.makeText(getContext(), "RecentPlayedMusic大小"+String.valueOf(recentPlayedMusics.size()), Toast.LENGTH_SHORT).show();
        musicList.clear();
        if(recentPlayedMusics.size()>0){
            for(int i=0;i<recentPlayedMusics.size();i++){
                List<Music> music=DataSupport.where("music_id=?",String.valueOf(recentPlayedMusics.get(i).getMusic_id())).find(Music.class);
                musicList.add(music.get(0));
            }
        }
        RecentPlayedAdapter adapter=new RecentPlayedAdapter(getActivity(),R.layout.activity_sjjmain_item,musicList);
       /* showMusicName=(TextView)getActivity().findViewById(R.id.music_info_textView);
        showSingerName=(TextView)getActivity().findViewById(R.id.singer_info_textView);*/
        listView=(ListView) getActivity().findViewById(R.id.list_view_recent_played);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(),String.valueOf(musicList.size()), Toast.LENGTH_SHORT).show();//打印出所有本地歌曲数量
                Music music=musicList.get(position);
                List<PlayedMusic> Played=DataSupport.findAll(PlayedMusic.class);
                PlayedMusic.deleteAll(PlayedMusic.class);
                for (int i = 0; i < musicList.size(); i++) {
                    PlayedMusic playedMusic = new PlayedMusic();
                    playedMusic.setPlayed_id(i);
                    playedMusic.setMusic_id(musicList.get(i).getMusic_id());
                    playedMusic.save();

                }
                playedMusics=DataSupport.where("music_id=?",String.valueOf(music.getMusic_id())).find(PlayedMusic.class);
                mListener.playMusic(music,playedMusics.get(0).getPlayed_id());//接口回调
            }
        });
        Button backTomain = (Button) getActivity().findViewById(R.id.backTomain);
        backTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeToMain();
            }
        });
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
      //  void playMusic(Music music,int id);
        void changeToMain();
        void playMusic(Music music,int id);
    }
}
