package com.ecjia.view.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.widgets.AutoReturnView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.entity.responsemodel.COLLECT_LIST;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CollectAdapter extends BaseAdapter {

    private Context context;
    public int flag;
    private LayoutInflater inflater;
    public ArrayList<HashMap<String, String>> list;
    COLLECT_LIST collect;
    public Handler parentHandler;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    int end;

    public CollectAdapter(Context context,
                          ArrayList<HashMap<String, String>> list, int flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
        end=list.size()-1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            collect = COLLECT_LIST.fromJson(new JSONObject(list.get(position)
                    .get("content")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.collect_item, null);
            holder.collect_img = (ImageView) convertView
                    .findViewById(R.id.collect_img);
            holder.goodname = (AutoReturnView) convertView
                    .findViewById(R.id.collect_goodname);
            holder.goodprice = (TextView) convertView
                    .findViewById(R.id.collect_goodprice);
            holder.del = (CheckBox) convertView
                    .findViewById(R.id.collect_item_del);
            holder.collect_item = (LinearLayout) convertView
                    .findViewById(R.id.collect_item);
            holder.topline = convertView.findViewById(R.id.collect_top_line);
            holder.buttomline = convertView
                    .findViewById(R.id.collect_buttom_line);
            holder.shortline=convertView.findViewById(R.id.collect_short_line);
            holder.collect_check_item = (LinearLayout) convertView
                    .findViewById(R.id.collect_check_item);
            holder.collect_rightitem = (LinearLayout) convertView
                    .findViewById(R.id.collect_rightitem);
            holder.marketprice = (TextView) convertView
                    .findViewById(R.id.collect_market_price);
            holder.marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.tv_saving = (TextView) convertView
                    .findViewById(R.id.tv_saving);
            holder.ll_goodlist_mb = (LinearLayout) convertView.findViewById(R.id.ll_goodlist_mb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.topline.setVisibility(View.VISIBLE);
        } else {
            holder.topline.setVisibility(View.GONE);
        }

        if(position!=end){
            holder.shortline.setVisibility(View.VISIBLE);
            holder.buttomline.setVisibility(View.GONE);
        }else{
            holder.shortline.setVisibility(View.GONE);
            holder.buttomline.setVisibility(View.VISIBLE);
        }


        if(AppConst.MOBILEBUY_GOODS.equals(collect.getActivity_type())){
            holder.ll_goodlist_mb.setVisibility(View.VISIBLE);
            holder.tv_saving.setText(collect.getFormatted_saving_price());
        }else{
            holder.ll_goodlist_mb.setVisibility(View.GONE);
        }

        if("免费".equals(collect.getShop_price())){
            holder.marketprice.setVisibility(View.INVISIBLE);
        }else {
            holder.marketprice.setVisibility(View.VISIBLE);
            holder.marketprice.setText(collect.getMarket_price());
        }

        if (flag == 1) {
            holder.collect_check_item.setVisibility(View.GONE);
            holder.collect_item
                    .setBackgroundResource(R.drawable.new_nothing_bg);
        } else if (flag == 2) {// 显示删除选项
            holder.collect_check_item.setVisibility(View.VISIBLE);
        }

        holder.collect_rightitem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                COLLECT_LIST collect1 = null;
                try {
                    collect1 = COLLECT_LIST.fromJson(new JSONObject(list.get(position)
                            .get("content")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (holder.collect_check_item.getVisibility() != View.VISIBLE) {
                    Intent intent = new Intent(context,
                            GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, collect1.getGoods_id()+"");
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(
                            R.anim.push_right_in, R.anim.push_right_out);
                }

            }
        });
        holder.collect_check_item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (holder.del.isChecked()) {
                    holder.del.setChecked(false);
                    list.get(position).put("flag", "false");
                } else {
                    holder.del.setChecked(true);
                    list.get(position).put("flag", "true");
                }
                if (haveselect()) {
                    Message msg = new Message();
                    msg.arg1 = 1;
                    parentHandler.handleMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.arg1 = 0;
                    parentHandler.handleMessage(msg);
                }
                CollectAdapter.this.notifyDataSetChanged();
                LG.i("====是否选中==="
                        + list.get(position).get("flag").equals("true"));
            }
        });
        holder.del.setChecked(list.get(position).get("flag").equals("true"));
        if (holder.collect_check_item.getVisibility() == View.VISIBLE) {
            if (!list.get(position).get("flag").equals("true")) {
                holder.collect_item
                        .setBackgroundResource(R.drawable.new_nothing_bg);
                holder.collect_rightitem
                        .setBackgroundResource(R.drawable.new_nothing_bg);
            } else {
                holder.collect_item
                        .setBackgroundResource(R.drawable.new_good_distance);
                holder.collect_rightitem
                        .setBackgroundResource(R.drawable.new_good_distance);
            }
        } else {
            holder.collect_rightitem
                    .setBackgroundResource(R.drawable.selecter_newitem_press);
        }


        holder.goodname.setContent(collect.getName());
        float promote = FormatUtil.formatStrToFloat(collect.getPromote_price());
        if (promote!=0) {
            holder.goodprice.setText(collect.getPromote_price());
            holder.marketprice.setText(collect.getShop_price());
        } else {
            float str = FormatUtil.formatStrToFloat(collect.getShop_price());
            if (str==0) {
                holder.goodprice.setText("免费");
                holder.marketprice.setText("");
            } else {
                holder.goodprice.setText(collect.getShop_price());
                holder.marketprice.setText(collect.getMarket_price());
            }
        }

        ImageLoaderForLV.getInstance(context).setImageRes(holder.collect_img, collect.getImg().getSmall());


        return convertView;
    }

    class ViewHolder {
        LinearLayout collect_item;
        LinearLayout collect_check_item;
        LinearLayout collect_rightitem;
        ImageView collect_img;
        AutoReturnView goodname;
        TextView goodprice;
        TextView marketprice;
        View topline;
        View buttomline,shortline;
        CheckBox del;
        TextView tv_saving;
        LinearLayout ll_goodlist_mb;
    }

    // 是否有需要删除的选项
    private boolean haveselect() {
        for (int i = 0; i < AppConst.datalist.size(); i++) {
            if (AppConst.datalist.get(i).get("flag").equals("true")) {
                return true;
            }
        }
        return false;
    }
}
