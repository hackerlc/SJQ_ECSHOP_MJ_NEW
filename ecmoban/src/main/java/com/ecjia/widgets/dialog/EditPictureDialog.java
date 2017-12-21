package com.ecjia.widgets.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

/**
 * Created by Administrator on 2015/4/2.
 */
public class EditPictureDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    public TextView takePhoto, fromPhotos, cancle,title;
    public LinearLayout ll_title;

    @SuppressLint("NewApi")
    public EditPictureDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_editheadpicture, null);
        view.setMinimumWidth(display.getWidth());
//        view.setAlpha(0.5f);
        takePhoto = (TextView) view.findViewById(R.id.editheadpicture_take_photo);
        fromPhotos = (TextView) view.findViewById(R.id.editheadpicture_from_photos);
        title = (TextView) view.findViewById(R.id.editheadpicture_title);
        ll_title = (LinearLayout) view.findViewById(R.id.ll_editheadpicture_title);
        cancle = (TextView) view.findViewById(R.id.editheadpicture_cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 创建Dialog
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

//    public EditPictureDialog builder() {
//        View view = LayoutInflater.from(context).inflate(R.layout.activity_editheadpicture, null);
//        view.setMinimumWidth(display.getWidth());
//        takePhoto = (TextView) view.findViewById(R.id.editheadpicture_take_photo);
//        fromPhotos = (TextView) view.findViewById(R.id.editheadpicture_from_photos);
//        cancle = (TextView) view.findViewById(R.id.editheadpicture_cancle);
//        cancle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        // 创建Dialog
//        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
//        dialog.setContentView(view);
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.x = 0;
//        lp.y = 0;
//        dialogWindow.setAttributes(lp);
//        return this;
//    }


    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

}