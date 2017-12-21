package com.ecjia.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.BONUSINFO;

/**
 * Created by Administrator on 2015/11/26.
 */
public class RedPaperDetailDialog {
    public Dialog mDialog;
    private Context context;
    private int distance;
    public BONUSINFO bonusinfo;
    private LinearLayout redpaper_item;
    //----------红包详情布局----------
    private TextView redpaper_name, redpaper_amount, redpaper_time, redpaper_condition;
    private LinearLayout detail_item;
    private Button add_ok, add_cancel;
    //---------------------------

    //----------重新输入布局----------
    private LinearLayout repeat_item;
    private Button btn_input_again;
    //----------添加成功布局----------
    private LinearLayout add_succeed_item;
    private Button btn_add_succeed;

    public RedPaperDetailDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.redpaper_detail, null);
        redpaper_item = (LinearLayout) view.findViewById(R.id.redpaper_item);
        this.context = context;
        distance = (int) context.getResources().getDimension(R.dimen.sixty_dp);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getDisplayMetricsWidth() - distance, ViewGroup.LayoutParams.WRAP_CONTENT);
        redpaper_item.setLayoutParams(params);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setWindowAnimations(R.style.redpaperWindowAnim);
        initView(view);
    }

    //设置数据1代表输入错误，2代表红包详情，3代表添加成功
    public void setDataByType(final int type) {
        switch (type) {
            case 1:
                btn_input_again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        mListener.onButtonItemClick(view, type);
                    }
                });
                detail_item.setVisibility(View.GONE);
                repeat_item.setVisibility(View.VISIBLE);
                add_succeed_item.setVisibility(View.GONE);

                break;
            case 2:
                detail_item.setVisibility(View.VISIBLE);
                repeat_item.setVisibility(View.GONE);
                add_succeed_item.setVisibility(View.GONE);
                redpaper_name.setText(bonusinfo.getBonus_name());
                redpaper_amount.setText(bonusinfo.getFormatted_bonus_amout());
                redpaper_time.setText(bonusinfo.getFormatted_start_date() + "-" + bonusinfo.getFormatted_end_date());
                redpaper_condition.setText("满" + bonusinfo.getFormatted_request_amount() + "才可使用此红包");
                add_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onButtonItemClick(view, type);
                        }
                    }
                });
                add_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                break;
            case 3:
                detail_item.setVisibility(View.GONE);
                repeat_item.setVisibility(View.GONE);
                add_succeed_item.setVisibility(View.VISIBLE);
                btn_add_succeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onButtonItemClick(view, type);
                        dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        //-------------红包详情---------------
        detail_item = (LinearLayout) view.findViewById(R.id.detail_item);
        redpaper_name = (TextView) view.findViewById(R.id.redpaper_name);
        redpaper_amount = (TextView) view.findViewById(R.id.redpaper_amount);
        redpaper_time = (TextView) view.findViewById(R.id.redpaper_time);
        redpaper_condition = (TextView) view.findViewById(R.id.redpaper_condition);
        add_ok = (Button) view.findViewById(R.id.add_ok);
        add_cancel = (Button) view.findViewById(R.id.add_cancel);
        //--------------重新输入------------
        repeat_item = (LinearLayout) view.findViewById(R.id.repeat_item);
        btn_input_again = (Button) view.findViewById(R.id.add_repeat_input);
        //--------------添加成功------------
        add_succeed_item = (LinearLayout) view.findViewById(R.id.add_succeed_item);
        btn_add_succeed = (Button) view.findViewById(R.id.btn_add_succeed);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    /**
     * 单击事件监听器
     */
    private OnButtonClickListener mListener = null;

    public void setOnItemClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonItemClick(View v, int type);
    }

}
