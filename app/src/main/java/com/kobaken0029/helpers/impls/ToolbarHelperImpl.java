package com.kobaken0029.helpers.impls;

import android.support.v7.widget.Toolbar;

import com.kobaken0029.R;
import com.kobaken0029.helpers.ToolbarHelper;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.interfaces.NavigationOnClickListener;
import com.kobaken0029.interfaces.OnMenuItemClickListener;

public class ToolbarHelperImpl implements ToolbarHelper {

    public void init(final BaseActivity activity, Toolbar toolbar, int titleId,
                     boolean isShowBackArrow, boolean isShowMenu, boolean canPlayVoice) {
        toolbar.setTitle(titleId);
        toolbar.setTitleTextColor(activity.getResources().getColor(android.R.color.white));

        if (isShowMenu) {
            if (canPlayVoice) {
                toolbar.inflateMenu(R.menu.main_menu_voice_on);
            } else {
                toolbar.inflateMenu(R.menu.main_menu_voice_off);
            }

            if (activity instanceof OnMenuItemClickListener) {
                OnMenuItemClickListener listener = (OnMenuItemClickListener) activity;
                toolbar.setOnMenuItemClickListener(listener::onMenuItemClicked);
            }
        }

        boolean isNavigationOnClickListenerImpl = activity instanceof NavigationOnClickListener;
        if (!isShowBackArrow) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
            if (isNavigationOnClickListenerImpl) {
                NavigationOnClickListener listener = (NavigationOnClickListener) activity;
                toolbar.setNavigationOnClickListener(v -> listener.onClicked());
            }
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
        }
    }

    public void change(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow) {
        toolbar.setTitle(titleId);

        boolean isNavigationOnClickListenerImpl = activity instanceof NavigationOnClickListener;
        if (!isShowBackArrow) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
            if (isNavigationOnClickListenerImpl) {
                NavigationOnClickListener listener = (NavigationOnClickListener) activity;
                toolbar.setNavigationOnClickListener(v -> listener.onClicked());
            }
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            if (isNavigationOnClickListenerImpl) {
                toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
            }
        }
    }
}
