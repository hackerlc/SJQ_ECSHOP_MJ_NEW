package com.ecjia.view.adapter;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.entity.responsemodel.NEWGOODITEM;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.entity.responsemodel.GOODS_LIST;
import com.ecjia.widgets.dialog.EditGoodDialog;
import com.ecjia.widgets.dialog.MyDialog;

import de.greenrobot.event.EventBus;

public class ShoppingCartAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<NEWGOODITEM> lists;
    protected ImageLoaderForLV imageLoader;

    public int GOODNUM_MIN = 1;//减少商品数量
    public int GOODNUM_MAX = 2;//增加商品数量
    public int GOODNUM_EDIT = 3;//编辑商品数量
    public int GOODNUM_DEL = 4;//减少商品数量至0

    private NEWGOODITEM newgooditem;
    public int flag = 1;//购买选中状态，2为删除选中状态
    private View view;
    int size = 0;
    private Handler messageHandler;
    private Resources res;
    public EventBus eventBus;


    public ShoppingCartAdapter(Context context, ArrayList<NEWGOODITEM> lists, int flag) {
        this.context = context;
        this.lists = lists;
        this.flag = flag;
        res = context.getResources();
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoaderForLV.getInstance(context);
//        unCheck();
    }

    public void setMessageHandler(Handler handler) {
        messageHandler = handler;
    }

    public void setData(ArrayList<NEWGOODITEM> list) {
        lists = list;
    }

    public ArrayList<NEWGOODITEM> getData() {
        return lists;
    }

    //全选
    public void CheckAll() {
        int listsizes = lists.size();
        for (int i = 0; i < listsizes; i++) {
            int goods = lists.get(i).getGoodslist().size();
            if (flag == 1) {
                lists.get(i).setIsCheckedbuy(true);
                for (int j = 0; j < goods; j++) {
                    lists.get(i).getGoodslist().get(j).setIsCheckedbuy(true);
                }
            } else if (flag == 2) {
                lists.get(i).setIscheckDelete(true);
                for (int j = 0; j < goods; j++) {
                    lists.get(i).getGoodslist().get(j).setIscheckDelete(true);
                }
            } else {

            }
        }
    }

    //全不选
    public void unCheck() {
        int listsizes = lists.size();
        for (int i = 0; i < listsizes; i++) {
            int goods = lists.get(i).getGoodslist().size();
            if (flag == 1) {
                lists.get(i).setIsCheckedbuy(false);
                for (int j = 0; j < goods; j++) {
                    lists.get(i).getGoodslist().get(j).setIsCheckedbuy(false);
                }
            } else if (flag == 2) {
                lists.get(i).setIscheckDelete(false);
                for (int j = 0; j < goods; j++) {
                    lists.get(i).getGoodslist().get(j).setIscheckDelete(false);
                }
            } else {

            }
        }
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    ViewHolder holder;

    //    GoodViewHolder goodViewHolder;
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        size = lists.get(position).getGoodslist().size();
        newgooditem = lists.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.new_shoppingcart_item, null);
            holder.choose_shop_item = (LinearLayout) convertView.findViewById(R.id.choose_shop_item);
            holder.checked_shop_item = (CheckBox) convertView.findViewById(R.id.checked_shop_item);
            holder.tv_shopname = (TextView) convertView.findViewById(R.id.shop_name);
            holder.ll_shopname = (LinearLayout) convertView.findViewById(R.id.shop_name_ll);
            holder.goods_viewgroup = (LinearLayout) convertView.findViewById(R.id.goods_viewgroup);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_shopname.setText(newgooditem.getName());

        final String sellerid = newgooditem.getId();

        holder.ll_shopname.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(sellerid) && !"0".equals(sellerid)) {
                    Intent intent = new Intent(context, ShopListActivity.class);
                    intent.putExtra(IntentKeywords.MERCHANT_ID, sellerid);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

            }
        });

        //Checkbox点击
        holder.choose_shop_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int sizes = lists.get(position).getGoodslist().size();
                if (flag == 1) {//选中购买
                    if (lists.get(position).getIsCheckedbuy()) {
                        lists.get(position).setIsCheckedbuy(false);
                        for (int j = 0; j < sizes; j++) {
                            lists.get(position).getGoodslist().get(j).setIsCheckedbuy(false);
                        }
                        eventBus.post(new MyEvent("uncheckAll"));
                    } else {
                        lists.get(position).setIsCheckedbuy(true);
                        for (int j = 0; j < sizes; j++) {
                            lists.get(position).getGoodslist().get(j).setIsCheckedbuy(true);
                        }
                        if (isCheckall()) {
                            eventBus.post(new MyEvent("checkAll"));
                        }
                    }
                    eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                } else if (flag == 2) {//选中删除
                    if (lists.get(position).getIscheckDelete()) {
                        lists.get(position).setIscheckDelete(false);
                        holder.checked_shop_item.setChecked(false);
                        for (int j = 0; j < sizes; j++) {
                            lists.get(position).getGoodslist().get(j).setIscheckDelete(false);
                        }
                        eventBus.post(new MyEvent("uncheckAll"));
                    } else {
                        lists.get(position).setIscheckDelete(true);
                        holder.checked_shop_item.setChecked(true);
                        for (int j = 0; j < sizes; j++) {
                            lists.get(position).getGoodslist().get(j).setIscheckDelete(true);
                        }
                        if (isCheckalldelete()) {
                            eventBus.post(new MyEvent("checkAll"));
                        }
                    }
                } else {

                }
                ShoppingCartAdapter.this.notifyDataSetChanged();
            }
        });

        if (flag == 1) {//购买选中
            holder.checked_shop_item.setChecked(lists.get(position).getIsCheckedbuy());
        } else if (flag == 2) {//删除选中
            holder.checked_shop_item.setChecked(lists.get(position).getIscheckDelete());
        } else {//默认不选中
            holder.checked_shop_item.setChecked(false);
        }
        //设置店铺中的商品

