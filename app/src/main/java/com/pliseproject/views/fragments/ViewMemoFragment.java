package com.pliseproject.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pliseproject.R;
import com.pliseproject.views.activities.NavigationDrawerActivity;
import com.pliseproject.views.viewmodels.ViewMemoViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;

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
