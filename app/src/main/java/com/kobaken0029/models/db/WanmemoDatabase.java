package com.kobaken0029.models.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * ワンメモのデータベースクラス。
 */
@Database(name = WanmemoDatabase.NAME, version = WanmemoDatabase.VERSION)
public class WanmemoDatabase {
    /** データベース名 */
    public static final String NAME = "test_wanmemo";

    /** データベースバージョン */
    public static final int VERSION = 1;
}
