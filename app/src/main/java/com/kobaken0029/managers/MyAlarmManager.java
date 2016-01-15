package com.kobaken0029.managers;

import java.util.Calendar;

import android.util.Log;

import com.kobaken0029.views.viewmodels.SetAlarmViewModel;

public class MyAlarmManager {
    private static final String TAG = MyAlarmManager.class.getSimpleName();

    private Calendar calendar;

    public MyAlarmManager(SetAlarmViewModel alarmViewModel) {
        setAlarm(alarmViewModel);
        Log.v(TAG, "初期化完了");
    }

    /**
     * アラーム情報をセットします。
     *
     * @param alarmViewModel 設定時刻情報
     */
    public void setAlarm(SetAlarmViewModel alarmViewModel) {
        // アラームの時間設定
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // 設定した時刻をカレンダーに設定
        calendar.set(Calendar.YEAR, alarmViewModel.getYear());
        calendar.set(Calendar.MONTH, alarmViewModel.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, alarmViewModel.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, alarmViewModel.getHour());
        calendar.set(Calendar.MINUTE, alarmViewModel.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Log.v(TAG, calendar.getTime() + "ms");
        Log.v(TAG, "アラームセット完了");
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
