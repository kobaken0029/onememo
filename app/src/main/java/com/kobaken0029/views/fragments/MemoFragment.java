package com.kobaken0029.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.views.activities.NavigationActivity;
import com.kobaken0029.views.viewmodels.MemoViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * メモを作成・編集するFragment。
 */
public class MemoFragment extends TextToSpeechFragment {
    /** タグ。 */
    public static final String TAG = MemoFragment.class.getName();

    private MemoViewModel mMemoViewModel;

    @Bind(R.id.subject_editText)
    EditText subjectEditText;
    @Bind(R.id.memo_editText)
    EditText memoEditText;

    /**
     * インスタンス生成。
     *
     * @return MemoFragmentのインスタンス
     */
    public static MemoFragment newInstance() {
        return new MemoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        // メモをViewに設定
        mMemoViewModel.setMemoView(memo, !mMemoHelper.isEmpty(memo));
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar t = findById(getActivity(), R.id.toolbar_menu);
        mToolbarHelper.change((BaseActivity) getActivity(), t, R.string.create_view, true);
    }

    @Override
    void bindView() {
        if (mMemoViewModel == null) {
            mMemoViewModel = new MemoViewModel();
        }
        mMemoViewModel.setSubjectEditText(subjectEditText);
        mMemoViewModel.setMemoEditText(memoEditText);
    }

    public MemoViewModel getMemoViewModel() {
        return mMemoViewModel;
    }
}
