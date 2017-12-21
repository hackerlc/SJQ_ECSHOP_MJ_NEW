package com.ecjia.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.ecmoban.android.sijiqing.R;

/**
 * Created by Administrator on 2015/12/23.
 */
public class AndroidView {
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,
                                                int idUnable) {
        int roundRadius=(int)context.getResources().getDimension(R.dimen.dp_5);
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : createShape(0,-1,roundRadius,idNormal);
        Drawable pressed = idPressed == -1 ? null : createShape(0,-1,roundRadius,idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_focused }, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[] {}, normal);
        return bg;
    }

    public static  StateListDrawable setSelectorBg(Context context, int normalId, int pressId) {//selector
        StateListDrawable bg = new StateListDrawable();

        Drawable idNormal = createShape(0,-1,5,normalId);
        Drawable idPressed =createShape(0,-1,5,pressId);
        bg.addState(new int[] { android.R.attr.state_pressed,
                android.R.attr.state_enabled, android.R.attr.selectable,
                android.R.attr.focusable }, idPressed);
        bg.addState(new int[] { android.R.attr.state_enabled }, idNormal);
        bg.addState(new int[] {}, idNormal);
        return bg;
    }

    public static GradientDrawable createShape(int strokeWidth,int strokeColor,float roundRadius,int fillColor){//shape
        GradientDrawable gd=new GradientDrawable();
        gd.setColor(fillColor);
        if(strokeColor!=-1){
            gd.setStroke(strokeWidth,strokeColor);
        }
        gd.setCornerRadius(roundRadius);

        return gd;
    }
}
