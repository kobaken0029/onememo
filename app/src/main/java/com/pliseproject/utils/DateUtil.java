package com.pliseproject.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy/MM/dd HH:mm:ss (E)";
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy年MM月dd日 HH時mm分";

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
}
