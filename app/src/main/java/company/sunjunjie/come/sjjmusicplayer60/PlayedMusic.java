package company.sunjunjie.come.sjjmusicplayer60;

import org.litepal.crud.DataSupport;

/**
 * Created by sunjunjie on 2017/12/15.
 */

public class PlayedMusic extends DataSupport {
    private int played_id;
    private int music_id;


    public int getPlayed_id() {
        return played_id;
    }

    public void setPlayed_id(int played_id) {
        this.played_id = played_id;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }
}
