
package com.ecjia.entity.responsemodel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class COMMENTS
{


    private String content;


    private String id;


    private String re_content;


    private String author;


    private String create;

    private String rank;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRe_content() {
        return re_content;
    }

    public void setRe_content(String re_content) {
        this.re_content = re_content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static COMMENTS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        COMMENTS   localItem = new COMMENTS();

        JSONArray subItemArray;

        localItem.content = jsonObject.optString("content");

        localItem.id = jsonObject.optString("id");

        localItem.re_content = jsonObject.optString("re_content");

        localItem.author = jsonObject.optString("author");

        localItem.create = jsonObject.optString("add_time");

        localItem.rank = jsonObject.optString("rank");

        localItem.email = jsonObject.optString("email");

        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("content", content);
        localItemObject.put("id", id);
        localItemObject.put("re_content", re_content);
        localItemObject.put("author", author);
        localItemObject.put("add_time", create);
        localItemObject.put("rank", rank);
        localItemObject.put("email", email);
        return localItemObject;
    }

}
