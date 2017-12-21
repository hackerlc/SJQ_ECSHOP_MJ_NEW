package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.AutoReturnView;
import com.ecjia.widgets.CYTextView;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.entity.responsemodel.HOTNEWS;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/16.
 */
public class FindHotPointAdapter extends BaseAdapter {
    private Context context;
    protected static final int TYPE_SINGLE = 1;
    protected static final int TYPE_MULTI = 2;
    private ArrayList<ArrayList<HOTNEWS>> hotNewsArrayList;
    private ImageLoaderForLV imageLoaderForLV;
    int width = 0;
    int tenmargin = 0;
    int fifthmargin = 0;
    ECJiaDealUtil ecJiaDealUtil;

    public FindHotPointAdapter(Context context, ArrayList<ArrayList<HOTNEWS>> list) {
        this.context = context;
        hotNewsArrayList = list;
        imageLoaderForLV = ImageLoaderForLV.getInstance(context);
        width = getDisplayMetricsWidth();
        tenmargin = (int) context.getResources().getDimension(R.dimen.ten_margin);
        fifthmargin = (int) context.getResources().getDimension(R.dimen.fifth_margin);
        ecJiaDealUtil = new ECJiaDealUtil(context);
    }

    @Override
    public int getCount() {
        return hotNewsArrayList.size();
    }

    @Override
    public int getViewTypeCount() {

        return 1000;
    }

