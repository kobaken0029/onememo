package com.pliseproject.di.modules;

import com.pliseproject.helpers.impls.MemoHelperImpl;
import com.pliseproject.helpers.MemoHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WanmemoModule {
    @Provides
    @Singleton
    MemoHelper provideMemoHelper() {
        return new MemoHelperImpl();
    }
}
