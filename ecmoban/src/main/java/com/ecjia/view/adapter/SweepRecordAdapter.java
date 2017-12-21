package com.ecjia.view.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.MyHorizontalScrollView;
import com.ecjia.widgets.ToastView;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.entity.responsemodel.SWEEP_HISTORY;

import java.util.ArrayList;

public class SweepRecordAdapter extends BaseAdapter {
    private Context context;
    private int mRightWidth = 0;
    public ArrayList<SWEEP_HISTORY> sweepHistoryArrayList;
    private View view;
    private boolean isshown = false;
    private String last_text, now_text, next_text;//标志位时间信息

    public SweepRecordAdapter(Context context, ArrayList<SWEEP_HISTORY> list, int RightViewWidth) {
        this.context = context;
        sweepHistoryArrayList = list;
        mRightWidth = RightViewWidth;
    }

    @Override
    public int getCount() {
        return sweepHistoryArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return sweepHistoryArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final SWEEP_HISTORY sweep_history = sweepHistoryArrayList.get(i);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sweeprecord_item, viewGroup, false);
            holder = new ViewHolder();
            holder.item_swep_left = (LinearLayout) convertView.findViewById(R.id.item_sweep_left);
            holder.action = (LinearLayout) convertView.findViewById(R.id.ll_action);
            holder.tv_record_time = (TextView) convertView.findViewById(R.id.tv_record_time);
            holder.sweep_content = (TextView) convertView.findViewById(R.id.sweep_content);
            holder.hSView = (MyHorizontalScrollView) convertView.findViewById(R.id.hsv_view);
            holder.btn_copy = (LinearLayout) convertView.findViewById(R.id.sweep_copy);
            holder.btn_delete = (LinearLayout) convertView.findViewById(R.id.sweep_delete);
            holder.item_top_view = convertView.findViewById(R.id.top_line);
            holder.item_buttom_long_view = convertView.findViewById(R.id.buttom_long_line);
            holder.item_top_short_view = convertView.findViewById(R.id.top_short_line);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.width = getDisplayMetricsWidth();
            lp.height = mRightWidth / 2;
            holder.item_swep_left.setLayoutParams(lp);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp1.width = mRightWidth;
            lp1.height = mRightWidth / 2;
            holder.action.setLayoutParams(lp1);
            holder.hSView.setAction(holder.action);//添加滑动视图
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        now_text = sweep_history.getSweep_date();
        if (i > 0) {
            last_text = sweepHistoryArrayList.get(i - 1).getSweep_date();
        }
        if (i < sweepHistoryArrayList.size() - 1) {
            next_text = sweepHistoryArrayList.get(i + 1).getSweep_date();
        }

        if (sweepHistoryArrayList.size() == 1) { //只有一条
            holder.tv_record_time.setVisibility(View.VISIBLE);
            holder.item_top_view.setVisibility(View.VISIBLE);
            holder.item_top_short_view.setVisibility(View.GONE);
            holder.item_buttom_long_view.setVisibility(View.VISIBLE);
        } else {//多条
            if (i == 0) { //第一项
                holder.tv_record_time.setVisibility(View.VISIBLE);
                holder.item_top_view.setVisibility(View.VISIBLE);
                holder.item_top_short_view.setVisibility(View.GONE);
                if (now_text.equals(next_text)) {  //是否在同年同月
                    holder.item_buttom_long_view.setVisibility(View.GONE);
                } else {
                    holder.item_buttom_long_view.setVisibility(View.VISIBLE);
                }


            } else if (i == sweepHistoryArrayList.size() - 1) { //最后一项

                holder.item_buttom_long_view.setVisibility(View.VISIBLE);
                if (now_text.equals(last_text)) {
                    holder.item_top_short_view.setVisibility(View.VISIBLE);
                    holder.item_top_view.setVisibility(View.GONE);
                    holder.tv_record_time.setVisibility(View.GONE);
                } else {
                    holder.item_top_short_view.setVisibility(View.GONE);
                    holder.item_top_view.setVisibility(View.VISIBLE);
                    holder.tv_record_time.setVisibility(View.VISIBLE);
                }

            } else { //中间项

                if (now_text.equals(last_text)) {
                    holder.item_top_short_view.setVisibility(View.VISIBLE);
                    holder.item_top_view.setVisibility(View.GONE);
                    holder.tv_record_time.setVisibility(View.GONE);
                } else {
                    holder.item_top_short_view.setVisibility(View.GONE);
                    holder.item_top_view.setVisibility(View.VISIBLE);
                    holder.tv_record_time.setVisibility(View.VISIBLE);
                }

                if (now_text.equals(next_text)) {
                    holder.item_buttom_long_view.setVisibility(View.GONE);
                } else {
                    holder.item_buttom_long_view.setVisibility(View.VISIBLE);
                }

            }
        }


        holder.tv_record_time.setText(sweep_history.getSweep_date());
        holder.sweep_content.setText(sweep_history.getSweep_content());
        holder.hSView.onStatusChangeListener(new MyHorizontalScrollView.onStatusListener() {
            @Override
            public void onStatusChange() {
                notifyDataSetChanged();
            }

            @Override
            public void setShowView(boolean isShow) {
                isshown = isShow;
            }

            @Override
            public boolean getShowView() {
                return isshown;
            }
        });


        holder.item_swep_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sweep_history.getSweep_content().contains("http://") || sweep_history.getSweep_content().contains("https://")) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", sweep_history.getSweep_content());
                    context.startActivity(intent);
                } else {
                    ToastView toastView = new ToastView(context, context.getResources().getString(R.string.no_todo));
                    toastView.show();
                }
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final MyDialog mDialog = new MyDialog(context, context.getResources().getString(R.string.point), context.getResources().getString(R.string.scan_history_delete_attention));
                mDialog.show();
                mDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sweepHistoryArrayList.remove(i);
                        isshown = false;
                        SweepSql.getIntence(context).deleteByContext(sweep_history.getSweep_content());
                        if (mListener != null) {
                            mListener.onItemChange();
                        }
                        notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
                mDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });

            }
        });
        holder.btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyContent(sweep_history.getSweep_content());
            }
        });

        // 这里防止删除一条item后,ListView处于操作状态,直接还原
        if (holder.hSView.getScrollX() != 0) {
            holder.hSView.smoothScrollTo(0, 0);
            isshown = false;
        }
        return convertView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void CopyContent(String sweep_content) {
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager c = (android.content.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("", sweep_content));

        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
            c.setText(sweep_content);
        }
        ToastView toastView = new ToastView(context, context.getResources().getString(R.string.copy_succeed));
        toastView.show();
    }

    class ViewHolder {
        private LinearLayout item_swep_left;
        private TextView tv_record_time, sweep_content;
        private LinearLayout btn_copy, btn_delete;
        private MyHorizontalScrollView hSView;
        private View action;
        private View item_top_view, item_top_short_view, item_buttom_long_view;
    }


    /**
     * 单击事件监听器
     */
    private onItemChangeListener mListener = null;

    public void setOnItemListener(onItemChangeListener listener) {
        mListener = listener;
    }

    public interface onItemChangeListener {
        void onItemChange();
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }
}
