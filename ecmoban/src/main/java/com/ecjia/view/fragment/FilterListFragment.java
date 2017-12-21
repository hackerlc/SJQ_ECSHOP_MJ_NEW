package com.ecjia.view.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.NewBaseFragment;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.FilterToolbarDataBindingBinding;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 布局页面筛选模块
 * Created by YichenZ on 2017/2/9 10:20.
 */

@SuppressLint("ValidFragment")
public class FilterListFragment extends NewBaseFragment {
    FilterToolbarDataBindingBinding mBinding;

    private TitleCellHolder tabOneCellHolder = new TitleCellHolder();
    private TitleCellHolder tabTwoCellHolder = new TitleCellHolder();
    private TitleCellHolder tabThreeCellHolder = new TitleCellHolder();
    private TitleCellHolder tabFourCellHolder = new TitleCellHolder();
    private int[] titleStr={R.string.new_good,R.string.popularity,R.string.price,R.string.filter};

    public FilterListFragment(int... titleStr){
        for (int i = 0; i < titleStr.length; i++) {
            if(i==4)
                break;
            this.titleStr[i]=titleStr[i];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater,R.layout.filter_toolbar_data_binding,container,false);

        Resources resource = (Resources) getActivity().getBaseContext().getResources();
        ColorStateList selectedTextColor = (ColorStateList) resource
                .getColorStateList(R.color.filter_text_color);
        ColorStateList selectTextColor = (ColorStateList) resource
                .getColorStateList(R.color.main_font_color_f82d7c);

        tabOneCellHolder.titleTextView = mBinding.filterTitleTabone;
        tabTwoCellHolder.titleTextView = mBinding.filterTitleTabtwo;
        tabThreeCellHolder.titleTextView = mBinding.filterTitleTabthree;
        tabFourCellHolder.titleTextView = mBinding.filterTitleTabfour;

        tabOneCellHolder.titleTextView.setText(titleStr[0]);
        tabTwoCellHolder.titleTextView.setText(titleStr[1]);
        tabThreeCellHolder.titleTextView.setText(titleStr[2]);
        tabFourCellHolder.titleTextView.setText(titleStr[3]);

        tabOneCellHolder.orderIconImageView = mBinding.filterOrderTabone;
        tabTwoCellHolder.orderIconImageView = mBinding.filterOrderTabtwo;
        tabThreeCellHolder.orderIconImageView = mBinding.filterOrderTabthree;
        tabFourCellHolder.orderIconImageView = mBinding.filterOrderTabfour;

        tabOneCellHolder.tabRelative = mBinding.tabOne;
        tabTwoCellHolder.tabRelative = mBinding.tabTwo;
        tabThreeCellHolder.tabRelative = mBinding.tabThree;
        tabFourCellHolder.tabRelative = mBinding.tabfour;

        tabOneCellHolder.tabRelative.setVisibility(tabOneCellHolder.show);
        tabTwoCellHolder.tabRelative.setVisibility(tabTwoCellHolder.show);
        tabThreeCellHolder.tabRelative.setVisibility(tabThreeCellHolder.show);
        tabFourCellHolder.tabRelative.setVisibility(tabFourCellHolder.show);

        tabOneCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabOneCellHolder.orderIconImageView
                                .setImageResource(R.drawable.item_grid_filter_down_active_arrow);
                        tabOneCellHolder.orderIconImageView.setWillNotCacheDrawing(true);
                        tabOneCellHolder.titleTextView.setTextColor(selectTextColor);


                        tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabFourCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);
                        mBinding.filterOne.setVisibility(View.VISIBLE);
                        mBinding.filterTwo.setVisibility(View.GONE);
                        mBinding.filterThree.setVisibility(View.GONE);
                        mBinding.filterFour.setVisibility(View.GONE);
                        tabOneCellHolder.mTitleCellOnClickListener.onClick(v);
                    }
                });
        tabTwoCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabTwoCellHolder.orderIconImageView
                                .setImageResource(R.drawable.item_grid_filter_down_active_arrow);
                        tabTwoCellHolder.titleTextView.setTextColor(selectTextColor);


                        tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabFourCellHolder.titleTextView.setTextColor(selectedTextColor);
                        mBinding.filterOne.setVisibility(View.GONE);
                        mBinding.filterTwo.setVisibility(View.VISIBLE);
                        mBinding.filterThree.setVisibility(View.GONE);
                        mBinding.filterFour.setVisibility(View.GONE);
                        tabTwoCellHolder.mTitleCellOnClickListener.onClick(v);
                    }
                });
        tabThreeCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabThreeCellHolder.titleTextView.setTextColor(selectTextColor);

                        tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabFourCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);
                        mBinding.filterOne.setVisibility(View.GONE);
                        mBinding.filterTwo.setVisibility(View.GONE);
                        mBinding.filterThree.setVisibility(View.VISIBLE);
                        mBinding.filterFour.setVisibility(View.GONE);

                        tabThreeCellHolder.mTitleCellOnClickListener.onClick(v);
                    }
                });
        tabFourCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabFourCellHolder.mTitleCellOnClickListener.onClick(v);

                        tabFourCellHolder.titleTextView.setTextColor(selectTextColor);

                        tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

                        tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

                        mBinding.filterOne.setVisibility(View.GONE);
                        mBinding.filterTwo.setVisibility(View.GONE);
                        mBinding.filterThree.setVisibility(View.GONE);
                        mBinding.filterFour.setVisibility(View.VISIBLE);
                    }
                });

        return mBinding.getRoot();
    }

    public void initView(){
        Resources resource = (Resources) getActivity().getBaseContext().getResources();
        ColorStateList selectedTextColor = (ColorStateList) resource
                .getColorStateList(R.color.filter_text_color);

    }

    public class TitleCellHolder {
        public ImageView triangleImageView;
        public TextView titleTextView;
        public ImageView orderIconImageView;
        public RelativeLayout tabRelative;
        public int show=View.VISIBLE;
        private TitleCellOnClickListener mTitleCellOnClickListener;

        public void setTitleCellOnClickListener(TitleCellOnClickListener titleCellOnClickListener) {
            mTitleCellOnClickListener = titleCellOnClickListener;
        }

        private Resources resource;
    }

    public TitleCellHolder getTabOneCellHolder() {
        return tabOneCellHolder;
    }

    public void setTabOneCellHolder(TitleCellHolder tabOneCellHolder) {
        this.tabOneCellHolder = tabOneCellHolder;
    }

    public TitleCellHolder getTabTwoCellHolder() {
        return tabTwoCellHolder;
    }

    public void setTabTwoCellHolder(TitleCellHolder tabTwoCellHolder) {
        this.tabTwoCellHolder = tabTwoCellHolder;
    }

    public TitleCellHolder getTabThreeCellHolder() {
        return tabThreeCellHolder;
    }

    public void setTabThreeCellHolder(TitleCellHolder tabThreeCellHolder) {
        this.tabThreeCellHolder = tabThreeCellHolder;
    }

    public TitleCellHolder getTabFourCellHolder() {
        return tabFourCellHolder;
    }

    public void setTabFourCellHolder(TitleCellHolder tabFourCellHolder) {
        this.tabFourCellHolder = tabFourCellHolder;
    }

    public interface TitleCellOnClickListener{
        void onClick(View v);
    }
}
