package com.pliseproject.di.components;

import android.support.v7.app.AppCompatActivity;

import com.pliseproject.di.PerActivity;
import com.pliseproject.di.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    AppCompatActivity activity();
}
