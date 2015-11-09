package com.pliseproject;

import com.pliseproject.di.components.ApplicationComponent;
import com.pliseproject.di.components.DaggerApplicationComponent;
import com.pliseproject.di.modules.ApplicationModule;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;

public class WanmemoApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        FlowManager.init(this);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
