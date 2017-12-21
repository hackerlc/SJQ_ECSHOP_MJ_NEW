package com.ecjia.entity.eventmodel;

import com.ecjia.entity.responsemodel.PRODUCTNUMBYCF;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-31.
 */

public class GoodSpecificationEvent implements Serializable {
    private String colorId;
    private List<PRODUCTNUMBYCF> datas;

    public GoodSpecificationEvent(String colorId, List<PRODUCTNUMBYCF> datas) {
        this.colorId = colorId;
        this.datas = datas;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public List<PRODUCTNUMBYCF> getDatas() {
        return datas;
    }

    public void setDatas(List<PRODUCTNUMBYCF> datas) {
        this.datas = datas;
    }
}
