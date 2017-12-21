package com.ecjia.view.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.view.adapter.SpinnerAdapter;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

public class Address2Activity extends BaseActivity {
    private ImageView back;
    private TextView title, cancle, choosed_title, choosed_area, choosed_area1, choosed_area2, choosed_area3;
    private ListView listView;
    private SpinnerAdapter spinnerAdapter;
    private AddressModel addressModel;
    private int i = 0;

    private String country_id = "";
    private String province_id = "";
    private String city_id = "";
    private String county_id = "";

    private String country_name = "";
    private String province_name = "";
    private String city_name = "";
    private String county_name = "";
    public Handler Intenthandler;
    private String scoun;
    private MyProgressDialog mpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_b);
        PushAgent.getInstance(this).onAppStart();
        mpd = MyProgressDialog.createDialog(this);
        mpd.setCanceledOnTouchOutside(false);
        mpd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.KEYCODE_SOFT_LEFT){
                    mpd.dismiss();
                    finish();
                }
                return false;
            }
        });

        back = (ImageView) findViewById(R.id.address_title_back);
        title = (TextView) findViewById(R.id.address_title);
        cancle = (TextView) findViewById(R.id.address_title_cancle);
        choosed_title = (TextView) findViewById(R.id.address_choosed_title);
        choosed_area = (TextView) findViewById(R.id.address_choosed_area);
        choosed_area1 = (TextView) findViewById(R.id.address_choosed_area1);
        choosed_area2 = (TextView) findViewById(R.id.address_choosed_area2);
        choosed_area3 = (TextView) findViewById(R.id.address_choosed_area3);
        listView = (ListView) findViewById(R.id.address_list);
        final Resources resource = (Resources) getBaseContext().getResources();
        scoun = resource.getString(R.string.addressb_country);
        title.setText(scoun);
        choosed_title.setText(R.string.address2_choosed_area);
        choosed_area.setText(R.string.address2_no_choosed);
        Intenthandler = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.obj == ProtocolConst.REGION) {
                    if (msg.what == 1) {
                        if(i==msg.arg1){
                            setCountry();
                            mpd.dismiss();
                            listView.setEnabled(true);
                        }
                    }else if(msg.what==0){
                        if(i==msg.arg1){
                            mpd.dismiss();
                            listView.setEnabled(true);
                            ToastView toast=new ToastView(Address2Activity.this,resource.getString(R.string.error_network));
                            toast.show();
                        }
                    }
                }

            }
        };
        addressModel = new AddressModel(this);
        addressModel.region("0", i, Intenthandler);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mpd.show();
                listView.setEnabled(false);
                // TODO Auto-generated method stub
                if (i == 1) {
                    country_id = addressModel.regionsList0.get(position).getId();
                    country_name = addressModel.regionsList0.get(position).getName();
                    choosed_area.setText(addressModel.regionsList0.get(position).getName());
                } else if (i == 2) {
                    province_id = addressModel.regionsList0.get(position).getId();
                    province_name = addressModel.regionsList0.get(position).getName();
                    choosed_area1.setText(addressModel.regionsList0.get(position).getName());

                } else if (i == 3) {
                    city_id = addressModel.regionsList0.get(position).getId();
                    city_name = addressModel.regionsList0.get(position).getName();
                    choosed_area2.setText(addressModel.regionsList0.get(position).getName());

                } else if (i == 4) {
                    county_id = addressModel.regionsList0.get(position).getId();
                    county_name = addressModel.regionsList0.get(position).getName();
                    choosed_area3.setText(addressModel.regionsList0.get(position).getName());
                }

                addressModel.region(addressModel.regionsList0.get(position).getId(), i, Intenthandler);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0){

                }else {
                    if (i == 1) {
                        finish();
                    }
                    title.setText(scoun);
                    choosed_area.setText(R.string.address2_no_choosed);
                    choosed_area1.setText(null);
                    choosed_area2.setText(null);
                    choosed_area3.setText(null);
                    i = 0;
                    mpd.show();
                    addressModel.region("0", i, Intenthandler);
                }
            }
        });

    }


    public void setCountry() {
        Resources resource = (Resources) getBaseContext().getResources();
        String spro = resource.getString(R.string.select_province);
        String scity = resource.getString(R.string.select_city);
        String sarea = resource.getString(R.string.select_area);

        if (addressModel.regionsList0.size() == 0) {
            Intent intent = new Intent();
            intent.putExtra("country_id", country_id);
            intent.putExtra("province_id", province_id);
            intent.putExtra("city_id", city_id);
            intent.putExtra("county_id", county_id);

            intent.putExtra("country_name", country_name);
            intent.putExtra("province_name", province_name);
            intent.putExtra("city_name", city_name);
            intent.putExtra("county_name", county_name);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        i++;
        if (i == 2) {
            title.setText(spro);
        } else if (i == 3) {
            title.setText(scity);
        } else if (i == 4) {
            title.setText(sarea);
        }
        if(null==spinnerAdapter) {
            spinnerAdapter = new SpinnerAdapter(this, addressModel.regionsList0);
            listView.setAdapter(spinnerAdapter);
        }else{
            spinnerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

    }
}
