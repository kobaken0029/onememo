package com.kobaken0029.helpers.impls;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kobaken0029.R;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.models.Memo;
import com.kobaken0029.receivers.MyAlarmNotificationReceiver;
import com.kobaken0029.utils.DateUtil;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.adapters.MemoListAdapter;
import com.kobaken0029.views.viewmodels.DrawerViewModel;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class MemoHelperImpl implements MemoHelper {

    public MemoHelperImpl() {
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
        String createdAt = DateUtil.converString(new Date());
        Memo memo = new Memo();
        memo.setSubject(subject);
        memo.setMemo(mainText);
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
        UiUtil.showToast(mContext, R.string.success_delete_message);
        ((AlarmManager) mContext.getSystemService(ALARM_SERVICE)).cancel(getPendingIntent(mContext, memo));
    }

    /**
     * メモ群を読み込む。
     */
    public void loadMemos(MemoListAdapter adapter, DrawerViewModel viewModel) {
        List<Memo> memos = findAll();
        adapter.setMemos(memos);
        adapter.notifyDataSetChanged();
        viewModel.modify(exists());
    }

    /**
     * メモの空判定をする。
     *
     * @param memo 対象メモ
     * @return 空だったらtrue
     */
    public boolean isEmpty(Memo memo) {
        return memo == null || memo.getId() == null;
    }

    /**
     * メモが存在するかどうか判定する。
     *
     * @return メモが存在したらtrue
     */
    public boolean exists() {
        List<Memo> memos = findAll();
        return memos != null && !memos.isEmpty();
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
}
