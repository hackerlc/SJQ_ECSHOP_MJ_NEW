package com.ecjia.view.adapter.adaptercell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;

public class MyHotCell extends LinearLayout {
	Context mContext;
	// 四张图片
	public ImageView good_cell_photo_one;
	public ImageView good_cell_photo_two;
	// 四个价格
	public TextView good_cell_price_one;
	public TextView good_cell_price_two;
	// 四个信息
	public TextView good_info_one;
	public TextView good_info_two;

    public FrameLayout fl_hot_left;
    public FrameLayout fl_hot_right;

	public LinearLayout good_cell_one;// 子布局
	public LinearLayout good_cell_two;//
	public LinearLayout  myhotcell2;
	public MyHotCell(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void cellinit() {

		if (null == good_cell_one) {
			good_cell_one = (LinearLayout) findViewById(R.id.newgood_item_one);
		}

		if (null == good_cell_two) {
			good_cell_two = (LinearLayout) findViewById(R.id.newgood_item_two);
		}

		if (null == good_cell_photo_one) {
			good_cell_photo_one = (ImageView) findViewById(R.id.new_gooditem_photo1);

		}

		if (null == good_cell_photo_two) {
			good_cell_photo_two = (ImageView) findViewById(R.id.new_gooditem_photo2);

		}


		if (null == good_cell_price_one) {
			good_cell_price_one = (TextView) findViewById(R.id.new_shop_price1);
		}

		if (null == good_cell_price_two) {
			good_cell_price_two = (TextView) findViewById(R.id.new_shop_price2);
		}


		if (null == good_info_one) {
			good_info_one = (TextView) findViewById(R.id.new_good_info1);
		}

		if (null == good_info_two) {
            good_info_two = (TextView) findViewById(R.id.new_good_info2);
		}

		if (null == fl_hot_left) {
            fl_hot_left = (FrameLayout) findViewById(R.id.fl_hot_left);
		}

		if (null == fl_hot_right) {
            fl_hot_right = (FrameLayout) findViewById(R.id.fl_hot_right);
		}

		if (null == myhotcell2) {
			myhotcell2 = (LinearLayout) findViewById(R.id.home_myhotcell2);
		}
	}
}
