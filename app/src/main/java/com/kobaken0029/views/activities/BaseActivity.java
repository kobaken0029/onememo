package com.kobaken0029.views.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kobaken0029.OneMemoApplication;
import com.kobaken0029.di.components.ApplicationComponent;
import com.kobaken0029.di.modules.ActivityModule;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.helpers.ToolbarHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Activityのベースクラス。
 */
public class BaseActivity extends AppCompatActivity {
    @Inject
    ToolbarHelper mToolbarHelper;
    @Inject
    MemoHelper mMemoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        mToolbarHelper = null;
        mMemoHelper = null;
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * フラグメントを追加する。
     *
     * @param containerViewId コンテナID
     * @param fragment        対象フラグメント
     * @param key             キー
     */
    public void addFragment(int containerViewId, Fragment fragment, String key) {
        getFragmentManager().beginTransaction()
                .add(containerViewId, fragment, key)
                .commit();
    }

    /**
     * フラグメントを置き換える。
     *
     * @param containerViewId コンテナID
     * @param fragment        対象フラグメント
     * @param key             キー
     */
    public void replaceFragment(int containerViewId, Fragment fragment, String key) {
        getFragmentManager().beginTransaction()
                .replace(containerViewId, fragment, key)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    /**
     * フラグメントを削除する。
     *
     * @param fragment 対象フラグメント
     */
    public void removeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
    }

    /**
     * アプリケーションコンポーネントを取得する。
     * @return アプリケーションコンポーネント
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((OneMemoApplication) getApplication()).getApplicationComponent();
    }

    /**
     * アクティビティモジュールを取得する。
     * @return アクティビティモジュール
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
