package com.ecjia.view.activity.goodsdetail.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.xlratingbar.XLHRatingBar;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.FullScreenViewPagerActivity;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;
import com.ecjia.util.ImageLoaderForLV;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adam on 2016/10/24.
 */
public class GoodsCommetView extends LinearLayout {
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

    private View divider;

    private Context mContext;
    SimpleDateFormat format;
    public GoodsCommetView(Context context) {
        super(context);
        mContext = context;
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    public GoodsCommetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    public GoodsCommetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    void init() {

        if (null == avatar_img) avatar_img = (ImageView) findViewById(R.id.avatar_img);
        if (null == user_name) user_name = (TextView) findViewById(R.id.user_name);
        if (null == comment_rank) comment_rank = (XLHRatingBar) findViewById(R.id.comment_rank);
        if (null == comment_time) comment_time = (TextView) findViewById(R.id.comment_time);
        if (null == comment_content) comment_content = (TextView) findViewById(R.id.comment_content);
        if (null == goods_attr) goods_attr = (TextView) findViewById(R.id.goods_attr);
        if (null == showorder_image_ll) showorder_image_ll = (LinearLayout) findViewById(R.id.showorder_image_ll);

        if (null == showorder_image1) showorder_image1 = (ImageView) findViewById(R.id.showorder_image1);
        if (null == showorder_image2) showorder_image2 = (ImageView) findViewById(R.id.showorder_image2);
        if (null == showorder_image3) showorder_image3 = (ImageView) findViewById(R.id.showorder_image3);
        if (null == showorder_image4) showorder_image4 = (ImageView) findViewById(R.id.showorder_image4);
        if (null == showorder_image5) showorder_image5 = (ImageView) findViewById(R.id.showorder_image5);
        if (null == divider) divider = findViewById(R.id.comment_div);

    }


    public void bindData(COMMENT_LIST da_ta) {
        init();

        user_name.setText(da_ta.getUser_name());
        comment_rank.setCountSelected(Integer.valueOf(da_ta.getComment_rank()));

        Date currentTime = null;
        try {
            currentTime = format.parse(da_ta.getComment_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comment_time.setText(format.format(currentTime));

        comment_content.setText(da_ta.getComment_content());

        if (TextUtils.isEmpty(da_ta.getGoods_attr())){
            goods_attr.setVisibility(GONE);
        } else {
            goods_attr.setVisibility(VISIBLE);
            goods_attr.setText(da_ta.getGoods_attr());
        }

        ImageLoaderForLV.getInstance().setImageRes(avatar_img, da_ta.getAvatar_img(), AppConst.USERAVATER);

        if (da_ta.getComment_image().size() > 0) {

            showorder_image_ll.setVisibility(View.VISIBLE);
            int size = Math.min(5, da_ta.getComment_image().size());

            if (size == 1) {
                showorder_image1.setVisibility(View.VISIBLE);
                showorder_image2.setVisibility(View.INVISIBLE);
                showorder_image3.setVisibility(View.INVISIBLE);
                showorder_image4.setVisibility(View.INVISIBLE);
                showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(0), showorder_image1);

                setImageOnClick(showorder_image1, da_ta.getComment_image(), 0);
            } else if (size == 2) {
                showorder_image1.setVisibility(View.VISIBLE);
                showorder_image2.setVisibility(View.VISIBLE);
                showorder_image3.setVisibility(View.INVISIBLE);
                showorder_image4.setVisibility(View.INVISIBLE);
                showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(0), showorder_image1);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(1), showorder_image2);

                setImageOnClick(showorder_image1, da_ta.getComment_image(), 0);
                setImageOnClick(showorder_image2, da_ta.getComment_image(), 1);
            } else if (size == 3) {
                showorder_image1.setVisibility(View.VISIBLE);
                showorder_image2.setVisibility(View.VISIBLE);
                showorder_image3.setVisibility(View.VISIBLE);
                showorder_image4.setVisibility(View.INVISIBLE);
                showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(0), showorder_image1);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(1), showorder_image2);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(2), showorder_image3);

                setImageOnClick(showorder_image1, da_ta.getComment_image(), 0);
                setImageOnClick(showorder_image2, da_ta.getComment_image(), 1);
                setImageOnClick(showorder_image3, da_ta.getComment_image(), 2);
            } else if (size == 4) {
                showorder_image1.setVisibility(View.VISIBLE);
                showorder_image2.setVisibility(View.VISIBLE);
                showorder_image3.setVisibility(View.VISIBLE);
                showorder_image4.setVisibility(View.VISIBLE);
                showorder_image5.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(0), showorder_image1);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(1), showorder_image2);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(2), showorder_image3);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(3), showorder_image4);

                setImageOnClick(showorder_image1, da_ta.getComment_image(), 0);
                setImageOnClick(showorder_image2, da_ta.getComment_image(), 1);
                setImageOnClick(showorder_image3, da_ta.getComment_image(), 2);
                setImageOnClick(showorder_image4, da_ta.getComment_image(), 3);
            } else if (size >= 5) {
                showorder_image1.setVisibility(View.VISIBLE);
                showorder_image2.setVisibility(View.VISIBLE);
                showorder_image3.setVisibility(View.VISIBLE);
                showorder_image4.setVisibility(View.VISIBLE);
                showorder_image5.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(0), showorder_image1);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(1), showorder_image2);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(2), showorder_image3);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(3), showorder_image4);
                ImageLoader.getInstance().displayImage(da_ta.getComment_image().get(4), showorder_image5);

                setImageOnClick(showorder_image1, da_ta.getComment_image(), 0);
                setImageOnClick(showorder_image2, da_ta.getComment_image(), 1);
                setImageOnClick(showorder_image3, da_ta.getComment_image(), 2);
                setImageOnClick(showorder_image4, da_ta.getComment_image(), 3);
                setImageOnClick(showorder_image5, da_ta.getComment_image(), 4);
            } else {
                showorder_image1.setVisibility(View.GONE);
                showorder_image2.setVisibility(View.GONE);
                showorder_image3.setVisibility(View.GONE);
                showorder_image4.setVisibility(View.GONE);
                showorder_image5.setVisibility(View.GONE);
            }
        }

    }

    public void setDivliverVisible(int visible) {
        divider.setVisibility(visible);
    }


    void setImageOnClick(ImageView showorder_image, final ArrayList<String> images, final int position) {

        showorder_image.setOnClickListener(new OnClickListener() {
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