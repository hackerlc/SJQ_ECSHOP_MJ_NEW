package com.ecjia.view.activity;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ecjia.base.BaseActivity;
import com.ecjia.widgets.ToastView;
import com.ecjia.view.adapter.InvoiceContentAdapter;
import com.ecjia.entity.responsemodel.INV_CONTENT_LIST;
import com.ecjia.entity.responsemodel.INV_TYPE_LIST;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.InvoiceTypeAdapter;
import com.umeng.message.PushAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class InvoiceActivity extends BaseActivity implements OnClickListener {

    private ImageView back;
    private Button save;

    private EditText taitou;

    private ListView listView1;
    private ListView listView2;
    private TextView invoice_clear;


    private ArrayList<INV_CONTENT_LIST> contentList = new ArrayList<INV_CONTENT_LIST>();//发票内容
    private ArrayList<INV_TYPE_LIST> typeList = new ArrayList<INV_TYPE_LIST>();//发票类型

    private InvoiceContentAdapter contentAdapter;
    private InvoiceTypeAdapter typeAdapter;
    private TextView type, content;
    private String type_id = null;
    private String content_id = null;
    private String rate = null;

    private String inv_payee = null; // 发票抬头

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice);
        PushAgent.getInstance(this).onAppStart();
        listView1 = (ListView) findViewById(R.id.invoice_list1);
        listView2 = (ListView) findViewById(R.id.invoice_list2);
        type = (TextView) findViewById(R.id.invoice_type);
        content = (TextView) findViewById(R.id.invoice_content);
        Intent intent = getIntent();
        String data = intent.getStringExtra("payment");
        inv_payee = intent.getStringExtra("inv_payee");//抬头
        content_id = intent.getStringExtra("inv_content");//发票内容
        type_id = intent.getStringExtra("inv_type");//发票类型

        try {
            if (StringUtils.isNotEmpty(data)) {

                JSONObject jo = new JSONObject(data);
                JSONArray contentArray = jo.optJSONArray("inv_content_list");
                if (null != contentArray && contentArray.length() > 0) {
                    contentList.clear();
                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject contentJsonObject = contentArray.getJSONObject(i);
                        INV_CONTENT_LIST content_Item = INV_CONTENT_LIST.fromJson(contentJsonObject);
                        contentList.add(content_Item);
                    }
                } else {
                    listView1.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                }

                JSONArray typetArray = jo.optJSONArray("inv_type_list");
                if (null != typetArray && typetArray.length() > 0) {
                    typeList.clear();
                    for (int i = 0; i < typetArray.length(); i++) {
                        JSONObject typeJsonObject = typetArray.getJSONObject(i);
                        INV_TYPE_LIST type_Item = INV_TYPE_LIST.fromJson(typeJsonObject);
                        typeList.add(type_Item);
                    }
                } else {
                    listView2.setVisibility(View.GONE);
                    type.setVisibility(View.GONE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        back = (ImageView) findViewById(R.id.invoice_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save = (Button) findViewById(R.id.invoice_save);
        save.setOnClickListener(this);
        taitou = (EditText) findViewById(R.id.invoice_taitou);
        taitou.setText(inv_payee);

        taitou.setFocusable(true);
        taitou.setFocusableInTouchMode(true);
        taitou.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) taitou.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(taitou, 0);
            }

        }, 300);

        //发票内容
        contentAdapter = new InvoiceContentAdapter(this, contentList, content_id);
        listView1.setAdapter(contentAdapter);
        setListViewHeightBasedOnChildren(listView1);
        listView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                contentAdapter.flag = position;
                contentAdapter.notifyDataSetChanged();
                content_id = contentList.get(position).getValue();

            }
        });

        //发票类型
        typeAdapter = new InvoiceTypeAdapter(this, typeList, type_id);
        listView2.setAdapter(typeAdapter);
        setListViewHeightBasedOnChildren(listView2);


        listView2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                typeAdapter.flag = position;
                typeAdapter.notifyDataSetChanged();
                type_id = typeList.get(position).getLabel_value();
                rate = typeList.get(position).getRate();
            }
        });
        invoice_clear = (TextView) findViewById(R.id.invoice_clear);
        invoice_clear.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invoice_save:
                if ((TextUtils.isEmpty(taitou.getText().toString()) && TextUtils.isEmpty(type_id) && TextUtils.isEmpty(content_id))
                        || (!TextUtils.isEmpty(taitou.getText().toString()) && !TextUtils
                        .isEmpty(type_id) && !TextUtils.isEmpty(content_id))) {
                    Intent intent = new Intent();
                    intent.putExtra("inv_type", type_id);
                    intent.putExtra("inv_type_rate", rate);//税率
                    intent.putExtra("inv_content", content_id);
                    intent.putExtra("inv_payee", taitou.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    ToastView toast = new ToastView(InvoiceActivity.this, "请填写完整的发票");
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;

            case R.id.invoice_clear:
                type_id = null;
                content_id = null;
                inv_payee = null;
                rate = null;
                taitou.setText("");
                contentAdapter.flag = -1;
                typeAdapter.flag = -1;
                contentAdapter.content = null;
                typeAdapter.id = null;
                contentAdapter.notifyDataSetChanged();
                typeAdapter.notifyDataSetChanged();
                break;
        }

    }

    // 设置listview让其完全显示
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
