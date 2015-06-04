package com.pliseproject.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.pliseproject.R;
import com.pliseproject.activities.SetAlarmActivity;
import com.pliseproject.fragments.bases.BaseNavigationDrawerFragment;
import com.pliseproject.models.CustomCheckData;
import com.pliseproject.models.Memo;
import com.pliseproject.views.adapters.CustomCheckAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static butterknife.ButterKnife.findById;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class SetAlarmFragment extends BaseNavigationDrawerFragment {
    private SetAlarmActivity activity;
    private CustomCheckAdapter checkAdapter;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @InjectView(R.id.setting_list)
    ListView settingList;

    private CheckBox checkBox;

    @OnItemClick(R.id.setting_list)
    void onItemClickSettingList(AdapterView<?> parent, View view, int position, long id) {
        /** 通知日ID */
        final int LIST_ID_POST_DAY = 0;

        /** 通知時間ID */
        final int LIST_ID_POST_TIME = 1;

        /** 通知有無ID */
        final int LIST_ID_POST = 2;

        switch (position) {
            case LIST_ID_POST_DAY:
                new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        CustomCheckData data = checkAdapter.getItem(0);
                        String y = String.valueOf(year);
                        String month = String.valueOf(monthOfYear + 1);
                        String day = String.valueOf(dayOfMonth);
                        data.setText(activity.getString(R.string.alarm_day_view)
                                + y + activity.getString(R.string.year)
                                + month + activity.getString(R.string.month)
                                + day + activity.getString(R.string.day));
                        settingList.setAdapter(checkAdapter);

                        SetAlarmFragment.this.year = year;
                        SetAlarmFragment.this.month = monthOfYear;
                        SetAlarmFragment.this.day = dayOfMonth;
                        activity.setYear(year);
                        activity.setMonth(monthOfYear);
                        activity.setDay(dayOfMonth);
                    }
                }, year, month, day).show();
                break;
            case LIST_ID_POST_TIME:
                new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        CustomCheckData data = checkAdapter.getItem(1);
                        String hour = String.valueOf(hourOfDay);
                        String min = String.valueOf(minute);

                        if (hourOfDay < 10) {
                            hour = "0" + hour;
                        }
                        if (minute < 10) {
                            min = "0" + min;
                        }

                        data.setText(activity.getString(R.string.alarm_time_view)
                                + hour + activity.getString(R.string.hour)
                                + min + activity.getString(R.string.minute));
                        settingList.setAdapter(checkAdapter);

                        SetAlarmFragment.this.hour = hourOfDay;
                        SetAlarmFragment.this.minute = minute;
                        activity.setHour(hourOfDay);
                        activity.setMinute(minute);
                    }
                }, hour, minute, true).show();
                break;
            case LIST_ID_POST:
                checkBox = findById(view, R.id.check_alarm);
                CustomCheckData data = checkAdapter.getItem(position);
                String isAlarm;

                checkBox.setChecked(!checkBox.isChecked());

                if (data.isCheckFlag()) {
                    isAlarm = activity.getString(R.string.is_alarm_view_yes);
                    memo.setPostFlg(1);
                } else {
                    isAlarm = activity.getString(R.string.is_alarm_view_no);
                    memo.setPostFlg(0);
                }

                data.setText(activity.getString(R.string.is_alarm_view) + isAlarm);
                settingList.setAdapter(checkAdapter);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SetAlarmActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_alarm, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        memo = (Memo) activity.getIntent().getSerializableExtra("memo");

        if (memo != null) {
            // メモの通知有無を取得
            boolean postFlg = !(memo.getPostFlg() == 0);

            // Listの作成
            List<CustomCheckData> list = new ArrayList<>();
            list.add(new CustomCheckData(activity.getString(R.string.alarm_day), true));
            list.add(new CustomCheckData(activity.getString(R.string.alarm_time), true));
            if (postFlg) {
                list.add(new CustomCheckData(
                        activity.getString(R.string.is_alarm_view)
                                + activity.getString(R.string.is_alarm_view_yes),
                        postFlg));
            } else {
                list.add(new CustomCheckData(
                        activity.getString(R.string.is_alarm_view)
                                + activity.getString(R.string.is_alarm_view_no),
                        postFlg));
            }
            checkAdapter = new CustomCheckAdapter(activity, list);
            settingList.setAdapter(checkAdapter);

            if (memo.getPostTime() != null) {
                Calendar calendar = memo.getPostTime();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                CustomCheckData date = checkAdapter.getItem(0);
                CustomCheckData time = checkAdapter.getItem(1);
                date.setText(activity.getString(R.string.alarm_day_view)
                        + year + activity.getString(R.string.year)
                        + (month + 1) + activity.getString(R.string.month)
                        + day + activity.getString(R.string.day));
                time.setText(activity.getString(R.string.alarm_time_view)
                        + hour + activity.getString(R.string.hour)
                        + minute + activity.getString(R.string.minute));
            }

            activity.setYear(year);
            activity.setMonth(month);
            activity.setDay(day);
            activity.setHour(hour);
            activity.setMinute(minute);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item, null);
    }

}
