package com.ecjia.view.adapter;

import android.content.Context;
import android.view.View;

import com.ecjia.entity.responsemodel.USERMENBERCHILDTYPE;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-03.
 */

public class UserApplyMenberChildAdapter extends CommonAdapter<USERMENBERCHILDTYPE> {

    public UserApplyMenberChildAdapter(Context context, List<USERMENBERCHILDTYPE> datas) {
        super(context, R.layout.item_user_applymenber_child, datas);
    }

    @Override
    protected void convert(ViewHolder holder, USERMENBERCHILDTYPE usermenbers, int position) {

        holder.setText(R.id.tv_type,usermenbers.getTagName());
        if (usermenbers.isSelect()) {
            holder.setImageResource(R.id.imageView3, R.drawable.ic_menber_select);
        } else {
            holder.setImageResource(R.id.imageView3, R.drawable.img_default_null);
        }
        holder.setOnClickListener(R.id.ly_parent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(usermenbers.isSelect()){
                       usermenbers.setSelect(false);
                       holder.setImageResource(R.id.imageView3, R.drawable.img_default_null);
                   } else{
                       usermenbers.setSelect(true);
                       holder.setImageResource(R.id.imageView3, R.drawable.ic_menber_select);
                   }
            }
        });

    }
}
