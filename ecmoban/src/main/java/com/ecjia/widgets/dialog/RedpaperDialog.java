package com.ecjia.widgets.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.ReceiveRedpaperAdapter;
import com.ecjia.entity.responsemodel.GOODS_COUPON;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/2.
 */
public class RedpaperDialog {
    private final ListView mListView;
    private Context context;
    private Dialog dialog;
    private Display display;
    public TextView takePhoto, fromPhotos, cancle;
    ArrayList<GOODS_COUPON> goods_coupons;
    public ReceiveRedpaperAdapter adapter;

    @SuppressLint("NewApi")
    public RedpaperDialog(Context context, ArrayList<GOODS_COUPON> goods_coupons) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_receive_redpaper, null);
        view.setMinimumWidth(display.getWidth());
        view.setAlpha(1f);

        mListView = (ListView)view.findViewById(R.id.redpaper_list);
        view.findViewById(R.id.redpaper_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (goods_coupons != null && goods_coupons.size() > 0) {
            adapter = new ReceiveRedpaperAdapter(context, goods_coupons);
            adapter.setReceiveRedpaperListener(listener);
            mListView.setAdapter(adapter);
            setListViewHeight(mListView);// 限制对话框的最大高度
        }

        // 创建Dialog
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
    }

    int position;

    public void setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    ReceiveRedpaperAdapter.ReceiveRedpaperListener listener;

    // 设置对话框的最大自适应高度
    public void setListViewHeight(ListView listView) {
        // 获取屏幕宽度
        int totalHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int dialogHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            dialogHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (totalHeight - dialogHeight < (int) (totalHeight * 14.0 / 25)) {
            params.height = totalHeight - (int) (totalHeight * 14.0 / 25);// 45为底部button的高度
        }

        listView.setLayoutParams(params);
    }
}