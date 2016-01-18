package com.kobaken0029.di.components;

import android.content.Context;

import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.di.modules.ApplicationModule;
import com.kobaken0029.views.fragments.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Applicationのコンポーネント。
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity activity);
    void inject(BaseFragment fragment);

    Context context();
}
