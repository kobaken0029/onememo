package com.kobaken0029.di.components;

import android.content.Context;

import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity activity);

    Context context();
}
