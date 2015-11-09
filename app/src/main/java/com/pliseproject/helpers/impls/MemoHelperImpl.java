package com.pliseproject.helpers.impls;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pliseproject.R;
import com.pliseproject.helpers.MemoHelper;
import com.pliseproject.models.Memo;
import com.pliseproject.receivers.MyAlarmNotificationReceiver;
import com.pliseproject.utils.DateUtil;
import com.pliseproject.utils.UiUtil;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class MemoHelperImpl implements MemoHelper {
    private Memo mCurrentMemo;

    public MemoHelperImpl() {
        List<Memo> memos = findAll();
        mCurrentMemo = memos.isEmpty() ? new Memo() : memos.get(0);
    }

    /**
     * IDからメモを取得します。
     *
     * @param id メモID
     * @return メモ
     */
    public Memo find(long id) {
        return new Select().from(Memo.class)
                .where(Condition.column(Memo.ID).eq(id)).querySingle();
    }

    /**
     * すべてのメモを取得します。
     *
     * @return メモ群
     */
    public List<Memo> findAll() {
        return new Select().from(Memo.class).queryList();
    }

    /**
     * メモを作成します。
     */
    public Memo create(String subject, String mainText) {
        Memo memo = new Memo();
        memo.setSubject(subject);
        memo.setMemo(mainText);
        String createdAt = DateUtil.converString(new Date());
        memo.setCreateAt(createdAt);
        memo.setUpdateAt(createdAt);
        memo.save();

        return memo;
    }

    /**
     * メモを更新します。
     */
    public Memo update(Context mContext, Memo memo) {
        memo.setUpdateAt(DateUtil.converString(new Date()));
        memo.update();

        // PendingIntentの発行
        PendingIntent pending = getPendingIntent(mContext, memo);

        // アラームをセット
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (memo.getPostFlg() == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, Locale.JAPAN);
            Calendar postTime = DateUtil.convertStringToCalendar(sdf.format(memo.getPostTime()));

            UiUtil.showToast(mContext, String.format("%02d時%02d分に通知します。",
                    postTime.get(Calendar.HOUR_OF_DAY),
                    postTime.get(Calendar.MINUTE)));
            am.set(AlarmManager.RTC_WAKEUP, postTime.getTimeInMillis(), pending);
        } else {
            am.cancel(pending);
        }

        return memo;
    }

    /**
     * メモを削除します。
     *
     * @param memo メモ
     */
    public void delete(Context mContext, Memo memo) {
        memo.delete();
        mCurrentMemo = null;
        UiUtil.showToast(mContext, R.string.success_delete_message);
        ((AlarmManager) mContext.getSystemService(ALARM_SERVICE)).cancel(getPendingIntent(mContext, memo));
    }

    /**
     * アラーム時に起動するアプリケーションを登録します。
     *
     * @param memo メモ
     * @return 遷移先
     */
    private PendingIntent getPendingIntent(Context mContext, Memo memo) {
        Intent intent = new Intent(mContext, MyAlarmNotificationReceiver.class);
        intent.putExtra("memo", memo);
        intent.setType(String.valueOf(memo.getId()));

        return PendingIntent.getBroadcast(mContext,
                PendingIntent.FLAG_ONE_SHOT, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public Memo getCurrentMemo() {
        return mCurrentMemo != null ? mCurrentMemo : new Memo();
    }

    public void setCurrentMemo(Memo memo) {
        mCurrentMemo = memo;
    }
}
