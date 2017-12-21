package com.ecjia.view.activity;

import java.util.ArrayList;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.ShopgoodsAdapter;
import com.ecjia.entity.responsemodel.GOODS_LIST;
import com.umeng.message.PushAgent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.res.Resources;

public class ShopGoodsActivity extends BaseActivity {
    private ArrayList<GOODS_LIST> list;
    Resources resource;
    private TextView title;
    private ImageView back;
    private ListView goodslistview;
    private ShopgoodsAdapter adapter;

    private boolean isorder = false;//订单商品的适配器可进入商品详情,购物车的不可以

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_goods);
        PushAgent.getInstance(this).onAppStart();
        list = (ArrayList<GOODS_LIST>) this.getIntent().getSerializableExtra("datalist");
        isorder = getIntent().getBooleanExtra("is_order", false);
        resource = (Resources) getBaseContext().getResources();
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(resource.getString(R.string.shopgoods));
        goodslistview = (ListView) findViewById(R.id.shop_goods);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new ShopgoodsAdapter(this, list, isorder);
        goodslistview.setAdapter(adapter);
    }


}
