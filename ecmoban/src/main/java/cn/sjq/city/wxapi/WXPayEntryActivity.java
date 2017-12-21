package cn.sjq.city.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecmoban.android.sijiqing.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = ".WXPayEntryActivity";

    private IWXAPI api;
    private ImageView pay_image_success, pay_image_fail;
    private TextView paysuccess_text;
    private Resources resource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        paysuccess_text = (TextView) findViewById(R.id.paysuccess_text);
        pay_image_success = (ImageView) findViewById(R.id.pay_image_success);
        pay_image_fail = (ImageView) findViewById(R.id.pay_image_fail);
        PushAgent.getInstance(this).onAppStart();

        EventBus.getDefault().register(this);

        api = WXAPIFactory.createWXAPI(this, AndroidManager.WXAPPID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        resource = AppConst.getResources(this);
        String paysuccess = resource.getString(R.string.payment_paysuccess);
        String payfail = resource.getString(R.string.payment_payfail);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (0 == resp.errCode) {
                paysuccess_text.setText(paysuccess);
                pay_image_success.setVisibility(View.VISIBLE);
                pay_image_fail.setVisibility(View.GONE);
                EventBus.getDefault().post(new MyEvent("wxpay"));
            } else {
                paysuccess_text.setText(payfail);
                pay_image_success.setVisibility(View.GONE);
                pay_image_fail.setVisibility(View.VISIBLE);
            }
        }

        Timer timer = new Timer();

        timer.schedule(task, 2000);
    }

    TimerTask task = new TimerTask(){
        public void run(){
            finish();
        }

    };

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Event event) {

    }
}
