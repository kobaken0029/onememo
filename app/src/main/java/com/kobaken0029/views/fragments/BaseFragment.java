package com.kobaken0029.views.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.kobaken0029.WanmemoApplication;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.helpers.ToolbarHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    protected static long mDeletedMemoId;

    @Inject
    ToolbarHelper mToolbarHelper;
    @Inject
    MemoHelper mMemoHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initializeInjector();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mToolbarHelper = null;
        mMemoHelper = null;
        super.onDestroy();
    }

    private void initializeInjector() {
        ((WanmemoApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    abstract void bindView();

    public long getDeletedMemoId() {
        return mDeletedMemoId;
    }
}
