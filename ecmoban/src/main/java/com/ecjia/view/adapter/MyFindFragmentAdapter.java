package com.ecjia.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.LG;
import com.ecjia.util.MyBitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/15.
 */
public class MyFindFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QUICK> findArrayList;
    private MyBitmapUtils myBitmapUtils;
    private ECJiaDealUtil ecJiaDealUtil;

    public MyFindFragmentAdapter(Context context, ArrayList<QUICK> list) {
        this.context = context;
        findArrayList = list;
        myBitmapUtils = MyBitmapUtils.getInstance(context);
        ecJiaDealUtil = new ECJiaDealUtil(context);
    }


    @Override
    public int getCount() {
        return findArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return findArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final QUICK quick = findArrayList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.customfind_item, null);
            holder.item = (LinearLayout) view.findViewById(R.id.myfind_item);
            holder.img = (ImageView) view.findViewById(R.id.myfind_img);
            holder.text = (TextView) view.findViewById(R.id.myfind_text);
            holder.toplongline = view.findViewById(R.id.find_top_long_line);
            holder.topshortline = view.findViewById(R.id.find_top_short_line);
            holder.buttomline = view.findViewById(R.id.find_buttom_line);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        myBitmapUtils.displayImage(holder.img, quick.getImg());
        holder.text.setText(quick.getText());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quick.getMap().isEmpty()) {
                    Intent intent = new Intent();
                    intent.setClass(context, WebViewActivity.class);
                    intent.putExtra("url", initECJiaUrl(quick.getUrl()));
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                } else {
                    ecJiaDealUtil.dealEcjiaAction(quick.getMap());
                }
            }
        });
        if (findArrayList.size() == 1) {
            holder.toplongline.setVisibility(View.VISIBLE);
            holder.topshortline.setVisibility(View.GONE);
            holder.buttomline.setVisibility(View.VISIBLE);
        } else if (findArrayList.size() == 2) {
            if (i == 0) {
                holder.toplongline.setVisibility(View.VISIBLE);
                holder.topshortline.setVisibility(View.GONE);
                holder.buttomline.setVisibility(View.GONE);
            } else {
                holder.toplongline.setVisibility(View.GONE);
                holder.topshortline.setVisibility(View.VISIBLE);
                holder.buttomline.setVisibility(View.VISIBLE);
            }
        } else {
            if (i == 0) {
                holder.toplongline.setVisibility(View.VISIBLE);
                holder.topshortline.setVisibility(View.GONE);
                holder.buttomline.setVisibility(View.GONE);
            } else if (i > 0 && i < findArrayList.size() - 1) {
                holder.toplongline.setVisibility(View.GONE);
                holder.topshortline.setVisibility(View.VISIBLE);
                holder.buttomline.setVisibility(View.GONE);
            } else if (i == findArrayList.size() - 1) {
                holder.toplongline.setVisibility(View.GONE);
                holder.topshortline.setVisibility(View.VISIBLE);
                holder.buttomline.setVisibility(View.VISIBLE);

            } else {

            }
        }

        return view;
    }

    private class ViewHolder {
        private View toplongline, topshortline, buttomline;
        private LinearLayout item;
        private ImageView img;
        private TextView text;
    }

    //初始化Touch登录
    private String initECJiaUrl(String url) {
        String TouchUrl = "";
        if (!TextUtils.isEmpty(SESSION.getInstance().getSid())) {
            TouchUrl = url.replace("token=token", "token=" + SESSION.getInstance().getSid());
        } else {
            TouchUrl = url.replace("token=token", "");
        }
        LG.i("initurl==" + TouchUrl);
        return TouchUrl;
    }
}
