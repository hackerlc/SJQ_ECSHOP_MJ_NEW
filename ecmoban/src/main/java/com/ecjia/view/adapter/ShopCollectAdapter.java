package com.ecjia.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.HorizontalListView;
import com.ecjia.widgets.webimageview.WebImageView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.entity.responsemodel.SELLERGOODS;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;

public class ShopCollectAdapter extends BaseAdapter {

    private Context context;
    public int flag;
    private LayoutInflater inflater;
    private ShopCollectHorizLVAdapter shopCollectHorizLVAdapter;
    public ArrayList<SELLERINFO> list;
    public Handler parentHandler;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public ShopCollectAdapter(Context context,
                              ArrayList<SELLERINFO> list, int flag) {
        this.context = context;
        this.list = list;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
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
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shop_collect_item, null);
            holder.collect_img = (WebImageView) convertView
                    .findViewById(R.id.collect_img);
            holder.name = (TextView) convertView
                    .findViewById(R.id.collect_name);
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
            holder.ll_collect_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_collect_bottom);
            holder.tv_newgoods_num=(TextView)convertView.findViewById(R.id.tv_newgoods_num);
            holder.shop_newgoodlist=(HorizontalListView)convertView.findViewById(R.id.shop_newgoodlist);
            holder.iv_open_arrow=(ImageView)convertView.findViewById(R.id.iv_open_arrow);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.topline.setVisibility(View.VISIBLE);
        } else {
            holder.topline.setVisibility(View.GONE);
        }

        if(position!=list.size()-1){
            holder.shortline.setVisibility(View.VISIBLE);
            holder.buttomline.setVisibility(View.GONE);
        }else{
            holder.shortline.setVisibility(View.GONE);
            holder.buttomline.setVisibility(View.VISIBLE);
        }


        if (flag == 1) {
            holder.collect_check_item.setVisibility(View.GONE);
            holder.iv_open_arrow.setVisibility(View.VISIBLE);
            holder.collect_item
                    .setBackgroundResource(R.drawable.new_nothing_bg);
        } else if (flag == 2) {// 显示删除选项
            holder.collect_check_item.setVisibility(View.VISIBLE);
            holder.iv_open_arrow.setVisibility(View.GONE);
            holder.ll_collect_bottom.setVisibility(View.GONE);
        }

        holder.collect_rightitem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(list.get(position).isClicked()){
                    list.get(position).setClicked(false);
                }else{
                    list.get(position).setClicked(true);
                }
                Message msg = new Message();
                msg.arg1 = 100;
                parentHandler.handleMessage(msg);
            }
        });

        if (holder.collect_check_item.getVisibility() != View.VISIBLE) {
            if(!list.get(position).isClicked()){
                holder.ll_collect_bottom.setVisibility(View.GONE);
                holder.iv_open_arrow.setImageResource(R.drawable.arrow_collect_down);
            }else{
                holder.ll_collect_bottom.setVisibility(View.VISIBLE);
                holder.iv_open_arrow.setImageResource(R.drawable.arrow_collect_up);
            }
        }

        holder.collect_check_item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (holder.del.isChecked()) {
                    holder.del.setChecked(false);
                    list.get(position).setChoose(false);
                } else {
                    holder.del.setChecked(true);
                    list.get(position).setChoose(true);
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
                ShopCollectAdapter.this.notifyDataSetChanged();
                LG.i("====是否选中==="
                        + list.get(position).isChoose());
            }
        });
        holder.del.setChecked(list.get(position).isChoose());
        if (holder.collect_check_item.getVisibility() == View.VISIBLE) {
            if (!list.get(position).isChoose()) {
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

        if(list.get(position).getSellergoods().size()>0){
            holder.collect_rightitem.setEnabled(true);
        }else{
            holder.collect_rightitem.setEnabled(false);
        }

        ArrayList<SELLERGOODS> finlist=new ArrayList<SELLERGOODS>();
        if(list.get(position).getSellergoods().size()>4){
            for (int i=0;i<4;i++){
                finlist.add(list.get(position).getSellergoods().get(i));
            }
            SELLERGOODS sellergoods=new SELLERGOODS();
            sellergoods.setId("+1");

            finlist.add(sellergoods);

            shopCollectHorizLVAdapter=new ShopCollectHorizLVAdapter(context,finlist);
        }else{
            shopCollectHorizLVAdapter=new ShopCollectHorizLVAdapter(context,list.get(position).getSellergoods());
        }

        final String sellerid=list.get(position).getId();

        final ArrayList<SELLERGOODS> arrayList =list.get(position).getSellergoods();

        holder.shop_newgoodlist.setAdapter(shopCollectHorizLVAdapter);

        holder.shop_newgoodlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String finid=arrayList.get(position).getId();
                if(!TextUtils.isEmpty(finid)&&"+1".equals(finid)){
                    Intent intent=new Intent(context, ShopListActivity.class);
                    intent.putExtra(IntentKeywords.MERCHANT_ID,sellerid);
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }else{
                    Intent intent=new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID,finid);
                    intent.putExtra(IntentKeywords.REC_TYPE,arrayList.get(position).getActivity_type());
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            }
        });

        holder.name.setText(list.get(position).getSeller_name());

        holder.tv_newgoods_num.setText(list.get(position).getSellergoods().size()+"");

        ImageLoaderForLV.getInstance(context).setImageRes(holder.collect_img, list.get(position).getSeller_logo());


        return convertView;
    }

    class ViewHolder {
        LinearLayout collect_item;
        LinearLayout collect_check_item;
        LinearLayout collect_rightitem;
        LinearLayout ll_collect_bottom;
        HorizontalListView shop_newgoodlist;
        WebImageView collect_img;
        TextView name;
        TextView tv_newgoods_num;
        ImageView iv_open_arrow;
        View topline;
        View buttomline,shortline;
        CheckBox del;
    }

    // 是否有需要删除的选项
    private boolean haveselect() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChoose()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 单击事件监听器
     */
    private onAdpItemClickListener mListener = null;

    public void setOnAdpItemClickListener(onAdpItemClickListener listener) {
        mListener = listener;
    }

    public interface onAdpItemClickListener {
        void onAdpItemClick(View v, int position);
    }
}
