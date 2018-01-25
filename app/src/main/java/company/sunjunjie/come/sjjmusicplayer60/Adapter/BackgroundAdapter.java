package company.sunjunjie.come.sjjmusicplayer60.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import company.sunjunjie.come.sjjmusicplayer60.DefineItem.Background;
import company.sunjunjie.come.sjjmusicplayer60.R;


/**
 * Created by sunjunjie on 2018/1/4.
 */

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {
    private Context context;
    private List<Background> mbackgroundList;
    public interface OnRecyclerViewListtener{//回调接口
        void onItemClick(View view,int position);//单击
    };
    private OnRecyclerViewListtener onRecyclerViewListtener;
    public void setOnRecyclerViewListtener(OnRecyclerViewListtener mOnItemClickListener)
    {
        this.onRecyclerViewListtener=mOnItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView background_image;
        TextView background_name;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView) view;
            background_image=(ImageView) view.findViewById(R.id.background_image);
            background_name=(TextView) view.findViewById(R.id.background_name);
        }
    }
    public BackgroundAdapter(List<Background> backgroundList){
        mbackgroundList=backgroundList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.background_item,parent,false);
        return new ViewHolder(view);
    }
    public void onBindViewHolder(final ViewHolder holder, int position){
        Background background=mbackgroundList.get(position);
        holder.background_name.setText(background.getBackgroundName());
        Glide.with(context).load(background.getBackgroundId()).into(holder.background_image);
       if(onRecyclerViewListtener!=null){
           holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int pos=holder.getLayoutPosition();
                   onRecyclerViewListtener.onItemClick(holder.cardView,pos);
               }
           });
       }
    }
    public int getItemCount(){
        return mbackgroundList.size();
    }
}
