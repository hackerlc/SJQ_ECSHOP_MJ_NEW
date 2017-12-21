package com.ecjia.view.activity.goodsorder.balance;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.entity.responsemodel.SHIPPING;
import com.ecjia.util.common.ImageUtils;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBlanceShopData;
import com.ecjia.view.activity.goodsorder.balance.entity.NewBlanceShopGoodsData;
import com.ecjia.view.activity.goodsorder.balance.entity.ShopCouponsData;
import com.ecjia.view.activity.goodsorder.balance.entity.ShopFavourable;
import com.ecjia.view.adapter.adapter.CommonAdapter;
import com.ecjia.view.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-06.
 */

public class NewBalanceAdapter extends CommonAdapter<NewBlanceShopData> {
    private Context context;
    private LayoutInflater inflater;
    private List<NewBlanceShopData> datas = new ArrayList<>();
    private View goodview;
    private FragmentManager manager;

    public NewBalanceAdapter(Context context, List<NewBlanceShopData> datas, FragmentManager manager) {
        super(context, R.layout.shop_goods_group, datas);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.datas.clear();
        this.datas.addAll(datas);
        this.manager = manager;
    }

    public void setUpdata(List<NewBlanceShopData> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, NewBlanceShopData newBlanceShopData, int position) {
//        ToastUtil.getInstance().makeShortToast(context,">><<<"+position+">><<<");
//        NewBlanceShopData newBlanceShopData = datas.get(position - 1);

        holder.setText(R.id.tv_shopname, newBlanceShopData.getShop_name());
        List<NewBlanceShopGoodsData> goodsDatas = newBlanceShopData.getGoods_list();

        LinearLayout ly_goodsinfo = (LinearLayout) holder.getView(R.id.ly_goodsinfo);
        ly_goodsinfo.removeAllViews();
        for (int i = 0; i < goodsDatas.size(); i++) {
            NewBlanceShopGoodsData good = goodsDatas.get(i);
            goodview = LayoutInflater.from(context).inflate(R.layout.layout_newbalance_goodinfo, null);
            ImageView image = (ImageView) goodview.findViewById(R.id.img_good_img);
            TextView tv_good_name = (TextView) goodview.findViewById(R.id.tv_good_name);
            TextView tv_good_price = (TextView) goodview.findViewById(R.id.tv_good_price);
            TextView tv_good_number = (TextView) goodview.findViewById(R.id.tv_good_number);
            LinearLayout ly_goods_spec = (LinearLayout) goodview.findViewById(R.id.ly_goods_spec);
            ImageUtils.showImage(context, good.getGoods_image(), image);
            tv_good_name.setText(good.getGoods_name() + "");
            tv_good_price.setText(good.getGoods_price() + "");
            tv_good_number.setText("X" + good.getGoods_number() + "");
            ly_goods_spec.removeAllViews();
            for (NewBlanceShopGoodsData.SpecBean spec : good.getSpec()) {
                View viewSpec = LayoutInflater.from(context).inflate(R.layout.layout_newbalance_goodinfo_spec, null);
                TextView tv_good_color = (TextView) viewSpec.findViewById(R.id.tv_good_color);
                TextView tv_good_size = (TextView) viewSpec.findViewById(R.id.tv_good_size);
                TextView tv_good_specnum = (TextView) viewSpec.findViewById(R.id.tv_good_specnum);
                tv_good_color.setText(spec.getColor() + "");
                tv_good_size.setText(spec.getSize() + "");
                tv_good_specnum.setText(spec.getProduct_number() + "");
                ly_goods_spec.addView(viewSpec);
            }
            ly_goodsinfo.addView(goodview);
        }

        if (newBlanceShopData.getShop_favourable() != null && newBlanceShopData.getShop_favourable().getAct_id() != null) {
            ShopFavourable favourable = newBlanceShopData.getShop_favourable();
            holder.setText(R.id.tv_shop_activity, "省" + favourable.getSave_money() + ":" + favourable.getAct_name());
        } else {
            holder.setText(R.id.tv_shop_activity, "暂无店铺优惠");
        }

//        if (newBlanceShopData.getShop_favourable().size()>0 ) {
//            ShopFavourable favourable = newBlanceShopData.getShop_favourable().get(0);
//            holder.setText(R.id.tv_shop_activity, "省" + favourable.getSave_money() + ":" + favourable.getAct_name());
//        } else {
//            holder.setText(R.id.tv_shop_activity, "暂无店铺优惠");
//        }

        if (newBlanceShopData.getCoupons().size() <= 0) {
            holder.setText(R.id.tv_shop_coupons, "暂无可用优惠券");
        } else {
            List<ShopCouponsData> coupons = newBlanceShopData.getCoupons();
//            ShopCouponsData c= new ShopCouponsData();
//            c.setSelect(false);
//            c.setCou_money("0");
//            c.setCou_name("不使用优惠");
//            coupons.add(c);
            holder.setText(R.id.tv_shop_coupons, "点击选择优惠券");
            for (int i = 0; i < coupons.size(); i++) {
                if (coupons.get(i).isSelect()) {
                    if ("不使用优惠".equals(coupons.get(i).getCou_name())) {
                        holder.setText(R.id.tv_shop_coupons, "不使用优惠");
                    } else {
                        holder.setText(R.id.tv_shop_coupons, "省" + coupons.get(i).getCou_money() + ":" + coupons.get(i).getCou_name());
                    }
                }
            }
            holder.setOnClickListener(R.id.ly_shop_coupons, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderBottomChooseCouponDialogFragment.newInstance().showDialog(manager, coupons, position - 1);
                }
            });
        }
        if (newBlanceShopData.getShipping_list().size() > 0) {
            List<SHIPPING> shippings = newBlanceShopData.getShipping_list();
            for (int i = 0; i < shippings.size(); i++) {
                if ("1".equals(shippings.get(i).getIs_use())) {//选中快递方式
                    holder.setText(R.id.tv_shipping_type, shippings.get(i).getShipping_name() + "");
                    if ("cac".equals(shippings.get(i).getShipping_code())) {
                        holder.setText(R.id.tv_shipping_fee, shippings.get(i).getCac_name() + "");
                        holder.setText(R.id.tv_shipping_name, "自提点");
                    } else {
//                        holder.setText(R.id.tv_shipping_fee, shippings.get(i).getShipping_feeString() + "");
                        holder.setText(R.id.tv_shipping_fee, shippings.get(i).getShipping_fee() + "");
                        holder.setText(R.id.tv_shipping_name, "运费");
                    }
                }
            }
            holder.setOnClickListener(R.id.ly_shipping, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderBottomChooseShippingDialogFragment.newInstance().showDialog(manager, shippings, position - 1);
                }
            });
        }
        holder.setText(R.id.tv_good_count, "共" + newBlanceShopData.getGoods_total() + "件商品小计(含运费) ：￥");
        holder.setText(R.id.tv_shop_amount_all, newBlanceShopData.getShop_AllGoodsMoney() + newBlanceShopData.getShop_shipping_fee() + "");
    }
}
