package com.kobaken0029.views.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.DateUtil;
import com.kobaken0029.views.activities.SetAlarmActivity;
import com.kobaken0029.views.viewmodels.SetAlarmViewModel;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * アラームをセットするFragment。
 */
public class SetAlarmFragment extends BaseFragment {
    @Bind(R.id.calendar_text)
    TextView mCalendarTextView;
    @Bind(R.id.time_text)
    TextView mTimeTextView;
    @Bind(R.id.posted_switch)
    Switch mSwitch;

    private Memo mPostedMemo;
    private SetAlarmViewModel mAlarmViewModel;

    @OnClick(R.id.calendar_text)
    void onClickCalendarText() {
        SetAlarmActivity activity = (SetAlarmActivity) getActivity();
        new DatePickerDialog(activity, (v, year, month, day) -> {
            mAlarmViewModel.setYear(year);
            mAlarmViewModel.setMonth(month);
            mAlarmViewModel.setDay(day);
            activity.setAlarmViewModel(mAlarmViewModel);
            mCalendarTextView.setText(DateUtil.convertToString(DateUtil.YEAR_MONTH_DAY, DateUtil.getDate(year, month, day)));
        }, mAlarmViewModel.getYear(), mAlarmViewModel.getMonth(), mAlarmViewModel.getDay()).show();
    }

    @OnClick(R.id.time_text)
    void onClickTimeText() {
        SetAlarmActivity activity = (SetAlarmActivity) getActivity();
        new TimePickerDialog(activity, (v, hour, minute) -> {
            mAlarmViewModel.setHour(hour);
            mAlarmViewModel.setMinute(minute);
            activity.setAlarmViewModel(mAlarmViewModel);
            mTimeTextView.setText(DateUtil.convertToString(DateUtil.HOUR_MINUTE, DateUtil.getDate(hour, minute)));
        }, mAlarmViewModel.getHour(), mAlarmViewModel.getMinute(), true).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_alarm, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindView();
    }

    @Override
    void bindView() {
        SetAlarmActivity activity = (SetAlarmActivity) getActivity();
        mAlarmViewModel = activity.getAlarmViewModel();
        mPostedMemo = activity.getPostedMemo();

        if (mPostedMemo != null) {
            if (mPostedMemo.getPostFlg() == 1) {
                mCalendarTextView.setText(DateUtil.convertToString(DateUtil.YEAR_MONTH_DAY, mPostedMemo.getPostTime()));
                mTimeTextView.setText(DateUtil.convertToString(DateUtil.HOUR_MINUTE, mPostedMemo.getPostTime()));
            } else {
                Date now = DateUtil.getCurrentDate();
                mCalendarTextView.setText(DateUtil.convertToString(DateUtil.YEAR_MONTH_DAY, now));
                mTimeTextView.setText(DateUtil.convertToString(DateUtil.HOUR_MINUTE, now));
            }

            mSwitch.setChecked(mPostedMemo.getPostFlg() == 1);
            mSwitch.setOnCheckedChangeListener((bv, isChecked) -> {
                mPostedMemo.setPostFlg(isChecked ? 1 : 0);
                activity.setPostedMemo(mPostedMemo);
            });
        }
    }
}
