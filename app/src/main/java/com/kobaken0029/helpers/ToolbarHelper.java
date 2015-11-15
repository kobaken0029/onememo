package com.kobaken0029.helpers;

import android.support.v7.widget.Toolbar;

import com.kobaken0029.views.activities.BaseActivity;

public interface ToolbarHelper {
    void init(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow, boolean isShowMenu);
    void change(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow);
}