//        if(position==1){
//        lists.get(position).getGoodslist().get(1).setIs_valid("0");
//        }

        if (size > 0) {
            holder.goods_viewgroup.removeAllViews();
            for (int i = 0; i < size; i++) {
                final int finalI = i;
                view = inflater.inflate(R.layout.shop_car_item, null);
                TextView total = (TextView) view.findViewById(R.id.shop_car_item_total);
                TextView tv_attr = (TextView) view.findViewById(R.id.tv_attr);
                TextView tv_arrive_warn = (TextView) view.findViewById(R.id.tv_arrive_warn);
                TextView tv_is_valid = (TextView) view.findViewById(R.id.tv_is_valid);
                ImageView image = (ImageView) view.findViewById(R.id.shop_car_item_image);
                TextView goodname = (TextView) view.findViewById(R.id.shop_car_item_text);
                CheckBox shop_car_item_check = (CheckBox) view.findViewById(R.id.shop_car_item_check);
                LinearLayout checkitem = (LinearLayout) view.findViewById(R.id.shop_car_check_item);
                View view_base = view.findViewById(R.id.view_base);
                View topline = view.findViewById(R.id.shop_item_topline);
                View buttomlongline = view.findViewById(R.id.buttom_long_line);
                View buttomshortline = view.findViewById(R.id.buttom_short_line);

                final TextView min = (TextView) view.findViewById(R.id.shop_car_item_min);
                final TextView max = (TextView) view.findViewById(R.id.shop_car_item_sum);
                final TextView editgoodnum = (TextView) view.findViewById(R.id.shop_car_item_editNum);

                if (size == 1) {
                    topline.setVisibility(View.VISIBLE);
                    buttomshortline.setVisibility(View.GONE);
                    buttomlongline.setVisibility(View.VISIBLE);
                } else {
                    if (i == 0) {
                        topline.setVisibility(View.VISIBLE);
                        buttomshortline.setVisibility(View.VISIBLE);
                        buttomlongline.setVisibility(View.GONE);
                    } else if (i < size - 1) {
                        topline.setVisibility(View.GONE);
                        buttomshortline.setVisibility(View.VISIBLE);
                        buttomlongline.setVisibility(View.GONE);
                    } else if (i == size - 1) {
                        topline.setVisibility(View.GONE);
                        buttomshortline.setVisibility(View.GONE);
                        buttomlongline.setVisibility(View.VISIBLE);
                    }
                }


                if (flag == 1) {
                    shop_car_item_check.setChecked(lists.get(position).getGoodslist().get(i).getIsCheckedbuy());
                    if ("2".equals(lists.get(position).getGoodslist().get(i).getIs_arrive())) {
                        tv_arrive_warn.setVisibility(View.VISIBLE);
                        tv_arrive_warn.setText(lists.get(position).getGoodslist().get(i).getArrive_warn());
                    } else {
                        tv_arrive_warn.setVisibility(View.GONE);
                    }
                } else if (flag == 2) {
                    shop_car_item_check.setChecked(lists.get(position).getGoodslist().get(i).getIscheckDelete());
                    tv_arrive_warn.setVisibility(View.GONE);
                } else {
                    tv_arrive_warn.setVisibility(View.GONE);
                }

                if ("1".equals(lists.get(position).getGoodslist().get(i).getIs_valid())) {////规格是否有效，1：有效正常显示 0：失效，灰色显示
                    view_base.setVisibility(View.GONE);
                    tv_is_valid.setVisibility(View.GONE);
                    shop_car_item_check.setVisibility(View.VISIBLE);
                } else {
                    if (flag == 2) {
                        view_base.setVisibility(View.GONE);
                        tv_is_valid.setVisibility(View.GONE);
                        shop_car_item_check.setVisibility(View.VISIBLE);
                    }else{
                        view_base.setVisibility(View.VISIBLE);
                        shop_car_item_check.setVisibility(View.GONE);
                        tv_is_valid.setVisibility(View.VISIBLE);
                        view_base.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                }

                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GoodsDetailActivity.class);
                        intent.putExtra("goods_id", lists.get(position).getGoodslist().get(finalI).getGoods_id() + "");
                        context.startActivity(intent);
                    }
                });

                checkitem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int sizes = lists.get(position).getGoodslist().size();
                        if (flag == 1) { //选中购买
                            if (lists.get(position).getGoodslist().get(finalI).getIsCheckedbuy()) { //选中状态
                                lists.get(position).getGoodslist().get(finalI).setIsCheckedbuy(false);
                                lists.get(position).setIsCheckedbuy(false);
                                eventBus.post(new MyEvent("uncheckAll"));
                            } else { //未选中状态
                                lists.get(position).getGoodslist().get(finalI).setIsCheckedbuy(true);
                                if (sizes == 1) {
                                    lists.get(position).setIsCheckedbuy(true);
                                }
                                //检测商店中是否有选中的商品
                                for (int j = 0; j < sizes; j++) {
                                    if (!lists.get(position).getGoodslist().get(j).getIsCheckedbuy()) {
                                        break;
                                    } else {
                                        if (j == sizes - 1) {
                                            lists.get(position).setIsCheckedbuy(true);
                                        }
                                    }
                                }
                                if (isCheckall()) {
                                    eventBus.post(new MyEvent("checkAll"));
                                }
                            }
                            eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                        } else if (flag == 2) { //选中删除
                            if (lists.get(position).getGoodslist().get(finalI).getIscheckDelete()) {
                                lists.get(position).getGoodslist().get(finalI).setIscheckDelete(false);
                                lists.get(position).setIscheckDelete(false);
                                eventBus.post(new MyEvent("uncheckAll"));
                            } else {
                                lists.get(position).getGoodslist().get(finalI).setIscheckDelete(true);
                                if (sizes == 1) {
                                    lists.get(position).setIscheckDelete(true);
                                }
                                for (int j = 0; j < sizes; j++) {
                                    if (!lists.get(position).getGoodslist().get(j).getIscheckDelete()) {
                                        break;
                                    } else {
                                        if (j == sizes - 1) {
                                            lists.get(position).setIscheckDelete(true);
                                        }
                                    }
                                }
                                if (isCheckalldelete()) {
                                    eventBus.post(new MyEvent("checkAll"));
                                }
                            }
                        } else {

                        }
                        ShoppingCartAdapter.this.notifyDataSetChanged();
                    }
                });

                min.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GOODS_LIST good = lists.get(position).getGoodslist().get(finalI);
                        if (flag == 1) {
                            if (good.getGoods_number() > 1) {
                                good.setGoods_number(good.getGoods_number() - 1);
                                Message msg = new Message();
                                msg.what = GOODNUM_MIN;
                                msg.arg1 = position;
                                msg.arg2 = finalI;
                                messageHandler.sendMessage(msg);
                                eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                            } else if (good.getGoods_number() == 1) {
                                String delete = context.getResources().getString(R.string.collect_delete);
                                String shopcar_deletes = context.getResources().getString(R.string.shopcar_deletes);

                                final MyDialog dialog = new MyDialog(context, delete, shopcar_deletes);
                                dialog.show();
                                dialog.positive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Message msg = new Message();
                                        msg.what = GOODNUM_DEL;
                                        msg.arg1 = position;
                                        msg.arg2 = finalI;
                                        messageHandler.sendMessage(msg);
                                        dialog.dismiss();
                                    }


                                });
                                dialog.negative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }

                        } else if (flag == 2) {
                            if (good.getGoods_number() > 1) {
                                good.setGoods_number(good.getGoods_number() - 1);
                                Message msg = new Message();
                                msg.what = GOODNUM_MIN;
                                msg.arg1 = position;
                                msg.arg2 = finalI;
                                messageHandler.sendMessage(msg);
                                eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                            } else if (good.getGoods_number() == 1) {
                                String delete = context.getResources().getString(R.string.collect_delete);
                                String shopcar_deletes = context.getResources().getString(R.string.shopcar_deletes);

                                final MyDialog dialog = new MyDialog(context, delete, shopcar_deletes);
                                dialog.show();
                                dialog.positive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Message msg = new Message();
                                        msg.what = GOODNUM_DEL;
                                        msg.arg1 = position;
                                        msg.arg2 = finalI;
                                        messageHandler.sendMessage(msg);
                                        dialog.dismiss();
                                    }


                                });
                                dialog.negative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }

                        } else {

                        }
                        ShoppingCartAdapter.this.notifyDataSetChanged();
                    }
                });

                max.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int num = lists.get(position).getGoodslist().get(finalI).getGoods_number();
                        if (flag == 1) {
                            Message msg = new Message();
                            msg.what = GOODNUM_MAX;
                            msg.arg1 = position;
                            msg.arg2 = finalI;
                            lists.get(position).getGoodslist().get(finalI).setGoods_number(num + 1);
                            messageHandler.sendMessage(msg);
                            eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                        } else if (flag == 2) {
                            Message msg = new Message();
                            msg.what = GOODNUM_MAX;
                            msg.arg1 = position;
                            msg.arg2 = finalI;
                            lists.get(position).getGoodslist().get(finalI).setGoods_number(num + 1);
                            messageHandler.sendMessage(msg);
                            eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                        } else {

                        }
                        ShoppingCartAdapter.this.notifyDataSetChanged();
                    }
                });

                editgoodnum.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int num = lists.get(position).getGoodslist().get(finalI).getGoods_number();
                        final String goods_num_cannot_empty = res.getString(R.string.shopcart_goods_num_cannot_empty);
                        final EditGoodDialog dialog = new EditGoodDialog(context,
                                num + "");
                        dialog.show();
                        dialog.negative.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CloseKeyBoard(context, dialog.edit);
                                dialog.dismiss();
                            }
                        });
                        dialog.positive.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(dialog.edit.getText().toString()) || Integer.valueOf(dialog.edit.getText().toString()) == 0) {

                                    String delete = context.getResources().getString(R.string.collect_delete);
                                    String shopcar_deletes = context.getResources().getString(R.string.shopcar_deletes);

                                    final MyDialog myDialog = new MyDialog(context, delete, shopcar_deletes);
                                    myDialog.show();
                                    myDialog.positive.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Message msg = new Message();
                                            msg.what = GOODNUM_DEL;
                                            msg.arg1 = position;
                                            msg.arg2 = finalI;
                                            messageHandler.sendMessage(msg);
                                            myDialog.dismiss();
                                        }


                                    });
                                    myDialog.negative.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();
                                        }
                                    });
                                    dialog.dismiss();
                                } else {
                                    if (dialog.edit
                                            .getText()
                                            .toString()
                                            .equals(editgoodnum.getText()
                                                    .toString())) {
                                    } else {
                                        GOODS_LIST good = lists.get(position).getGoodslist().get(finalI);
                                        good.setGoods_number(Integer.valueOf(dialog.edit.getText().toString()));
                                        Message msg = new Message();
                                        msg.what = GOODNUM_EDIT;
                                        msg.arg1 = position;
                                        msg.arg2 = finalI;
                                        editgoodnum.setText(dialog.edit.getText());
                                        messageHandler.sendMessage(msg);
                                        eventBus.post(new MyEvent("RefreshGoodsTotalFee"));
                                        CloseKeyBoard(context, dialog.edit);
                                        dialog.dismiss();
                                    }
                                    CloseKeyBoard(context, dialog.edit);
                                    dialog.dismiss();
                                }
                                ShoppingCartAdapter.this.notifyDataSetChanged();
                            }
                        });

                    }
                });

                GOODS_LIST goods = newgooditem.getGoodslist().get(i);
                imageLoader.setImageRes(image, newgooditem.getGoodslist().get(i).getImg().getThumb());

                if (FormatUtil.formatStrToFloat(goods.getGoods_price()) == 0) {
                    total.setText("免费");
                } else {
                    total.setText(goods.getFormated_goods_price());
                }
                goodname.setText(goods.getGoods_name());
                editgoodnum.setText(goods.getGoods_number() + "");
                tv_attr.setText("[" + goods.getAttr() + "]");
                holder.goods_viewgroup.addView(view);
            }
        }


        return convertView;
    }

    private class ViewHolder {
        private LinearLayout choose_shop_item;
        private CheckBox checked_shop_item;
        private TextView tv_shopname;
        private LinearLayout goods_viewgroup;
        private LinearLayout ll_shopname;
    }


    public void CloseKeyBoard(Context c, EditText et) {
        et.clearFocus();
        InputMethodManager imm = (InputMethodManager) c
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    //商品全部选中
    private boolean isCheckall() {
        if (lists.size() > 0) {
            int size = lists.size();
            for (int i = 0; i < size; i++) {
                if (!lists.get(i).getIsCheckedbuy()) {
                    return false;
                } else {
                    if (i == size - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //商品全部选中
    private boolean isCheckalldelete() {
        if (lists.size() > 0) {
            int size = lists.size();
            for (int i = 0; i < size; i++) {
                if (!lists.get(i).getIscheckDelete()) {
                    return false;
                } else {
                    if (i == size - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
