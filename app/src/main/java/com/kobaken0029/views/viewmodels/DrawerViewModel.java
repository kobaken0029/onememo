package com.kobaken0029.views.viewmodels;

import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class DrawerViewModel {
    DrawerLayout drawerLayout;
    LinearLayout drawer;
    RelativeLayout headerLayout;
    ListView memoListView;
    RelativeLayout memoListEmptyLayout;

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void modify(boolean exist) {
        drawer.removeView(memoListEmptyLayout);
        drawer.removeView(memoListView);

        if (exist) {
            drawer.addView(memoListView);
        } else {
            drawer.addView(memoListEmptyLayout);
        }
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
