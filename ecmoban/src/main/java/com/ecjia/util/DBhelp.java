package com.ecjia.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelp extends SQLiteOpenHelper {
    private static final String DBNAME = "LastBrowse.db";


    /**
     * VERSION 4
     *
     * VERSION 5 msginfo 表 增加了 keyword 和 un_read
     *
     * VERSION 6 -----
     */

    /**
     * 每次更新数据表的时候，version数字必须增加
     */
    public static final int version = 5; //1.3之前版本都是1

    public DBhelp(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String newGoodsSql = "CREATE TABLE IF NOT EXISTS goods_history(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,goods_id INTEGER NOT NULL,goods text not null )";
        db.execSQL(newGoodsSql);

        String msgsql = "CREATE TABLE IF NOT EXISTS msginfo(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "msgtitle VARCHAR(20) NOT NULL," +
                "msgcontent VARCHAR(80) NOT NULL," +
                "msgcustom VARCHAR(80) NOT NULL," +
                "msgtime  VARCHAR(20) NOT NULL," +
                "msgtype VARCHAR(20) NOT NULL," +
                "msgurl VARCHAR(80)," +
                "msgActivity VARCHAR(20)," +
                "msg_id VARCHAR(32) NOT NULL," +
                "open_type VARCHAR(20)," +
                "category_id VARCHAR(20)," +
                "webUrl VARCHAR(50)," +
                "goods_id_comment VARCHAR(20)," +
                "goods_id VARCHAR(20)," +
                "order_id VARCHAR(20)," +
                "keyword VARCHAR(20)," +
                "un_read INTEGER)";
        db.execSQL(msgsql);

        String sweepsql = "CREATE TABLE sweephistory(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,sweep_title VARCHAR(20) NOT NULL,sweep_content VARCHAR(50) NOT NULL,save_date VARCHER(20) NOT NULL)";
        db.execSQL(sweepsql);

    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 3) {
            String newGoodsSql = "CREATE TABLE IF NOT EXISTS goods_history(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,goods_id INTEGER NOT NULL,goods text not null )";
            db.execSQL(newGoodsSql);
        }

        if (oldVersion <= 5) {
            db.execSQL("ALTER TABLE msginfo ADD keyword VARCHAR(20)");//增减项 保存用户数据
            db.execSQL("ALTER TABLE msginfo ADD un_read INTEGER");
        }

    }

}
