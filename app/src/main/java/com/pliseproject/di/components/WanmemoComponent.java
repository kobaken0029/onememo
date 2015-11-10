package com.pliseproject.di.components;

import com.pliseproject.di.modules.WanmemoModule;
import com.pliseproject.views.fragments.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = WanmemoModule.class)
public interface WanmemoComponent {
    void inject(BaseFragment baseFragment);
}
