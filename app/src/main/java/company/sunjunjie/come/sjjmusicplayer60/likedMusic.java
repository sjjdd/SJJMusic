package company.sunjunjie.come.sjjmusicplayer60;

import org.litepal.crud.DataSupport;

/**
 * Created by sunjunjie on 2017/12/21.
 */

public class likedMusic extends DataSupport {
    private int id;
    private int music_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }
}
