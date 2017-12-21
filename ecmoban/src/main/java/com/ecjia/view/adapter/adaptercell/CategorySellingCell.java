package com.ecjia.view.adapter.adaptercell;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.util.LG;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.entity.responsemodel.CATEGORYGOODS;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.widgets.webimageview.WebImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;

public class CategorySellingCell extends LinearLayout {
	Context mContext;
	private WebImageView good_cell_photo_one;
	private WebImageView good_cell_photo_two;
	private WebImageView good_cell_photo_three;
	private TextView good_cell_name_one;
	private TextView good_cell_name_two;
	private TextView good_cell_name_three;
	private TextView good_cell_price_two;
	private TextView good_cell_price_three;

	private LinearLayout good_cell_one;
	private LinearLayout good_cell_two;
	private LinearLayout good_cell_three;
	CATEGORYGOODS categorygoods;
	Handler mHandler;
    protected ImageLoader imageLoader = ImageLoader.getInstance();


	public CategorySellingCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				bindDataDelay();
			}
		};
	}

	void init() {

		if (null == good_cell_one) {
			good_cell_one = (LinearLayout) findViewById(R.id.good_cell_one);
		}

		if (null == good_cell_two) {
			good_cell_two = (LinearLayout) findViewById(R.id.good_cell_two);
		}

		if (null == good_cell_three) {
			good_cell_three = (LinearLayout) findViewById(R.id.good_cell_three);
		}

		if (null == good_cell_photo_one) {
			good_cell_photo_one = (WebImageView) findViewById(R.id.good_cell_photo_one);
		}

		if (null == good_cell_photo_two) {
			good_cell_photo_two = (WebImageView) findViewById(R.id.good_cell_photo_two);
		}

		if (null == good_cell_photo_three) {
			good_cell_photo_three = (WebImageView) findViewById(R.id.good_cell_photo_three);
		}

		if (null == good_cell_name_one) {
			good_cell_name_one = (TextView) findViewById(R.id.good_cell_name_one);
		}

		if (null == good_cell_name_two) {
			good_cell_name_two = (TextView) findViewById(R.id.good_cell_name_two);
		}

		if (null == good_cell_name_three) {
			good_cell_name_three = (TextView) findViewById(R.id.good_cell_name_three);
		}

		if (null == good_cell_price_two) {
			good_cell_price_two = (TextView) findViewById(R.id.good_cell_price_two);
		}

		if (null == good_cell_price_three) {
			good_cell_price_three = (TextView) findViewById(R.id.good_cell_price_three);
		}

	}

	int count = 0;

	public void bindDataDelay() {
		init();
		ArrayList<SIMPLEGOODS> listData = categorygoods.getGoods();


		if (null != categorygoods.getName()) {
			good_cell_name_one.setText(categorygoods.getName());
			count++;

			good_cell_one.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
                    LG.i("=======点击事件=====");
					Intent it = new Intent(mContext, GoodsListActivity.class);
					FILTER filter = new FILTER();
					filter.setCategory_id(String.valueOf(categorygoods.getId()));
					try {
						it.putExtra("filter", filter.toJson()
								.toString());
					} catch (JSONException e) {

					}

					mContext.startActivity(it);
					((Activity) mContext).overridePendingTransition(
							R.anim.push_right_in, R.anim.push_right_out);
				}
			});
			good_cell_one.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					return true;
				}
			});
		}

		if (listData.size() > 0) {
			SIMPLEGOODS goodOne = listData.get(0);

			good_cell_photo_one.setVisibility(View.VISIBLE);

            imageLoader.displayImage(goodOne.getImg().getUrl(),good_cell_photo_one);


			if (listData.size() > 1) {
				good_cell_two.setVisibility(View.VISIBLE);
				final SIMPLEGOODS goodTwo = listData.get(1);
				if (null != goodTwo && null != goodTwo.getImg()
						&& null != goodTwo.getImg().getThumb()
						&& null != goodTwo.getImg().getSmall()) {

                    imageLoader.displayImage(goodTwo.getImg().getThumb(), good_cell_photo_two);

					good_cell_two.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent it = new Intent(mContext,
									GoodsDetailActivity.class);
							it.putExtra("good_id", goodTwo.getId());
							mContext.startActivity(it);
							((ECJiaMainActivity) mContext)
									.overridePendingTransition(
											R.anim.push_right_in,
											R.anim.push_right_out);
						}
					});

				}
				good_cell_name_two.setText(goodTwo.getName());
				good_cell_price_two.setText(goodTwo.getShop_price());

				if (listData.size() > 2) {
					good_cell_three.setVisibility(View.VISIBLE);
					final SIMPLEGOODS goodThree = listData.get(2);
					if (null != goodThree && null != goodThree.getImg()
							&& null != goodThree.getImg().getThumb()
							&& null != goodThree.getImg().getSmall()) {

                        imageLoader.displayImage(goodThree.getImg().getThumb(), good_cell_photo_three);

						good_cell_three
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent it = new Intent(mContext,
												GoodsDetailActivity.class);
										it.putExtra("good_id", goodThree.getId());
										mContext.startActivity(it);
										((ECJiaMainActivity) mContext)
												.overridePendingTransition(
														R.anim.push_right_in,
														R.anim.push_right_out);
									}
								});

					}
					good_cell_name_three.setText(goodThree.getName());
					good_cell_price_three.setText(goodThree.getShop_price());
				} else {
					good_cell_three.setVisibility(View.INVISIBLE);
				}
			} else {
				good_cell_two.setVisibility(View.INVISIBLE);
				good_cell_three.setVisibility(View.INVISIBLE);
			}
		} else {
			good_cell_photo_one.setVisibility(View.INVISIBLE);
			good_cell_two.setVisibility(View.INVISIBLE);
			good_cell_three.setVisibility(View.INVISIBLE);
		}
	}

	public void bindData(CATEGORYGOODS categorygoods) {
		this.categorygoods = categorygoods;
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 30);

	}
}
