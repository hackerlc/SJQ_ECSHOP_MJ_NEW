package com.ecjia.view.adapter;


import com.ecjia.entity.responsemodel.GOODS;
import com.ecjia.entity.responsemodel.PRODUCTNUMBYCF;
import com.ecjia.entity.responsemodel.SPECIFICATION;
import com.ecjia.entity.responsemodel.SPECIFICATION_VALUE;
import com.ecjia.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;


public class GoodDetailDraft {

    public GOODS goodDetail = new GOODS();

    public ArrayList<SPECIFICATION_VALUE> selectedSpecification = new ArrayList<SPECIFICATION_VALUE>();
    public int goodQuantity = 1;

    private static GoodDetailDraft instance;

    public static GoodDetailDraft getInstance() {
        if (instance == null) {
            instance = new GoodDetailDraft();
        }
        return instance;
    }

    public void clear() {
        goodDetail=null;
//        goodDetail = new GOODS();
        selectedSpecification.clear();
        selectedGoodSpec.clear();
        goodQuantity = 1;
        goodAllQuantity = 1;
    }

    public boolean isHasSpecId(int specId) {
        for (int i = 0; i < selectedSpecification.size(); i++) {
            SPECIFICATION_VALUE selectedSpecificationValue = selectedSpecification.get(i);
            if (selectedSpecificationValue.getId() == specId) {
                return true;
            }
        }
        return false;
    }

    public boolean isHasSpecName(String specName) {
        for (int i = 0; i < selectedSpecification.size(); i++) {
            SPECIFICATION_VALUE selectedSpecificationValue = selectedSpecification.get(i);
            if (0 == selectedSpecificationValue.specification.getName().compareTo(specName)) {
                return true;
            }
        }
        return false;
    }

    public void removeSpecId(int specId) {
        for (int i = 0; i < selectedSpecification.size(); i++) {
            SPECIFICATION_VALUE selectedSpecificationValue = selectedSpecification.get(i);
            if (selectedSpecificationValue.getId() == specId) {
                selectedSpecification.remove(i);
                return;
            }
        }
        return;
    }

    public void addSelectedSpecification(SPECIFICATION_VALUE specification_value) {
        if (0 == specification_value.specification.getAttr_type().compareTo(SPECIFICATION.MULTIPLE_SELECT)) {
            if (!isHasSpecId(specification_value.getId())) {
                selectedSpecification.add(specification_value);
            }

        } else {
            for (int i = 0; i < selectedSpecification.size(); i++) {
                SPECIFICATION_VALUE selectedSpecificationValue = selectedSpecification.get(i);
                if (0 == selectedSpecificationValue.specification.getName().compareTo(specification_value.specification.getName())) {
                    selectedSpecification.remove(i);
                }
            }
            selectedSpecification.add(specification_value);
        }
    }

    public float getTotalPrice() {

        if (null == goodDetail || null == goodDetail.getPromote_price()) {
            return 0;
        }
        float singlePrice = 0;
        try {
            singlePrice = Float.valueOf(FormatUtil.stringFormatDecimal(goodDetail.getPromote_price()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < selectedSpecification.size(); i++) {
            SPECIFICATION_VALUE specification_value = selectedSpecification.get(i);
            try {
                singlePrice += Float.valueOf(FormatUtil.stringFormatDecimal(specification_value.getPrice()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return singlePrice * goodQuantity;
    }

    public String getSelectProductsNum() {
        String num = "";
        if (selectedSpecification.size() <= 0) {
            num = goodDetail.getGoods_number();
        } else if (selectedSpecification.size() > 0 && selectedSpecification.size() < 2) {
            num = goodDetail.getSelectProductsNumByID(selectedSpecification.get(0).getId() + "", "");
        } else if (selectedSpecification.size() > 1 && selectedSpecification.size() <= 2) {
            num = goodDetail.getSelectProductsNumByID(selectedSpecification.get(0).getId() + "", selectedSpecification.get(1).getId() + "");
        } else {
            num = goodDetail.getGoods_number();
        }
        return num;
    }


    public ArrayList<PRODUCTNUMBYCF> selectedGoodSpec = new ArrayList<PRODUCTNUMBYCF>();

    public int goodAllQuantity = 1;

    public int getTotalAcount() {
        return 1;
    }

    public boolean isHasChooesSpecId(String specId) {
        for (int i = 0; i < selectedGoodSpec.size(); i++) {
            PRODUCTNUMBYCF selectedSpecificationValue = selectedGoodSpec.get(i);
            if (specId.equals(selectedSpecificationValue.getGoods_attr())) {
                return true;
            }
        }
        return false;
    }

    public void removeSpecId(String specId) {
        for (int i = 0; i < selectedGoodSpec.size(); i++) {
            PRODUCTNUMBYCF selectedSpecificationValue = selectedGoodSpec.get(i);
            if (selectedSpecificationValue.getGoods_attr().equals(specId)) {
                selectedGoodSpec.remove(i);
                return;
            }
        }
        return;
    }

    public void addSelectedSpecification(PRODUCTNUMBYCF specification_value, String selectionType) {
        if (selectionType.equals(SPECIFICATION.MULTIPLE_SELECT)) {
            if (!isHasChooesSpecId(specification_value.getGoods_attr())) {
                selectedGoodSpec.add(specification_value);
            }

        } else {
            for (int i = 0; i < selectedGoodSpec.size(); i++) {
                selectedGoodSpec.clear();
//                PRODUCTNUMBYCF selectedSpecificationValue = selectedGoodSpec.get(i);
//                if (0 == selectedSpecificationValue.specification.getName().compareTo(specification_value.specification.getName())) {
//                    selectedSpecification.remove(i);
//                }
            }
            selectedGoodSpec.add(specification_value);
        }
    }

    public String getSizeNumByColorId(String idColor, String idSize) {
        return goodDetail.getSelectProductsNumByID(idColor, idSize);
    }

    public List<PRODUCTNUMBYCF> getSizeListByColorId(String idColor) {
        return goodDetail.getSizeListByColorId(idColor);
    }

    public void setSelectData(List<PRODUCTNUMBYCF> list) {
        for (PRODUCTNUMBYCF data : list) {
            for (int i = 0; i < goodDetail.getProducts().size(); i++) {
                if (data.getProduct_id().equals(goodDetail.getProducts().get(i).getProduct_id())) {
                    goodDetail.getProducts().get(i).setSizeSelectCount(data.getSizeSelectCount());
                }
            }
        }
    }

    public String getSelectAllCount() {
        int count=0;
        StringBuffer selcetShow=new StringBuffer();
        for (int i = 0; i < goodDetail.getProducts().size(); i++) {
            count=count+goodDetail.getProducts().get(i).getSizeSelectCount();
            if(goodDetail.getProducts().get(i).getSizeSelectCount()>0){
                selcetShow.append("["+goodDetail.getProducts().get(i).getGoods_attr_str()+"]/");
            }
        }
        goodAllQuantity=count;
        if(selcetShow.length()>0){
            return selcetShow.deleteCharAt(selcetShow.length() - 1).toString();
        }else {
            return "请选择";
        }
    }
}
