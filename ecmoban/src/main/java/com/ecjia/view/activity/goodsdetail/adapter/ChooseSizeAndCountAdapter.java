package com.ecjia.view.activity.goodsdetail.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecjia.entity.eventmodel.GoodSpecificationEvent;
import com.ecjia.entity.responsemodel.PRODUCTNUMBYCF;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.ecmoban.android.sijiqing.R;

import java.util.List;

import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-30.
 */

public class ChooseSizeAndCountAdapter extends RecyclerView.Adapter<ChooseSizeAndCountAdapter.ViewHolder> {

    public List<PRODUCTNUMBYCF> datas;
    public String colorName;
    public Context context;
    private OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(View view, String circleID, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public ChooseSizeAndCountAdapter(Context context, List<PRODUCTNUMBYCF> datas,String colorName) {
        this.datas = datas;
        this.context = context;
        this.colorName = colorName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chooesgood_sizeandcolor, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PRODUCTNUMBYCF szieAndCount = datas.get(position);
        holder.tv_size.setTag(position);
        holder.tv_size.setText(szieAndCount.getShowSize(colorName));
        holder.etNumber.setText(szieAndCount.getSizeSelectCount() + "");
        if (Integer.parseInt(szieAndCount.getProduct_number())<=0) {//缺货状态
            holder.tv_size.setTextColor(context.getResources().getColor(R.color._999999));
            holder.tv_count.setTextColor(context.getResources().getColor(R.color._999999));
            holder.etNumber.setTextColor(context.getResources().getColor(R.color._999999));
            holder.tv_count.setText("缺货");
            holder.etNumber.clearFocus();
            holder.etNumber.setFocusable(false);
            holder.btMinus.clearFocus();
            holder.btMinus.setFocusable(false);
            holder.btAdd.clearFocus();
            holder.btAdd.setFocusable(false);
        } else {//货源充足
            holder.tv_size.setTextColor(context.getResources().getColor(R.color._333333));
            holder.tv_count.setTextColor(context.getResources().getColor(R.color._333333));
            holder.etNumber.setTextColor(context.getResources().getColor(R.color._333333));
            holder.tv_count.setText(szieAndCount.getProduct_number());
            holder.btMinus.setFocusable(true);
            holder.btAdd.setFocusable(true);
        }
    }

    @Override
    public int getItemCount() {
        if (datas == null) {
            return 0;
        } else {
            return datas.size();
        }
    }

    Handler mHandler = new Handler(msg -> {
        if (msg.what == 1) {
            notifyDataSetChanged();
        }
        return true;
    });


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText etNumber;
        private TextView tv_count;
        private TextView tv_size;
        private Button btMinus;
        private Button btAdd;

        public ViewHolder(View view) {
            super(view);
            etNumber = (EditText) view.findViewById(R.id.etNumber);
            tv_count = (TextView) view.findViewById(R.id.tv_count);
            tv_size = (TextView) view.findViewById(R.id.tv_size);
            btMinus = (Button) view.findViewById(R.id.btMinus);
            btAdd = (Button) view.findViewById(R.id.btAdd);


            etNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = (int) tv_size.getTag();
                    String s1 = s.toString();
                    if (!TextUtils.isEmpty(s1)) {
                        datas.get(position).setSizeSelectCount(Integer.parseInt(s.toString()) > Integer.parseInt(datas.get(position).getProduct_number()) ? Integer.parseInt(datas.get(position).getProduct_number()) : Integer.parseInt(s.toString()));
                        //ToastUtil.makeShortToast(context, datas.get(position).getSizeSelectCount()+"222222");
//                        Log.d("》》》》》", datas.get(position).getSizeSelectCount() + "getSizeSelectCount" + "===" + position + "===" + datas.get(position).getSizeAliasSize());
                    }else{
                        datas.get(position).setSizeSelectCount(0);
                    }
                    eventBus();
                    mHandler.handleMessage(mHandler.obtainMessage(1));
                }
            });

            btMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tv_size.getTag();
//                    int bei = Integer.parseInt(datas.get(position).getSizeBnum());
                    int bei = 1;
                    int count = datas.get(position).getSizeSelectCount();
                    datas.get(position).setSizeSelectCount(count - bei <= 0 ? 0 : count - bei);
                    notifyDataSetChanged();
                    eventBus();
                }
            });

            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) tv_size.getTag();
//                    int bei = Integer.parseInt(datas.get(position).getSizeBnum());
                    int bei = 1;
                    int count = datas.get(position).getSizeSelectCount();
                    datas.get(position).setSizeSelectCount(count + bei > Integer.parseInt(datas.get(position).getProduct_number()) ? count : count + bei);
                    notifyDataSetChanged();
                    eventBus();
                }
            });
        }
    }

    public void eventBus(){
        GoodSpecificationEvent color = new GoodSpecificationEvent(colorName,datas);
        RxBus.getInstance().post(RxBus.TAG_UPDATE,color);
    }

}
