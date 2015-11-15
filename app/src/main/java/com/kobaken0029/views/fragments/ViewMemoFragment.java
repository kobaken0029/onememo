package com.kobaken0029.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.views.activities.NavigationDrawerActivity;
import com.kobaken0029.views.viewmodels.ViewMemoViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public class ViewMemoFragment extends TextToSpeechFragment {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        Toolbar t = findById(getActivity(), R.id.toolbar_menu);
        mToolbarHelper.change((BaseActivity) getActivity(), t, R.string.read_view, false);
    }

    @Override
    void bindView() {
        mViewMemoViewModel = new ViewMemoViewModel();
        mViewMemoViewModel.setSubjectView(subjectView);
        mViewMemoViewModel.setMemoView(memoView);
    }

    public void refresh() {
        NavigationDrawerActivity activity = (NavigationDrawerActivity) getActivity();
        mMemoHelper.loadMemos(activity.getMemoListAdapter(), activity.getDrawerViewModel());
        mMemoHelper.loadMemo(mViewMemoViewModel);
    }

    public ViewMemoViewModel getViewMemoViewModel() {
        return mViewMemoViewModel;
    }
}
