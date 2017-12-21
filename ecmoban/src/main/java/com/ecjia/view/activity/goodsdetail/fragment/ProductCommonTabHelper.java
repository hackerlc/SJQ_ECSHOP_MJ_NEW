package com.ecjia.view.activity.goodsdetail.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.view.activity.goodsdetail.adapter.ProductDetailPagerAdapter;
import com.ecjia.view.activity.goodsdetail.fragment.interfaces.TabsHelper;

import java.util.ArrayList;


/**
 * 商品详情的商品介绍页
 */
public abstract class ProductCommonTabHelper implements TabsHelper, View.OnClickListener {

    private final Context mContext;
    private CommentModel commentModel;
    private String goods_id;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private ArrayList<String> titles = new ArrayList<>();

    private ProductDetailPagerAdapter pagerAdapter;

    private View comment_count_ll, comment_positive_ll, comment_moderate_ll, comment_negative_ll, comment_showorder_ll;
    private TextView comment_count_name, comment_positive_name, comment_moderate_name, comment_negative_name, comment_showorder_name;
    private TextView comment_count, comment_positive, comment_moderate, comment_negative, comment_showorder;
    private int unselected_color;
    private int selected_color;

    public ProductCommonTabHelper(ProductCommonFragment context, ViewGroup contentView) {
        mContext = context.getActivity();
        View mContentView = LayoutInflater.from(context.getActivity()).inflate(R.layout.fragment_goods_comment_top, null);
        initView(mContentView);
        contentView.addView(mContentView);
    }


    private void initView(View view) {
        //新加入控件
        selected_color = mContext.getResources().getColor(R.color.public_theme_color_normal);
        unselected_color = mContext.getResources().getColor(R.color.my_dark);
        comment_count_ll = view.findViewById(R.id.comment_count_ll);
        comment_positive_ll = view.findViewById(R.id.comment_positive_ll);
        comment_moderate_ll = view.findViewById(R.id.comment_moderate_ll);
        comment_negative_ll = view.findViewById(R.id.comment_negative_ll);
        comment_showorder_ll = view.findViewById(R.id.comment_showorder_ll);

        comment_count_name = (TextView) view.findViewById(R.id.comment_count_name);
        comment_positive_name = (TextView) view.findViewById(R.id.comment_positive_name);
        comment_moderate_name = (TextView) view.findViewById(R.id.comment_moderate_name);
        comment_negative_name = (TextView) view.findViewById(R.id.comment_negative_name);
        comment_showorder_name = (TextView) view.findViewById(R.id.comment_showorder_name);

        comment_count = (TextView) view.findViewById(R.id.comment_count);
        comment_positive = (TextView) view.findViewById(R.id.comment_positive);
        comment_moderate = (TextView) view.findViewById(R.id.comment_moderate);
        comment_negative = (TextView) view.findViewById(R.id.comment_negative);
        comment_showorder = (TextView) view.findViewById(R.id.comment_showorder);


        comment_count_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(1);
            }
        });
        comment_positive_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(2);

            }
        });
        comment_moderate_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(3);
            }
        });
        comment_negative_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(4);
            }
        });
        comment_showorder_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(5);
            }
        });

    }


    public void setTitles(ArrayList<String> tabNames, ArrayList<String> commentNum) {
        comment_count_name.setText(tabNames.get(0));
        comment_positive_name.setText(tabNames.get(1));
        comment_moderate_name.setText(tabNames.get(2));
        comment_negative_name.setText(tabNames.get(3));
        comment_showorder_name.setText(tabNames.get(4));
        comment_count.setText(commentNum.get(0));
        comment_positive.setText(commentNum.get(1));
        comment_moderate.setText(commentNum.get(2));
        comment_negative.setText(commentNum.get(3));
        comment_showorder.setText(commentNum.get(4));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void selectItem(int tabId) {
        setItemView(tabId);
        tabChanged(tabId);
    }

    @Override
    public void setItemView(int tabId) {
        if (tabId != selectedItem) {

            switch (selectedItem) {
                case 1:
                    comment_count_name.setTextColor(unselected_color);
                    comment_count.setTextColor(unselected_color);
                    break;
                case 2:
                    comment_positive_name.setTextColor(unselected_color);
                    comment_positive.setTextColor(unselected_color);
                    break;
                case 3:
                    comment_moderate_name.setTextColor(unselected_color);
                    comment_moderate.setTextColor(unselected_color);
                    break;
                case 4:
                    comment_negative_name.setTextColor(unselected_color);
                    comment_negative.setTextColor(unselected_color);
                    break;
                case 5:
                    comment_showorder_name.setTextColor(unselected_color);
                    comment_showorder.setTextColor(unselected_color);
                    break;
            }

            switch (tabId) {
                case 1:
                    comment_count_name.setTextColor(selected_color);
                    comment_count.setTextColor(selected_color);
                    break;
                case 2:
                    comment_positive_name.setTextColor(selected_color);
                    comment_positive.setTextColor(selected_color);
                    break;
                case 3:
                    comment_moderate_name.setTextColor(selected_color);
                    comment_moderate.setTextColor(selected_color);
                    break;
                case 4:
                    comment_negative_name.setTextColor(selected_color);
                    comment_negative.setTextColor(selected_color);
                    break;
                case 5:
                    comment_showorder_name.setTextColor(selected_color);
                    comment_showorder.setTextColor(selected_color);
                    break;
            }
        }
        selectedItem = tabId;
    }

    @Override
    public void initItemView() {
        comment_count_name.setTextColor(unselected_color);
        comment_count.setTextColor(unselected_color);
        comment_positive_name.setTextColor(unselected_color);
        comment_positive.setTextColor(unselected_color);
        comment_moderate_name.setTextColor(unselected_color);
        comment_moderate.setTextColor(unselected_color);
        comment_negative_name.setTextColor(unselected_color);
        comment_negative.setTextColor(unselected_color);
        comment_showorder_name.setTextColor(unselected_color);
        comment_showorder.setTextColor(unselected_color);
    }

    private int selectedItem = -1;//起始选中后的item的id，起始没有选中设置为-1

}
