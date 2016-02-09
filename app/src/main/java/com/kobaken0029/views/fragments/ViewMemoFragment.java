package com.kobaken0029.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.views.activities.NavigationActivity;
import com.kobaken0029.views.viewmodels.ViewMemoViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

/**
 * メモを閲覧するFragment。
 */
public class ViewMemoFragment extends TextToSpeechFragment {
    /** タグ。 */
    public static final String TAG = ViewMemoFragment.class.getName();

    @Bind(R.id.subject_textView)
    public TextView subjectView;
    @Bind(R.id.memo_textView)
    public TextView memoView;

    private ViewMemoViewModel mViewMemoViewModel;

    /**
     * インスタンス生成。
     *
     * @return ViewMemoFragmentのインスタンス
     */
    public static ViewMemoFragment newInstance() {
        return new ViewMemoFragment();
    }

    @OnClick(R.id.memomiya)
    void onClickMemomiya() {
        if (mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }

        Memo memo = mMemoHelper.find(mViewMemoViewModel.getMemoId());
        if (memo != null) {
            StringBuilder str = new StringBuilder();
            str.append("");
            if (!TextUtils.isEmpty(memo.getSubject())) {
                str.append(memo.getSubject());
                str.append("。");
            }
            if (!TextUtils.isEmpty(memo.getMemo())) {
                str.append(memo.getMemo());
            }
            ttsSpeak(str.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_memo, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long id;
        if (getArguments() != null) {
            id = ((NavigationActivity) getActivity()).currentMemoId;
        } else if (mMemoHelper.exists()) {
            id = Stream.of(mMemoHelper.findAll())
                    .sorted((o1, o2) -> (int) (o2.getId() - o1.getId()))
                    .collect(Collectors.toList()).get(0).getId();
        } else {
            id = 0L;
        }
        mViewMemoViewModel.setMemoId(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar t = findById(getActivity(), R.id.toolbar_menu);
        mToolbarHelper.change((BaseActivity) getActivity(), t, R.string.read_view, false);

        Memo memo = mMemoHelper.find(mViewMemoViewModel.getMemoId());
        if (memo != null) {
            mViewMemoViewModel.setMemoView(memo, !mMemoHelper.isEmpty(memo));
        } else {
            ((NavigationActivity) getActivity()).getDrawerViewModel().modify(false);
        }
    }

    @Override
    void bindView() {
        if (mViewMemoViewModel == null) {
            mViewMemoViewModel = new ViewMemoViewModel();
        }
        mViewMemoViewModel.setSubjectView(subjectView);
        mViewMemoViewModel.setMemoView(memoView);
    }

    public ViewMemoViewModel getViewMemoViewModel() {
        return mViewMemoViewModel;
    }
}
