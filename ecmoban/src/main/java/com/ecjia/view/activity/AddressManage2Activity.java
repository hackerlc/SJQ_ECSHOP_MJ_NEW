package com.ecjia.view.activity;
/*
* 编辑收货地址
* */

import android.content.res.Resources;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressManage2Activity extends BaseActivity {


    private AddressModel addressModel;
    private EditText name;
    private EditText tel;
    private EditText email;
    private EditText phoneNum;
    private EditText zipCode;
    private LinearLayout area;
    private TextView address;
    private EditText detail;
    private CheckBox setDefault;

    private String country_id;
    private String province_id;
    private String city_id;
    private String county_id;
    private String address_id;

    private LinearLayout setdefaultitem;
    private Button address_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_address_edit);
        initView();
    }

    public void setAddressInfo() {
        name.setText(addressModel.address.getConsignee());
        tel.setText(addressModel.address.getTel());
        email.setText(addressModel.address.getEmail());
        phoneNum.setText(addressModel.address.getMobile());
        zipCode.setText(addressModel.address.getZipcode());
        detail.setText(addressModel.address.getAddress());
        StringBuffer sbf = new StringBuffer();
        sbf.append(addressModel.address.getProvince_name() + " ");
        sbf.append(addressModel.address.getCity_name() + " ");
        sbf.append(addressModel.address.getDistrict_name());
        address.setText(sbf.toString());

        if (addressModel.address.getDefault_address() == 1) {
            setdefaultitem.setVisibility(View.GONE);
        } else {
            setdefaultitem.setVisibility(View.VISIBLE);
        }
        country_id = addressModel.address.getCountry();
        province_id = addressModel.address.getProvince();
        city_id = addressModel.address.getCity();
        county_id = addressModel.address.getDistrict();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data != null) {
                country_id = data.getStringExtra("country_id");
                province_id = data.getStringExtra("province_id");
                city_id = data.getStringExtra("city_id");
                county_id = data.getStringExtra("county_id");

                StringBuffer sbf = new StringBuffer();
                sbf.append(data.getStringExtra("province_name") + " ");
                sbf.append(data.getStringExtra("city_name") + " ");
                sbf.append(data.getStringExtra("county_name"));
                address.setText(sbf.toString());
            }
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ADDRESS_INFO) {
            if (status.getSucceed() == 1) {
                setAddressInfo();
            }
        }
        if (url == ProtocolConst.ADDRESS_DEFAULT) {
            if (status.getSucceed() == 1) {
                Resources resource = (Resources) getBaseContext().getResources();
                String suc = resource.getString(R.string.successful_operation);
                ToastView toast = new ToastView(AddressManage2Activity.this, suc);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        }
        if (url == ProtocolConst.ADDRESS_UPDATE) {
            if (status.getSucceed() == 1) {
                if (setDefault.isChecked()) {
                    addressModel.addressDefault(address_id);
                } else {
                    Resources resource = (Resources) getBaseContext().getResources();
                    String suc = resource.getString(R.string.successful_operation);
                    ToastView toast = new ToastView(AddressManage2Activity.this, suc);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                }
            }
        }
    }

    void initView() {

        initTopView();

        name = (EditText) findViewById(R.id.address_manage2_name);
        tel = (EditText) findViewById(R.id.address_manage2_telNum);
        email = (EditText) findViewById(R.id.address_manage2_email);
        phoneNum = (EditText) findViewById(R.id.address_manage2_phoneNum);
        zipCode = (EditText) findViewById(R.id.address_manage2_zipCode);
        area = (LinearLayout) findViewById(R.id.address_manage2_area);
        address = (TextView) findViewById(R.id.address_manage2_address);
        detail = (EditText) findViewById(R.id.address_manage2_detail);
        setDefault = (CheckBox) findViewById(R.id.address_manage2_default);

        address_submit = (Button) findViewById(R.id.address_submit);
        setdefaultitem = (LinearLayout) findViewById(R.id.setdefault_item);
        area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressManage2Activity.this, Address2Activity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        });


        Intent intent = getIntent();
        address_id = intent.getStringExtra("address_id");

        addressModel = new AddressModel(this);
        addressModel.addResponseListener(this);
        addressModel.getAddressInfo(address_id);

        address_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String consignee = name.getText().toString();
                String telNum = tel.getText().toString();
                String mail = email.getText().toString();
                String mobile = phoneNum.getText().toString();
                String zipcode = zipCode.getText().toString();
                String address = detail.getText().toString();

                Resources resource = (Resources) getBaseContext().getResources();
                String name = resource.getString(R.string.add_name);
                String tel = resource.getString(R.string.add_tel);
                String email = resource.getString(R.string.add_email);
                String cor = resource.getString(R.string.add_correct_email);
                String addr = resource.getString(R.string.add_address);
                String con = resource.getString(R.string.confirm_address);
                String address_zipcode_notnull = resource.getString(R.string.address_zipcode_notnull);
                String address_name_toolong = resource.getString(R.string.address_name_toolong);
                String address_mobile_false = resource.getString(R.string.address_mobile_false);
                String address_zipcode_false = resource.getString(R.string.address_zipcode_false);

                if ("".equals(consignee)) {
                    ToastView toast = new ToastView(AddressManage2Activity.this, name);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (consignee.length() > 15) {
                    ToastView toast = new ToastView(AddressManage2Activity.this,
                            address_name_toolong);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mobile.length() != 11) {
                    ToastView toast = new ToastView(AddressManage2Activity.this,
                            address_mobile_false);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (country_id == null || province_id == null || city_id == null || county_id == null) {
                    ToastView toast = new ToastView(AddressManage2Activity.this, con);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if ("".equals(address)) {
                    ToastView toast = new ToastView(AddressManage2Activity.this, addr);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    addressModel.addressUpdate(address_id, consignee, mobile, mail, mobile, zipcode, address,
                            country_id, province_id, city_id, county_id);
                }

            }
        });
    }

    @Override
    public void initTopView() {
        topView = (ECJiaTopView) findViewById(R.id.address_edit_topview);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setTitleText(R.string.manage_edit_address);
    }
}
