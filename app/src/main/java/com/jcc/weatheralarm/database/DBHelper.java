package com.jcc.weatheralarm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Cecilia on 2018/1/29.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weather_alarm.db";
    public static SQLiteDatabase database;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (null == database || !database.isOpen()) {
            database = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AlarmDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
