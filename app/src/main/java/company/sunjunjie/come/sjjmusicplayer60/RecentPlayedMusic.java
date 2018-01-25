package company.sunjunjie.come.sjjmusicplayer60;

import org.litepal.crud.DataSupport;

/**
 * Created by sunjunjie on 2017/12/19.
 */
//最近播放
public class RecentPlayedMusic extends DataSupport {
    private int  recent_id;
    private int music_id;

    public int getRecent_id() {
        return recent_id;
    }

    public void setRecent_id(int recent_id) {
        this.recent_id = recent_id;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }
}
