package com.kobaken0029.helpers;

import android.support.v7.widget.Toolbar;

import com.kobaken0029.views.activities.BaseActivity;

public interface ToolbarHelper {
    /**
     * ツールバーを設定する。
     * @param activity ツールバーを表示させるアクティビティ
     * @param toolbar ツールバー
     * @param titleId タイトルリソースID
     * @param isShowBackArrow 矢印(バック)を表示させる場合true
     * @param isShowMenu メニューを表示させる場合true
     */
    void init(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow, boolean isShowMenu);

    /**
     * ツールバーを再設定する。
     * @param activity ツールバーを表示させるアクティビティ
     * @param toolbar ツールバー
     * @param titleId タイトルリソースID
     * @param isShowBackArrow 矢印(バック)を表示させる場合true
     */
    void change(final BaseActivity activity, Toolbar toolbar, int titleId, boolean isShowBackArrow);
}
