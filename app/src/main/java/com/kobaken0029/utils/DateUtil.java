package com.kobaken0029.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日付に関するUtil。
 */
public class DateUtil {
    /** スラッシュ区切りのパターン。 */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy/MM/dd HH:mm:ss (E)";

    /** スラッシュ区切りのパターン。 */
    public static final String YEAR_MONTH_DAY_SLASH = "yyyy/MM/dd";

    /** 漢字区切りのパターン。 */
    public static final String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy年MM月dd日 HH時mm分";

    /** 漢字区切りのパターン。 */
    public static final String YEAR_MONTH_DAY = "yyyy年MM月dd日";

    /** コロン区切りのパターン。 */
    public static final String HOUR_MINUTE = "HH:mm";

    /** 昼。 */
    public static final int NOON = 1;

    /** 夕方。 */
    public static final int EVENING = 2;

    /** 夜。*/
    public static final int NIGHT = 3;

    /** 深夜。 */
    public static final int LATE_NIGHT = 4;

    /**
     * コンストラクタ。
     */
    private DateUtil() {
    }

    /**
     * 現在日時を取得する。
     *
     * @return 現在日時
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 指定年月日の日時を取得する。
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日時
     */
    public static Date getDate(int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_SLASH, Locale.JAPAN);
        Date date;

        try {
            date = new Date(sdf.parse(String.format(Locale.JAPAN, "%4d/%2d/%2d", year, month + 1, day)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            date = getCurrentDate();
        }

        return date;
    }

    /**
     * 指定年月日の日時を取得する。
     *
     * @param hour   時間
     * @param minute 分
     * @return 日時
     */
    public static Date getDate(int hour, int minute) {
        SimpleDateFormat sdf = new SimpleDateFormat(HOUR_MINUTE, Locale.JAPAN);
        Date date;

        try {
            date = new Date(sdf.parse(String.format(Locale.JAPAN, "%2d:%2d", hour, minute)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            date = getCurrentDate();
        }

        return date;
    }

    /**
     * Dateから文字列に変換します。
     *
     * @param date Date
     * @return 文字列
     */
    public static String convertToString(String pattern, Date date) {
        return date != null ? new SimpleDateFormat(pattern, Locale.JAPAN).format(date) : "";
    }

    /**
     * 文字列からCalendarに変換します。
     *
     * @param data 文字列
     * @return Calendar
     */
    public static Calendar convertStringToCalendar(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_MINUTE, Locale.JAPAN);
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

        if (now.after(startNoonDate) && now.before(startEveningDate)) {
            return NOON;
        } else if (now.after(startEveningDate) && now.before(startNightDate)) {
            return EVENING;
        } else if (now.after(startNightDate) && now.before(startLateNightDate)) {
            return NIGHT;
        } else {
            return LATE_NIGHT;
        }
    }
}
