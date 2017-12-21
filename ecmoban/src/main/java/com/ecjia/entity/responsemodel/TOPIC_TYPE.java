package com.ecjia.entity.responsemodel;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TOPIC_TYPE {
    private String type_text;
    private boolean selected;


    public TOPIC_TYPE(){

    }
    public TOPIC_TYPE(String type_text){
        this.type_text=type_text;
        selected=true;
    }

    public String getType_text() {
        return type_text;
    }

    public void setType_text(String type_text) {
        this.type_text = type_text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
