package com.ecjia.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.ecjia.view.adapter.SpecificationAdapter;
import com.ecjia.entity.responsemodel.SPECIFICATION_VALUE;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;

import de.greenrobot.event.EventBus;
/**
 * 商品规格选择页面
 */
public class SpecificationActivity extends BaseActivity implements SpecificationAdapter.OnSpecficationChangedListener {
    private ListView specificationListView;
    private TextView title;
    private ImageView back;
    private TextView minusImageView;
    private TextView addImageView;
    public ImageView dismiss;
    //    private Button ok;
    private TextView quantityEditText;
    private TextView goodTotalPriceTextView, addtocart, buynow;
    private SharedPreferences shared;
    private View addItemComponent, specificationbuttonview;
    private int num;
    private boolean isbuynow = false;
    private SpecificationAdapter sp;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView price;
    private float baseprice = 0.0f;
    private final int SPACIFICATION_ADDCART = 10016;//商品属性
    private final int SPACIFICATION_BALANCE = 10017;//商品属性
    ImageView img;
    private String object_id;
    TextView store ;
    String specific_inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specification_activity);
        EventBus.getDefault().register(this);
        PushAgent.getInstance(this).onAppStart();
        Resources resources = getBaseContext().getResources();
        String please_select = resources.getString(R.string.please_select);
        String specific_classify = resources.getString(R.string.specific_classify);
        specific_inventory = resources.getString(R.string.specific_inventory);//库存
        Window lp = getWindow();
        lp.setGravity(Gravity.BOTTOM);

        lp.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        final Intent intent = getIntent();
        object_id = intent.getStringExtra(IntentKeywords.OBJECT_ID);

        shared = getSharedPreferences("userInfo", 0);
        num = intent.getIntExtra("num", 0);
        dismiss = (ImageView) findViewById(R.id.spec_dismiss);
        dismiss.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        specificationListView = (ListView) findViewById(R.id.specification_list);

        // 添加顶部布局
        img = (ImageView) findViewById(R.id.spec_img);
        imageLoader.displayImage(intent.getStringExtra("imgurl"), img);

        price = (TextView) this.findViewById(R.id.spec_fee);
        store = (TextView) this.findViewById(R.id.spec_stock);
        try {
            price.setText(FormatUtil.formatToSymbolPrice(intent.getStringExtra("price")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("免费".equals(intent.getStringExtra("price"))) {
            baseprice = 0;
        } else {
            baseprice = FormatUtil.formatStrToFloat(intent.getStringExtra("price"));
        }
        store.setText(specific_inventory + "：" + intent.getStringExtra("store"));//库存
        // 添加对话框底部按钮
        addItemComponent = (View) LayoutInflater.from(this).inflate(R.layout.add_item_component, null);
        specificationListView.addFooterView(addItemComponent);
        sp = new SpecificationAdapter(this, GoodDetailDraft.getInstance().goodDetail.specification,this);
        specificationListView.setAdapter(sp);
        // 限制对话框的最大高度
        setListViewHeight(specificationListView);
        addtocart = (TextView) this.findViewById(R.id.spec_add_to_cart);
        if (!TextUtils.isEmpty(object_id)) {
            addtocart.setEnabled(false);
//            addtocart.setBackgroundColor(R.color.TextColorGray);
            addtocart.setBackgroundResource(R.color._999999);
        }

        addtocart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isbynow", false);
                setResult(SPACIFICATION_ADDCART, intent);
                finish();
            }
        });
        buynow = (TextView) this.findViewById(R.id.spec_buy_now);
        buynow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isbynow", true);
                setResult(SPACIFICATION_BALANCE, intent);
                finish();
            }
        });

        goodTotalPriceTextView = (TextView) addItemComponent.findViewById(R.id.good_total_price);

        minusImageView = (TextView) addItemComponent.findViewById(R.id.shop_car_item_min);
        minusImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoodDetailDraft.getInstance().goodQuantity - 1 > 0) {
                    GoodDetailDraft.getInstance().goodQuantity--;
                    quantityEditText.setText(String.valueOf(GoodDetailDraft.getInstance().goodQuantity));
                }
            }
        });

        addImageView = (TextView) addItemComponent.findViewById(R.id.shop_car_item_sum);
        addImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoodDetailDraft.getInstance().goodQuantity > num - 1) {
                    Resources resource = (Resources) getBaseContext().getResources();
                    String und = resource.getString(R.string.understock);
                    ToastView toast = new ToastView(SpecificationActivity.this, und);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    GoodDetailDraft.getInstance().goodQuantity++;
                    quantityEditText.setText(String.valueOf(GoodDetailDraft.getInstance().goodQuantity));
                }

            }
        });

        quantityEditText = (TextView) addItemComponent.findViewById(R.id.shop_car_item_editNum);
        quantityEditText.setText(String.valueOf(GoodDetailDraft.getInstance().goodQuantity));
        quantityEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String count = s.toString();
                if (count.length() > 0) {
                    GoodDetailDraft.getInstance().goodQuantity = Integer.valueOf(count).intValue();
                    refreshTotalPrice();
                }

            }
        });
        refreshTotalPrice();
    }

    void refreshTotalPrice() {
        Resources resource = (Resources) getBaseContext().getResources();
        String tot = resource.getString(R.string.total_price);
        String totolPrice = tot + GoodDetailDraft.getInstance().getTotalPrice();
        goodTotalPriceTextView.setText(totolPrice);
    }


    public void onEvent(Object event) {
        if (event.getClass() == SPECIFICATION_VALUE.class) {
            refreshTotalPrice();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    // 设置对话框的最大自适应高度
    public void setListViewHeight(ListView listView) {
        // 获取屏幕宽度
        int totalHeight = getWindowManager().getDefaultDisplay().getHeight();
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int dialogHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            dialogHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (totalHeight - dialogHeight < (int) (totalHeight * 14.0 / 25)) {
            params.height = totalHeight - (int) (totalHeight * 14.0 / 25);// 45为底部button的高度
        }
        listView.setLayoutParams(params);
    }

    @Override
    public void SpecChanged(float price,String num) {
        store.setText(specific_inventory + "：" + num);//库存
        try {
            this.price.setText(FormatUtil.formatToSymbolPrice((baseprice + price + "")));
            EventBus.getDefault().post(new MyEvent(FormatUtil.formatToDigetPrice((baseprice + price + "")), 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
