package com.pliseproject.helpers.impls;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pliseproject.R;
import com.pliseproject.helpers.ToolbarHelper;

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

        if (isShowBackArrow) {
//            mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }
}
