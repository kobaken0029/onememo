package com.kobaken0029.receivers;

import com.kobaken0029.R;
import com.kobaken0029.helpers.impls.MemoHelperImpl;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.NavigationActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * 通知のレシーバークラス。
 */
public class MyAlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(Memo.ID, 0L);
        String subject = intent.getStringExtra(Memo.SUBJECT);
        String contentText = intent.getStringExtra(Memo.MEMO);

        // 通知設定をOFFにする
        Memo memo = new MemoHelperImpl().find(id);
        if (memo != null) {
            memo.setPostFlg(0);
            memo.update();
        }

        // アラームを受け取って起動するActivityを指定
        Intent intent2 = new Intent(context, NavigationActivity.class);
        intent2.putExtra(Memo.ID, id);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // notificationの設定
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.wanmemo_notification_icon)
                .setColor(Color.rgb(79, 55, 48))
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(subject)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm));

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // 古い通知を削除
        notificationManager.cancelAll();

        // 通知
        notificationManager.notify(R.string.app_name, builder.build());
    }
}
