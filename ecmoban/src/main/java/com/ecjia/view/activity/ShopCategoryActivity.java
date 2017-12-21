package com.ecjia.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.FlowLayout;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/10/18.
 */
public class ShopCategoryActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopcartegory);
        initView();
    }

    private void initView() {

        findViewById(R.id.shopcategory_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initFlowLayout();
    }

    private void initFlowLayout() {
        final FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        final ArrayList<String> strings = getIntent().getStringArrayListExtra("seller_category");
        final int position = getIntent().getIntExtra("position", 0);

        int itemWith = (getDisplayMetricsWidth() - (int) getResources().getDimension(R.dimen.dp_10) * 6) / 3;
        for (int i = 0; i < strings.size(); i++) {
            final int j = i;
            final TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_shop_category,null);
            textView.setTextColor(getResources().getColor(R.color.my_dark));
            textView.setGravity(Gravity.CENTER);
            ViewGroup.MarginLayoutParams params3 = new ViewGroup.MarginLayoutParams(itemWith, getResources().getDimensionPixelSize(R.dimen.dp_40));
            params3.setMargins((int) getResources().getDimension(R.dimen.dp_10), (int) getResources().getDimension(R.dimen.dp_10), 0, 0);
            textView.setLayoutParams(params3);
            flowLayout.addView(textView, params3);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int k = 0; k < flowLayout.getChildCount(); k++) {
                        ((TextView) flowLayout.getChildAt(j)).setTextColor(getResources().getColor(R.color.my_dark));
                    }
                    textView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
                    Intent intent = new Intent();
                    intent.putExtra("position", j);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            if (position == i) {
                textView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            }
            textView.setText(strings.get(j));
        }
    }


}
