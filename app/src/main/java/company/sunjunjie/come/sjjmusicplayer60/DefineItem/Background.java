package company.sunjunjie.come.sjjmusicplayer60.DefineItem;

/**
 * Created by sunjunjie on 2018/1/4.
 */

public class Background {
    private int backgroundId;
    private String backgroundName;
    public Background(int backgroundId,String backgroundName){
        this.backgroundId=backgroundId;
        this.backgroundName=backgroundName;
    }
    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public void setBackgroundName(String backgroundName) {
        this.backgroundName = backgroundName;
    }

    public String getBackgroundName() {
        return backgroundName;
    }
}
