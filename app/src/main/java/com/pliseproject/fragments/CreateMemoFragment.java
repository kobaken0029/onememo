package com.pliseproject.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.pliseproject.R;
import com.pliseproject.activities.SetAlarmActivity;
import com.pliseproject.fragments.bases.BaseNavigationDrawerFragment;
import com.pliseproject.fragments.bases.BaseTextToSpeechFragment;
import com.pliseproject.managers.AppController;
import com.pliseproject.models.Memo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateMemoFragment extends BaseTextToSpeechFragment {
    private static final int SET_ALARM_ACTIVITY = 1;

//    private int alarmHour;
//    private int alarmMinute;
    private AppController appController;
    private Memo memo;

    @InjectView(R.id.subject_editText)
    EditText subjectEditText;

    @InjectView(R.id.memo_editText)
    EditText memoEditText;

    @InjectView(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;

    @InjectView(R.id.alert_button)
    FloatingActionButton setAlertButton;

    @OnClick(R.id.store_button)
    void onClickStoreMemoButton() {
        if (memo == null || memo.getId() == getDeletedMemoId()) {
            appController.saveMemo(subjectEditText, memoEditText);
        } else {
            appController.updateMemo(memo, subjectEditText, memoEditText);
        }
        activity.finish();
        floatingActionsMenu.collapse();
    }

    @OnClick(R.id.alert_button)
    void onClickSetAlertButton() {
        Intent intent = new Intent(activity, SetAlarmActivity.class);
        intent.putExtra("memo", memo);
        startActivityForResult(intent, SET_ALARM_ACTIVITY);
        floatingActionsMenu.collapse();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appController = (AppController) activity.getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_memo, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // メモを取得
        memo = (Memo) activity.getIntent().getSerializableExtra("memo");

        setAlertButton.setVisibility(View.GONE);

        if (memo != null) {
            subjectEditText.setText(memo.getSubject());
            memoEditText.setText(memo.getMemo());
            setAlertButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_ALARM_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                // 通知時間が設定されたメモを取得
                memo = (Memo) data.getSerializableExtra("memo");

                // 通知時間を取得
//                alarmHour = data.getIntExtra("hour", 0);
//                alarmMinute = data.getIntExtra("minute", 0);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                activity.finish();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item, null);
    }
}
