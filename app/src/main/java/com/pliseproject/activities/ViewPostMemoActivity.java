package com.pliseproject.activities;

import com.pliseproject.managers.AppController;
import com.pliseproject.models.Memo;

import android.os.Bundle;
import android.view.WindowManager;

public class ViewPostMemoActivity extends ViewMemoActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // memo取得
        Memo memo = (Memo) getIntent().getSerializableExtra("memo");

        // 通知OFFにする
        memo.setPostFlg(0);

        // メモを更新
        AppController.dbAdapter.open();
        AppController.dbAdapter.updateMemo(memo);
        AppController.dbAdapter.close();

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
