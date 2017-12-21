package com.ecjia.view.activity.goodsorder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.RETURNSTATUS;
import com.ecjia.entity.responsemodel.SHIPPING;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.common.BitmapUtils;
import com.ecjia.util.common.BottomChoosePhotoUtils;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.common.ImageUtils;
import com.ecjia.util.common.T;
import com.ecjia.util.gallery.GalleryImageUtils;
import com.ecjia.widgets.dialog.EditPictureDialog;
import com.ecjia.widgets.paycenter.alipay.alipayutil.Base64;
import com.ecmoban.android.sijiqing.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：申请提交填写物流信息
 * Created by sun
 * Created time 2017-03-10.
 */

public class ApplyAddShippingInfoActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.spinner_type)
    Spinner spinner_type;
    @BindView(R.id.edit_money)
    EditText edit_money;

    @BindView(R.id.iv_upload1)
    ImageView iv_upload1;
    @BindView(R.id.iv_del_pic1)
    ImageView iv_del_pic1;

//    private ArrayList<RETURNCAUSE> spinnerTypeList = new ArrayList<>();
    private Map<String, String> spinnerMap = new HashMap<>();
    private String imgPath;


    private String retId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_shippinginfo;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        GalleryImageUtils.configGallery(mActivity);
        textView_title.setText("填写物流信息");
        Bundle bundle = getIntent().getExtras();
        retId = bundle.getString(IntentKeywords.RET_ID);
        if (TextUtils.isEmpty(retId)) {
            finish();
            return;
        }
        iv_del_pic1.setVisibility(View.GONE);
        getShipping();//请求快递接口
    }

    private void  setChooesImg(int requestPostion){
        BottomChoosePhotoUtils.newInstance().show(getSupportFragmentManager(), new BottomChoosePhotoUtils.OnChoosePhoneResult() {
            @Override
            public void onHandlerSuccess(List<PhotoInfo> resultList, int requestPostion) {
                switch (requestPostion) {
                    case 20001:
                    ImageUtils.showImageFilePath(mActivity, resultList.get(0).getPhotoPath(), iv_upload1);
//                        ImageLoaderForLV.getInstance(mActivity).setImageRes(iv_upload1,resultList.get(0).getPhotoPath());
                        imgPath = resultList.get(0).getPhotoPath();
                        iv_del_pic1.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onHandlerFailure(String errorMsg,int requestPostion) {

            }
        },requestPostion);
    }


    @OnClick({R.id.imageView_back, R.id.iv_upload1, R.id.iv_del_pic1, R.id.apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                DialogUtils.showDialog(mActivity, "提示", "填写资料未保存,是否退出", new DialogUtils.ButtonClickListener() {
                    @Override
                    public void negativeButton() {//取消
                    }

                    @Override
                    public void positiveButton() {//确定
                        finish();
                    }
                });
                break;
            case R.id.iv_upload1:
                setChooesImg(20001);
                break;
            case R.id.iv_del_pic1:
                imgPath = "";
                iv_upload1.setImageResource(R.drawable.img_default_null);
                iv_del_pic1.setVisibility(View.GONE);
                break;
            case R.id.apply:
                String rejected_reason = spinnerMap.get(spinner_type.getSelectedItem().toString());
                String rejected_value = edit_money.getText().toString();
                String first_image = TextUtils.isEmpty(imgPath) ? "" : Base64.encode(BitmapUtils.getImage(mActivity, imgPath, 300));
                T.show(mActivity, rejected_reason);
                if (TextUtils.isEmpty(rejected_reason)) {
                    T.show(mActivity, "请选择快递公司");
                    return;
                }
                if (TextUtils.isEmpty(rejected_value)) {
                    T.show(mActivity, "填写快递单号");
                    return;
                }
                getSendCourier(rejected_reason, rejected_value, first_image);
                break;
        }
    }

    private void getShipping() {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getShipping(GoodsQuery.getInstance().getReturnause())
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ArrayList<SHIPPING>>() {
                    @Override
                    public void onNext(ArrayList<SHIPPING> returncauses) {
                        int size = returncauses.size();
                        String[] arr = new String[size];
                        for (int i = 0; i < size; i++) {
                            arr[i] = returncauses.get(i).getShipping_name();
                            spinnerMap.put(arr[i], returncauses.get(i).getShipping_id());
                        }
                        ArrayAdapter<String> zhuyingAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, arr);
                        zhuyingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //绑定 Adapter到控件
                        spinner_type.setAdapter(zhuyingAdapter);
                        spinner_type.setSelection(0);
                    }

                    @Override
                    public void onError(Throwable t) {
                        T.show(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getSendCourier(String shipping_id, String invoice_no, String images) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getSendCourier(GoodsQuery.getInstance().getSendCourier(retId, shipping_id, invoice_no, images))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<RETURNSTATUS>() {
                    @Override
                    public void onNext(RETURNSTATUS returnstatus) {
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        T.show(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogUtils.showDialog(mActivity, "提示", "填写资料未保存,是否退出", new DialogUtils.ButtonClickListener() {
                @Override
                public void negativeButton() {//取消
                }

                @Override
                public void positiveButton() {//确定
                    finish();
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dispose() {

    }
}
