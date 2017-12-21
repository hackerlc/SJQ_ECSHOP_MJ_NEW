package com.ecjia.view.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecjia.entity.responsemodel.MYMESSAGE;
import com.ecjia.util.DBhelp;
import com.ecjia.util.LG;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2014/12/15.
 */
public class MsgSql {
    public SQLiteDatabase db = null;
    DBhelp helper = null;

    private MsgSql(Context ct) {
        helper = new DBhelp(ct);
    }

    //添加数据
    public void insertMessage(MYMESSAGE msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = df.format(new Date()).toString();
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();

        //一下插入代码一旦写完，顺序不得修改，只能在后面插入
        cv.put("msgtitle", msg.getTitle());
        cv.put("msgcontent", msg.getContent());
        cv.put("msgcustom", msg.getCustom());
        cv.put("msgtime", time);
        cv.put("msgtype", msg.getType());
        cv.put("msgurl", msg.getUrl());
        cv.put("msgActivity", msg.getGotoActivity());
        cv.put("msg_id", msg.getMsg_id());


        //2.7.0
        cv.put("open_type", msg.getOpen_type());
        cv.put("category_id", msg.getCategory_id());
        cv.put("webUrl", msg.getWebUrl());
        cv.put("goods_id_comment", msg.getGoods_id_comment());
        cv.put("goods_id", msg.getGoods_id());
        cv.put("order_id", msg.getOrder_id());


        cv.put("keyword", msg.getKeyword());
        cv.put("un_read", msg.getUn_read());

        LG.i("un_read==" + msg.getUn_read());
        db.insert("msginfo", "id", cv);
        db.close();
    }

    //查询数据
    public Cursor getAllmsg() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from msginfo order by id desc", null);
        //db.close();
        return c;
    }


    //查询未读数据
    public Cursor getUnReadMsg() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from msginfo where un_read=1", null);
        //db.close();
        return c;
    }

    //查询未读数据条数
    public int getUnReadMsgCount() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from msginfo where un_read=1", null);
        int count = c.getCount();
        c.close();
        db.close();
        return count;
    }

    public static MsgSql msg;

    public static MsgSql getIntence(Context c) {
        if (null == msg) {
            msg = new MsgSql(c);
        }
        return msg;
    }


    //查询数据
    public Cursor getMessage(String msg_id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from msginfo where msg_id=" + "\"" + msg_id + "\"", null);
        //db.close();
        return c;
    }


    //查询数据
    public void updateMessageReadStatus(String msg_id, int readStatus) {
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("un_read", readStatus);
        String[] whereValue = {"\"" + msg_id + "\""};
        db.update("msginfo", cv, "msg_id=?", new String[]{msg_id});
        LG.i("执行更新语句 msg_id=" + msg_id);
    }
}
