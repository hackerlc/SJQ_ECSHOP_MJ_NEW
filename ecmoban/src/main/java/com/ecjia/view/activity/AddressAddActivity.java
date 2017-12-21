package com.ecjia.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressAddActivity extends BaseActivity implements HttpResponse {

    private Button insert;
    private EditText name;
    private EditText tel;
    private EditText email;
    private EditText phoneNum;
    private EditText zipCode;
    private LinearLayout area;
    private TextView address;
    private EditText detail;


    private String country_id;
    private String province_id;
    private String city_id;
    private String county_id;
    private AddressModel addressModel;
    private boolean isfirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_address_add);
        initView();
        isfirst=getIntent().getBooleanExtra("isfirst",false);
    }


    void initView() {
        initTopView();
        insert = (Button) findViewById(R.id.address_add_submit);
        Resources resource = getBaseContext().getResources();

        name = (EditText) findViewById(R.id.add_address_name);
        tel = (EditText) findViewById(R.id.add_address_telNum);
        email = (EditText) findViewById(R.id.add_address_email);
        phoneNum = (EditText) findViewById(R.id.add_address_phoneNum);
        zipCode = (EditText) findViewById(R.id.add_address_zipCode);
        area = (LinearLayout) findViewById(R.id.add_address_area);
        address = (TextView) findViewById(R.id.add_address_address);
        detail = (EditText) findViewById(R.id.add_address_detail);


        area.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressAddActivity.this, Address2Activity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);

            }
        });

        insert.setOnClickListener(new OnClickListener() {

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
                String addtel = resource.getString(R.string.add_tel);
                String email = resource.getString(R.string.add_email);
                String cor = resource.getString(R.string.add_correct_email);
                String adda = resource.getString(R.string.add_address);
                String con = resource.getString(R.string.confirm_address);
                String address_zipcode_notnull = resource.getString(R.string.address_zipcode_notnull);
                String address_name_toolong = resource.getString(R.string.address_name_toolong);
                String address_mobile_false = resource.getString(R.string.address_mobile_false);
                String address_zipcode_false = resource.getString(R.string.address_zipcode_false);
                String address_email_false = resource.getString(R.string.address_email_false);


                if ("".equals(consignee)) {
                    ToastView toast = new ToastView(AddressAddActivity.this, name);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (consignee.length() > 15) {
                    ToastView toast = new ToastView(AddressAddActivity.this, address_name_toolong);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(mobile)) {
                    ToastView toast = new ToastView(AddressAddActivity.this, addtel);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (mobile.length() != 11) {
                    ToastView toast = new ToastView(AddressAddActivity.this, address_mobile_false);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (country_id == null || province_id == null || city_id == null || county_id == null) {
                    ToastView toast = new ToastView(AddressAddActivity.this, con);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(address)) {
                    ToastView toast = new ToastView(AddressAddActivity.this, adda);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                else {
                    addressModel = new AddressModel(AddressAddActivity.this);
                    addressModel.addResponseListener(AddressAddActivity.this);
                    addressModel.addAddress(consignee, mobile, mail, mobile, zipcode, address, country_id,
                            province_id, city_id, county_id);
                }

            }
        });
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
                sbf.append(data.getStringExtra("country_name") + " ");
                sbf.append(data.getStringExtra("province_name") + " ");
                sbf.append(data.getStringExtra("city_name") + " ");
                sbf.append(data.getStringExtra("county_name"));
                address.setText(sbf.toString());

            }
        }
    }

    public static boolean isEmail(String email) {
        String str = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.ADDRESS_ADD)) {
            if (status.getSucceed() == 1) {
                if(isfirst){
                    addressModel.getAddressList();
                }else{
                    ToastView toastView=new ToastView(AddressAddActivity.this,R.string.address_add_succeed);
                    toastView.show();
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }
            }else{
                ToastView toastView=new ToastView(AddressAddActivity.this,status.getError_desc());
                toastView.show();
            }
        }else
        if (url.equals(ProtocolConst.ADDRESS_LIST)) {
            if (status.getSucceed() == 1) {
                if(addressModel.addressList.size()>0){
                    addressModel.addressDefault("" + addressModel.addressList.get(0).getId());
                }
            }
        }else
        if (url.equals(ProtocolConst.ADDRESS_DEFAULT)) {
            if (status.getSucceed() == 1) {
                ToastView toastView=new ToastView(AddressAddActivity.this,R.string.address_add_succeed);
                toastView.show();
                setResult(Activity.RESULT_OK, new Intent());
                finish();
            }
        }
    }

    @Override
    public void initTopView() {
        topView = (ECJiaTopView) findViewById(R.id.add_address_topview);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });
        topView.setTitleText(R.string.manage_add_address);
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_GONE);
    }
}
