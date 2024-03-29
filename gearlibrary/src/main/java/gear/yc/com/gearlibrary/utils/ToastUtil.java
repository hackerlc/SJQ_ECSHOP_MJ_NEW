package gear.yc.com.gearlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @version 1.0
 * Created by Android on 2016/5/31.
 */
public class ToastUtil {
    private static ToastUtil ourInstance;

    public static ToastUtil getInstance() {
        if(ourInstance==null){
            synchronized (ToastUtil.class){
                if(ourInstance==null){
                    ourInstance=new ToastUtil();
                }
            }
        }
        return ourInstance;
    }

    private Toast mToast;
    int duration = Toast.LENGTH_SHORT;
    private String netError="未能连接到网络,请检查网络连接.";

    private ToastUtil() {
    }

    public void makeLongToast(Context context, String text) {
        shortToast(context, text, true);
    }

    public void makeShortToast(Context context, String text) {
        shortToast(context, text, false);
    }

    public void shortToast(Context context, String text,boolean isLong){
        if(isLong){
            duration = Toast.LENGTH_LONG;
        } else {
            duration = Toast.LENGTH_SHORT;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, duration);
//            mToast.setGravity(Gravity.CENTER,0,0);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public void makeNetErrorToast(Context context){
        this.makeLongToast(context,netError);
    }
}
