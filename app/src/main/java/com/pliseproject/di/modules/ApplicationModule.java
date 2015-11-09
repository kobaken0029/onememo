package com.pliseproject.di.modules;

import android.content.Context;

import com.pliseproject.WanmemoApplication;
import com.pliseproject.helpers.ToolbarHelper;
import com.pliseproject.helpers.impls.ToolbarHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final WanmemoApplication application;

    public ApplicationModule(WanmemoApplication application) {
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
}
