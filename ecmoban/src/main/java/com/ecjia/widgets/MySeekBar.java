package com.ecjia.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-16.
 */

public class MySeekBar extends LinearLayout {

    private Context context;

    private TextView tv_goods_price1, tv_goods_price2, tv_goods_price3;

    private TextView tv_goods_price1_content, tv_goods_price2_content, tv_goods_price3_content;

    private CheckBox cb_price1, cb_price2, cb_price3;

    private View view_zan1, view_zan2;

    private ProgressBar progress_1, progress_2;
    private double nowGoodPrice;


    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_my_seekbar, this);
        tv_goods_price1 = (TextView) view.findViewById(R.id.tv_goods_price1);
        tv_goods_price2 = (TextView) view.findViewById(R.id.tv_goods_price2);
        tv_goods_price3 = (TextView) view.findViewById(R.id.tv_goods_price3);

        tv_goods_price1_content = (TextView) view.findViewById(R.id.tv_goods_price1_content);
        tv_goods_price2_content = (TextView) view.findViewById(R.id.tv_goods_price2_content);
        tv_goods_price3_content = (TextView) view.findViewById(R.id.tv_goods_price3_content);

        cb_price1 = (CheckBox) view.findViewById(R.id.cb_price1);
        cb_price2 = (CheckBox) view.findViewById(R.id.cb_price2);
        cb_price3 = (CheckBox) view.findViewById(R.id.cb_price3);

        progress_1 = (ProgressBar) view.findViewById(R.id.progress_1);
        progress_2 = (ProgressBar) view.findViewById(R.id.progress_2);

        view_zan1 = view.findViewById(R.id.view_zan1);
        view_zan2 = view.findViewById(R.id.view_zan2);

    }

    public void setData(double price1, double price2, double price3, int price1_num, int price2_num, int price3_num, int nowNum) {
        tv_goods_price1.setText(price1 + "");
        tv_goods_price2.setText(price2 + "");

        tv_goods_price1_content.setText(price1_num + "");
        tv_goods_price2_content.setText(price2_num + "");

//        nowNum=price1_num>nowNum?0:price1_num-nowNum;

        //判断两档价格还是三档
        if (price3 != 0 && price3_num != 0) {//三档
            tv_goods_price3.setVisibility(View.VISIBLE);
            progress_2.setVisibility(View.VISIBLE);
            tv_goods_price3_content.setVisibility(View.VISIBLE);
            view_zan1.setVisibility(View.VISIBLE);
            view_zan2.setVisibility(View.VISIBLE);

            tv_goods_price3.setText(price3 + "");
            tv_goods_price3_content.setText(price3_num + "");

            progress_1.setMax(price2_num);
            progress_2.setMax(price3_num - price2_num);

            if (nowNum < price2_num) {
                if (nowNum > price1_num) {
                    cb_price1.setChecked(true);
                    tv_goods_price1.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                    tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
                } else {
                    tv_goods_price1.setTextColor(getResources().getColor(R.color._ffa6cf));
//                    tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_12));
//                    progress_1.setProgress(0);
                }
                progress_1.setProgress(price1_num>nowNum?0:nowNum-price1_num);
                nowGoodPrice = price1;
            } else {
                cb_price1.setChecked(true);
                cb_price2.setChecked(true);
                tv_goods_price1.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
                tv_goods_price2.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                tv_goods_price2.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
                if (nowNum < price3_num) {
                    tv_goods_price3.setTextColor(getResources().getColor(R.color._ffa6cf));
//                    tv_goods_price3.setTextSize(getResources().getDimension(R.dimen.texte_size_12));
                    nowGoodPrice = price2;
                } else {
                    cb_price3.setChecked(true);
                    tv_goods_price3.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                    tv_goods_price3.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
                    nowGoodPrice = price3;
                }
                progress_1.setProgress(price2_num);
                progress_2.setProgress(nowNum - price2_num);
            }

        } else if (price2 != 0 && price2_num != 0 && price3 == 0 && price3_num == 0) {//二档
            tv_goods_price3.setVisibility(View.GONE);
            progress_2.setVisibility(View.GONE);
            tv_goods_price3_content.setVisibility(View.GONE);
            view_zan1.setVisibility(View.GONE);
            view_zan2.setVisibility(View.GONE);
            cb_price3.setVisibility(View.GONE);
            progress_1.setMax(price2_num);
            progress_1.setProgress(nowNum >= price2_num ? price2_num : nowNum);
            if (nowNum > price1_num) {
                tv_goods_price1.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
                cb_price1.setChecked(true);
            } else {
                tv_goods_price1.setTextColor(getResources().getColor(R.color._ffa6cf));
//                tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_12));
            }

            if (nowNum >= price2_num) {
                nowGoodPrice = price2;
                cb_price2.setChecked(true);
                tv_goods_price2.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                tv_goods_price2.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
            } else {
                tv_goods_price2.setTextColor(getResources().getColor(R.color._ffa6cf));
//                tv_goods_price2.setTextSize(getResources().getDimension(R.dimen.texte_size_12));
                nowGoodPrice = price1;
            }
        }else{//一档价格
            tv_goods_price1.setText( "");
            tv_goods_price2.setText(price1 + "");

            tv_goods_price1_content.setText( "");
            tv_goods_price2_content.setText(price1_num + "");

            tv_goods_price3.setVisibility(View.GONE);
            progress_2.setVisibility(View.GONE);
            tv_goods_price3_content.setVisibility(View.GONE);
            view_zan1.setVisibility(View.GONE);
            view_zan2.setVisibility(View.GONE);
            cb_price3.setVisibility(View.GONE);
            progress_1.setMax(price1_num);
            progress_1.setProgress(nowNum >= price1_num ? price1_num : nowNum);

//            if (nowNum > price1_num) {
//                tv_goods_price1.setTextColor(getResources().getColor(R.color.TextColorWhite));
////                tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
//                cb_price1.setChecked(true);
//            } else {
//                tv_goods_price1.setTextColor(getResources().getColor(R.color._ffa6cf));
////                tv_goods_price1.setTextSize(getResources().getDimension(R.dimen.texte_size_12));
//            }

            if (nowNum >= price1_num) {
                nowGoodPrice = price2;
                cb_price2.setChecked(true);
                tv_goods_price2.setTextColor(getResources().getColor(R.color.TextColorWhite));
//                tv_goods_price2.setTextSize(getResources().getDimension(R.dimen.texte_size_16));
            } else {
                tv_goods_price2.setTextColor(getResources().getColor(R.color._ffa6cf));
//                tv_goods_price2.setTextSize(getResources().getDimension(R.dimen.texte_size_12));
                nowGoodPrice = 0;
            }
        }
    }

    public double getNowGoodsPrice() {
        return nowGoodPrice;
    }

}
