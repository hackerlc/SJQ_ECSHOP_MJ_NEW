package com.ecjia.entity.responsemodel;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-03.
 */

public class USERMENBERCHILDTYPE implements Serializable {

    public String tagName;
    public boolean select = false;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
