package com.ecjia.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.SPECIFICATION;
import com.ecjia.entity.responsemodel.SPECIFICATION_VALUE;
import com.ecjia.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

public class SpecificationAdapter extends BaseAdapter {
    private ArrayList<SPECIFICATION> datalist;
    private Context c;
    private Resources res;
    float totalprice = 0;
    float price[];

    public SpecificationAdapter(Context c, ArrayList<SPECIFICATION> list, OnSpecficationChangedListener listener) {
        this.mListener = listener;
        this.c = c;
        res = c.getResources();
        this.datalist = list;
        price = new float[datalist.size()];
        for (int j = 0; j < datalist.size(); j++) {
            for (int i = 0; i < datalist.get(j).value.size(); i++) {
                if (0 == datalist.get(j).value.get(i).specification.getAttr_type().compareTo(SPECIFICATION
                        .MULTIPLE_SELECT)) {
                    if (GoodDetailDraft.getInstance().isHasSpecId(datalist.get(j).value.get(i).getId())) {
                        try {
                            price[j] = price[j] + Float.valueOf(FormatUtil.stringFormatDecimal(datalist.get(j).value.get
                                    (i).getPrice()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (0 == datalist.get(j).value.get(i).specification.getAttr_type().compareTo(SPECIFICATION
                        .SINGLE_SELECT)) {
                    if (GoodDetailDraft.getInstance().isHasSpecId(datalist.get(j).value.get(i).getId())) {
                        try {
                            price[j] = Float.valueOf(FormatUtil.stringFormatDecimal(datalist.get(j).value.get(i).getPrice()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        gettotalprice();
        if (mListener != null) {
            mListener.SpecChanged(totalprice,GoodDetailDraft.getInstance().getSelectProductsNum());
        }
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int arg0) {

        return datalist.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LinearLayout.inflate(c, R.layout.specification_cell, null);
            holder.specNameTextView = (TextView) convertView.findViewById(R.id.specification_name);
            holder.specValueLayout = (LinearLayout) convertView.findViewById(R.id.specification_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (0 == datalist.get(position).getAttr_type().compareTo("1")) {
            holder.specNameTextView.setText(datalist.get(position).getName() + res.getString(R.string.single_choice));
        } else {
            holder.specNameTextView.setText(datalist.get(position).getName() + res.getString(R.string.double_choice));
        }
        initspecificationvalue(position, holder);
        return convertView;
    }

    private class ViewHolder {
        private TextView specNameTextView;
        private LinearLayout specValueLayout;
    }

    void initspecificationvalue(final int position, ViewHolder holder) {
        holder.specValueLayout.removeAllViews();
        final List<SPECIFICATION_VALUE> list = datalist.get(position).value;
        if (datalist.get(position).value.size() > 0) {
            for (int i = 0; i < list.size(); i = i + 3) {
                final TextView specOne_discript;
                final TextView specTwo_discript;
                final TextView specThree_discript;
                LinearLayout itemCell = (LinearLayout) LayoutInflater.from(c).inflate(R.layout.specification_value_cell, null);
                final SPECIFICATION_VALUE specification_value_one = list.get(i);
                specOne_discript = (TextView) itemCell.findViewById(R.id.specification_value_text_one_discript);
                specTwo_discript = (TextView) itemCell.findViewById(R.id.specification_value_text_two_discript);
                specThree_discript = (TextView) itemCell.findViewById(R.id.specification_value_text_three_discript);
                if (list.size() > i) {
                    SPECIFICATION_VALUE specification_value = list.get(i);
                    specOne_discript.setText(specification_value.getLabel());
                    if (GoodDetailDraft.getInstance().isHasSpecId(specification_value.getId())) {
                        specOne_discript.setTextColor(Color.WHITE);
                        specOne_discript.setBackgroundResource(R.drawable.shape_specification_text_bg_selected);
                    }
                    if (list.size() > i + 1) {
                        SPECIFICATION_VALUE specification_value_two = list.get(i + 1);
                        specTwo_discript.setText(specification_value_two.getLabel());
                        if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_two.getId())) {
                            specTwo_discript.setTextColor(Color.WHITE);
                            specTwo_discript.setBackgroundResource(R.drawable.shape_specification_text_bg_selected);
                        }
                    } else {
                        specTwo_discript.setVisibility(View.INVISIBLE);
                    }

                    if (list.size() > i + 2) {
                        specThree_discript.setVisibility(View.VISIBLE);
                        SPECIFICATION_VALUE specification_value_three = list.get(i + 2);
                        specThree_discript.setText(specification_value_three.getLabel());
                        if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_three.getId())) {
                            specThree_discript.setTextColor(Color.WHITE);
                            specThree_discript.setBackgroundResource(R.drawable.shape_specification_text_bg_selected);
                        }
                    } else {
                        specThree_discript.setVisibility(View.INVISIBLE);
                    }
                }
                specOne_discript.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 多选
                        if (0 == specification_value_one.specification.getAttr_type().compareTo(SPECIFICATION
                                .MULTIPLE_SELECT)) {
                            if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_one.getId())) {
                                GoodDetailDraft.getInstance().removeSpecId(specification_value_one.getId());
                                specOne_discript.setTextColor(Color.BLACK);
                                specOne_discript.setBackgroundResource(R.drawable
                                        .shape_specification_text_bg_unselected);
                                try {
                                    price[position] = price[position] - Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_one.getPrice()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                specOne_discript.setTextColor(Color.WHITE);
                                specOne_discript.setBackgroundResource(R.drawable.shape_specification_text_bg_selected);
                                GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_one);
                                try {
                                    price[position] = price[position] + Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_one.getPrice()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // 单选
                        if (0 == specification_value_one.specification.getAttr_type().compareTo(SPECIFICATION
                                .SINGLE_SELECT)) {
                            if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_one.getId())) {

                            } else {
                                specOne_discript.setTextColor(Color.WHITE);
                                specOne_discript.setBackgroundResource(R.drawable
                                        .shape_specification_text_bg_selected);

                                GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_one);
                                try {
                                    price[position] = Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_one.getPrice()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        gettotalprice();
                        if (mListener != null) {
                            mListener.SpecChanged(totalprice,GoodDetailDraft.getInstance().getSelectProductsNum());
                        }
                        SpecificationAdapter.this.notifyDataSetChanged();
                    }
                });
                if (i + 1 < list.size()) {
                    final SPECIFICATION_VALUE specification_value_two = list.get(i + 1);

                    specTwo_discript.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 多选
                            if (0 == specification_value_two.specification.getAttr_type().compareTo(SPECIFICATION
                                    .MULTIPLE_SELECT)) {
                                if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_two.getId())) {

                                    GoodDetailDraft.getInstance().removeSpecId(specification_value_two.getId());
                                    specTwo_discript.setTextColor(Color.BLACK);
                                    specTwo_discript.setBackgroundResource(R.drawable
                                            .shape_specification_text_bg_unselected);
                                    try {
                                        price[position] = price[position] - Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_two
                                                .getPrice()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    specTwo_discript.setTextColor(Color.WHITE);
                                    specTwo_discript.setBackgroundResource(R.drawable
                                            .shape_specification_text_bg_selected);

                                    GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_two);
                                    try {
                                        price[position] = price[position] + Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_two
                                                .getPrice()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            // 单选
                            if (0 == specification_value_two.specification.getAttr_type().compareTo(SPECIFICATION
                                    .SINGLE_SELECT)) {
                                if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_two.getId())) {

                                } else {
                                    specTwo_discript.setTextColor(Color.WHITE);
                                    specTwo_discript.setBackgroundResource(R.drawable
                                            .shape_specification_text_bg_selected);
                                    GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_two);
                                    try {
                                        price[position] = Float.valueOf(FormatUtil.stringFormatDecimal
                                                (specification_value_two.getPrice()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            gettotalprice();
                            if (mListener != null) {
                                mListener.SpecChanged(totalprice,GoodDetailDraft.getInstance().getSelectProductsNum());
                            }
                            SpecificationAdapter.this.notifyDataSetChanged();
                        }
                    });
                } else {
                    specTwo_discript.setVisibility(View.INVISIBLE);
                }

                if (i + 2 < list.size()) {
                    final SPECIFICATION_VALUE specification_value_three = list.get(i + 2);

                    specThree_discript.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // 多选
                            if (0 == specification_value_three.specification.getAttr_type().compareTo(SPECIFICATION
                                    .MULTIPLE_SELECT)) {
                                if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_three.getId())) {
                                    GoodDetailDraft.getInstance().removeSpecId(specification_value_three.getId());
                                    specThree_discript.setTextColor(Color.BLACK);
                                    specThree_discript.setBackgroundResource(R.drawable
                                            .shape_specification_text_bg_unselected);
                                    try {
                                        price[position] = price[position] - Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_three
                                                .getPrice()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    specThree_discript.setTextColor(Color.WHITE);
                                    specThree_discript.setBackgroundResource(R.drawable
                                            .shape_specification_text_bg_selected);
                                    GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_three);
                                    try {
                                        price[position] = price[position] + Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_three
                                                .getPrice()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            // 单选
                            if (0 == specification_value_three.specification.getAttr_type().compareTo(SPECIFICATION
                                    .SINGLE_SELECT)) {
                                if (GoodDetailDraft.getInstance().isHasSpecId(specification_value_three.getId())) {

                                } else {
                                    specThree_discript.setTextColor(Color.WHITE);
                                    specThree_discript.setBackgroundResource(R.drawable
                                            .shape_specification_text_bg_selected);
                                    GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_three);
                                    try {
                                        price[position] = Float.valueOf(FormatUtil.stringFormatDecimal(specification_value_three.getPrice()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            gettotalprice();
                            if (mListener != null) {
                                mListener.SpecChanged(totalprice,GoodDetailDraft.getInstance().getSelectProductsNum());
                            }
                            SpecificationAdapter.this.notifyDataSetChanged();
                        }
                    });
                } else {
                    specThree_discript.setVisibility(View.INVISIBLE);
                }
                holder.specValueLayout.addView(itemCell);
            }
        }
    }

    public void gettotalprice() {
        totalprice = 0;
        for (int i = 0; i < price.length; i++) {
            try {
                totalprice = price[i] + Float.valueOf(FormatUtil.decimalFormatDecimal(totalprice));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public OnSpecficationChangedListener mListener;

    public void setOnSpecficationChangedListener(OnSpecficationChangedListener listener) {
        this.mListener = listener;
    }

    public interface OnSpecficationChangedListener {
        void SpecChanged(float price,String num);
    }

}
