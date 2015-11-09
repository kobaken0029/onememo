package com.pliseproject.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.pliseproject.R;
import com.pliseproject.views.activities.SetAlarmActivity;
import com.pliseproject.models.Memo;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MemoFragment extends TextToSpeechFragment {
    private static final int SET_ALARM_ACTIVITY = 1;

    @Bind(R.id.subject_editText)
    EditText subjectEditText;
    @Bind(R.id.memo_editText)
    EditText memoEditText;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu mFloatingActionMenu;
    @Bind(R.id.store_button_in_create_view)
    FloatingActionButton mStoreInCreateViewFloatingActionButton;

    @OnClick(R.id.store_button_in_create_view)
    void onClickStoreMemoInCreateViewButton() {
        mMemoHelper.setCurrentMemo(mMemoHelper.create(subjectEditText.getText().toString(), memoEditText.getText().toString()));
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.alert_button)
    void onClickSetAlertButton() {
        Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
        intent.putExtra(Memo.class.getName(), mMemoHelper.getCurrentMemo());
        startActivityForResult(intent, SET_ALARM_ACTIVITY);
    }

    @OnClick(R.id.store_button)
    void onClickStoreMemoButton() {
        Memo memo = mMemoHelper.getCurrentMemo();
        if (isMemoEmpty(memo) || memo.getId() == deletedMemoId) {
            memo = mMemoHelper.create(subjectEditText.getText().toString(), memoEditText.getText().toString());
        } else {
            memo.setSubject(subjectEditText.getText().toString());
            memo.setMemo(memoEditText.getText().toString());
            mMemoHelper.update(getActivity(), memo);
        }
        mMemoHelper.setCurrentMemo(memo);
        getFragmentManager().popBackStack();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // メモを取得
        Memo memo = (Memo) getArguments().getSerializable(Memo.class.getName());
        mMemoHelper.setCurrentMemo(memo);

        if (!isMemoEmpty(memo)) {
            subjectEditText.setText(memo.getSubject());
            memoEditText.setText(memo.getMemo());
            mStoreInCreateViewFloatingActionButton.setVisibility(View.GONE);
            mFloatingActionMenu.setVisibility(View.VISIBLE);
        } else {
            subjectEditText.setText("");
            memoEditText.setText("");
            mStoreInCreateViewFloatingActionButton.setVisibility(View.VISIBLE);
            mFloatingActionMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_ALARM_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                // 通知時間が設定されたメモを取得
                mMemoHelper.setCurrentMemo((Memo) data.getSerializableExtra(Memo.class.getName()));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mFloatingActionMenu.setVisibility(View.VISIBLE);
    }

    /**
     * メモの空判定をする。
     *
     * @param memo 対象メモ
     * @return 空だったらtrue
     */
    private boolean isMemoEmpty(Memo memo) {
        return memo == null || memo.getId() == null;
    }
}
