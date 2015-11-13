package com.kobaken0029.helpers.impls;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kobaken0029.R;
import com.kobaken0029.helpers.ToolbarHelper;
import com.kobaken0029.views.activities.NavigationDrawerActivity;

public class ToolbarHelperImpl implements ToolbarHelper {

    /**
     * ツールバーを設定する。
     */
    public void init(final AppCompatActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow,
                     boolean isShowMenu, Toolbar.OnMenuItemClickListener menuItemClickListener) {
        toolbar.setTitle(titleId);
        toolbar.setTitleTextColor(activity.getResources().getColor(android.R.color.white));

        if (isShowMenu) {
            toolbar.inflateMenu(R.menu.main_menu);
            toolbar.setOnMenuItemClickListener(menuItemClickListener);
        }

        if (activity instanceof NavigationDrawerActivity) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationDrawerActivity) activity).getDrawerViewModel()
                            .getDrawerLayout()
                            .openDrawer(GravityCompat.START);
                }
            });
        } else if (isShowBackArrow) {
//            mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.getFragmentManager().popBackStack();
                }
            });
        }
    }
}
