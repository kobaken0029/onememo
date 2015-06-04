package com.pliseproject.services;

import com.pliseproject.activities.ViewPostMemoActivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyAlarmService extends Service {
    private static final String TAG = MyAlarmService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "Create");
        Thread thr = new Thread(null, mTask, "MyAlarmServiceThread");
        thr.start();
        Log.v(TAG, "スレッド開始");
    }

    // アラーム用サービス
    Runnable mTask = new Runnable() {
        @Override
        public void run() {

            // アラームを受け取るActivityを指定
            Intent alarmBroadcast = new Intent(getApplicationContext(), ViewPostMemoActivity.class);

            // ここでActionをセット
            alarmBroadcast.setAction("MyAlarmAction");

            // レシーバーへ渡す
            sendBroadcast(alarmBroadcast);

            Log.v(TAG, "通知画面起動メッセージを送った");

            // 役目を終えたサービスを止める
            MyAlarmService.this.stopSelf();
            Log.v(TAG, "サービス停止");
        }
    };
}
