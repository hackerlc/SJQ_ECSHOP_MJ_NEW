package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.widgets.SelectableRoundedImageView;
import com.ecjia.entity.responsemodel.ADSENSE;
import com.ecjia.entity.responsemodel.ADSENSE_GROUP;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.MyBitmapUtils;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    private Context context;
    protected ImageLoaderForLV imageLoader;
    private ArrayList<ADSENSE_GROUP> mylist = new ArrayList<ADSENSE_GROUP>();
    private ECJiaDealUtil ecJiaDealUtil;
    private ADSENSE left1, left2, left3, left4;
    private MyBitmapUtils bitmapUtils;

    public EventAdapter(Context context, ArrayList<ADSENSE_GROUP> datalist) {
        this.context = context;
        imageLoader = ImageLoaderForLV.getInstance(context);
        mylist = datalist;
        ecJiaDealUtil = new ECJiaDealUtil(context);
    }

    public void setData(ArrayList<ADSENSE_GROUP> datalist) {
        mylist = datalist;
    }


    @Override
    public Object getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.event_item, null);
            holder.iv_event = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_event);
            holder.iv_event2 = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_event2);
            holder.iv_event3 = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_event3);
            holder.iv_event4 = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_event4);
            holder.event_title = (View) convertView.findViewById(R.id.event_title);
            holder.first_item = (View) convertView.findViewById(R.id.first_item);
            holder.event_all_title = (TextView) convertView.findViewById(R.id.event_all_title);
            holder.item_image_all = (LinearLayout) convertView.findViewById(R.id.item_image_all);
            holder.item_image = (LinearLayout) convertView.findViewById(R.id.item_image);
            holder.item_image2 = (LinearLayout) convertView.findViewById(R.id.item_image2);
            holder.ll_event2 = (LinearLayout) convertView.findViewById(R.id.ll_event2);
            holder.ll_event3 = (LinearLayout) convertView.findViewById(R.id.ll_event3);
            holder.ll_event4 = (LinearLayout) convertView.findViewById(R.id.ll_event4);
            holder.title_item = (RelativeLayout) convertView.findViewById(R.id.event_title_item);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ViewGroup.LayoutParams params3 = holder.item_image2.getLayoutParams();
        params3.width = getDisplayMetricsWidth();
        params3.height = (int) (params3.width * 1.0 / 3);
        holder.item_image2.setLayoutParams(params3);

        ViewGroup.LayoutParams params2 = holder.item_image.getLayoutParams();
        params2.width = getDisplayMetricsWidth();
        params2.height = params3.height;
        holder.item_image.setLayoutParams(params2);


        initSuggest(holder, position);
        return convertView;
    }


    private void initSuggest(ViewHolder holder, int position) {

        if (TextUtils.isEmpty(mylist.get(position).getTitle())) {
            holder.title_item.setVisibility(View.GONE);
            holder.first_item.setVisibility(View.GONE);
        } else {
            holder.title_item.setVisibility(View.VISIBLE);
            holder.first_item.setVisibility(View.VISIBLE);
            holder.event_all_title.setText(mylist.get(position).getTitle());
        }


        int size=mylist.get(position).getAdsense().size();

        if (size > 0) {
            holder.item_image_all.setVisibility(View.VISIBLE);
            if(size==1||size==4){
                holder.item_image.setVisibility(View.VISIBLE);
                holder.first_item.setVisibility(View.VISIBLE);
                left1 = mylist.get(position).getAdsense().get(0);
                imageLoader.setImageRes(holder.iv_event, left1.getImg());
                final String link1 = left1.getLink();
                holder.iv_event.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ECJiaOpenType.getDefault().startAction(context, link1);
                    }
                });
            }else{
                holder.item_image.setVisibility(View.GONE);
                holder.first_item.setVisibility(View.GONE);
            }

            if(size>1){
                holder.item_image2.setVisibility(View.VISIBLE);
                if(size<=3){
                    left2 = mylist.get(position).getAdsense().get(0);
                    final String link2 = left2.getLink();
                    imageLoader.setImageRes(holder.iv_event2, left2.getImg());
                    holder.iv_event2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ECJiaOpenType.getDefault().startAction(context, link2);
                        }
                    });
                    left3 = mylist.get(position).getAdsense().get(1);
                    final String link3 = left3.getLink();
                    imageLoader.setImageRes(holder.iv_event3, left3.getImg());
                    holder.iv_event3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ECJiaOpenType.getDefault().startAction(context, link3);
                        }
                    });
                    if(size==3){
                        holder.ll_event4.setVisibility(View.VISIBLE);
                        left4=mylist.get(position).getAdsense().get(2);
                        final String link4 = left4.getLink();
                        imageLoader.setImageRes(holder.iv_event4, left4.getImg());
                        holder.iv_event4.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ECJiaOpenType.getDefault().startAction(context, link4);
                            }
                        });
                    } else {
                        holder.ll_event4.setVisibility(View.INVISIBLE);
                    }

                }else{
                    left2 = mylist.get(position).getAdsense().get(1);
                    final String link2 = left2.getLink();
                    imageLoader.setImageRes(holder.iv_event2, left2.getImg());
                    holder.iv_event2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ECJiaOpenType.getDefault().startAction(context, link2);
                        }
                    });
                    left3 = mylist.get(position).getAdsense().get(2);
                    final String link3 = left3.getLink();
                    imageLoader.setImageRes(holder.iv_event3, left3.getImg());
                    holder.iv_event3.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ECJiaOpenType.getDefault().startAction(context, link3);
                        }
                    });
                    holder.ll_event4.setVisibility(View.VISIBLE);
                    left4=mylist.get(position).getAdsense().get(3);
                    final String link4 = left4.getLink();
                    imageLoader.setImageRes(holder.iv_event4, left4.getImg());
                    holder.iv_event4.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ECJiaOpenType.getDefault().startAction(context, link4);
                        }
                    });
                }
            } else {
                holder.item_image2.setVisibility(View.GONE);
            }
        } else {
            holder.item_image_all.setVisibility(View.GONE);
        }

    }


    private class ViewHolder {
        SelectableRoundedImageView iv_event, iv_event2, iv_event3, iv_event4;
        View event_title,first_item;
        LinearLayout item_image,item_image_all, item_image2,ll_event2,ll_event3,ll_event4;
        RelativeLayout title_item;
        public TextView event_all_title;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }
}
