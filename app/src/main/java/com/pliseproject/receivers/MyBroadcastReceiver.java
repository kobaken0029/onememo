package com.pliseproject.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * BroadcastReceiverを扱うクラスです。
 */
public class MyBroadcastReceiver {
    Context context;
    Activity activity;
    BroadcastReceiver broadcastReceiver;

    /**
     * コンストラクタ。
     * @param context コンテキスト
     * @param activity アクティビティ
     */
    public MyBroadcastReceiver(Context context, Object activity) {
        this.context = context;
        this.activity = (Activity) activity;
    }

    /**
     * BroadcastRecieverを登録します。
     */
    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.pliseproject.APP_FINISH");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
                activity.finish();
            }
        };

        // LocalBroadcastManagerを使って登録
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * BroadcastRecieverを送信します。
     */
    public void sendReceiver() {
        LocalBroadcastManager.getInstance(context).sendBroadcast(
                new Intent().setAction("com.pliseproject.APP_FINISH"));
    }
}
