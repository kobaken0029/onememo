package com.kobaken0029.views.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kobaken0029.WanmemoApplication;
import com.kobaken0029.di.components.ApplicationComponent;
import com.kobaken0029.di.modules.ActivityModule;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.helpers.ToolbarHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    @Inject
    ToolbarHelper mToolbarHelper;
    @Inject
    MemoHelper mMemoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getApplicationComponent().inject(this);
        }
    }

    @Override
    protected void onDestroy() {
        mToolbarHelper = null;
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    public void addFragment(int containerViewId, Fragment fragment, String key) {
        getFragmentManager().beginTransaction()
                .add(containerViewId, fragment, key)
                .commit();
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String key) {
        getFragmentManager().beginTransaction()
                .replace(containerViewId, fragment, key)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void removeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((WanmemoApplication) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
