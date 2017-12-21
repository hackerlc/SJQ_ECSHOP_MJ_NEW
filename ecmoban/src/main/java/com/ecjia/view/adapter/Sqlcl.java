package com.ecjia.view.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecjia.entity.responsemodel.GOODS;
import com.ecjia.util.DBhelp;
import com.ecjia.util.LG;

import org.json.JSONException;

/*
* 保存浏览记录
*
* */
public class Sqlcl {
    public static SQLiteDatabase db = null;
    DBhelp helper = null;

    public Sqlcl(Context ct) {
        helper = new DBhelp(ct);
    }

    /**
     * 最新的业务（添加数据）
     */
    public void insertbrowse(GOODS goods) {
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("goods_id", goods.getId());
        try {
            cv.put("goods", goods.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.insert("goods_history", "id", cv);
        db.close();
    }

    //查询数据
    public Cursor getGoodBrowse() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from goods_history order by id desc", null);
        //db.close();
        return c;
    }

    //在数据库通过id中查询一条记录
    public boolean getGoodBrowsebyid(int id) {
        boolean repeat = false;
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from goods_history where goods_id=" + id, null);
        if (c.getCount() != 0) {
            repeat = true;
        }
        db.close();
        return repeat;
    }

    //删除数据
    public void delete() {
        db = helper.getReadableDatabase();
        db.execSQL("delete from goods_history");
        db.close();

    }

    //删除一条数据
    public void deletebyid(int id) {
        db = helper.getReadableDatabase();
        db.execSQL("delete from goods_history where goods_id=" + id);
        LG.i("删除一条记录");
        db.close();

    }

    public int selectmin() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from goods_history", null);
        int goodid = 0;
        LG.i("==记录总数==" + c.getCount());
        while (c.moveToNext()) {
            if (c.isFirst()) {
                goodid = c.getInt(1);
                break;
            }
        }

        return goodid;
    }

    public void droptable() {
        db = helper.getReadableDatabase();
        db.execSQL("drop database ecjialite.db");
        db.close();

    }


}
