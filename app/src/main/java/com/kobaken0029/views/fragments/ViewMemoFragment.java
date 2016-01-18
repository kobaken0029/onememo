package com.kobaken0029.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.views.activities.NavigationDrawerActivity;
import com.kobaken0029.views.viewmodels.ViewMemoViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        Long id;
        if (getArguments() != null) {
            id = ((NavigationDrawerActivity) getActivity()).currentMemoId;
        } else if (mMemoHelper.exists()) {
            id = Stream.of(mMemoHelper.findAll())
                    .sorted((o1, o2) -> o2.getId().compareTo(o1.getId()))
                    .collect(Collectors.toList()).get(0).getId();
        } else {
            id = 0l;
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
            ((NavigationDrawerActivity) getActivity()).getDrawerViewModel().modify(false);
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
