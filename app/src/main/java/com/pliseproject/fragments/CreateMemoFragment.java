package com.pliseproject.fragments;

import android.app.Activity;
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
import com.pliseproject.activities.ViewMemoActivity;
import com.pliseproject.fragments.bases.BaseTextToSpeechFragment;
import com.pliseproject.fragments.interfaces.OnBackListener;
import com.pliseproject.models.Memo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateMemoFragment extends BaseTextToSpeechFragment implements OnBackListener {
    private static final int SET_ALARM_ACTIVITY = 1;

    @InjectView(R.id.subject_editText)
    EditText subjectEditText;

    @InjectView(R.id.memo_editText)
    EditText memoEditText;

    @InjectView(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;

    @InjectView(R.id.store_button_in_create_view)
    FloatingActionButton storeInCreateViewFloatingActionButton;

    @OnClick(R.id.store_button)
    void onClickStoreMemoButton() {
        if (memo == null || memo.getId() == getDeletedMemoId()) {
            appController.saveMemo(subjectEditText, memoEditText);
        } else {
            memo.setSubject(subjectEditText.getText().toString());
            memo.setMemo(memoEditText.getText().toString());
            appController.updateMemo(memo);
        }
        activity.finish();
        floatingActionsMenu.collapse();
    }

    @OnClick(R.id.store_button_in_create_view)
    void onClickStoreMemoInCreateViewButton() {
        appController.saveMemo(subjectEditText, memoEditText);
        activity.finish();
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

        storeInCreateViewFloatingActionButton.setVisibility(View.VISIBLE);
        floatingActionsMenu.setVisibility(View.GONE);

        if (memo != null) {
            subjectEditText.setText(memo.getSubject());
            memoEditText.setText(memo.getMemo());
            storeInCreateViewFloatingActionButton.setVisibility(View.GONE);
            floatingActionsMenu.setVisibility(View.VISIBLE);
        } else {
            subjectEditText.setText("");
            memoEditText.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_ALARM_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                // 通知時間が設定されたメモを取得
                memo = (Memo) data.getSerializableExtra("memo");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                activity.finish();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item, null);
    }

    /**
     * メモを保存・更新する。
     */
    private void storeMemo() {
        if (memo == null || memo.getId() == getDeletedMemoId()) {
            appController.saveMemo(subjectEditText, memoEditText);
            activity.finish();
        } else if (isModified()) {
            Memo memo = (Memo) activity.getIntent().getSerializableExtra("memo");
            memo.setSubject(subjectEditText.getText().toString());
            memo.setMemo(memoEditText.getText().toString());
            memo.setPostFlg(this.memo.getPostFlg());
            memo.setPostTime(this.memo.getPostTime());
            Intent intent = new Intent(activity, ViewMemoActivity.class);
            intent.putExtra("memo", memo);
            appController.showDialogBeforeMoveMemoView(activity, intent);
            activity.setModifiedFlg(true);
        } else {
            activity.finish();
        }
    }

    private boolean isModified() {
        return !(memo.getSubject().equals(subjectEditText.getText().toString())
                && memo.getMemo().equals(memoEditText.getText().toString()));
    }

    @Override
    public void onBackPressed() {
        storeMemo();
    }
}
