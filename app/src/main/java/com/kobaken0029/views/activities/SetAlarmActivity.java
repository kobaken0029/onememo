package com.kobaken0029.views.activities;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.viewmodels.SetAlarmViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * アラーム設定画面のActivityです。
 */
public class SetAlarmActivity extends BaseActivity {
    /** リクエストコード。*/
    public static final int SET_ALARM_ACTIVITY = 1;

    private SetAlarmViewModel mAlarmViewModel;
    private Memo mPostedMemo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        ButterKnife.bind(this);
        mAlarmViewModel = new SetAlarmViewModel();
        mPostedMemo = (Memo) getIntent().getSerializableExtra(Memo.TAG);

        Calendar calendar;
        if (mPostedMemo.getPostTime() != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(mPostedMemo.getPostTime());
        } else {
            calendar = Calendar.getInstance(Locale.JAPAN);
        }
        mAlarmViewModel.setAlarmTime(calendar);
    }

    @Override
    public void finish() {
        if (mPostedMemo != null) {
            saveSetting();
        }
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 設定を保存する。
     */
    public void saveSetting() {
        Calendar calendar = mAlarmViewModel.generatePostedCalendar();

        // 過去だったらエラーメッセージを出す
        if (calendar.getTimeInMillis() < System.currentTimeMillis() && mPostedMemo.getPostFlg() == 1) {
            UiUtil.showToast(getApplicationContext(), getString(R.string.error_past_date_message));
        } else {
            mPostedMemo.setPostTime(calendar.getTime());
            mMemoHelper.update(mPostedMemo);
            mMemoHelper.setAlarm(getApplicationContext(), mPostedMemo);

            Intent intent = new Intent();
            intent.putExtra(Memo.TAG, mPostedMemo);
            setResult(Activity.RESULT_OK, intent);
        }
    }

    public Memo getPostedMemo() {
        return mPostedMemo;
    }

    public void setPostedMemo(Memo mPostedMemo) {
        this.mPostedMemo = mPostedMemo;
    }

    public SetAlarmViewModel getAlarmViewModel() {
        return mAlarmViewModel;
    }

    public void setAlarmViewModel(SetAlarmViewModel mAlarmViewModel) {
        this.mAlarmViewModel = mAlarmViewModel;
    }
}
