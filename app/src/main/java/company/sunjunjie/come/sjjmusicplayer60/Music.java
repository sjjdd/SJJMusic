package company.sunjunjie.come.sjjmusicplayer60;

import org.litepal.crud.DataSupport;

/**
 * Created by sunjunjie on 2017/11/17.
 */

public class Music extends DataSupport{
    private int music_id;
    private String music_name;
    private String music_singer_name;
    private String music_path;
    private String music_lyrics_path;

    public String getMusic_lyrics_path() {
        return music_lyrics_path;
    }

    public void setMusic_lyrics_path(String music_lyrics_path) {
        this.music_lyrics_path = music_lyrics_path;
    }

    private int music_image;
    private int love;
    private int isplaying;
    public Music(){

    }
    public Music(int music_id, int music_image, String music_name, String music_singer_name, String music_path,String music_lyrics_path){
        this.music_id=music_id;
        this.music_image=music_image;
        this.music_name=music_name;
        this.music_singer_name=music_singer_name;
        this.music_path=music_path;
        this.music_lyrics_path=music_lyrics_path;
        this.love=0;
        this.isplaying=0;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public int getIsplaying() {
        return isplaying;
    }

    public void setIsplaying(int isplaying) {
        this.isplaying = isplaying;
    }

    public int getMusic_id() {
        return music_id;
    }

    public int getMusic_image() {
        return music_image;
    }

    public void setMusic_image(int music_image) {
        this.music_image = music_image;
    }

    public String getMusic_name() {
        return music_name;
    }

    public String getMusic_singer_name() {
        return music_singer_name;
    }

    public String getMusic_path() {
        return music_path;
    }

    public void setMusic_path(String music_path) {
        this.music_path = music_path;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public void setMusic_singer_name(String music_singer_name) {
        this.music_singer_name = music_singer_name;
    }
}
