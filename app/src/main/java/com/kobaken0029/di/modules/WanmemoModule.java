package com.kobaken0029.di.modules;

import com.kobaken0029.helpers.impls.MemoHelperImpl;
import com.kobaken0029.helpers.MemoHelper;

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
