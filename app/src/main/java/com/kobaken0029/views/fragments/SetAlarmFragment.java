package com.kobaken0029.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.SetAlarmActivity;
import com.kobaken0029.views.viewmodels.SetAlarmViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * アラームをセットするFragment。
 */
public class SetAlarmFragment extends BaseFragment {
    @Bind(R.id.date_picker)
    DatePicker mDatePicker;
    @Bind(R.id.time_picker)
    TimePicker mTimePicker;
    @Bind(R.id.posted_switch)
    Switch mSwitch;

    private Memo mPostedMemo;
    private SetAlarmViewModel mAlarmViewModel;
    private SetAlarmActivity mActivity;

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
        mActivity = (SetAlarmActivity) getActivity();
        bindView();
    }

    @Override
    void bindView() {
        mAlarmViewModel = mActivity.getAlarmViewModel();
        mPostedMemo = mActivity.getPostedMemo();

        if (mPostedMemo != null) {
            mDatePicker.init(mAlarmViewModel.getYear(), mAlarmViewModel.getMonth(), mAlarmViewModel.getDay(),
                    (v, year, month, day) -> {
                        mAlarmViewModel.setYear(year);
                        mAlarmViewModel.setMonth(month);
                        mAlarmViewModel.setDay(day);
                        mActivity.setAlarmViewModel(mAlarmViewModel);
                    });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker.setHour(mAlarmViewModel.getHour());
                mTimePicker.setMinute(mAlarmViewModel.getMinute());
            } else {
                mTimePicker.setCurrentHour(mAlarmViewModel.getHour());
                mTimePicker.setCurrentMinute(mAlarmViewModel.getMinute());
            }
            mTimePicker.setOnTimeChangedListener((v, hourOfDay, minute) -> {
                mAlarmViewModel.setHour(hourOfDay);
                mAlarmViewModel.setMinute(minute);
                mActivity.setAlarmViewModel(mAlarmViewModel);
            });

            mSwitch.setChecked(mPostedMemo.getPostFlg() == 1);
            mSwitch.setOnCheckedChangeListener((bv, isChecked) -> {
                mPostedMemo.setPostFlg(isChecked ? 1 : 0);
                mActivity.setPostedMemo(mPostedMemo);
            });
        }
    }
}
