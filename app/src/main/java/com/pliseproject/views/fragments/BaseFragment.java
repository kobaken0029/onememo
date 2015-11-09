package com.pliseproject.views.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.pliseproject.di.components.DaggerWanmemoComponent;
import com.pliseproject.di.modules.WanmemoModule;
import com.pliseproject.helpers.MemoHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment {
    protected static long deletedMemoId;

    @Inject
    MemoHelper mMemoHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initializeInjector();
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        mMemoHelper = null;
        super.onDestroy();
    }

    private void initializeInjector() {
        DaggerWanmemoComponent.builder()
                .wanmemoModule(new WanmemoModule())
                .build()
                .inject(this);
    }
}
