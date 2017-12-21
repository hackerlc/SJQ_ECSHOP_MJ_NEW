package com.ecjia.util.city;

import com.ecjia.entity.model.City;
import com.ecjia.entity.model.Region;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * 根据省份获取大区的简称
 * Created by YichenZ on 2017/4/15 11:51.
 */

public class CityUril {
    static final String regions ="[{\"region\":\"华东\",\"city\":\"上海市\"},{\"region\":\"华东\",\"city\":\"浙江省\"},{\"region\":\"华东\",\"city\":\"江苏省\"},{\"region\":\"华东\",\"city\":\"安徽省\"},{\"region\":\"华东\",\"city\":\"江西省\"},{\"region\":\"华东\",\"city\":\"山东省\"},{\"region\":\"华东\",\"city\":\"台湾省\"},{\"region\":\"东北\",\"city\":\"辽宁省\"},{\"region\":\"东北\",\"city\":\"吉林省\"},{\"region\":\"东北\",\"city\":\"黑龙江省\"},{\"region\":\"华南\",\"city\":\"广东省\"},{\"region\":\"华南\",\"city\":\"福建省\"},{\"region\":\"华南\",\"city\":\"广西壮族自治区\"},{\"region\":\"华南\",\"city\":\"海南省\"},{\"region\":\"华南\",\"city\":\"香港特别行政区\"},{\"region\":\"华南\",\"city\":\"澳门特别行政区\"},{\"region\":\"华中\",\"city\":\"河南省\"},{\"region\":\"华中\",\"city\":\"湖北省\"},{\"region\":\"华中\",\"city\":\"湖南省\"},{\"region\":\"西南\",\"city\":\"四川省\"},{\"region\":\"西南\",\"city\":\"重庆市\"},{\"region\":\"西南\",\"city\":\"云南省\"},{\"region\":\"西南\",\"city\":\"贵州省\"},{\"region\":\"西南\",\"city\":\"西藏自治区\"},{\"region\":\"西北\",\"city\":\"陕西省\"},{\"region\":\"西北\",\"city\":\"宁夏回族自治区\"},{\"region\":\"西北\",\"city\":\"青海省\"},{\"region\":\"西北\",\"city\":\"甘肃省\"},{\"region\":\"西北\",\"city\":\"新疆维吾尔自治区\"},{\"region\":\"华北\",\"city\":\"北京市\"},{\"region\":\"华北\",\"city\":\"天津市\"},{\"region\":\"华北\",\"city\":\"河北省\"},{\"region\":\"华北\",\"city\":\"内蒙古自治区\"},{\"region\":\"华北\",\"city\":\"山西省\"}]";
    /**
     *
     * @param cities 城市列表/大区列表
     * @param city 城市/省份
     * @return 大区简称 hz ...
     */
    public static String getAlias(List<City> cities, String city){
        String alias = "hz";//默认大区
        if(cities == null){
            return alias;
        }
        if(cities != null && cities.size() <= 0 ){
            return alias;
        }
        if(city == null || "".equals(city)){
            return alias;
        }
        Type type = new TypeToken<ArrayList<Region>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<Region> data = gson.fromJson(regions, type);

        String regionStr = null;
        for (Region region : data) {
            if(city.equals(region.getCity())){
                regionStr = region.getRegion();
                break;
            }
        }
        if(regionStr == null){
            return alias;
        }
        for (City city1 : cities) {
            if(regionStr.equals(city1.getName())){
                alias = city1.getAlias();
                break;
            }
        }

        return alias;
    }

    /**
     *
     * @param cities 城市列表/大区列表
     * @param city 城市/省份
     * @return 大区简称 hz ...
     */
    public static City getCity(List<City> cities, String city){
        City ci = null;
        if(cities == null){
            return ci;
        }
        if(cities != null && cities.size() <= 0 ){
            return ci;
        }
        if(city == null || "".equals(city)){
            return ci;
        }
        Type type = new TypeToken<ArrayList<Region>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<Region> data = gson.fromJson(regions, type);

        String regionStr = null;
        for (Region region : data) {
            if(city.equals(region.getCity())){
                regionStr = region.getRegion();
                break;
            }
        }
        if(regionStr == null){
            return ci;
        }
        for (City city1 : cities) {
            if(regionStr.equals(city1.getName())){
                ci = city1;
                break;
            }
        }

        return ci;
    }

}
