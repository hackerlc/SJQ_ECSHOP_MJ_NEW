package com.ecjia.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.ConsultModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.view.adapter.ConsultViewAdapter;
import com.ecjia.entity.responsemodel.CONSULTION;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;


/**
 * Created by Adam on 2015/1/27.
 */
public class ConsultActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener, TextWatcher {

    private ImageView back;
    private TextView title;
    public Handler Intenthandler;
    private final String TYPE1 = "order_consult";//订单咨询
    private final String TYPE2 = "goods_consult";//商品咨询
    private final String TYPE3 = "all_consult";

    private LinearLayout consult_goods;
    private LinearLayout consult_order;

    private TextView orderNumber, orderPrice, orderTime;
    private ImageView orderImg;
    private Intent intent;
    private TextView goodsTitle, goodsPrice, goodsUrl;
    private ImageView goodsImg;
    private XListView mListView;
    private EditText mEditText;
    private TextView send;

    private ConsultModel consultModel;
    private ConsultViewAdapter mConsltViewAdapter;
    private String intentType;

    private ArrayList<CONSULTION> consultionList = new ArrayList<CONSULTION>();
    int i = 1;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String uid;
    private Bitmap profilePhoto;
    int size = 0;
    private TextView close_keyboardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
        if (!TextUtils.isEmpty(uid)) {
            profilePhoto = ProfilePhotoUtil.getInstance().getProfileBitmap(uid);
        }
        intent = getIntent();
        intentType = intent.getStringExtra("type");

