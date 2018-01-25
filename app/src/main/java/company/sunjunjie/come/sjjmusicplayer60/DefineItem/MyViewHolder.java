package company.sunjunjie.come.sjjmusicplayer60.DefineItem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import company.sunjunjie.come.sjjmusicplayer60.R;

/**
 * Created by sunjunjie on 2018/1/4.
 */

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView textView;
    ImageView imageView;
    private OnItemClickListener mListener;// 声明自定义的接口

    // 构造函数中添加自定义的接口的参数
    public MyViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        mListener = listener;
        // 为ItemView添加点击事件
        itemView.setOnClickListener(this);

        textView = (TextView) itemView.findViewById(R.id.background_name);
        imageView=(ImageView) itemView.findViewById(R.id.background_image);
    }

    @Override
    public void onClick(View v) {
        // getpostion()为Viewholder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
        mListener.onItemClick(v, getPosition());
    }

}
