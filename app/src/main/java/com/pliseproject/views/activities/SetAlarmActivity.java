package com.pliseproject.views.activities;

import com.pliseproject.R;
import com.pliseproject.managers.MyAlarmManager;
import com.pliseproject.models.Memo;
import com.pliseproject.utils.UiUtil;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * アラーム設定画面のActivityです。
 */
public class SetAlarmActivity extends BaseActivity {

    private Memo postedMemo;
    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);

    @Bind(R.id.toolbar_menu)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        ButterKnife.bind(this);

        mToolbarHelper.init(this, toolbar, R.string.setting_view, false, false, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && postedMemo != null) {
            saveSetting(postedMemo);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && postedMemo != null) {
            saveSetting(postedMemo);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 設定を保存する。
     */
    public void saveSetting(Memo memo) {
        calendar = new MyAlarmManager(this).setAlarm(year, month, day, hour, minute);

        // 過去だったらエラーメッセージを出す
        if (calendar.getTimeInMillis() < System.currentTimeMillis() && memo.getPostFlg() == 1) {
            UiUtil.showToast(this, getString(R.string.error_past_date_message));
            return;
        }

        memo.setPostTime(calendar.getTime());

//        Intent intent = new Intent(this, CreateMemoActivity.class);
//        intent.putExtra("memo", memo);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
    }

    public void setPostedMemo(Memo postedMemo) {
        this.postedMemo = postedMemo;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}