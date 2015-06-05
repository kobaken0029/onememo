package com.pliseproject.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy/MM/dd HH:mm:ss (E)";
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy年MM月dd日 HH時mm分";

    /**
     * 昼
     */
    public static final int NOON = 1;

    /**
     * 夕方
     */
    public static final int EVENING = 2;

    /**
     * 夜
     */
    public static final int NIGHT = 3;

    /**
     * 深夜
     */
    public static final int LATE_NIGHT = 4;


    /**
     * コンストラクタ。
     */
    private DateUtil() {
    }

    /**
     * 文字列からCalendarに変換します。
     *
     * @param data 文字列
     * @return Calendar
     */
    public static Calendar convertStringToCalendar(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_MINUTE);
        Date date = null;

        if (data == null) {
            return Calendar.getInstance();
        }

        try {
            date = new Date(sdf.parse(data).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 現在時間の時間帯を判定する。
     *
     * @return 時間帯
     */
    public static int checkTimeNow() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        // 朝・昼開始時刻をセット
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                7,
                0);
        Date startNoonDate = calendar.getTime();

        // 夕方開始時刻をセット
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                16,
                0);
        Date startEveningDate = calendar.getTime();

        // 夜開始時刻をセット
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                19,
                0);
        Date startNightDate = calendar.getTime();

        // 深夜開始時刻をセット
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23,
                59);
        Date startLateNightDate = calendar.getTime();

        int timeArea;
        if (now.after(startNoonDate) && now.before(startEveningDate)) {
            timeArea = NOON;
        } else if (now.after(startEveningDate) && now.before(startNightDate)) {
            timeArea = EVENING;
        } else if (now.after(startNightDate) && now.before(startLateNightDate)) {
            timeArea = NIGHT;
        } else {
            timeArea = LATE_NIGHT;
        }

        return timeArea;
    }
}
