package com.pliseproject.di.components;

import android.content.Context;

import com.pliseproject.views.activities.BaseActivity;
import com.pliseproject.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity activity);

    Context context();
}
