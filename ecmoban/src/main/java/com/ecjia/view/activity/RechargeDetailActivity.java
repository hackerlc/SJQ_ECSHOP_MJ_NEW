package com.ecjia.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RechargeModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.RECHARGE_INFO;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class RechargeDetailActivity extends BaseActivity implements HttpResponse {
    private RECHARGE_INFO info;
    private TextView tv_name, tv_type, tv_amount, payment_name, payment_type, add_time, tv_number;
    private Button btn_cancle, btn_ok, raply_cancel;
    private LinearLayout success_item, needpay_item, needcancel_item;
    private TextView success_text;
    SharedPreferences username;
    RechargeModel model;
    MyDialog mDialog;
    Resources res;
    private ImageView recharge_profilephoto;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_detail);
        EventBus.getDefault().register(this);
        PushAgent.getInstance(this).onAppStart();
        initView();

    }

    private void initView() {
        res = getResources();
        try {
            info = RECHARGE_INFO.fromJson(new JSONObject(getIntent().getStringExtra("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        initTopView();
        recharge_profilephoto = (ImageView) findViewById(R.id.recharge_profilephoto);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        payment_name = (TextView) findViewById(R.id.payment_name);
        payment_type = (TextView) findViewById(R.id.payment_type);
        add_time = (TextView) findViewById(R.id.add_time);
        tv_number = (TextView) findViewById(R.id.tv_number);
        success_text = (TextView) findViewById(R.id.success_text);

        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        raply_cancel = (Button) findViewById(R.id.raply_cancel);

        success_item = (LinearLayout) findViewById(R.id.success_item);
        needpay_item = (LinearLayout) findViewById(R.id.needpay_item);
        needcancel_item = (LinearLayout) findViewById(R.id.needcancel_item);

        username = getSharedPreferences("userInfo", 0);
        String uid = username.getString("uid", "");
        bitmap = ProfilePhotoUtil.getInstance().getProfileBitmap(uid);
        if (bitmap != null) {
            recharge_profilephoto.setImageBitmap(bitmap);
        } else {
            recharge_profilephoto.setImageResource(R.drawable.profile_no_avarta_icon_light);
        }
        tv_name.setText(username.getString("uname", ""));
        tv_type.setText(info.getType_lable());

        model = new RechargeModel(this);
        model.addResponseListener(this);

        if ("deposit".equals(info.getType())) {
            tv_amount.setText("+" + info.getAmount());
            LG.i("_____" + info.getPayment_name());
            payment_name.setText(info.getPayment_name());

            needcancel_item.setVisibility(View.GONE);
            if ("0".equals(info.getIs_paid())) {

                success_item.setVisibility(View.GONE);
                needpay_item.setVisibility(View.VISIBLE);

                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog = new MyDialog(RechargeDetailActivity.this,
                                res.getString(R.string.point), res.getString(R.string.sure));
                        mDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                        mDialog.setPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                                model.AccountCancel(info.getAccount_id());
                            }
                        });
                        mDialog.setNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDialog.dismiss();
                            }
                        });
                        mDialog.show();

                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RechargeDetailActivity.this, ChoosePayActivity.class);
                        intent.putExtra(IntentKeywords.PAY_TYPE, IntentKeywords.ACCOUNT_ID);
                        intent.putExtra(IntentKeywords.ACCOUNT_ID, info.getAccount_id());
                        intent.putExtra(IntentKeywords.PAY_IS_CREATE, false);
                        intent.putExtra(IntentKeywords.PAY_BODY, "余额充值");
                        intent.putExtra(IntentKeywords.PAY_AMOUNT, info.getAmount());
                        intent.putExtra(IntentKeywords.PAY_ID, info.getPayment_id());
                        startActivity(intent);
                    }
                });

            } else {
                success_item.setVisibility(View.VISIBLE);
                needpay_item.setVisibility(View.GONE);
                success_text.setText(info.getPay_status());
            }
        } else if ("raply".equals(info.getType())) {
            tv_amount.setText(info.getAmount());
            needpay_item.setVisibility(View.GONE);
            payment_name.setText(res.getString(R.string.user_account));

            if ("0".equals(info.getIs_paid())) {
                needcancel_item.setVisibility(View.VISIBLE);
                success_item.setVisibility(View.GONE);
                needpay_item.setVisibility(View.GONE);
                raply_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog = new MyDialog(RechargeDetailActivity.this,
                                res.getString(R.string.point), res.getString(R.string.sure));
                        mDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                        mDialog.setPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                                model.AccountCancel(info.getAccount_id());

                            }
                        });
                        mDialog.setNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mDialog.dismiss();
                            }
                        });
                        mDialog.show();


                    }
                });
            } else {

                LG.i("_____" + info.getPayment_name());

                success_item.setVisibility(View.VISIBLE);
                success_text.setText(info.getPay_status());
            }
        }


        payment_type.setText(info.getType_lable());
        add_time.setText(info.getAdd_time());
        tv_number.setText(info.getAccount_id());


    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.recharge_detail_topview);
        topView.setTitleText(R.string.accoubt_record_detail);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    public void onEvent(Event event) {

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.USER_ACCOUNT_CANCLE) {
            if (status.getSucceed() == 1) {
                EventBus.getDefault().post(new MyEvent("recharge_cancel"));
                finish();
            }
        }
    }
}
