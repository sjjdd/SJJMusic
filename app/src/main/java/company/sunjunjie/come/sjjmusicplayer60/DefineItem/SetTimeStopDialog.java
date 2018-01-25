package company.sunjunjie.come.sjjmusicplayer60.DefineItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import company.sunjunjie.come.sjjmusicplayer60.DialogClickCallBack;
import company.sunjunjie.come.sjjmusicplayer60.R;


/**
 * Created by sunjunjie on 2018/1/3.
 */

public class SetTimeStopDialog extends Dialog {
    Context mContext;
    /**TextView*/
    private TextView tvTest;
    /**dialog点击回调*/
    private DialogClickCallBack dialogClickCallBack;

    public SetTimeStopDialog(Context context){
        super(context, R.style.MyDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time_stop);
        /*tvTest = (TextView) findViewById(R.id.tv_test);
        tvTest.setOnClickListener(new OnTvClickListener());
        Button test_item = (Button) findViewById(R.id.test_item);
        test_item.setOnClickListener(new OnTvClickListener());*/
       /* TextView bottomPlayer=(TextView) findViewById(R.id.bottomPlayer);
        bottomPlayer.setOnClickListener(new OnTvClickListener());*/
       LinearLayout close_set_time=(LinearLayout) findViewById(R.id.close_set_time);
       close_set_time.setOnClickListener(new OnTvClickListener());
    }
    private class OnTvClickListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {
            /**把点击的view Id传出去*/
           /* dialogClickCallBack.viewClick(R.id.played_music_list);
            dialogClickCallBack.viewClick(R.id.bottomPlayer);*/
            //退出dialog
            //cancel();
            dialogClickCallBack.viewClick(R.id.close_set_time);
        }
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Activity activity=getOwnerActivity();
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
