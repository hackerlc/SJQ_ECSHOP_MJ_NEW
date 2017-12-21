
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LOCATION
{


    public String longitude;

    public String latitude ;

    public String distance;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static LOCATION fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        LOCATION localItem =new LOCATION();

        JSONArray subItemArray;

        localItem.longitude = jsonObject.optString("longitude");

        localItem.latitude = jsonObject.optString("latitude");

        localItem.distance = jsonObject.optString("distance");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("longitude", longitude);
        localItemObject.put("latitude", latitude);
        localItemObject.put("distance", distance);
        return localItemObject;
    }

}