        if (TextUtils.isEmpty(intentType)) {
            intentType = TYPE3;
        }
        initView(intentType);
        Intenthandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.obj == ProtocolConst.CONSULTION_GET) {
                    if (msg.what == 1) {
                        mListView.stopRefresh();
                        if (consultModel.paginated.getMore() == 0) {
                            mListView.setPullRefreshEnable(false);
                        } else {
                            mListView.setPullRefreshEnable(true);
                        }
                        consultionList = consultModel.consultionList;
                        size = consultionList.size();
                        if (mConsltViewAdapter == null) {
                            mConsltViewAdapter = new ConsultViewAdapter(ConsultActivity.this, consultionList, profilePhoto);
                            mListView.setAdapter(mConsltViewAdapter);
                            mListView.setSelection(mListView.getCount() - 1);
                        } else {
                            mConsltViewAdapter.notifyDataSetChanged();
                            mListView.setSelection(0);
                        }
                    }
                    if (msg.what == 2) {//刷新
                        mListView.stopRefresh();
                        if (consultModel.paginated.getMore() == 0) {
                            mListView.setPullRefreshEnable(false);
                        } else {
                            mListView.setPullRefreshEnable(true);
                        }
                        consultionList = consultModel.consultionList;
                        mConsltViewAdapter.notifyDataSetChanged();
                        mListView.setSelection(consultionList.size() - size);
                        size = consultionList.size();
                    }
                    if (msg.what == 0) {//请求返回值为空
                        mListView.stopRefresh();
                        if (mConsltViewAdapter == null) {
                            mConsltViewAdapter = new ConsultViewAdapter(ConsultActivity.this, consultionList, profilePhoto);
                            mListView.setAdapter(mConsltViewAdapter);
                            mListView.setSelection(mListView.getCount() - 1);
                        } else {
                            mConsltViewAdapter.notifyDataSetChanged();
                            mListView.setSelection(0);
                        }
                    }

                }
                if (msg.obj == ProtocolConst.CONSULTION_CREATE) {
                    if (msg.what == 1) {
                        upDateListView();
                    }
                }
            }
        };
        newConsult(intentType);

    }

    private void initView(final String intentType) {
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.top_view_text);
        close_keyboardView = (TextView) findViewById(R.id.consult_close_keyboard);
        close_keyboardView.setOnClickListener(this);
        mListView = (XListView) findViewById(R.id.consult_list);
        mListView.setXListViewListener(ConsultActivity.this, 0);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
        mListView.setRefreshTime();
        //订单咨询
        View consult_order_head = LayoutInflater.from(this).inflate(R.layout.head_consult_order, null);
        consult_order = (LinearLayout) consult_order_head.findViewById(R.id.consult_order);
        orderNumber = (TextView) consult_order_head.findViewById(R.id.consult_order_number);
        orderPrice = (TextView) consult_order_head.findViewById(R.id.consult_order_price);
        orderTime = (TextView) consult_order_head.findViewById(R.id.consult_order_time);
        orderImg = (ImageView) consult_order_head.findViewById(R.id.consult_order_img);

        //商品咨询
        View consult_goods_head = LayoutInflater.from(this).inflate(R.layout.head_consult_goods, null);
        consult_goods = (LinearLayout) consult_goods_head.findViewById(R.id.consult_goods);
        goodsTitle = (TextView) consult_goods_head.findViewById(R.id.consult_goods_title);
        goodsPrice = (TextView) consult_goods_head.findViewById(R.id.consult_goods_price);
        goodsUrl = (TextView) consult_goods_head.findViewById(R.id.consult_goods_sendurl);
        goodsImg = (ImageView) consult_goods_head.findViewById(R.id.consult_goods_img);

        if (intentType.equals(TYPE1)) {

            orderNumber.setText(intent.getStringExtra("order_sn"));
            if(0==FormatUtil.formatStrToFloat(intent.getStringExtra("order_price"))){
                orderPrice.setText("免费");
            }else{
                orderPrice.setText(intent.getStringExtra("order_price"));
            }

            orderTime.setText(intent.getStringExtra("order_time"));
            MyBitmapUtils.getInstance(this).displayImage(orderImg, intent.getStringExtra("order_goodsImg"));
            mListView.addHeaderView(consult_order_head);
            title.setText(R.string.consult_order);

        } else if (intentType.equals(TYPE2)) {

            goodsTitle.setText(intent.getStringExtra("goods_title"));
            if(0==FormatUtil.formatStrToFloat(intent.getStringExtra("goods_price"))){
                goodsPrice.setText("免费");
            }else{
                try {
                    goodsPrice.setText(FormatUtil.formatToSymbolPrice(intent.getStringExtra("goods_price")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            MyBitmapUtils.getInstance(this).displayImage(goodsImg, intent.getStringExtra("goods_img"));
            goodsUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendUrl();
                }
            });
            mListView.addHeaderView(consult_goods_head);
            title.setText(R.string.consult_goods);
        } else if (intentType.equals(TYPE3)) {
            title.setText(R.string.consult);
        }


        mEditText = (EditText) findViewById(R.id.consult_edit);
        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    close_keyboardView.setVisibility(View.VISIBLE);
                    close_keyboardView.setEnabled(true);
                } else {
                    close_keyboardView.setVisibility(View.GONE);
                    close_keyboardView.setEnabled(false);
                }
            }
        });
        send = (TextView) findViewById(R.id.consult_send);
        send.setOnClickListener(this);

        //头部
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.consult_edit:
                break;
            case R.id.consult_send:
                send(intentType);
                break;
            case R.id.top_view_back:
                closeKeyBoard();
                finish();
                break;
            case R.id.consult_close_keyboard:
                closeKeyBoard();
                break;
        }

    }

    private int sendType = 1;
    private String url;

    //发送咨询（链接）。
    private void sendUrl() {
        int goods_id = intent.getIntExtra("goods_id", 0);
        url = AndroidManager.OFFOCIALWEB + "/goods.php?id=" + goods_id;
        if (uid != null && !uid.equals("")) {
            consultModel.createConsultion(goods_id + "", "goods", mApp.getUser().getName(), url, Intenthandler);
        } else {
            consultModel.createConsultion(goods_id + "", "goods", null, url, Intenthandler);
        }
        sendType = 0;
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    //发送咨询。
    private void send(String type) {

        String contString = mEditText.getText().toString();
        if (contString.trim().isEmpty()) {
            ToastView toast = new ToastView(ConsultActivity.this, "内容不能为空");
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            mEditText.setText("");
        } else {

            if (consultModel == null) {
                consultModel = new ConsultModel(this);
            }
            if (type.equals(TYPE1)) {
                consultModel.createConsultion(intent.getStringExtra("order_id"), "orders", mApp.getUser().getName(), contString, Intenthandler);
            } else if (type.equals(TYPE2)) {
                if (!TextUtils.isEmpty(uid)) {
                    consultModel.createConsultion(intent.getIntExtra("goods_id", 0) + "", "goods", mApp.getUser().getName(), contString, Intenthandler);
                } else {
                    consultModel.createConsultion(intent.getIntExtra("goods_id", 0) + "", "goods", null, contString, Intenthandler);
                }

            } else {
                if (!TextUtils.isEmpty(uid)) {
                    consultModel.createConsultion(null, "common", mApp.getUser().getName(), contString, Intenthandler);
                } else {
                    consultModel.createConsultion(null, "common", null, contString, Intenthandler);
                }
            }
            sendType = 1;
        }
    }

    //获取咨询
    private void newConsult(String type) {
        consultModel = new ConsultModel(this);
        if (type.equals(TYPE1)) {
            consultModel.getConsultionList(intent.getStringExtra("order_id"), "orders", Intenthandler);
        } else if (type.equals(TYPE2)) {
            if (uid != null && !uid.equals("")) {
                consultModel.getConsultionList(intent.getIntExtra("goods_id", 0) + "", "goods", Intenthandler);
            } else {
                consultModel.getConsultionList(intent.getIntExtra("goods_id", 0) + "", "goods", Intenthandler);
            }
        } else {
            if (uid != null && !uid.equals("")) {
                consultModel.getConsultionList(null, "common", Intenthandler);
            } else {
                consultModel.getConsultionList(null, "common", Intenthandler);
            }

        }

    }

    //获取咨询（更多）
    private void getConsultMore(String type) {
        if (consultModel == null) {
            consultModel = new ConsultModel(this);
        }
        if (type.equals(TYPE1)) {
            consultModel.getConsultionListMore(intent.getStringExtra("order_id"), "orders", Intenthandler);
        } else if (type.equals(TYPE2)) {
            if (mApp.getUser() != null) {
                consultModel.getConsultionListMore(intent.getIntExtra("goods_id", 0) + "", "goods", Intenthandler);
            } else {
                consultModel.getConsultionListMore(intent.getIntExtra("goods_id", 0) + "", "goods", Intenthandler);
            }
        } else {
            if (mApp.getUser() != null) {
                consultModel.getConsultionListMore(null, "common", Intenthandler);
            } else {
                consultModel.getConsultionListMore(null, "common", Intenthandler);
            }

        }

    }


    public void upDateListView() {
        CONSULTION consultion = new CONSULTION();
        switch (sendType) {
            case 0:
                consultion.setContent(url);
                consultion.setIs_myself("1");
                consultionList.add(0, consultion);
                mConsltViewAdapter.notifyDataSetChanged();
                mListView.setSelection(mListView.getCount() - 1);
                break;
            case 1:
                String contString = mEditText.getText().toString();
                consultion.setContent(contString);
                consultion.setIs_myself("1");
                mEditText.setText("");
                consultionList.add(0, consultion);
                mConsltViewAdapter.notifyDataSetChanged();
                mListView.setSelection(mListView.getCount() - 1);
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mEditText.getText().toString() == null || "".equals(mEditText.getText().toString())) {
            send.setBackgroundResource(0);
            send.setTextColor(getResources().getColor(R.color.my_dark));
            send.setEnabled(false);
        } else {
            send.setBackgroundResource(R.drawable.shape_public_bg);
            send.setTextColor(Color.parseColor("#ffffffff"));
            send.setEnabled(true);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mEditText.getText().toString() == null || "".equals(mEditText.getText().toString())) {
            send.setBackgroundResource(0);
            send.setTextColor(getResources().getColor(R.color.my_dark));
            send.setEnabled(false);
        } else {
            send.setBackgroundResource(R.drawable.shape_public_bg);
            send.setTextColor(Color.parseColor("#ffffffff"));
            send.setEnabled(true);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mEditText.getText().toString() == null || "".equals(mEditText.getText().toString())) {
            send.setBackgroundResource(0);
            send.setTextColor(getResources().getColor(R.color.my_dark));
            send.setEnabled(false);
        } else {
            send.setBackgroundResource(R.drawable.shape_public_bg);
            send.setTextColor(Color.parseColor("#ffffffff"));
            send.setEnabled(true);
        }
    }

    @Override
    public void onRefresh(int id) {
        getConsultMore(intentType);
    }

    @Override
    public void onLoadMore(int id) {

    }


    // 关闭键盘
    public void closeKeyBoard() {
        mEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }


}

