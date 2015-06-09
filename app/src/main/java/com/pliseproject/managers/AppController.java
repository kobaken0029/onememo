package com.pliseproject.managers;

import com.pliseproject.R;
import com.pliseproject.activities.SetAlarmActivity;
import com.pliseproject.models.Memo;
import com.pliseproject.receivers.MyAlarmNotificationReceiver;
import com.pliseproject.utils.DateUtil;
import com.pliseproject.utils.UiUtil;
import com.pliseproject.utils.packageUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController extends Application {
    private static Context mContext;
    public static DBAdapter dbAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        dbAdapter = new DBAdapter(mContext);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (dbAdapter != null) {
            dbAdapter.close();
            dbAdapter = null;
        }
    }

    /**
     * 画面遷移前確認ダイアログを表示する。
     */
    public void showDialogBeforeMoveMemoView(final Activity activity, final Intent intent) {
        UiUtil.showDialog(activity, getString(R.string.confirmation_of_not_saved_message),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (activity instanceof SetAlarmActivity) {
                            activity.setResult(Activity.RESULT_CANCELED, intent);
                        } else {
                            startActivity(intent);
                        }

                        activity.finish();

                    }
                });
    }

    /**
     * IDからメモを捜索する。
     *
     * @return memo
     */
    public Memo findMemo(int memoId) {
        Memo memo = new Memo();
        memo.setSubject("あなたへ");
        memo.setMemo("メモ書いて！");

        if (memoId != -1) {
            AppController.dbAdapter.open();
            Cursor c = AppController.dbAdapter.findMemo(memoId);
            c.moveToFirst();
            memo = new Memo(c.getInt(c.getColumnIndex(DBAdapter.COL_ID)),
                    c.getString(c.getColumnIndex(DBAdapter.COL_SUBJECT)),
                    c.getString(c.getColumnIndex(DBAdapter.COL_MEMO)),
                    DateUtil.convertStringToCalendar(c.getString(c.getColumnIndex(DBAdapter.COL_POST_TIME))),
                    c.getInt(c.getColumnIndex(DBAdapter.COL_POST_FLG)),
                    c.getString(c.getColumnIndex(DBAdapter.COL_CREATE_AT)),
                    c.getString(c.getColumnIndex(DBAdapter.COL_UPDATE_AT)));
            AppController.dbAdapter.close();
        }

        return memo;
    }


    /**
     * メモを保存します。
     */
    public void saveMemo(View... views) {
        AppController.dbAdapter.open();
        AppController.dbAdapter.saveMemo(
                ((TextView) views[0]).getText().toString(),
                ((TextView) views[1]).getText().toString());
        AppController.dbAdapter.close();
        ((TextView) views[0]).setText("");
        ((TextView) views[1]).setText("");
    }

    /**
     * メモを更新します。
     */
    public void updateMemo(Memo memo, View... views) {
        if (views.length == 2) {
            // メモに値をセット
            memo.setSubject(((TextView) views[0]).getText().toString());
            memo.setMemo(((TextView) views[1]).getText().toString());
            ((TextView) views[0]).setText("");
            ((TextView) views[1]).setText("");
        }

        // メモを更新
        AppController.dbAdapter.open();
        AppController.dbAdapter.updateMemo(memo);
        AppController.dbAdapter.close();

        // PendingIntentの発行
        PendingIntent pending = getPendingIntent(memo);

        // アラームをセット
        AlarmManager am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        if (memo.getPostFlg() == 1) {
            UiUtil.showToast(mContext, String.format("%02d時%02d分に通知します。",
                    memo.getPostTime().get(Calendar.HOUR_OF_DAY),
                    memo.getPostTime().get(Calendar.MINUTE)));
            am.set(AlarmManager.RTC_WAKEUP, memo.getPostTime().getTimeInMillis(), pending);
        } else {
            am.cancel(pending);
        }
    }

    /**
     * メモを削除します。
     *
     * @param memo メモ
     */
    public void deleteMemo(Memo memo) {
        AppController.dbAdapter.open();
        if (AppController.dbAdapter.deleteMemo(memo.getId())) {
            UiUtil.showToast(mContext, R.string.success_delete_message);
        }
        AppController.dbAdapter.close();
        ((AlarmManager) getSystemService(ALARM_SERVICE)).cancel(getPendingIntent(memo));
    }

    /**
     * アラーム時に起動するアプリケーションを登録します。
     *
     * @param memo メモ
     * @return 遷移先
     */
    private PendingIntent getPendingIntent(Memo memo) {
        Intent intent = new Intent(mContext, MyAlarmNotificationReceiver.class);
        intent.putExtra("memo", memo);
        intent.setType(String.valueOf(memo.getId()));

        return PendingIntent.getBroadcast(getApplicationContext(),
                PendingIntent.FLAG_ONE_SHOT, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 音声読上げ設定画面へ遷移します。
     */
    public void settingReadVoice(final Context context) {
        if (packageUtil.packageCheck(packageUtil.N2TTS_PACKAGE_NAME, context.getPackageManager())) {
            Intent n2tts = new Intent(Intent.ACTION_MAIN);
            n2tts.setAction("android.intent.category.LAUNCHER");
            n2tts.setClassName(packageUtil.N2TTS_PACKAGE_NAME, packageUtil.N2TTS_PACKAGE_NAME + ".TtsServiceSettings");
            n2tts.setFlags(0x10000000);
            context.startActivity(n2tts);
        } else {
            new AlertDialog.Builder(context)
                    .setMessage(packageUtil.N2TTS_PACKAGE_NOT_FOUND_MESSAGE)
                    .setPositiveButton(getResources().getString(R.string.go_play_stroe),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=jp.kddilabs.n2tts&hl=ja");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.no), null).show();
        }
    }

    /**
     * xmlからMapのListへパースする。
     *
     * @param arrayId arrayId
     */
    public List<Map<String, Object>> parse(int arrayId) {
        TypedArray drawerMenuList = getResources().obtainTypedArray(arrayId);

        // RecyclerView.Adapter に渡すデータ
        List<Map<String, Object>> mDrawerMenuArr = new ArrayList<>();

        for (int i = 0; i < drawerMenuList.length(); i++) {
            TypedArray itemArr = getResources().obtainTypedArray(drawerMenuList.getResourceId(i, 0));
            Map<String, Object> content = new HashMap<>();
            mDrawerMenuArr.add(content);
            for (int j = 0; j < itemArr.length(); j++) {
                TypedArray contentArr = getResources().obtainTypedArray(itemArr.getResourceId(j, 0));

                // key-value
                if (contentArr.getString(0).contains("icon")) {
                    content.put(contentArr.getString(0), contentArr.getDrawable(1));
                } else {
                    content.put(contentArr.getString(0), contentArr.getString(1));
                }
                contentArr.recycle();
            }
            itemArr.recycle();
        }

        return mDrawerMenuArr;
    }
}
