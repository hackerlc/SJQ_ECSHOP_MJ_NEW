package com.ecjia.view.activity.goodsdetail.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.xlratingbar.XLHRatingBar;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.FullScreenViewPagerActivity;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;
import com.ecjia.view.adapter.ECJiaBaseAdapter;
import com.ecjia.util.ImageLoaderForLV;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adam on 2015/3/26.
 */
public class GoodsdetailCommentAdapter extends ECJiaBaseAdapter<COMMENT_LIST> {

    private LayoutInflater mInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    SimpleDateFormat format;

    public GoodsdetailCommentAdapter(Context context, ArrayList<COMMENT_LIST> list) {
        super(context, list);
        mInflater = LayoutInflater.from(context);
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    protected ECJiaCellHolder createCellHolder(View cellView) {
        return null;
    }

    @Override
    protected View bindData(int position, View cellView, ViewGroup parent, ECJiaCellHolder h) {
        return null;
    }


    @Override
    public View createCellView() {
        return null;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public COMMENT_LIST getItem(int position) {
        return dataList.get(position);
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
            convertView = mInflater.inflate(R.layout.comment_item, null);

            holder.avatar_img = (ImageView) convertView.findViewById(R.id.avatar_img);
            holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.comment_rank = (XLHRatingBar) convertView.findViewById(R.id.comment_rank);
            holder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            holder.goods_attr = (TextView) convertView.findViewById(R.id.goods_attr);
            holder.showorder_image_ll = (LinearLayout) convertView.findViewById(R.id.showorder_image_ll);

            holder.showorder_image1 = (ImageView) convertView.findViewById(R.id.showorder_image1);
            holder.showorder_image2 = (ImageView) convertView.findViewById(R.id.showorder_image2);
            holder.showorder_image3 = (ImageView) convertView.findViewById(R.id.showorder_image3);
            holder.showorder_image4 = (ImageView) convertView.findViewById(R.id.showorder_image4);
            holder.showorder_image5 = (ImageView) convertView.findViewById(R.id.showorder_image5);
            holder.div = convertView.findViewById(R.id.comment_div);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.user_name.setText(dataList.get(position).getUser_name());
        holder.comment_rank.setCountSelected(Integer.valueOf(dataList.get(position).getComment_rank()));
        holder.comment_time.setText(dataList.get(position).getComment_time());

        Date currentTime = null;
        try {
            currentTime = format.parse(dataList.get(position).getComment_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.comment_time.setText(format.format(currentTime));

        holder.comment_content.setText(dataList.get(position).getComment_content());
        holder.goods_attr.setText(dataList.get(position).getGoods_attr());
        if (TextUtils.isEmpty(dataList.get(position).getGoods_attr())) {
            holder.goods_attr.setVisibility(View.GONE);
        } else {
            holder.goods_attr.setVisibility(View.VISIBLE);
            holder.goods_attr.setText(dataList.get(position).getGoods_attr());
        }

        ImageLoaderForLV.getInstance().setImageRes(holder.avatar_img, dataList.get(position).getAvatar_img(), AppConst.USERAVATER);

        if (dataList.get(position).getComment_image().size() > 0) {

            holder.showorder_image_ll.setVisibility(View.VISIBLE);
            int size = Math.min(5, dataList.get(position).getComment_image().size());

            if (size == 1) {
                holder.showorder_image1.setVisibility(View.VISIBLE);
                holder.showorder_image2.setVisibility(View.INVISIBLE);
                holder.showorder_image3.setVisibility(View.INVISIBLE);
                holder.showorder_image4.setVisibility(View.INVISIBLE);
                holder.showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(0), holder.showorder_image1);
                setImageOnClick(holder.showorder_image1, dataList.get(position).getComment_image(), 0);
            } else if (size == 2) {
                holder.showorder_image1.setVisibility(View.VISIBLE);
                holder.showorder_image2.setVisibility(View.VISIBLE);
                holder.showorder_image3.setVisibility(View.INVISIBLE);
                holder.showorder_image4.setVisibility(View.INVISIBLE);
                holder.showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(0), holder.showorder_image1);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(1), holder.showorder_image2);


                setImageOnClick(holder.showorder_image1, dataList.get(position).getComment_image(), 0);
                setImageOnClick(holder.showorder_image2, dataList.get(position).getComment_image(), 1);

            } else if (size == 3) {
                holder.showorder_image1.setVisibility(View.VISIBLE);
                holder.showorder_image2.setVisibility(View.VISIBLE);
                holder.showorder_image3.setVisibility(View.VISIBLE);
                holder.showorder_image4.setVisibility(View.INVISIBLE);
                holder.showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(0), holder.showorder_image1);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(1), holder.showorder_image2);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(2), holder.showorder_image3);

                setImageOnClick(holder.showorder_image1, dataList.get(position).getComment_image(), 0);
                setImageOnClick(holder.showorder_image2, dataList.get(position).getComment_image(), 1);
                setImageOnClick(holder.showorder_image3, dataList.get(position).getComment_image(), 2);
            } else if (size == 4) {
                holder.showorder_image1.setVisibility(View.VISIBLE);
                holder.showorder_image2.setVisibility(View.VISIBLE);
                holder.showorder_image3.setVisibility(View.VISIBLE);
                holder.showorder_image4.setVisibility(View.VISIBLE);
                holder.showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(0), holder.showorder_image1);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(1), holder.showorder_image2);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(2), holder.showorder_image3);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(3), holder.showorder_image4);

                setImageOnClick(holder.showorder_image1, dataList.get(position).getComment_image(), 0);
                setImageOnClick(holder.showorder_image2, dataList.get(position).getComment_image(), 1);
                setImageOnClick(holder.showorder_image3, dataList.get(position).getComment_image(), 2);
                setImageOnClick(holder.showorder_image4, dataList.get(position).getComment_image(), 3);
            } else if (size >= 5) {
                holder.showorder_image1.setVisibility(View.VISIBLE);
                holder.showorder_image2.setVisibility(View.VISIBLE);
                holder.showorder_image3.setVisibility(View.VISIBLE);
                holder.showorder_image4.setVisibility(View.VISIBLE);
                holder.showorder_image5.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(0), holder.showorder_image1);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(1), holder.showorder_image2);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(2), holder.showorder_image3);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(3), holder.showorder_image4);
                ImageLoader.getInstance().displayImage(dataList.get(position).getComment_image().get(4), holder.showorder_image5);

                setImageOnClick(holder.showorder_image1, dataList.get(position).getComment_image(), 0);
                setImageOnClick(holder.showorder_image2, dataList.get(position).getComment_image(), 1);
                setImageOnClick(holder.showorder_image3, dataList.get(position).getComment_image(), 2);
                setImageOnClick(holder.showorder_image4, dataList.get(position).getComment_image(), 3);
                setImageOnClick(holder.showorder_image5, dataList.get(position).getComment_image(), 4);
            } else {
                holder.showorder_image1.setVisibility(View.GONE);
                holder.showorder_image2.setVisibility(View.GONE);
                holder.showorder_image3.setVisibility(View.GONE);
                holder.showorder_image4.setVisibility(View.GONE);
                holder.showorder_image5.setVisibility(View.GONE);
            }
        } else {
            holder.showorder_image_ll.setVisibility(View.GONE);
        }


        if (position == dataList.size() - 1) {
            holder.div.setVisibility(View.GONE);
        } else {
            holder.div.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private ImageView avatar_img;
        private TextView user_name;
        private XLHRatingBar comment_rank;
        private TextView comment_time;
        private TextView comment_content;
        private TextView goods_attr;
        private LinearLayout showorder_image_ll;
        private ImageView showorder_image1;
        private ImageView showorder_image2;
        private ImageView showorder_image3;
        private ImageView showorder_image4;
        private ImageView showorder_image5;
        private View div;

    }

    void setImageOnClick(ImageView showorder_image, final ArrayList<String> images, final int position) {

        showorder_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, FullScreenViewPagerActivity.class);
                it.putExtra("position", position);
                ArrayList<String> pictures = new ArrayList<String>();
                int size = Math.min(5, images.size());
                for (int i = 0; i < size; i++) {
                    pictures.add(images.get(i));
                }
                it.putExtra("size", size);
                it.putExtra("pictures", pictures);
                mContext.startActivity(it);
                ((Activity) mContext).overridePendingTransition(R.anim.my_scale_action, R.anim.my_scale_action);
            }
        });
    }
}