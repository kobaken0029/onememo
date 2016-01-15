package com.kobaken0029.receivers;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class MyAlarmNotificationReceiver extends BroadcastReceiver {
    boolean changeRingerModeFlg;
    boolean silentFlg;
    boolean vibrateFlg;
    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Memo memo = (Memo) intent.getSerializableExtra(Memo.TAG);

        // アラームを受け取って起動するActivityを指定
        Intent intent2 = new Intent();
        intent2.putExtra(Memo.TAG, memo);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // notificationの設定
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.wanmemo_notification_icon)
                .setTicker("メモを通知します")
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(memo.getSubject())
                .setContentText(memo.getMemo())
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm));

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();

        // マナーモードかどうかの判定
        if (ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            changeRingerModeFlg = true;
            // サイレントモード場合
            if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
                silentFlg = true;
            }
            // マナーモードの場合
            if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                vibrateFlg = true;
            }
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager
                    .setStreamVolume(AudioManager.STREAM_NOTIFICATION, 1, 1);
        }

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // 古い通知を削除
        notificationManager.cancelAll();

        // 通知
        notificationManager.notify(R.string.app_name, builder.build());

        // RingerModeを元の状態に戻す
        new Handler().postDelayed(changeRingerModeTask, 2000);
    }

    Runnable changeRingerModeTask = new Runnable() {
        @Override
        public void run() {
            restoreRingerMode();
        }
    };

    /**
     * RingerModeを元の状態に戻します。
     */
    private void restoreRingerMode() {
        if (changeRingerModeFlg) {
            if (silentFlg) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            if (vibrateFlg) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        }
    }
}
