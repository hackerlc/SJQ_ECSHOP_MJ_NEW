package com.ecjia.entity.eventmodel;

import com.ecjia.entity.responsemodel.BONUS;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-14.
 */

public class GoodRedPaperEvent implements Serializable {

    private String flag;

    private List<BONUS> listData;

    public GoodRedPaperEvent() {
    }

    public GoodRedPaperEvent(String flag,List<BONUS> listData) {
        this.flag=flag;
        this.listData=listData;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<BONUS> getListData() {
        return listData;
    }

    public void setListData(List<BONUS> listData) {
        this.listData = listData;
    }
}
