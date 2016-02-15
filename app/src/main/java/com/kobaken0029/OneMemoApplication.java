package com.kobaken0029;

import com.kobaken0029.di.components.ApplicationComponent;
import com.kobaken0029.di.components.DaggerApplicationComponent;
import com.kobaken0029.di.modules.ApplicationModule;
import com.raizlabs.android.dbflow.config.FlowManager;

import android.app.Application;

public class OneMemoApplication extends Application {

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
