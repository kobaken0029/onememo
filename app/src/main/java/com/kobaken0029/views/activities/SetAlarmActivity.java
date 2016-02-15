package com.kobaken0029.views.activities;

import com.kobaken0029.R;
import com.kobaken0029.interfaces.SetAlarmHandler;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.fragments.SetAlarmFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * アラーム設定画面のActivityです。
 */
public class SetAlarmActivity extends BaseActivity {
    /** リクエストコード。*/
    public static final int SET_ALARM_ACTIVITY = 1;

    private SetAlarmHandler mHandler;

    public static Intent createIntent(Context context, @NonNull Memo postedMemo) {
        Intent intent = new Intent(context, SetAlarmActivity.class);
        intent.putExtra(Memo.TAG, postedMemo);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        Memo postedMemo = (Memo) getIntent().getSerializableExtra(Memo.TAG);
        mHandler = (SetAlarmFragment) getFragmentManager().findFragmentById(R.id.set_alarm_fragment);
        mHandler.setPostedMemo(postedMemo);
    }

    @Override
    public void finish() {
        if (mHandler == null) {
            mHandler = (SetAlarmFragment) getFragmentManager().findFragmentById(R.id.set_alarm_fragment);
        }
        mHandler.saveSetting();
        super.finish();
    }
}
