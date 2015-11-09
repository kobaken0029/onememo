package com.pliseproject.di.modules;

import android.support.v7.app.AppCompatActivity;

import com.pliseproject.di.PerActivity;
import com.pliseproject.helpers.impls.ToolbarHelperImpl;
import com.pliseproject.helpers.ToolbarHelper;

import dagger.Module;
import dagger.Provides;

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
