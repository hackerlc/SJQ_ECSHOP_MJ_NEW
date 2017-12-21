package com.ecjia.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

public class OrderCancelDialog {
    private Dialog mDialog;
    public EditText edit;
    public TextView negative;
    public TextView positive;

    public OrderCancelDialog(Context c) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.dailog_order_cancel, null);
        view.setMinimumWidth(((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth());
        mDialog = new Dialog(c, R.style.ActionSheetDialogStyle);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        negative = (TextView) view.findViewById(R.id.cancel);
        positive = (TextView) view.findViewById(R.id.sure);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    public void showKeybord() {
        edit.requestFocus();
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
