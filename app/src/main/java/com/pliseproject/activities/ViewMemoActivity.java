package com.pliseproject.activities;

import com.pliseproject.R;
import com.pliseproject.activities.bases.BaseNavigationDrawerActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * メモ閲覧画面のActivityです。
 */
public class ViewMemoActivity extends BaseNavigationDrawerActivity {
    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memo);
        initToolbar(R.string.read_view);
    }

    /**
     * ツールバーを設定する。
     */
    @Override
    protected void initToolbar(int titleResId) {
        super.initToolbar(titleResId);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // NavigationDrawerとActivityの状態を同期
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
