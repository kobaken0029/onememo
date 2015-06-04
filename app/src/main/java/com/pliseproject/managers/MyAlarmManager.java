package com.pliseproject.managers;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.pliseproject.utils.UiUtil;

public class MyAlarmManager {
    private Context mContext;
    private AlarmManager am;
    private PendingIntent mAlarmSender;
    private static final String TAG = MyAlarmManager.class.getSimpleName();

    public MyAlarmManager(Context context) {
        // 初期化
        mContext = context;
        am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Log.v(TAG, "初期化完了");
    }

    /**
     * アラーム情報をセットします。
     *
     * @param alarmYear   設定年
     * @param alarmMonth  設定月
     * @param alarmDay    設定日
     * @param alarmHour   設定時
     * @param alarmMinute 設定分
     * @return アラーム通知情報
     */
    public Calendar setAlarm(int alarmYear, int alarmMonth, int alarmDay,
                             int alarmHour, int alarmMinute) {
        // アラームの時間設定
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        // 設定した時刻をカレンダーに設定
        cal.set(Calendar.YEAR, alarmYear);
        cal.set(Calendar.MONTH, alarmMonth);
        cal.set(Calendar.DAY_OF_MONTH, alarmDay);
        cal.set(Calendar.HOUR_OF_DAY, alarmHour);
        cal.set(Calendar.MINUTE, alarmMinute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Log.v(TAG, cal.getTime() + "ms");
        Log.v(TAG, "アラームセット完了");

        return cal;
    }

    /**
     * アラームをキャンセルします。
     */
    public void stopAlarm() {
        Log.d(TAG, "stopAlarm()");
        am.cancel(mAlarmSender);
    }
}
