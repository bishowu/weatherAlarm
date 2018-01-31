package com.jcc.weatheralarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Cecilia on 2018/1/29.
 */

public class AlarmDAO {
    public static final String TABLE_NAME = "alarm_table";
    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_TIME_HOUR = "alarm_time_hour"; /* 24hr */
    public static final String ALARM_TIME_MINUTE = "alarm_time_minute"; /* 0 ~ 59 min */
    public static final String ALARM_THEME = "alarm_theme"; /* 0: OFF, 1: Rain, 2: Snow */
    public static final String ALARM_REPEAT = "repeat_days"; /* 0: OFF, 1: Mon., 7: Sun. */
    public static final String SNOOZE_MODE = "snooze_mode"; /* 0: OFF, 1: 5min, 2: 10min, 3: 10min, 5min */
    public static final String END_TIME = "end_time"; /* 0: auto stop */
    public static final String ALARM_ON = "alarm_on"; /* ON or OFF */

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ALARM_ID + " VARCHAR(64) PRIMARY KEY," +
                    ALARM_TIME_HOUR + " INTEGER DEFAULT 0," +
                    ALARM_TIME_MINUTE + " INTEGER DEFAULT 0," +
                    ALARM_THEME + " INTEGER DEFAULT 0," +
                    ALARM_REPEAT + " INTEGER DEFAULT 0," +
                    SNOOZE_MODE + " INTEGER DEFAULT 0," +
                    END_TIME + " INTEGER DEFAULT 0," +
                    ALARM_ON + " INTEGER DEFAULT 1" +
                    ")";


    private static SQLiteDatabase db;
    private static AlarmDAO instance = null;

    public AlarmDAO(Context context) {
        db = DBHelper.getDatabase(context);
    }

    public static AlarmDAO getInstance(Context context) {
        if (null == instance) {
            instance = new AlarmDAO(context);
        }
        return instance;
    }

    public void close() {
        db.close();
    }

    public boolean insert(AlarmItem item) {
        ContentValues cv = new ContentValues();

        cv.put(ALARM_ID, item.getAlarmId());
        cv.put(ALARM_TIME_HOUR, item.getAlarmHour());
        cv.put(ALARM_TIME_MINUTE, item.getAlarmMinute());
        cv.put(ALARM_THEME, item.getAlarmTheme());
        cv.put(ALARM_REPEAT, item.getAlarmRepeat());
        cv.put(SNOOZE_MODE, item.getSnoozeMode());
        cv.put(END_TIME, item.getEndTime());
        cv.put(ALARM_ON, item.getAlarmState());

        return db.insert(TABLE_NAME, null, cv) > 0;
    }

    public boolean update(AlarmItem item) {
        String where = ALARM_ID + "='" + item.getAlarmId() + "'";
        ContentValues cv = new ContentValues();

        cv.put(ALARM_TIME_HOUR, item.getAlarmHour());
        cv.put(ALARM_TIME_MINUTE, item.getAlarmMinute());
        cv.put(ALARM_THEME, item.getAlarmTheme());
        cv.put(ALARM_REPEAT, item.getAlarmRepeat());
        cv.put(SNOOZE_MODE, item.getSnoozeMode());
        cv.put(END_TIME, item.getEndTime());
        cv.put(ALARM_ON, item.getAlarmState());

        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    public boolean delete(String alarmId) {
        String where = ALARM_ID + "='" + alarmId + "'";

        return db.delete(TABLE_NAME, where, null) > 0;
    }

    public ArrayList<AlarmItem> getAllAlarms() {
        ArrayList<AlarmItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE 1", null);

        if (null != cursor && cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                list.add(getRecord(cursor));
            } while (cursor.moveToNext());
        }

        if (null != cursor) cursor.close();
        return list;
    }

    public AlarmItem getRecord(Cursor cursor) {
        AlarmItem result = new AlarmItem(
                cursor.getString(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getLong(6),
                cursor.getInt(7)
        );

        return result;
    }

    public static class AlarmItem {
        private String mAlarmId;
        private int mAlarmTimeHour;
        private int mAlarmTimeMinute;
        private int mAlarmTheme;
        private int mAlarmRepeat;
        private int mSnoozeMode;
        private long mEndTime;
        private boolean mAlarmOn;

        public AlarmItem(int hourOfDay, int minute) {
            mAlarmId = "ALARM_" + System.currentTimeMillis();
            mAlarmTimeHour = hourOfDay;
            mAlarmTimeMinute = minute;
            mAlarmTheme = 0;
            mAlarmRepeat = 0;
            mSnoozeMode = 0;
            mEndTime = 0;
            mAlarmOn = true;
        }

        public AlarmItem(String id, int hourOfDay, int minute, int theme, int repeat, int snooze, long endTime, int state) {
            mAlarmId = id;
            mAlarmTimeHour = hourOfDay;
            mAlarmTimeMinute = minute;
            mAlarmTheme = theme;
            mAlarmRepeat = repeat;
            mSnoozeMode = snooze;
            mEndTime = endTime;
            mAlarmOn = state > 0;
        }

        public String getAlarmId() {
            return mAlarmId;
        }

        public int getAlarmHour() {
            return mAlarmTimeHour;
        }

        public int getAlarmMinute() {
            return mAlarmTimeMinute;
        }

        public int getAlarmTheme() {
            return mAlarmTheme;
        }

        public int getAlarmRepeat() {
            return mAlarmRepeat;
        }

        public int getSnoozeMode() {
            return mSnoozeMode;
        }

        public long getEndTime() {
            return mEndTime;
        }

        public boolean getAlarmState() {
            return mAlarmOn;
        }

        public String toTimeString() {
            return String.format("%02d:%02d", mAlarmTimeHour, mAlarmTimeMinute);
        }

        public void setOnOff(boolean on) {
            mAlarmOn = on;
        }
    }

}
