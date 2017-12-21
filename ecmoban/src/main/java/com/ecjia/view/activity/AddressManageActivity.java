package com.ecjia.view.activity;


import android.content.res.Resources;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.view.adapter.AddressManageAdapter;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.SwipeListView;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收货地址管理
 */
public class AddressManageActivity extends BaseActivity implements HttpResponse {

    private ImageView back;
    private SwipeListView listView;
    private View bg;
    private AddressManageAdapter addressManageAdapter;

    private AddressModel addressModel;
    ProgressDialog pd = null;
    public Handler messageHandler;
    private ImageView newaddress;
    public int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.address_manage);
        initView();
    }

    private void initView() {
        initTopView();
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        listView = (SwipeListView) findViewById(R.id.address_manage_list);
        bg = findViewById(R.id.address_list_bg);

        addressModel = new AddressModel(this);
        addressModel.addResponseListener(this);

        messageHandler = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == 1) {
                    Integer address_id = msg.arg1;
                    addressModel.addressDefault(address_id + "");
                }

            }
        };
    }

    public void setAddress() {
        if (addressModel.addressList.size() == 0) {
            listView.setVisibility(View.GONE);
            Resources resource = (Resources) getBaseContext().getResources();
            String non = resource.getString(R.string.non_address);
            ToastView toast = new ToastView(this, non);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            bg.setVisibility(View.VISIBLE);
        } else {
            bg.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            if (addressManageAdapter == null) {
                addressManageAdapter = new AddressManageAdapter(this, addressModel.addressList, flag, listView
                        .getRightViewWidth());
            }
            addressManageAdapter.setOnRightItemClickListener(new AddressManageAdapter.onRightItemClickListener() {

                @Override
                public void onRightItemClick(View v, final int position) {

                    Resources resource = (Resources) getBaseContext().getResources();
                    String can = resource.getString(R.string.can_not_delete);
                    String isdefault = resource.getString(R.string.is_default_address);
                    if (v.getId() == R.id.address_delete) {
                        if (addressModel.addressList.get(position).getDefault_address() == 1) {
                            ToastView toast = new ToastView(AddressManageActivity.this, can);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            final MyDialog dialog = new MyDialog(AddressManageActivity.this, resource.getString(R
                                    .string.point), resource.getString(R.string.address_delete_attention));
                            dialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                            dialog.setPositiveListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addressModel.addressDelete("" + addressModel.addressList.get(position).getId());
                                    addressModel.addressList.remove(position);
                                    listView.deleteItem(listView.getChildAt(position));
                                    dialog.dismiss();
                                    addressManageAdapter.notifyDataSetChanged();
                                }
                            });

                            dialog.setNegativeListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else if (v.getId() == R.id.address_setdefault) {
                        if (addressModel.addressList.get(position).getDefault_address() == 1) {
                            ToastView toast = new ToastView(AddressManageActivity.this, isdefault);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            addressModel.addressDefault("" + addressModel.addressList.get(position).getId());
                            addressManageAdapter.notifyDataSetChanged();
                        }
                    }
                    addressManageAdapter.notifyDataSetChanged();
                }
            });
            listView.setAdapter(addressManageAdapter);
            addressManageAdapter.parentHandler = messageHandler;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        addressModel.getAddressList();
    }


    public void onRefresh(int id) {
        addressModel.getAddressList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    // 设置对话框的最大自适应高度
    public void setListViewHeight(ListView listView) {
        // 获取屏幕宽度
        int totalHeight = getWindowManager().getDefaultDisplay().getHeight();
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int dialogHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            dialogHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (totalHeight - dialogHeight < 380) {
            params.height = totalHeight - 380;// 45为底部button的高度
        }

        listView.setLayoutParams(params);
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.ADDRESS_LIST) {
            if (status.getSucceed() == 1) {
                setAddress();
            }
        }
        if (url == ProtocolConst.ADDRESS_DELETE) {

            if(addressManageAdapter.getCount()>0){
                listView.setVisibility(View.VISIBLE);
                bg.setVisibility(View.GONE);
            }else{
                listView.setVisibility(View.GONE);
                bg.setVisibility(View.VISIBLE);
            }
        }
        if (url == ProtocolConst.ADDRESS_DEFAULT) {
            if (flag == 1) {
                Intent intent = new Intent();
                intent.putExtra("address_id", jo.optString("address_id"));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                addressModel.getAddressList();
            }
        }
    }

    @Override
    public void initTopView() {
        topView = (ECJiaTopView) findViewById(R.id.address_manage_topview);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setTitleText(R.string.manage_address);
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_IMAGE);
        topView.setRightImage(R.drawable.address_add, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressManageActivity.this, AddressAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}
