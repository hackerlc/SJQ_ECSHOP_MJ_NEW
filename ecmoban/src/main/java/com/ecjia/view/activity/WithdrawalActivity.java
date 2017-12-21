package com.ecjia.view.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RechargeModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WithdrawalActivity extends BaseActivity implements HttpResponse {
    TextView usermoney;
    EditText input_money, infomation_context;
    Button submit;
    RechargeModel model;
    MyDialog dialog;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        res=getResources();
        init();
    }

    private void init() {

        initTopView();

        model = new RechargeModel(this);
        model.addResponseListener(this);


        usermoney = (TextView) findViewById(R.id.user_money);
        input_money = (EditText) findViewById(R.id.input_money);
        infomation_context = (EditText) findViewById(R.id.infomation_context);
        submit = (Button) findViewById(R.id.withdrawal_ok);

        usermoney.setText(mApp.getUser().getFormated_user_money());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WithdrawalStart();//提交申请
            }
        });

        input_money.setFocusable(true);
        input_money.setFocusableInTouchMode(true);
        input_money.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) input_money.getContext().getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(input_money, 0);
            }
        }, 300);

    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.withdrawal_topview);
        topView.setTitleText(R.string.withdraw);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void WithdrawalStart() {
        if (StringUtils.isEmpty(input_money.getText().toString())) {
            ToastView toast = new ToastView(this, res.getString(R.string.not_null));
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } else if (Float.valueOf(mApp.getUser().getFormated_user_money().replace("￥", "").replace("元", "")) < Float.valueOf(input_money.getText().toString())) {
            ToastView toast = new ToastView(this, res.getString(R.string.too_large));
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } else {
            model.accountRaply(input_money.getText().toString(), infomation_context.getText().toString());//提交申请
        }

    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.USER_RAPLY) {
            if(status.getSucceed()==1){
                dialog=new MyDialog(WithdrawalActivity.this,res.getString(R.string.point),model.RaplyReuslt);
                dialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                dialog.setSureOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        closeKeyBoard(input_money);
    }
}
