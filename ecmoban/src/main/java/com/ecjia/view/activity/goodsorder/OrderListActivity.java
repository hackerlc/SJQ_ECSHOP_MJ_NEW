package com.ecjia.view.activity.goodsorder;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.OrderType;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.order.fragment.OrderListFragment;
import com.ecmoban.android.sijiqing.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class OrderListActivity extends FragmentActivity {

    private String flag;
    private TextView title;
    private ImageView back;
    private View null_paView;

    //private TextView head_waitpay, head_waitship, head_shiped, head_finished;
    //private LinearLayout headbg;
    public boolean orderupdate = false;

    private RadioGroup group_top;


    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_list);
        PushAgent.getInstance(this).onAppStart();
        final Resources resource = (Resources) getBaseContext().getResources();
        EventBus.getDefault().register(this);
        flag = getIntent().getStringExtra(IntentKeywords.ORDER_TYPE);
        if (TextUtils.isEmpty(flag)) {
            flag = OrderType.AWAIT_PAY;
        }
        String tradelists = resource.getString(R.string.trade_lists);
        fragmentArrayList.add(new OrderListFragment(OrderType.AWAIT_PAY));//待付款
        fragmentArrayList.add(new OrderListFragment(OrderType.AWAIT_TUANPI));//待成团
        fragmentArrayList.add(new OrderListFragment(OrderType.AWAIT_SHIP));//待发货
        fragmentArrayList.add(new OrderListFragment(OrderType.SHIPPED));//待收货
        //fragmentArrayList.add(new OrderListFragment(OrderType.ALLOW_COMMENT));//已完成--待评价
        fragmentArrayList.add(new OrderRejectedListFragment(OrderType.RETURN_GOOD));//退换货
        title = (TextView) findViewById(R.id.top_view_text);

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        group_top = (RadioGroup) this.findViewById(R.id.group_top);
        null_paView = findViewById(R.id.null_pager);

        if (OrderType.AWAIT_PAY.equals(flag)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(0)).commit();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(0);
            radioButton.setChecked(true);
        } else if (OrderType.AWAIT_TUANPI.equals(flag)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(1)).commit();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(1);
            radioButton.setChecked(true);
        } else if (OrderType.AWAIT_SHIP.equals(flag)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(2)).commit();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(2);
            radioButton.setChecked(true);
        } else if (OrderType.SHIPPED.equals(flag)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(3)).commit();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(3);
            radioButton.setChecked(true);
        } else if (OrderType.RETURN_GOOD.equals(flag)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(4)).commit();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(4);
            radioButton.setChecked(true);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(0)).commit();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(0);
            radioButton.setChecked(true);
        }

        group_top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getChildAt(0).getId() == checkedId){
                    flag = OrderType.AWAIT_PAY;
                    getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(0)).commit();
                }else if(group.getChildAt(1).getId() == checkedId){
                    flag = OrderType.AWAIT_TUANPI;
                    getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(1)).commit();
                }else if(group.getChildAt(2).getId() == checkedId){
                    flag = OrderType.AWAIT_SHIP;
                    getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(2)).commit();
                }else if(group.getChildAt(3).getId() == checkedId){
                    flag = OrderType.SHIPPED;
                    getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(3)).commit();
                }else if(group.getChildAt(4).getId() == checkedId){
                    flag = OrderType.RETURN_GOOD;
                    getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(4)).commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.order_list_framgent_layout, fragmentArrayList.get(0)).commit();
                }

            }
        });
        title.setText(tradelists);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderupdate && flag.equals(OrderType.AWAIT_SHIP)) {
            Resources resource = (Resources) getBaseContext().getResources();
            RadioButton radioButton= (RadioButton) group_top.getChildAt(2);
            radioButton.setChecked(true);
            orderupdate = false;
        }

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return true;
    }

    public void onEvent(MyEvent event) {
        orderupdate = event.getFlag();
        if (1 == event.getmTag()) {
            flag = OrderType.AWAIT_PAY;
        } else if (2 == event.getmTag()) {
            flag = OrderType.AWAIT_SHIP;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
