package com.pliseproject.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pliseproject.R;
import com.pliseproject.models.Memo;
import com.pliseproject.views.activities.NavigationDrawerActivity;
import com.pliseproject.views.activities.SetAlarmActivity;
import com.pliseproject.views.viewmodels.FloatingActionViewModel;
import com.pliseproject.views.viewmodels.MemoViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MemoFragment extends TextToSpeechFragment {
    public static final String TAG = MemoFragment.class.getName();

    private MemoViewModel mMemoViewModel;

    @Bind(R.id.subject_editText)
    EditText subjectEditText;
    @Bind(R.id.memo_editText)
    EditText memoEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // メモを取得
        Memo memo = (Memo) getArguments().getSerializable(Memo.TAG);
        mMemoHelper.setCurrentMemo(memo);

        FloatingActionViewModel viewModel = ((NavigationDrawerActivity) getActivity()).getFloatingActionViewModel();
        View storeFab = viewModel.getStoreInCreateViewFab();
        View floatingActionMenu = viewModel.getFloatingActionMenu();
        if (!mMemoHelper.isMemoEmpty(memo)) {
            subjectEditText.setText(memo.getSubject() == null ? "" : memo.getSubject());
            memoEditText.setText(memo.getMemo());
            storeFab.setVisibility(View.GONE);
            floatingActionMenu.setVisibility(View.VISIBLE);
        } else {
            subjectEditText.setText("");
            memoEditText.setText("");
            storeFab.setVisibility(View.VISIBLE);
            floatingActionMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetAlarmActivity.SET_ALARM_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                // 通知時間が設定されたメモを取得
                mMemoHelper.setCurrentMemo((Memo) data.getSerializableExtra(Memo.TAG));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((NavigationDrawerActivity) getActivity()).getFloatingActionViewModel()
                .getFloatingActionMenu()
                .setVisibility(View.VISIBLE);
    }

    @Override
    void bindView() {
        mMemoViewModel = new MemoViewModel();
        mMemoViewModel.setSubjectEditText(subjectEditText);
        mMemoViewModel.setMemoEditText(memoEditText);
    }

    public MemoViewModel getMemoViewModel() {
        return mMemoViewModel;
    }
}
