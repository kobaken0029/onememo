package com.pliseproject.helpers;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public interface ToolbarHelper {
    void init(final AppCompatActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow,
              boolean isShowMenu, Toolbar.OnMenuItemClickListener menuItemClickListener);
}
