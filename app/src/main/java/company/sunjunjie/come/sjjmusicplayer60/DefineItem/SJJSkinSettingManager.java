package company.sunjunjie.come.sjjmusicplayer60.DefineItem;

import android.app.Activity;
import android.content.SharedPreferences;

import company.sunjunjie.come.sjjmusicplayer60.R;

/**
 * Created by sunjunjie on 2018/1/3.
 */

public class SJJSkinSettingManager {
    public final static String SKIN_PREF = "skinSetting";

    public SharedPreferences skinSettingPreference;

    private int[] skinResources = { R.drawable.playerbackground,
            R.drawable.background1,R.drawable.background2,R.drawable.background3,
            R.drawable.background4
    };

    private Activity mActivity;


    public SJJSkinSettingManager(Activity activity) {
        this.mActivity = activity;
        skinSettingPreference = mActivity.getSharedPreferences(SKIN_PREF, 3);
    }

    /**
     * 获取当前程序的皮肤序号
     *
     * @return
     */
    public int getSkinType() {
        String key = "skin_type";
        return skinSettingPreference.getInt(key, 0);
    }

    /**
     * 把皮肤序号写到全局设置里去
     *
     * @param j
     */
    public void setSkinType(int j) {
        SharedPreferences.Editor editor = skinSettingPreference.edit();
        String key = "skin_type";

        editor.putInt(key, j);
        editor.commit();
    }

    /**
     * 获取当前皮肤的背景图资源id
     *
     * @return
     */
    public int getCurrentSkinRes() {
        int skinLen = skinResources.length;
        int getSkinLen = getSkinType();
        if(getSkinLen >= skinLen){
            getSkinLen = 0;
        }

        return skinResources[getSkinLen];
    }

    public void toggleSkins(){

        int skinType = getSkinType();
        if(skinType == skinResources.length - 1){
            skinType = 0;
        }else{
            skinType ++;
        }
        setSkinType(skinType);
        mActivity.getWindow().setBackgroundDrawable(null);
        try {
            mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
        } catch (Throwable e) {
            e.printStackTrace();

        }


    }

    /**
     * 用于初始化皮肤
     */
    public void initSkins(){
        mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
    }

    /**
     * 随即切换一个背景皮肤
     */
    public void changeSkin(int id) {

        setSkinType(id);
        mActivity.getWindow().setBackgroundDrawable(null);
        try {
            mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
