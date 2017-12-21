package com.ecjia.view.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecjia.entity.responsemodel.SWEEP_HISTORY;
import com.ecjia.util.DBhelp;
import com.ecjia.util.LG;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SweepSql {
    public SQLiteDatabase db = null;
    DBhelp helper = null;
    private SweepSql(Context context){
        helper = new DBhelp(context);
    }
    private static SweepSql sweepSql;

    public static SweepSql getIntence(Context c) {
        if (null == sweepSql) {
            sweepSql = new SweepSql(c);
        }
        return sweepSql;
    }

    //添加数据
    public void insertSweepHistory(SWEEP_HISTORY sweep_history) {
        deleteByContext(sweep_history.getSweep_content());
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        String time = df.format(new Date()).toString();
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();

        //一下插入代码一旦写完，顺序不得修改，只能在后面插入
        cv.put("sweep_title", sweep_history.getSweep_title());
        cv.put("sweep_content",sweep_history.getSweep_content());
        cv.put("save_date", time);

        db.insert("sweephistory", "id", cv);
        db.close();
    }

    //查询数据
    public Cursor getAllRecord() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from sweephistory order by id desc", null);
        //db.close();
        return c;
    }

    //清空数据
    public void deletedata(){
        db= helper.getReadableDatabase();
        db.execSQL("delete from sweephistory");
        db.close();

    }
    //删除一条数据
    public void deleteByContext(String content_text){
        db= helper.getReadableDatabase();
        db.execSQL("delete from sweephistory where sweep_content='"+content_text+"'");
        LG.i("删除一条记录");
        db.close();

    }
}