    @Override
    public Object getItem(int i) {
        return hotNewsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if (hotNewsArrayList.get(position).size() > 1) {
            return TYPE_MULTI;
        } else {
            return TYPE_SINGLE;
        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ArrayList<HOTNEWS> hotnewsArray = hotNewsArrayList.get(i);
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;
        int type = getItemViewType(i);

        if (view == null) {

            switch (type) {
                case TYPE_SINGLE:
                    view = LayoutInflater.from(context).inflate(R.layout.find_todayhot_single_item, null);
                    holder2 = new ViewHolder2();
                    holder2.news_single_top_view = (View) view.findViewById(R.id.news_single_top_view);
                    holder2.single_news_all = (LinearLayout) view.findViewById(R.id.single_news_all);
                    holder2.tv_news_single_time = (TextView) view.findViewById(R.id.tv_news_single_time);
                    holder2.single_news_title = (CYTextView) view.findViewById(R.id.single_news_title);
                    holder2.single_news_date = (TextView) view.findViewById(R.id.single_news_date);
                    holder2.single_news_first_img = (ImageView) view.findViewById(R.id.single_news_first_img);
                    holder2.single_news_first_text = (TextView) view.findViewById(R.id.single_news_first_text);
                    holder2.single_news_content = (CYTextView) view.findViewById(R.id.single_news_content);
                    FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    lp1.width = width - fifthmargin;
                    lp1.height = (width - fifthmargin) * 10 / 23;
                    lp1.setMargins(tenmargin, tenmargin, tenmargin, tenmargin);
                    holder2.single_news_first_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    lp1.gravity = Gravity.CENTER;
                    view.setTag(holder2);
                    break;
                case TYPE_MULTI:
                    view = LayoutInflater.from(context).inflate(R.layout.find_todayhot_item, null);
                    holder = new ViewHolder();
                    holder.item_viewgroup = (ViewGroup) view.findViewById(R.id.viewgroup_find_todayhot);
                    holder.first_item1 = (FrameLayout) view.findViewById(R.id.first_item1);
                    holder.tv_news_time = (TextView) view.findViewById(R.id.tv_news_time);
                    holder.first_img1 = (ImageView) view.findViewById(R.id.hot_news_first_img1);
                    holder.first_text1 = (TextView) view.findViewById(R.id.hot_news_first_text1);
                    holder.news_top_view = (View) view.findViewById(R.id.news_top_view);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    lp.width = width - fifthmargin;
                    lp.height = (width - fifthmargin) * 10 / 23;
                    lp.setMargins(tenmargin, tenmargin, tenmargin, tenmargin);
                    holder.first_img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    lp.gravity = Gravity.CENTER;
                    holder.first_img1.setLayoutParams(lp);
                    view.setTag(holder);
                    break;
            }

        } else {
            switch (type) {
                case TYPE_SINGLE:
                    holder2 = (ViewHolder2) view.getTag();
                    break;
                case TYPE_MULTI:
                    holder = (ViewHolder) view.getTag();
                    break;
            }
        }

        switch (type) {
            case TYPE_SINGLE:
                if (i == 0) {
                    holder2.news_single_top_view.setVisibility(View.VISIBLE);
                } else {
                    holder2.news_single_top_view.setVisibility(View.GONE);
                }
                if (i == hotNewsArrayList.size() - 1) {
                    holder2.tv_news_single_time.setText(TimeUtil.getFomartNewsTopTime(hotnewsArray.get(0).getCreate_time()));
                } else {
                    holder2.tv_news_single_time.setText(TimeUtil.getFomartNewsTopTime(hotnewsArray.get(0).getCreate_time()));
                }

                imageLoaderForLV.setImageRes(holder2.single_news_first_img, hotnewsArray.get(0).getImage());
                holder2.single_news_title.SetText(hotnewsArray.get(0).getTitle());
                holder2.single_news_first_text.setText(hotnewsArray.get(0).getDescription());
                holder2.single_news_content.SetText(hotnewsArray.get(0).getDescription());
                holder2.single_news_date.setText(TimeUtil.getFormatNowDay(hotnewsArray.get(0).getCreate_time()));
                holder2.single_news_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hotnewsArray.get(0).getMap().isEmpty()) {
                            Intent intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("url", hotnewsArray.get(0).getContent_url());
                            context.startActivity(intent);
                        } else {
                            ecJiaDealUtil.dealEcjiaAction(hotnewsArray.get(0).getMap());
                        }
                    }
                });

                break;
            case TYPE_MULTI:
                if (i == 0) {
                    holder.news_top_view.setVisibility(View.VISIBLE);
                } else {
                    holder.news_top_view.setVisibility(View.GONE);
                }
                if (i == hotNewsArrayList.size() - 1) {
                    holder.tv_news_time.setText(TimeUtil.getFomartNewsTopTime(hotnewsArray.get(0).getCreate_time()));
                } else {
                    holder.tv_news_time.setText(TimeUtil.getFomartNewsTopTime(hotnewsArray.get(0).getCreate_time()));
                }

                holder.item_viewgroup.removeAllViews();
                imageLoaderForLV.setImageRes(holder.first_img1, hotnewsArray.get(0).getImage());
                holder.first_text1.setText(hotnewsArray.get(0).getTitle());
                holder.first_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (hotnewsArray.get(0).getMap().isEmpty()) {
                            Intent intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("url", hotnewsArray.get(0).getContent_url());
                            context.startActivity(intent);
                        } else {
                            ecJiaDealUtil.dealEcjiaAction(hotnewsArray.get(i).getMap());
                        }
                    }
                });
                holder.item_viewgroup.setVisibility(View.VISIBLE);
                for (int j = 1; j < hotnewsArray.size(); j++) {
                    View v = LayoutInflater.from(context).inflate(R.layout.find_todayhot_small_item, null);
                    View top = v.findViewById(R.id.find_hot_small_topline);
                    View buttom = v.findViewById(R.id.find_hot_small_buttomline);
                    if (hotnewsArray.size() == 2) {
                        v.setBackgroundResource(R.drawable.selector_hot_news_buttom);
                        top.setVisibility(View.VISIBLE);
                        buttom.setVisibility(View.GONE);
                    } else {
                        if (j == 1) {
                            top.setVisibility(View.VISIBLE);
                            buttom.setVisibility(View.VISIBLE);
                            v.setBackgroundResource(R.drawable.selector_hot_news_buttom);
                        } else if (j > 1 && j < hotnewsArray.size() - 1) {
                            top.setVisibility(View.GONE);
                            buttom.setVisibility(View.VISIBLE);
                            v.setBackgroundResource(R.drawable.selector_hot_news);
                        } else if (j == hotnewsArray.size() - 1) {
                            v.setBackgroundResource(R.drawable.selector_hot_news_buttom);
                            top.setVisibility(View.GONE);
                            buttom.setVisibility(View.GONE);
                        }
                    }
                    AutoReturnView text = (AutoReturnView) v.findViewById(R.id.find_small_hot_text);
                    text.setContent(hotnewsArray.get(j).getTitle());
                    ImageView img = (ImageView) v.findViewById(R.id.find_hot_news_img);
                    imageLoaderForLV.setImageRes(img, hotnewsArray.get(j).getImage());
                    final int finalJ = j;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (hotnewsArray.get(finalJ).getMap().isEmpty()) {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", hotnewsArray.get(finalJ).getContent_url());
                                context.startActivity(intent);
                            } else {
                                ecJiaDealUtil.dealEcjiaAction(hotnewsArray.get(finalJ).getMap());
                            }
                        }
                    });
                    holder.item_viewgroup.addView(v);
                }
                break;
        }


        return view;
    }

    class ViewHolder {
        private ViewGroup item_viewgroup;
        private FrameLayout first_item1;
        private TextView first_text1;
        private ImageView first_img1;
        public TextView tv_news_time;
        public View news_top_view;
    }

    class ViewHolder2 {
        public View news_single_top_view;
        public CYTextView single_news_content;
        public TextView tv_news_single_time;
        public CYTextView single_news_title;
        public TextView single_news_date;
        public ImageView single_news_first_img;
        public TextView single_news_first_text;
        public LinearLayout single_news_all;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

}
