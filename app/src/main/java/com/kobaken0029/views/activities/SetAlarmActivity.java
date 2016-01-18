package com.kobaken0029.views.activities;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.viewmodels.SetAlarmViewModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * アラーム設定画面のActivityです。
 */
public class SetAlarmActivity extends BaseActivity {
    public static final int SET_ALARM_ACTIVITY = 1;

    @Bind(R.id.toolbar_menu)
    Toolbar toolbar;

    private Memo postedMemo;
    private SetAlarmViewModel alarmViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        ButterKnife.bind(this);
        mToolbarHelper.init(this, toolbar, R.string.setting_view, true, false);
        postedMemo = (Memo) getIntent().getSerializableExtra(Memo.TAG);
    }

    @Override
    public void finish() {
        if (postedMemo != null) {
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
        Calendar calendar = alarmViewModel.generatePostedCalendar();

        // 過去だったらエラーメッセージを出す
        if (calendar.getTimeInMillis() < System.currentTimeMillis() && postedMemo.getPostFlg() == 1) {
            UiUtil.showToast(this, getString(R.string.error_past_date_message));
            return;
        }

        postedMemo.setPostTime(calendar.getTime());

        Intent intent = new Intent();
        intent.putExtra(Memo.TAG, postedMemo);
        setResult(Activity.RESULT_OK, intent);
    }

    public void setPostedMemo(Memo postedMemo) {
        this.postedMemo = postedMemo;
    }

    public void setAlarmViewModel(SetAlarmViewModel alarmViewModel) {
        this.alarmViewModel = alarmViewModel;
    }
}
