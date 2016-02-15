package com.kobaken0029.di.modules;

import android.content.Context;

import com.kobaken0029.OneMemoApplication;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.helpers.ToolbarHelper;
import com.kobaken0029.helpers.impls.MemoHelperImpl;
import com.kobaken0029.helpers.impls.ToolbarHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Applicationのモジュール。
 */
@Module
public class ApplicationModule {
    private final OneMemoApplication application;

    public ApplicationModule(OneMemoApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application.getApplicationContext();
    }

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
