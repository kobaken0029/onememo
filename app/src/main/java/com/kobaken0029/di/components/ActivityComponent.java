package com.kobaken0029.di.components;

import android.support.v7.app.AppCompatActivity;

import com.kobaken0029.di.PerActivity;
import com.kobaken0029.di.modules.ActivityModule;

import dagger.Component;

/**
 * Activityのコンポーネント。
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    AppCompatActivity activity();
}
