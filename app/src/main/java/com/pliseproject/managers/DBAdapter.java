package com.pliseproject.managers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.pliseproject.models.Memo;
import com.pliseproject.utils.DateUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBのAdapterです。
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "wanmemo.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "memos";
    public static final String COL_ID = "_id";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_MEMO = "memo";
    public static final String COL_POST_TIME = "post_time";
    public static final String COL_POST_FLG = "post_flg";
    public static final String COL_CREATE_AT = "create_at";
    public static final String COL_UPDATE_AT = "update_at";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    /**
     * コンストラクタ。
     *
     * @param context コンテキスト
     */
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    /**
     * DBを開きます。
     *
     * @return DBadapter
     */
    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * DBを閉じます。
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * すべてのメモを削除します。
     *
     * @return 成否
     */
    public boolean daleteAllMemos() {
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    /**
     * 指定IDのメモを削除します。
     *
     * @param id メモID
     * @return 成否
     */
    public boolean deleteMemo(int id) {
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }

    /**
     * すべてのメモを取得します。
     *
     * @return 検索結果カーソル
     */
    public Cursor getAllMemos() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * IDからメモを取得します。
     *
     * @param id メモID
     * @return 検索結果カーソル
     */
    public Cursor findMemo(int id) {
        final String sql = "select * from memos where " + COL_ID + " = " + id;
        return db.rawQuery(sql, null);
    }

    /**
     * メモを保存します。
     *
     * @param subject 件名
     * @param memo    メモ本文
     */
    public void saveMemo(String subject, String memo) {
        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, Locale.JAPAN);
        values.put(COL_SUBJECT, subject);
        values.put(COL_MEMO, memo);
        values.put(COL_CREATE_AT, sdf.format(dateNow));
        values.put(COL_UPDATE_AT, sdf.format(dateNow));
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    /**
     * メモを更新します。
     *
     * @param memo メモ
     */
    public void updateMemo(Memo memo) {
        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, Locale.JAPAN);
        SimpleDateFormat sdf2 = new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE, Locale.JAPAN);
        String whereClause = COL_ID + " = ?";
        String[] whereArgs = {String.valueOf(memo.getId())};
        values.put(COL_SUBJECT, memo.getSubject());
        values.put(COL_MEMO, memo.getMemo());
        values.put(COL_POST_TIME, sdf2.format(memo.getPostTime().getTime()));
        values.put(COL_POST_FLG, memo.getPostFlg());
        values.put(COL_UPDATE_AT, sdf.format(dateNow));
        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    /**
     * DBのヘルパークラスです。
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_SUBJECT + " TEXT,"
                    + COL_MEMO + " TEXT,"
                    + COL_POST_TIME + " TEXT,"
                    + COL_POST_FLG + " INTEGER,"
                    + COL_CREATE_AT + " TEXT NOT NULL,"
                    + COL_UPDATE_AT + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
