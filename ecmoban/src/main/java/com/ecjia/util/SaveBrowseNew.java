package com.ecjia.util;

import android.content.Context;

import com.ecjia.view.adapter.Sqlcl;
import com.ecjia.entity.responsemodel.GOODS;

/*
* 保存浏览记录
*
* */
public class SaveBrowseNew {

    private static Sqlcl s;

    //获得数据
    public static void getInfo(Context ct, GOODS goods) {
        s = new Sqlcl(ct);
        if (s.getGoodBrowsebyid(goods.getId())) {
            s.deletebyid(goods.getId());
        }
        s.insertbrowse(goods);
    }

}
