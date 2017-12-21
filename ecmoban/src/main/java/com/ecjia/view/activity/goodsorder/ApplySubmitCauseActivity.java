package com.ecjia.view.activity.goodsorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.intentmodel.OrderGoodInrtent;
import com.ecjia.entity.responsemodel.ADDRESS;
import com.ecjia.entity.responsemodel.REJECTEDGOODSTATU;
import com.ecjia.entity.responsemodel.RETURNCAUSE;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.common.BitmapUtils;
import com.ecjia.util.common.BottomChoosePhotoUtils;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.common.ImageUtils;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.util.common.T;
import com.ecjia.util.gallery.GalleryImageUtils;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.widgets.paycenter.alipay.alipayutil.Base64;
import com.ecmoban.android.sijiqing.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：提交申请售后原因页面
 * Created by sun
 * Created time 2017-03-06.
 */

public class ApplySubmitCauseActivity extends LibActivity implements HttpResponse {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.spinner_type)
    Spinner spinner_type;//类型
    @BindView(R.id.tv_reason_title)
    TextView tv_reason_title;//退换货说明,title


    @BindView(R.id.ly_return_money)
    LinearLayout ly_return_money;//退款金额
    @BindView(R.id.edit_money)
    EditText edit_money;//退款金额
    @BindView(R.id.line_return_money)
    View line_return_money;//退款金额的下划线

    @BindView(R.id.ly_return_shipping_fee_money)
    LinearLayout ly_return_shipping_fee_money;//退运费金额
    @BindView(R.id.edit_shipping_fee_money)
    EditText edit_shipping_fee_money;//退运费金额
    @BindView(R.id.line_return_shipping_fee_money)
    View line_return_shipping_fee_money;//退运费金额的下划线


    @BindView(R.id.ly_return_goods_count)
    LinearLayout ly_return_goods_count;//退换货数量
    @BindView(R.id.tv_return_goods_count_title)
    TextView tv_return_goods_count_title;//退换货数量,title
    @BindView(R.id.shop_car_item_min)
    TextView shop_car_item_min;//退换货数量减号
    @BindView(R.id.shop_car_item_sum)
    TextView shop_car_item_sum;//退换货数量加号
    @BindView(R.id.shop_car_item_editNum)
    TextView shop_car_item_editNum;//退换货数量
    @BindView(R.id.line_return_goods_count)
    View line_return_goods_count;//退换货数量的下划线

    @BindView(R.id.edit_comment)
    EditText edit_comment;//退换货说明
    @BindView(R.id.tv_comment_title)
    TextView tv_comment_title;//退换货说明,title


    @BindView(R.id.ly_img)
    LinearLayout ly_img;//上传的图片
    @BindView(R.id.tv_img_content)
    TextView tv_img_content;//上传的图片说明

    @BindView(R.id.iv_upload1)
    ImageView iv_upload1;
    @BindView(R.id.tv_upload1)
    TextView tv_upload1;
    @BindView(R.id.iv_del_pic1)
    ImageView iv_del_pic1;

    @BindView(R.id.iv_upload2)
    ImageView iv_upload2;
    @BindView(R.id.tv_upload2)
    TextView tv_upload2;
    @BindView(R.id.iv_del_pic2)
    ImageView iv_del_pic2;

    @BindView(R.id.iv_upload3)
    ImageView iv_upload3;
    @BindView(R.id.tv_upload3)
    TextView tv_upload3;
    @BindView(R.id.iv_del_pic3)
    ImageView iv_del_pic3;

    @BindView(R.id.balance_user)//地址的布局
            LinearLayout balance_user;
    @BindView(R.id.balance_name)
    TextView balance_name;
    @BindView(R.id.balance_phoneNum)
    TextView balance_phoneNum;
    @BindView(R.id.balance_address)
    TextView balance_address;


    private ArrayList<RETURNCAUSE> spinnerTypeList = new ArrayList<>();
    private Map<String, String> spinnerMap = new HashMap<>();

    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ArrayList<ImageView> delImgViews = new ArrayList<ImageView>();
    private ArrayList<TextView> textViews = new ArrayList<TextView>();

    private ArrayList<String> pathlist = new ArrayList<String>();
    private String returnType;//退换货类型  0仅退款   1退货    2换货)
    private String recId;
    private AddressModel addressModel;
    private ADDRESS address;

    private OrderGoodInrtent orderGoodInrtent;
    private float allMoney;
    private int allNumber = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_applycause;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        GalleryImageUtils.configGallery(mActivity);
        Bundle bundle = getIntent().getExtras();
        recId = bundle.getString(IntentKeywords.REC_ID);
        returnType = bundle.getString(IntentKeywords.RETURN_TYPE);
        orderGoodInrtent = JsonUtil.getObj(bundle.getString(IntentKeywords.ORDER_GOODINRTENT), OrderGoodInrtent.class);
        balance_user.setVisibility(View.GONE);
        getReturnause();//获取选择理由类别
        edit_shipping_fee_money.setText("0");
        shop_car_item_editNum.setText("1");
        if ("0".equals(returnType)) {//退换货类型  0仅退款   1退货    2换货)
            textView_title.setText("仅退款");
            // 退货原因， 退货金额 运费金额 不要数量 退货说明  不要图片
            balance_user.setVisibility(View.GONE);
            ly_return_money.setVisibility(View.VISIBLE);//退货金额
            line_return_money.setVisibility(View.VISIBLE);//
            ly_return_shipping_fee_money.setVisibility(View.VISIBLE);//运费金额
            line_return_shipping_fee_money.setVisibility(View.VISIBLE);
            ly_return_goods_count.setVisibility(View.GONE);//不要数量
            line_return_goods_count.setVisibility(View.GONE);
            ly_img.setVisibility(View.GONE);//不要图片
            tv_img_content.setVisibility(View.GONE);
            //allNumber = Integer.parseInt(orderGoodInrtent.getReturn_number());
            initReturnMoney();
        } else if ("1".equals(returnType)) {
            textView_title.setText("退货退款");
            //需要传照片时初始化
            initImgList();//集合

            // 退货原因， 退货金额  运费金额 退货数量 退货说明  图片
            balance_user.setVisibility(View.GONE);
            ly_return_money.setVisibility(View.VISIBLE);//退货金额
            line_return_money.setVisibility(View.VISIBLE);//
            ly_return_shipping_fee_money.setVisibility(View.VISIBLE);//运费金额
            line_return_shipping_fee_money.setVisibility(View.VISIBLE);
            ly_return_goods_count.setVisibility(View.VISIBLE);//退货数量
            line_return_goods_count.setVisibility(View.VISIBLE);
            ly_img.setVisibility(View.VISIBLE);//图片
            initReturnMoney();
        } else if ("2".equals(returnType)) {
            textView_title.setText("换货");
            //需要传照片时初始化
            initImgList();//集合

            //用户地址  换货原因   换货说明  图片
            balance_user.setVisibility(View.VISIBLE);
            ly_return_money.setVisibility(View.GONE);//退货金额 no
            line_return_money.setVisibility(View.GONE);//
            ly_return_shipping_fee_money.setVisibility(View.GONE);//运费金额 no
            line_return_shipping_fee_money.setVisibility(View.GONE);
            ly_return_goods_count.setVisibility(View.VISIBLE);//换货数量
            line_return_goods_count.setVisibility(View.VISIBLE);
            ly_img.setVisibility(View.VISIBLE);//图片

            tv_reason_title.setText("换货原因");
            tv_return_goods_count_title.setText("换货数量");
            tv_comment_title.setText("换货说明");
            addressModel = new AddressModel(this);
            addressModel.addResponseListener(this);
            addressModel.getAddressList();
        } else {
            finish();
            return;
        }
    }

    private void initReturnMoney() {
        edit_money.setHint("最多可申请金额： " + getAllMoney());
//        edit_money.setText("0");
        edit_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    float n = Float.parseFloat(s.toString());
                    if (n > getAllMoney()) {
                        edit_money.setText(getAllMoney() + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_shipping_fee_money.setHint("最多可申请运费金额： " + getShippingFee());
        edit_shipping_fee_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    float n = Float.parseFloat(s.toString());
                    if (n > getShippingFee()) {
                        edit_shipping_fee_money.setText(getShippingFee() + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private float getAllMoney() {
//        return allNumber * Float.parseFloat(orderGoodInrtent.getShould_return());
        return Float.parseFloat(orderGoodInrtent.getShould_return());
    }

    private float getShippingFee() {
        return Float.parseFloat(orderGoodInrtent.getReturn_shipping_fee());
    }

    private void initImgList() {
        imageViews.add(iv_upload1);
        textViews.add(tv_upload1);
        delImgViews.add(iv_del_pic1);
        imageViews.add(iv_upload2);
        textViews.add(tv_upload2);
        delImgViews.add(iv_del_pic2);
        imageViews.add(iv_upload3);
        textViews.add(tv_upload3);
        delImgViews.add(iv_del_pic3);
    }

    private void addImgView() {
        int size = pathlist.size();
        for (int i = 0; i < size; i++) {
            ImageUtils.showImage(mActivity, pathlist.get(i), imageViews.get(i));
//            ImageLoaderForLV.getInstance(mActivity).setImageRes(imageViews.get(i),pathlist.get(i));
            imageViews.get(i).setVisibility(View.VISIBLE);
            delImgViews.get(i).setVisibility(View.VISIBLE);
            textViews.get(i).setVisibility(View.INVISIBLE);
        }
        if (size < textViews.size()) {
            textViews.get(size).setVisibility(View.VISIBLE);
        }
    }

    private void delImgView(int position) {
        int size = pathlist.size();
        if (position <= size) {
            pathlist.remove(position - 1);
        }
        addImgView();

        for (int i = imageViews.size() - 1; i > pathlist.size() - 1; i--) {
            imageViews.get(i).setVisibility(View.INVISIBLE);
            delImgViews.get(i).setVisibility(View.INVISIBLE);
            if (i == 0) {
                imageViews.get(i).setVisibility(View.INVISIBLE);
                delImgViews.get(i).setVisibility(View.INVISIBLE);
                textViews.get(i).setVisibility(View.VISIBLE);
            } else {
                imageViews.get(i).setVisibility(View.INVISIBLE);
                delImgViews.get(i).setVisibility(View.INVISIBLE);
                textViews.get(i).setVisibility(View.INVISIBLE);
                textViews.get(pathlist.size()).setVisibility(View.VISIBLE);
            }
        }
    }

    private void setChooesImg(int requestPostion) {
        BottomChoosePhotoUtils.newInstance().show(getSupportFragmentManager(), new BottomChoosePhotoUtils.OnChoosePhoneResult() {
            @Override
            public void onHandlerSuccess(List<PhotoInfo> resultList, int requestPostion) {
                switch (requestPostion) {
                    case 20001:
//                    ImageUtils.showImageFilePath(mActivity, imageFilePath, img_idcard_up);
//                        ImageLoaderForLV.getInstance(mActivity).setImageRes(iv_upload1,resultList.get(0).getPhotoPath());
//                        imgPath = resultList.get(0).getPhotoPath();
//                        iv_del_pic1.setVisibility(View.VISIBLE);
                        pathlist.add(resultList.get(0).getPhotoPath());
                        addImgView();
                        break;
                }
            }

            @Override
            public void onHandlerFailure(String errorMsg, int requestPostion) {

            }
        }, requestPostion);
    }

    @OnClick({R.id.imageView_back, R.id.tv_upload1, R.id.iv_del_pic1, R.id.tv_upload2, R.id.iv_del_pic2, R.id.tv_upload3, R.id.iv_del_pic3,
            R.id.apply, R.id.balance_user, R.id.shop_car_item_min, R.id.shop_car_item_sum})
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
            case R.id.tv_upload1:
                setChooesImg(20001);
                break;
            case R.id.iv_del_pic1:
                delImgView(1);
                break;
            case R.id.tv_upload2:
                setChooesImg(20001);
                break;
            case R.id.iv_del_pic2:
                delImgView(2);
                break;
            case R.id.tv_upload3:
                setChooesImg(20001);
                break;
            case R.id.iv_del_pic3:
                delImgView(3);
                break;
            case R.id.apply:
                // 退货原因， 退货金额 运费金额 不要数量 退货说明  图片
                OrderGoodInrtent requestOrder = new OrderGoodInrtent();
                String rejected_reason = spinnerMap.get(spinner_type.getSelectedItem().toString());//原因
                String rejected_explain = edit_comment.getText().toString();//内容
                String return_number = shop_car_item_editNum.getText().toString();//数量
                String should_return = edit_money.getText().toString();//价格
                String return_shipping_fee = edit_shipping_fee_money.getText().toString();//运费
                if(TextUtils.isEmpty(return_shipping_fee)){
                    return_shipping_fee = "0";
                }
                if(TextUtils.isEmpty(should_return)){
                    should_return = "0";
                }
                if (TextUtils.isEmpty(rejected_reason)) {
                    T.show(mActivity, "请请选择原因");
                    return;
                }
                if (TextUtils.isEmpty(rejected_explain)) {
                    T.show(mActivity, "请填写退换货说明");
                    return;
                }
                requestOrder.setRec_id(recId);
                requestOrder.setCause_id(rejected_reason);
                requestOrder.setReturn_brief(rejected_explain);
                requestOrder.setCredentials("0");
                if ("0".equals(returnType)) {//退换货类型  0仅退款   1退货    2换货)
                    requestOrder.setReturn_type("0");
                    if (Float.parseFloat(should_return) <= 0) {
                        T.show(mActivity, "请填写金额");
                        return;
                    }
                    if (Float.parseFloat(return_shipping_fee) <= 0) {
                        return_shipping_fee = "0";
                    }
                    requestOrder.setShould_return(should_return);
                    requestOrder.setReturn_shipping_fee(return_shipping_fee);

                } else if ("1".equals(returnType)) {
                    requestOrder.setReturn_type("1");
                    if (Float.parseFloat(should_return) <= 0) {
                        T.show(mActivity, "请填写金额");
                        return;
                    }
                    if (Float.parseFloat(return_shipping_fee) <= 0) {
                        return_shipping_fee = "0";
                    }
                    if (Float.parseFloat(return_number) <= 0) {
                        T.show(mActivity, "数量不能为0");
                        return;
                    }
                    requestOrder.setShould_return(should_return);
                    requestOrder.setReturn_shipping_fee(return_shipping_fee);
                    requestOrder.setReturn_number(return_number);

                } else if ("2".equals(returnType)) {
                    requestOrder.setReturn_type("2");
                    if (Float.parseFloat(return_number) <= 0) {
                        T.show(mActivity, "数量不能为0");
                        return;
                    }
                    if (address == null) {
                        T.show(mActivity, "请选择收货地址");
                        return;
                    }
                    requestOrder.setReturn_number(return_number);
                    requestOrder.setCountry(address.getCountry());
                    requestOrder.setProvince(address.getProvince());
                    requestOrder.setCity(address.getCity());
                    requestOrder.setStreet(address.getDistrict());
                    requestOrder.setAddress(address.getAddress());
                    requestOrder.setZipcode(address.getZipcode());
                    requestOrder.setAddressee(address.getAddress());
                    requestOrder.setPhone(address.getMobile());
                }
                String first_image = pathlist.size() > 0 ? Base64.encode(BitmapUtils.getImage(mActivity, pathlist.get(0), 300)) : "";
                String second_image = pathlist.size() > 1 ? Base64.encode(BitmapUtils.getImage(mActivity, pathlist.get(1), 300)) : "";
                String third_image = pathlist.size() > 2 ? Base64.encode(BitmapUtils.getImage(mActivity, pathlist.get(2), 300)) : "";
                requestOrder.setFirst_image(first_image);
                requestOrder.setSecond_image(second_image);
                requestOrder.setThird_image(third_image);
//                LG.i("jo==" + JsonUtil.toHttpFormatString(requestOrder));
//                edit_comment.setText(GsonUtils.getInstance().toJson(requestOrder));
                submitInfo(JsonUtil.toHttpFormatString(requestOrder));
                break;
            case R.id.balance_user:
                Intent intent = new Intent(this, AddressManageActivity.class);
                intent.putExtra("flag", 1);
                startActivityForResult(intent, 1);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.shop_car_item_min:
                if (allNumber - 1 <= 0) {
                    T.show(mActivity, "最小数量不能小于1");
                } else {
                    allNumber--;
                }
                shop_car_item_editNum.setText(allNumber + "");
                break;
            case R.id.shop_car_item_sum:
                if (allNumber + 1 > Integer.parseInt(orderGoodInrtent.getReturn_number())) {
                    T.show(mActivity, "已经超过最大数量");
                } else {
                    allNumber++;
                }
                shop_car_item_editNum.setText(allNumber + "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                addressModel.getAddressList();
            }
        }
    }

    private void getReturnause() {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getReturnause(GoodsQuery.getInstance().getReturnause())
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ArrayList<RETURNCAUSE>>() {
                    @Override
                    public void onNext(ArrayList<RETURNCAUSE> returncauses) {
                        int size = returncauses.size();
                        String[] arr = new String[size];
                        for (int i = 0; i < size; i++) {
                            arr[i] = returncauses.get(i).getCause_name();
                            spinnerMap.put(arr[i], returncauses.get(i).getCause_id());
                        }
                        ArrayAdapter<String> zhuyingAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, arr);
                        zhuyingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //绑定 Adapter到控件
                        spinner_type.setAdapter(zhuyingAdapter);
                        spinner_type.setSelection(0);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void submitInfo(String queryStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIGoods()
                .getRejected(queryStr)
//                .getRejected(GoodsQuery.getInstance().getRejected(order_id, rejected_type, rejected_reason, rejected_value,
//                        rejected_explain, first_image, second_image, third_image))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<REJECTEDGOODSTATU>() {
                    @Override
                    public void onNext(REJECTEDGOODSTATU rejectedgoodstatu) {
                        MyEvent event = new MyEvent(EventKeywords.UPDATE_ORDER);
                        EventBus.getDefault().post(event);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ADDRESS_LIST) {
            if (status.getSucceed() == 1) {
                setAddress();
            }
        }
    }

    public void setAddress() {
        if (addressModel.addressList.size() > 0) {
            address = addressModel.addressList.get(0);
            balance_name.setText(address.getConsignee());
            balance_phoneNum.setText(address.getMobile());
            StringBuffer sbf = new StringBuffer();
            sbf.append(address.getProvince_name() + " ");
            sbf.append(address.getCity_name() + " ");
            sbf.append(address.getDistrict_name() + " ");
            sbf.append(address.getAddress());
//        address.setText(sbf.toString());
            String c = ToDBC(sbf.toString());
            balance_address.setText(c);
        }
    }

    public String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
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
