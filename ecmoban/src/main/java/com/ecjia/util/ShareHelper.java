package com.ecjia.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Gravity;

import com.ecjia.base.ECJiaApplication;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.GoodsDetailModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.ShareActivity;
import com.ecjia.entity.responsemodel.PHOTO;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.media.UMImage;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Adam on 2016-03-17.
 */
public class ShareHelper {

    public static void reject() {
        PlatformConfig.setSinaWeibo(AndroidManager.SINAAPPID, AndroidManager.SINAAPPSECRET);
        PlatformConfig.setWeixin(AndroidManager.WXAPPID, AndroidManager.WXAPPSECRET);
        PlatformConfig.setQQZone(AndroidManager.QQAPPID, AndroidManager.QQAPPSECRET);
    }

    private Context mContext;
    private GoodsDetailModel model;

    public ShareHelper(Context context, GoodsDetailModel model) {
        mContext = context;
        this.model = model;
    }

    /**
     * 判断分享的图片是否是默认图片
     *
     * @return boolean
     */
    public boolean isDefaultIamge(UMImage image) {
        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.umeng_socialize_share_pic);
        return image == new UMImage(mContext, b);
    }

    public void share() {

        String refresh_info = mContext.getResources().getString(R.string.detail_refresh_info);
        String empty_img = mContext.getResources().getString(R.string.detail_empty_img);
        String sale = mContext.getResources().getString(R.string.detail_sale);
        String good = mContext.getResources().getString(R.string.detail_good);
        String no_network = mContext.getResources().getString(R.string.detail_no_network);
        String shopname = mContext.getResources().getString(R.string.app_name);
        boolean internetconnect = CheckInternet.isConnectingToInternet(mContext);// 检查网络连接
        if (internetconnect) {
            // 检查数据是否刷新
            if ((StringUtils.isEmpty(model.goodDetail.getImg().getSmall()) &&
                    StringUtils.isEmpty(model.goodDetail.getImg().getThumb()) &&
                    StringUtils.isEmpty(model.goodDetail.getImg().getThumb()))
                    || StringUtils.isEmpty(model.goodDetail.getGoods_name())) {
                ToastView toast = new ToastView(mContext, refresh_info);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                String goodsname = model.goodDetail.getGoods_name();
                PHOTO photo = model.goodDetail.getImg();
                // 定义所需传入的的url地址
                String imageurl[] = {photo.getSmall(), photo.getThumb(), photo.getUrl()};
                UMImage umimg = null;
                String url = null;
                // 判断分享的图片是否是默认图片
                for (String x : imageurl) {
                    umimg = new UMImage(mContext, x);
                    if (!isDefaultIamge(umimg)) {
                        url = x;
                        break;
                    }
                }
                if (TextUtils.isEmpty(url)) {
                    ToastView toast = new ToastView(mContext, empty_img);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                String goodsurl = "";
                goodsurl = ((ECJiaApplication) mContext.getApplicationContext()).getConfig().getGoods_url() + model.goodDetail.getId();
                String mycontent = shopname + sale + goodsname + good + goodsurl;
                Intent it = new Intent(mContext, ShareActivity.class);

                it.putExtra(IntentKeywords.SHARE_CONTENT, mycontent);
                it.putExtra(IntentKeywords.SHARE_IMAGE_URL, url);
                it.putExtra(IntentKeywords.SHARE_GOODS_URL, model.goodDetail.getShare_link());
                it.putExtra(IntentKeywords.SHARE_GOODS_NAME, goodsname);
                mContext.startActivity(it);
                ((Activity) mContext).overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        } else {
            ToastView toast = new ToastView(mContext, no_network);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}
