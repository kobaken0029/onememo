package com.kobaken0029.views.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.interfaces.AlarmSettingHandler;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.DateUtil;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.activities.AlarmSettingActivity;
import com.kobaken0029.views.viewmodels.AlarmSettingViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * アラームをセットするFragment。
 */
public class AlarmSettingFragment extends TextToSpeechFragment implements AlarmSettingHandler {
    @Bind(R.id.alarm_setting_layout)
    RelativeLayout mAlarmLayout;
    @Bind(R.id.calendar_text)
    TextView mCalendarTextView;
    @Bind(R.id.time_text)
    TextView mTimeTextView;
    @Bind(R.id.posted_switch)
    Switch mSwitch;

    private Memo mPostedMemo;
    private AlarmSettingViewModel mAlarmViewModel;
    private String mMessage;

    @OnLongClick(R.id.memomiya)
    boolean onLongClickMemomiya() {
        mMessage = getString(R.string.voice_set_alarm_long_tap);
        return false;
    }

    @OnClick(R.id.memomiya)
    void onClickMemomiya() {
        if (mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }

        ttsSpeak(mMessage);
        mMessage = getString(R.string.voice_set_alarm);
    }

    @OnClick(R.id.calendar_text)
    void onClickCalendarText() {
        AlarmSettingActivity activity = (AlarmSettingActivity) getActivity();
        new DatePickerDialog(activity, (v, year, month, day) -> {
            mAlarmViewModel.setYear(year);
            mAlarmViewModel.setMonth(month);
            mAlarmViewModel.setDay(day);
            setCalendarText(mCalendarTextView, DateUtil.YEAR_MONTH_DAY, DateUtil.getDate(year, month, day));
        }, mAlarmViewModel.getYear(), mAlarmViewModel.getMonth(), mAlarmViewModel.getDay()).show();
    }

    @OnClick(R.id.time_text)
    void onClickTimeText() {
        AlarmSettingActivity activity = (AlarmSettingActivity) getActivity();
        new TimePickerDialog(activity, (v, hour, minute) -> {
            mAlarmViewModel.setHour(hour);
            mAlarmViewModel.setMinute(minute);
            mTimeTextView.setText(DateUtil.convertToString(DateUtil.HOUR_MINUTE, DateUtil.getDate(hour, minute)));
        }, mAlarmViewModel.getHour(), mAlarmViewModel.getMinute(), true).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindView();
        mMessage = getString(R.string.voice_set_alarm);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 時刻に応じて背景を変える
        switch (DateUtil.checkTimeNow()) {
            case DateUtil.NOON:
                mAlarmLayout.setBackgroundResource(R.drawable.school_corridor_at_noon);
                break;
            case DateUtil.EVENING:
                mAlarmLayout.setBackgroundResource(R.drawable.school_corridor_at_evening);
                break;
            case DateUtil.NIGHT:
                mAlarmLayout.setBackgroundResource(R.drawable.school_corridor_at_night);
                break;
            case DateUtil.LATE_NIGHT:
                mAlarmLayout.setBackgroundResource(R.drawable.school_corridor_at_last_night);
                break;
        }
    }

    @Override
    void bindView() {
        if (mAlarmViewModel == null) {
            mAlarmViewModel = new AlarmSettingViewModel();
        }

        Calendar calendar;
        if (mPostedMemo.getPostTime() != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(mPostedMemo.getPostTime());
        } else {
            calendar = Calendar.getInstance(Locale.JAPAN);
        }
        mAlarmViewModel.setAlarmTime(calendar);

        if (mPostedMemo != null) {
            Date postedTime = mPostedMemo.getPostTime();
            if (postedTime != null) {
                setCalendarText(mCalendarTextView, DateUtil.YEAR_MONTH_DAY, postedTime);
                mTimeTextView.setText(DateUtil.convertToString(DateUtil.HOUR_MINUTE, postedTime));
            } else {
                mCalendarTextView.setText(getString(R.string.today));
                mTimeTextView.setText(DateUtil.convertToString(DateUtil.HOUR_MINUTE, DateUtil.getCurrentDate()));
            }

            mSwitch.setChecked(mPostedMemo.getPostFlg() == 1);
            mSwitch.setOnCheckedChangeListener((bv, isChecked) -> mPostedMemo.setPostFlg(isChecked ? 1 : 0));
        }
    }

    /**
     * 通知時刻をセットする。
     *
     * @param textView 対象View
     * @param pattern パターン
     * @param postedTime 通知時刻
     */
    private void setCalendarText(TextView textView, String pattern, Date postedTime) {
        if (DateUtil.isToday(postedTime)) {
            textView.setText(getString(R.string.today));
        } else {
            textView.setText(DateUtil.convertToString(pattern, postedTime));
        }
    }

    @Override
    public void setPostedMemo(Memo target) {
        mPostedMemo = target;
    }

    @Override
    public void saveSetting() {
        if (mPostedMemo != null) {
            Calendar calendar = mAlarmViewModel.generatePostedCalendar();

            // 過去だったらエラーメッセージを出す
            if (calendar.getTimeInMillis() < System.currentTimeMillis() && mPostedMemo.getPostFlg() == 1) {
                UiUtil.showToast(getActivity(), getString(R.string.error_past_date_message));
            } else {
                mPostedMemo.setPostTime(calendar.getTime());
                mMemoHelper.update(mPostedMemo);
                mMemoHelper.setAlarm(getActivity(), mPostedMemo);

                Intent intent = new Intent();
                intent.putExtra(Memo.TAG, mPostedMemo);
                getActivity().setResult(Activity.RESULT_OK, intent);
            }
        }
    }
}
