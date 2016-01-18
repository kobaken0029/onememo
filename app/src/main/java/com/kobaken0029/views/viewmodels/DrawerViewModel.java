package com.kobaken0029.views.viewmodels;

import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * ナビゲーションドロワーのViewModel。
 */
public class DrawerViewModel {
    private DrawerLayout drawerLayout;
    private LinearLayout drawer;
    private RelativeLayout headerLayout;
    private ListView memoListView;
    private RelativeLayout memoListEmptyLayout;

    public void modify(boolean exist) {
        drawer.removeView(memoListEmptyLayout);
        drawer.removeView(memoListView);

        if (exist) {
            drawer.addView(memoListView);
        } else {
            drawer.addView(memoListEmptyLayout);
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public LinearLayout getDrawer() {
        return drawer;
    }

    public void setDrawer(LinearLayout drawer) {
        this.drawer = drawer;
    }

    public RelativeLayout getHeaderLayout() {
        return headerLayout;
    }

    public void setHeaderLayout(RelativeLayout headerLayout) {
        this.headerLayout = headerLayout;
    }

    public ListView getMemoListView() {
        return memoListView;
    }

    public void setMemoListView(ListView memoListView) {
        this.memoListView = memoListView;
    }

    public RelativeLayout getMemoListEmptyLayout() {
        return memoListEmptyLayout;
    }

    public void setMemoListEmptyLayout(RelativeLayout memoListEmptyLayout) {
        this.memoListEmptyLayout = memoListEmptyLayout;
    }
}
