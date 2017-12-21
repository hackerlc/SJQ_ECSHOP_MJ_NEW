package com.ecjia.view.activity.user;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.USERMENBERS;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.UserQuery;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * 类名介绍：申请认证会员入口
 * Created by sun
 * Created time 2017-02-20.
 */

public class UserApplyMenberActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.edit_shopname)
    EditText edit_shopname;

    @BindView(R.id.edit_apply_address)
    EditText edit_apply_address;

    @BindView(R.id.apply_zhuying_type)
    TextView apply_zhuying_type;

    @BindView(R.id.ly_idcard_up)
    RelativeLayout ly_idcard_up;
    @BindView(R.id.img_idcard_up)
    ImageView img_idcard_up;

    @BindView(R.id.ly_idcard_below)
    RelativeLayout ly_idcard_below;
    @BindView(R.id.img_idcard_below)
    ImageView img_idcard_below;

    @BindView(R.id.ly_business_license)
    RelativeLayout ly_business_license;
    @BindView(R.id.img_business_license)
    ImageView img_business_license;

    String imagePath1 = "";
    String imagePath2 = "";
    String imagePath3 = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_applymenber;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        GalleryImageUtils.configGallery(mActivity);
        textView_title.setText("采购商认证");
    }

    private void  setChooesImg(int requestPostion){
        BottomChoosePhotoUtils.newInstance().show(getSupportFragmentManager(), new BottomChoosePhotoUtils.OnChoosePhoneResult() {
            @Override
            public void onHandlerSuccess(List<PhotoInfo> resultList, int requestPostion) {
                switch (requestPostion) {
                    case 20001:
                    ImageUtils.showImageFilePath(mActivity, resultList.get(0).getPhotoPath(), img_idcard_up);
//                        ImageLoaderForLV.getInstance(mActivity).setImageRes(img_idcard_up,resultList.get(0).getPhotoPath());
                        imagePath1 = resultList.get(0).getPhotoPath();
                        break;
                    case 20002:
                    ImageUtils.showImageFilePath(mActivity, resultList.get(0).getPhotoPath(), img_idcard_below);
//                        ImageLoaderForLV.getInstance(mActivity).setImageRes(img_idcard_below,resultList.get(0).getPhotoPath());
                        imagePath2 = resultList.get(0).getPhotoPath();
                        break;
                    case 20003:
                    ImageUtils.showImageFilePath(mActivity, resultList.get(0).getPhotoPath(), img_business_license);
//                        ImageLoaderForLV.getInstance(mActivity).setImageRes(img_business_license,resultList.get(0).getPhotoPath());
                        imagePath3 = resultList.get(0).getPhotoPath();
                        break;
                }
            }

            @Override
            public void onHandlerFailure(String errorMsg,int requestPostion) {

            }
        },requestPostion);
    }


    private void getListData(String shopName, String address, String shopType, String imagCardOnPath, String imagCardDpwnPath, String businessLicensePath) {
        //        Base64.encode(BitmapUtils.getImage()
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIUser()//
                .getApplyMenber(UserQuery.getInstance().uploudApplyMenberInfo(shopName, address, shopType, imagCardOnPath, imagCardDpwnPath, businessLicensePath))//
                .compose(liToDestroy())//
                .compose(RxSchedulersHelper.io_main())//
                .compose(SchedulersHelper.handleResult())//
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())//
                .subscribe(new ResourceSubscriber<USERMENBERS>() {//
                    @Override
                    public void onNext(USERMENBERS usermenbers) {
//                        mApp.getUser().setUsers_real(usermenbers.getUsers_real());
                        mApp.getUser().setPurchaser_valid(usermenbers.getPurchaser_valid());
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


    @OnClick({R.id.imageView_back, R.id.apply, R.id.apply_zhuying_type, R.id.ly_idcard_up, R.id.ly_idcard_below, R.id.ly_business_license})
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
            case R.id.apply_zhuying_type:
                Intent intent = new Intent(mActivity, UserApplyMenberChildActivity.class);
                if (!TextUtils.isEmpty(apply_zhuying_type.getText().toString())) {
                    intent.putExtra(IntentKeywords.MENBER_TYPE, apply_zhuying_type.getText().toString());
                }
                startActivityForResult(intent, 1001);
                break;
            case R.id.apply:
                String name = edit_shopname.getText().toString();
                String addr = edit_apply_address.getText().toString();
                String typeStr = apply_zhuying_type.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    T.show(mActivity, "店铺名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(typeStr)) {
                    T.show(mActivity, "主营类目不能为空");
                    return;
                }
                if (TextUtils.isEmpty(imagePath1)) {
                    T.show(mActivity, "请选择身份证照片");
                    return;
                }
                if (TextUtils.isEmpty(imagePath2)) {
                    T.show(mActivity, "请选择身份证照片");
                    return;
                }
                if (TextUtils.isEmpty(imagePath3)) {
                    T.show(mActivity, "请选择营业执照");
                    return;
                }
                getListData(name, addr, typeStr, Base64.encode(BitmapUtils.getImage(mActivity, imagePath1, 300)), Base64.encode(BitmapUtils.getImage(mActivity, imagePath2, 300)), Base64.encode(BitmapUtils.getImage(mActivity, imagePath3, 300)));
                break;
            case R.id.ly_idcard_up:
                setChooesImg(20001);
                break;
            case R.id.ly_idcard_below:
                setChooesImg(20002);
                break;
            case R.id.ly_business_license:
                setChooesImg(20003);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 1001) {
            apply_zhuying_type.setText(data.getStringExtra(IntentKeywords.MENBER_TYPE) + "");
        }
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
