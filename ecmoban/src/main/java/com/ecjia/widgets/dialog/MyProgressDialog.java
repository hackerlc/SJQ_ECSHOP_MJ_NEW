package com.ecjia.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

public class MyProgressDialog extends Dialog {
    private Context context = null;
    private static MyProgressDialog customProgressDialog = null;

    private MyProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static MyProgressDialog createDialog(Context context) {
        customProgressDialog = new MyProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.customprogressdialog);

        WindowManager.LayoutParams lp = customProgressDialog.getWindow().getAttributes();
        customProgressDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        lp.y = -100;
        customProgressDialog.getWindow().setAttributes(lp);
//        customProgressDialog.setCanceledOnTouchOutside(false);
//        customProgressDialog.setCancelable(false);
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }

//		ImageView imageView = (ImageView) customProgressDialog
//				.findViewById(R.id.loadingImageView);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
//				.getBackground();
//		animationDrawable.start();
    }


    public MyProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }


    public MyProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog
                .findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }

}
