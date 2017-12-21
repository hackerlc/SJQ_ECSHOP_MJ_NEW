package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.entity.responsemodel.SELLERINFO;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ShopListAdapter extends BaseAdapter {
    private Resources res;
    private Context context;
    private ArrayList<SELLERINFO> sellerlist;
    private ImageLoaderForLV imageloader;
    private int width, distance;

    public Handler messageHandler;

    public int eight_margin;

    public ShopListAdapter(Context context, ArrayList<SELLERINFO> sellerlist, int width) {
        this.context = context;
        this.sellerlist = sellerlist;
        this.width = width;
        eight_margin = (int) context.getResources().getDimension(R.dimen.eight_margin);
        res = context.getResources();
        distance = (int) res.getDimension(R.dimen.dp_5);
        imageloader = ImageLoaderForLV.getInstance(context);
    }

    public void setList(ArrayList<SELLERINFO> sellerlist) {
        this.sellerlist = sellerlist;
    }

    public ArrayList<SELLERINFO> getList() {
        return this.sellerlist;
    }

    @Override
    public int getCount() {
        return sellerlist.size();
    }

    @Override
    public Object getItem(int position) {
        return sellerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final SELLERINFO sellerinfo = sellerlist.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sellerlist_item, null);
            holder.shop_item = (LinearLayout) convertView.findViewById(R.id.shoplist_item);
            holder.seller_logo = (ImageView) convertView.findViewById(R.id.seller_logo);
            holder.seller_banner = (ImageView) convertView.findViewById(R.id.seller_banner);
            holder.seller_item_shopname = (TextView) convertView.findViewById(R.id.seller_item_shopname);
            holder.collect_num = (TextView) convertView.findViewById(R.id.seller_collect_num);
            holder.collected_item = (LinearLayout) convertView.findViewById(R.id.shoplist_collected);
            holder.uncollect_item = (LinearLayout) convertView.findViewById(R.id.shoplist_uncollect);
            holder.seller_desc = (TextView) convertView.findViewById(R.id.seller_desc);
            holder.tv_seller_distance = (TextView) convertView.findViewById(R.id.tv_seller_distance);

            holder.img1 = (ImageView) convertView.findViewById(R.id.goodimg_1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.goodimg_2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.goodimg_3);
            holder.img4 = (ImageView) convertView.findViewById(R.id.goodimg_4);
            holder.img5 = (ImageView) convertView.findViewById(R.id.goodimg_5);
            initImageView(holder.img1);
            initImageView(holder.img2);
            initImageView(holder.img3);
            initImageView(holder.img4);
            initImageView(holder.img5);
            convertView.setTag(holder);

            holder.div = convertView.findViewById(R.id.bottom_div);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initBigImageViewLayoutParamas(holder.shop_item.getChildAt(1));

        if (!TextUtils.isEmpty(sellerinfo.getSeller_banner())) {
            imageloader.setImageRes(holder.seller_banner, sellerinfo.getSeller_banner());
        }

        holder.collect_num.setText(sellerinfo.getFollower() + res.getString(R.string.follower_num));


        holder.collected_item.setVisibility(View.GONE);
        holder.uncollect_item.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(sellerinfo.getSeller_desc())) {
            holder.seller_desc.setText(sellerinfo.getSeller_desc());
        }

        holder.seller_item_shopname.setText(sellerinfo.getSeller_name());


        if(TextUtils.isEmpty(sellerinfo.getDistance())||"null".equals(sellerinfo.getDistance())){
            holder.tv_seller_distance.setText("");
        }else{
            holder.tv_seller_distance.setText(sellerinfo.getDistance());
        }


        holder.uncollect_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = position;
                messageHandler.sendMessage(msg);
            }
        });
        holder.collected_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 2;
                msg.arg1 = position;
                messageHandler.sendMessage(msg);
            }
        });
        holder.shop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopListActivity.class);
                intent.putExtra("sellerid", sellerinfo.getId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);

            }
        });
        int size = sellerinfo.getSellergoods().size();
        if (size > 0) {
            holder.img1.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img1, sellerinfo.getSellergoods().get(0).getImg().getThumb());
            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(0).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img1.setVisibility(View.INVISIBLE);
        }
        if (size > 1) {
            holder.img2.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img2, sellerinfo.getSellergoods().get(1).getImg().getThumb());
            holder.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(1).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img2.setVisibility(View.INVISIBLE);
        }
        if (size > 2) {
            holder.img3.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img3, sellerinfo.getSellergoods().get(2).getImg().getThumb());
            holder.img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(2).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img3.setVisibility(View.INVISIBLE);
        }

        if (size > 3) {
            holder.img4.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img4, sellerinfo.getSellergoods().get(3).getImg().getThumb());
            holder.img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(3).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img4.setVisibility(View.INVISIBLE);
        }

        if (size > 4) {
            holder.img5.setVisibility(View.VISIBLE);
            imageloader.setImageRes(holder.img5, sellerinfo.getSellergoods().get(4).getImg().getThumb());
            holder.img5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, sellerinfo.getSellergoods().get(4).getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }
            });
        } else {
            holder.img5.setVisibility(View.INVISIBLE);
        }
        holder.div.setVisibility(View.GONE);

        return convertView;
    }

    private class ViewHolder {
        private ImageView img1, img2, img3, img4, img5, seller_logo, seller_banner;
        private TextView seller_desc,tv_seller_distance;
        private LinearLayout shop_item, collected_item, uncollect_item;
        private TextView collect_num, seller_item_shopname;
        private View div;
    }

    void initBigImageViewLayoutParamas(View img) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width + eight_margin * 2, (width + eight_margin * 2) / 2);
        img.setLayoutParams(lp);
    }

    void initImageView(ImageView img) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                width / 5 - distance * 2, width / 5 - distance * 2);
        lp.setMargins(distance, distance, distance, distance);
        lp.height = width / 5 - distance * 2;
        img.setLayoutParams(lp);
    }

}
