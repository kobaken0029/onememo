package com.pliseproject.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pliseproject.di.components.DaggerWanmemoComponent;
import com.pliseproject.di.modules.WanmemoModule;
import com.pliseproject.helpers.MemoHelper;

import javax.inject.Inject;

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

    private void initializeInjector() {
        DaggerWanmemoComponent.builder()
                .wanmemoModule(new WanmemoModule())
                .build()
                .inject(this);
    }
}
