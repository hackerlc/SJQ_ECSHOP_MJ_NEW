package com.ecjia.network.netmodle;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;

import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.LOCATION;
import com.ecjia.util.DeviceInfoUtil;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.widgets.ToastView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseModel implements HttpResponse {

    public  DEVICE device;
    protected Context mContext;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    protected ArrayList<HttpResponse> businessResponseArrayList = new ArrayList<HttpResponse>();
    public Resources res;
    public HttpUtils httpUtils;
    public String area_id;
    public ECJiaApplication mApp;
    public MyProgressDialog pd;
    public LOCATION location;

    public BaseModel() {

    }

    public BaseModel(Context context) {
        res = context.getResources();
        mContext = context;
        mApp = (ECJiaApplication) mContext.getApplicationContext();
        httpUtils = new HttpUtils();
        device = DEVICE.getInstance();
        location=new LOCATION();
        location.setLatitude(AppConst.LNG_LAT[1]+"");
        location.setLongitude(AppConst.LNG_LAT[0]+"");
        if (TextUtils.isEmpty(device.getUdid()) || TextUtils.isEmpty(device.getClient())) {
            DeviceInfoUtil.getDevice(context);
        }
        pd = MyProgressDialog.createDialog(context);
        pd.setMessage(res.getString(R.string.loading));
        shared = context.getSharedPreferences(SharedPKeywords.SPUSER, 0);
    }


    public RequestParams addHeader(RequestParams params){
        params.addHeader("Device-client",device.getClient());
        params.addHeader("Device-code",device.getCode());
        params.addHeader("Device-udid",device.getUdid());
        params.addHeader("Api-vesion","1.7.0");
        return params;
    }


    //公共的错误处理
    public void callback(JSONObject jo) {
        try {
            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));


            Resources resource = mContext.getResources();
            String way = resource.getString(R.string.delivery);
            String col = resource.getString(R.string.collected);
            String und = resource.getString(R.string.understock);
            String been = resource.getString(R.string.been_used);
            String sub = resource.getString(R.string.submit_the_parameter_error);
            String fai = resource.getString(R.string.failed);
            String pur = resource.getString(R.string.purchase_failed);
            String noi = resource.getString(R.string.no_shipping_information);
            String wrong = responseStatus.getError_desc();

            if (responseStatus.getSucceed() != AppConst.ResponseSucceed) {
                if (responseStatus.getError_code() == AppConst.InvalidSession) {
                    editor = shared.edit();

                    editor.putString(SharedPKeywords.SPUSER_KUID, "");
                    editor.putString(SharedPKeywords.SPUSER_KSID, "");
                    editor.commit();
                    SESSION.getInstance().uid = "";
                    SESSION.getInstance().sid = "";


                } else if (responseStatus.getError_code() == AppConst.SelectedDeliverMethod) {
                    ToastView toast = new ToastView(mContext, way);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.AlreadyCollected) {
                    //Toast.makeText(mContext, "您已收藏过此商品", Toast.LENGTH_SHORT).show();
                    ToastView toast = new ToastView(mContext, col);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.InventoryShortage) {
                    ToastView toast = new ToastView(mContext, und);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.UserOrEmailExist) {
                    ToastView toast = new ToastView(mContext, been);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.InvalidParameter) {
                    ToastView toast = new ToastView(mContext, sub);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.ProcessFailed) {
                    ToastView toast = new ToastView(mContext, fai);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.BuyFailed) {
                    ToastView toast = new ToastView(mContext, pur);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (responseStatus.getError_code() == AppConst.NoShippingInformation) {
                    ToastView toast = new ToastView(mContext, noi);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    ToastView toast = new ToastView(mContext, wrong);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        } catch (JSONException e) {

        }

    }

    public void addResponseListener(HttpResponse listener) {
        if (!businessResponseArrayList.contains(listener)) {
            businessResponseArrayList.add(listener);
        }
    }

    public void removeAllResponseListener() {
        businessResponseArrayList.clear();
    }

    public void removeResponseListener(HttpResponse listener) {
        businessResponseArrayList.remove(listener);
    }

    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        for (HttpResponse iterable_element : businessResponseArrayList) {
            iterable_element.onMessageResponse(url, jo, status);
        }
    }

    protected void showXutilsFailure() {
        ToastView toastView = new ToastView(mContext, R.string.failue_service);
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }
}
