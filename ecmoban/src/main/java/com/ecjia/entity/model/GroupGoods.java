package com.ecjia.entity.model;

import com.ecjia.widgets.IndicatorProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/28 15:12.
 */

public class GroupGoods extends Goods {


    /**
     *  "objectId":."1", //活动id
     *   recType":."GROUPBUY_GOODS",
     * id : 478
     * img1 : images/201512/goods_img/478_G_1451090335004.jpg
     * img2 : images/201512/thumb_img/478_thumb_G_1451090335895.jpg
     * img3 : images/201512/thumb_img/478_thumb_G_1451090335895.jpg
     * group_rule : [{"price":1100,"num":30,"price_str":"￥1100","num_str":"30件成团"},{"price":1200,"num":20,"price_str":"￥1200","num_str":"20件"},{"price":1300,"num":10,"price_str":"￥1300","num_str":"10件"}]
     */

    private String img1;
    private String img2;
    private String img3;
    private int buy_num;
    private long left_second;
    private List<GroupRuleBean> group_rule;
    private String objectId;
    private String recType;

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public List<GroupRuleBean> getGroup_rule() {
        return group_rule;
    }

    public void setGroup_rule(List<GroupRuleBean> group_rule) {
        this.group_rule = group_rule;
    }

    public int getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(int buy_num) {
        this.buy_num = buy_num;
    }

    public long getLeft_second() {
        return left_second;
    }

    public void setLeft_second(long left_second) {
        this.left_second = left_second;
    }

    public void lessLeftSecond() {
        this.left_second--;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRecType() {
        return recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    public static class GroupRuleBean {
        /**
         * price : 1100
         * num : 30
         * price_str : ￥1100
         * num_str : 30件成团
         */

        private float price;
        private int num;
        private String price_str;
        private String num_str;

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getPrice_str() {
            return price_str;
        }

        public void setPrice_str(String price_str) {
            this.price_str = price_str;
        }

        public String getNum_str() {
            return num_str;
        }

        public void setNum_str(String num_str) {
            this.num_str = num_str;
        }
    }

    public List<IndicatorProgressBar.Section> getSections(){
        List<IndicatorProgressBar.Section> sections = new ArrayList<>();
        for(GroupRuleBean groupRuleBean : group_rule){
            IndicatorProgressBar.Section section = new IndicatorProgressBar.Section();
            section.setNum(groupRuleBean.getNum());
            section.setNumStr(groupRuleBean.getNum_str());
            section.setSectionStr(groupRuleBean.getPrice_str());
            sections.add(section);
        }

        return sections;
    }

    @Override
    public String toString() {
        return "GroupGoods{" +
                "img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                ", buy_num=" + buy_num +
                ", left_second=" + left_second +
                ", group_rule=" + group_rule +
                '}';
    }
}
