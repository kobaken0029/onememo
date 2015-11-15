package com.kobaken0029.di.modules;

import com.kobaken0029.helpers.ToolbarHelper;
import com.kobaken0029.helpers.impls.MemoHelperImpl;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.helpers.impls.ToolbarHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WanmemoModule {

    @Provides
    @Singleton
    ToolbarHelper provideToolbarHelper() {
        return new ToolbarHelperImpl();
    }

    @Provides
    @Singleton
    MemoHelper provideMemoHelper() {
        return new MemoHelperImpl();
    }
}
