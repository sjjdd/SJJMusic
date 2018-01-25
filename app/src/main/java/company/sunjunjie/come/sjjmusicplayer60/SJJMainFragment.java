package company.sunjunjie.come.sjjmusicplayer60;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SJJMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SJJMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SJJMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView amountOfLocal,amountOfLiked,amountOfRecent;
    public SJJMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SJJMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SJJMainFragment newInstance(String param1, String param2) {
        SJJMainFragment fragment = new SJJMainFragment();
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
        View view= inflater.inflate(R.layout.fragment_sjjmain, container, false);
        Button music_reposity=(Button) view.findViewById(R.id.music_reposity);

        LinearLayout music_local_block=(LinearLayout) view.findViewById(R.id.local_music_block);
        LinearLayout music_liked_block=(LinearLayout) view.findViewById(R.id.liked_music_block);
        LinearLayout music_recent_block=(LinearLayout) view.findViewById(R.id.recent_music_block);

        amountOfLocal=(TextView) view.findViewById(R.id.amountOfLocal);
        amountOfLiked=(TextView) view.findViewById(R.id.amountOfLiked);
        amountOfRecent=(TextView) view.findViewById(R.id.amountOfrecent);
        mListener.initView(amountOfLocal,amountOfLiked,amountOfRecent);
        if(music_reposity!=null) {
            music_reposity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.changetoMusicReposity();
                }
            });
        }
        if(music_local_block!=null){
            music_local_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.changetoLocalMusic();
                }
            });
        }
        if(music_liked_block!=null){
            music_liked_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.changetoLikedMusic();
                }
            });
        }
        if(music_recent_block!=null){
            music_recent_block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.changetoRecentMusic();
                }
            });
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.changetoMusicReposity();
            mListener.changetoLocalMusic();
            mListener.changetoLikedMusic();
            mListener.changetoRecentMusic();
            mListener.initView(amountOfLocal,amountOfLiked,amountOfRecent);
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
        void changetoMusicReposity();
        void changetoLocalMusic();
        void initView(TextView amountOfLocal,TextView amountOfLIked,TextView amountOfRecent);
        void changetoLikedMusic();
        void changetoRecentMusic();
    }
}
