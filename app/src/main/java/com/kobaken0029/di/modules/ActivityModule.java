package com.kobaken0029.di.modules;

import android.support.v7.app.AppCompatActivity;

import com.kobaken0029.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Activityのモジュール。
 */
@Module
public class ActivityModule {
    private final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    AppCompatActivity activity() {
        return activity;
    }
}
