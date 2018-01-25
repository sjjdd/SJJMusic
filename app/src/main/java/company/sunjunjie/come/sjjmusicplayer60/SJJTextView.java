package company.sunjunjie.come.sjjmusicplayer60;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sunjunjie on 2017/12/6.
 */

public class SJJTextView extends TextView {
    public SJJTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SJJTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SJJTextView(Context context) {
        super(context);
    }

    @Override

    public boolean isFocused() {
        //就是把这里返回true即可
        return true;
    }
}
