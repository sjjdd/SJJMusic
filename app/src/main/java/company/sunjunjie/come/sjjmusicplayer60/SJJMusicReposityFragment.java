package company.sunjunjie.come.sjjmusicplayer60;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SJJMusicReposityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SJJMusicReposityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SJJMusicReposityFragment extends Fragment {
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String songPath="/storage/sdcard0/./kugoumusic/";
    Music music1=new Music();
    List<File> list=new ArrayList<File>();//定义一个list待会儿用于存放寻找的文件
    private ListView listView;
    private List<Music> musicList=new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public SJJMusicReposityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SJJMusicReposityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SJJMusicReposityFragment newInstance(String param1, String param2) {
        SJJMusicReposityFragment fragment = new SJJMusicReposityFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button backTomain=(Button) getActivity().findViewById(R.id.backTomain);
        backTomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeToMain();
            }
        });
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);

        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater =getLayoutInflater();
        View view1 = inflater.inflate(R.layout.item01, null);
        View view2 = inflater.inflate(R.layout.item02, null);
        View view3 = inflater.inflate(R.layout.item03, null);

        //将view装入数组
        pageview =new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            //是从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(pageview.get(arg1));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageview.get(arg1));
                return pageview.get(arg1);
            }


        };

        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sjjmusic_reposity, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction2();
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
        void onFragmentInteraction2();
         void changeToMain();
    }
}
