package com.kobaken0029.views.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.kobaken0029.R;
import com.kobaken0029.models.CustomCheckData;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.SetAlarmActivity;
import com.kobaken0029.views.adapters.CustomCheckAdapter;
import com.kobaken0029.views.viewmodels.SetAlarmViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static butterknife.ButterKnife.findById;

/**
 * アラームをセットするFragment。
 */
public class SetAlarmFragment extends TextToSpeechFragment {
    @Bind(R.id.setting_list)
    ListView settingList;

    private SetAlarmActivity activity;
    private CustomCheckAdapter checkAdapter;
    private SetAlarmViewModel alarmViewModel;
    private Memo memo;

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
                new DatePickerDialog(activity, (v, year, month, day) -> {
                    CustomCheckData data = checkAdapter.getItem(0);
                    String y = String.valueOf(year);
                    String m = String.valueOf(month + 1);
                    String d = String.valueOf(day);
                    data.setText(activity.getString(R.string.alarm_day)
                            + y + activity.getString(R.string.year)
                            + m + activity.getString(R.string.month)
                            + d + activity.getString(R.string.day));
                    settingList.setAdapter(checkAdapter);

                    alarmViewModel.setYear(year);
                    alarmViewModel.setMonth(month);
                    alarmViewModel.setDay(day);
                }, alarmViewModel.getYear(), alarmViewModel.getMonth(), alarmViewModel.getDay()).show();
                break;
            case LIST_ID_POST_TIME:
                new TimePickerDialog(activity, (v, hour, minute) -> {
                    CustomCheckData data = checkAdapter.getItem(1);
                    String h = String.valueOf(hour);
                    String m = String.valueOf(minute);

                    if (hour < 10) {
                        h = "0" + h;
                    }
                    if (minute < 10) {
                        m = "0" + m;
                    }

                    data.setText(activity.getString(R.string.alarm_time)
                            + h + activity.getString(R.string.hour)
                            + m + activity.getString(R.string.minute));
                    settingList.setAdapter(checkAdapter);

                    alarmViewModel.setHour(hour);
                    alarmViewModel.setMinute(minute);
                }, alarmViewModel.getHour(), alarmViewModel.getMinute(), true).show();
                break;
            case LIST_ID_POST:
                CheckBox checkBox = findById(view, R.id.check_alarm);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_alarm, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (SetAlarmActivity) getActivity();
        memo = (Memo) activity.getIntent().getSerializableExtra(Memo.TAG);

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
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(memo.getPostTime());
                alarmViewModel.setAlarmTime(calendar);

                CustomCheckData date = checkAdapter.getItem(0);
                CustomCheckData time = checkAdapter.getItem(1);
                date.setText(activity.getString(R.string.alarm_day)
                        + alarmViewModel.getYear() + activity.getString(R.string.year)
                        + (alarmViewModel.getMonth() + 1) + activity.getString(R.string.month)
                        + alarmViewModel.getDay() + activity.getString(R.string.day));
                time.setText(activity.getString(R.string.alarm_time)
                        + alarmViewModel.getHour() + activity.getString(R.string.hour)
                        + alarmViewModel.getMinute() + activity.getString(R.string.minute));
            }

            activity.setPostedMemo(memo);
            activity.setAlarmViewModel(alarmViewModel);
        }
    }

    @Override
    void bindView() {
        if (alarmViewModel == null) {
            alarmViewModel = new SetAlarmViewModel();
            alarmViewModel.setAlarmTime(Calendar.getInstance(Locale.JAPAN));
        }
    }
}
