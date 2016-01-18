package com.kobaken0029.views.viewmodels;

import java.util.Calendar;

/**
 * アラームセットのViewModel。
 */
public class SetAlarmViewModel {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    /**
     * カレンダーオブジェクトから年・月・日・時間・分を設定する。
     *
     * @param calendar カレンダー
     */
    public void setAlarmTime(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    /**
     * 現在、設定されている年・月・日・時間・分からカレンダーオブジェクトを生成する。
     *
     * @return 年・月・日・時間・分で設定されたカレンダー
     */
    public Calendar generatePostedCalendar() {
        // アラームの時間設定
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // 設定した時刻をカレンダーに設定
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
