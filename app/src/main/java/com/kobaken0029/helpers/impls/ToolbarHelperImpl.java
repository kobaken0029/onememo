package com.kobaken0029.helpers.impls;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.kobaken0029.R;
import com.kobaken0029.helpers.ToolbarHelper;
import com.kobaken0029.utils.N2ttsUtil;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.views.activities.NavigationDrawerActivity;

public class ToolbarHelperImpl implements ToolbarHelper {

    /**
     * ツールバーを設定する。
     */
    public void init(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow, boolean isShowMenu) {
        toolbar.setTitle(titleId);
        toolbar.setTitleTextColor(activity.getResources().getColor(android.R.color.white));

        if (isShowMenu) {
            toolbar.inflateMenu(R.menu.main_menu);
            toolbar.setOnMenuItemClickListener(createMenuItemClickListener(activity));
        }

        boolean isNavigationDrawerActivity = activity instanceof NavigationDrawerActivity;
        if (!isShowBackArrow) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
            if (isNavigationDrawerActivity) {
                toolbar.setNavigationOnClickListener(createMenuClickListener((NavigationDrawerActivity) activity));
            }
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(createBackClickListener(activity));
        }
    }

    public void change(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow) {
        toolbar.setTitle(titleId);

        boolean isNavigationDrawerActivity = activity instanceof NavigationDrawerActivity;
        if (!isShowBackArrow) {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
            if (isNavigationDrawerActivity) {
                toolbar.setNavigationOnClickListener(createMenuClickListener((NavigationDrawerActivity) activity));
            }
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            if (isNavigationDrawerActivity) {
                toolbar.setNavigationOnClickListener(createBackClickListener((NavigationDrawerActivity) activity));
            }
        }
    }

    private Toolbar.OnMenuItemClickListener createMenuItemClickListener(final BaseActivity activity) {
        return new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case android.R.id.home:
                        activity.finish();
                        break;
                    case R.id.menu_setting:
                        N2ttsUtil.settingReadVoice(activity);
                        break;
                }
                return true;
            }
        };
    }

    private View.OnClickListener createMenuClickListener(final NavigationDrawerActivity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getDrawerViewModel()
                        .getDrawerLayout()
                        .openDrawer(GravityCompat.START);

            }
        };
    }

    private View.OnClickListener createBackClickListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        };
    }
}
