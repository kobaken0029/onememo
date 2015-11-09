package com.pliseproject.views.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.pliseproject.R;
import com.pliseproject.utils.PackageUtil;
import com.pliseproject.views.fragments.ViewMemoFragment;
import com.pliseproject.utils.DateUtil;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationDrawerActivity extends BaseActivity {
    /**
     * コンテキストメニューの編集ID
     */
    public static final int MENU_ITEM_ID_EDIT = 1;

    /**
     * コンテキストメニューの削除ID
     */
    public static final int MENU_ITEM_ID_DELETE = 2;


    @Bind(R.id.toolbar_menu)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_header_layout)
    RelativeLayout drawerHeaderRelativeLayout;

    @OnClick(R.id.drawer_create_memo)
    void onClickDrawerCreateMemo() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private Toolbar.OnMenuItemClickListener mMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    break;
                case R.id.menu_setting:
                    settingReadVoice(NavigationDrawerActivity.this);
                    break;
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        ButterKnife.bind(this);

        mToolbarHelper.init(this, toolbar, R.string.read_view, false, true, mMenuItemClickListener);

        ViewMemoFragment f = new ViewMemoFragment();
        addFragment(R.id.container, f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(findViewById(android.R.id.list));

        switch (DateUtil.checkTimeNow()) {
            case DateUtil.NOON:
                drawerHeaderRelativeLayout.setBackgroundResource(R.drawable.school_classroom_at_noon);
                break;
            case DateUtil.EVENING:
                drawerHeaderRelativeLayout.setBackgroundResource(R.drawable.school_classroom_at_evening);
                break;
            case DateUtil.NIGHT:
                drawerHeaderRelativeLayout.setBackgroundResource(R.drawable.school_classroom_at_night);
                break;
            case DateUtil.LATE_NIGHT:
                drawerHeaderRelativeLayout.setBackgroundResource(R.drawable.school_classroom_at_late_night);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_ITEM_ID_EDIT, 0, getString(R.string.edit));
        menu.add(0, MENU_ITEM_ID_DELETE, 0, getString(R.string.delete));
    }

    /**
     * 音声読上げ設定画面へ遷移します。
     */
    private void settingReadVoice(final Context mContext) {
        if (PackageUtil.packageCheck(PackageUtil.N2TTS_PACKAGE_NAME, mContext.getPackageManager())) {
            Intent n2tts = new Intent(Intent.ACTION_MAIN);
            n2tts.setAction("android.intent.category.LAUNCHER");
            n2tts.setClassName(PackageUtil.N2TTS_PACKAGE_NAME, PackageUtil.N2TTS_PACKAGE_NAME + ".TtsServiceSettings");
            n2tts.setFlags(0x10000000);
            mContext.startActivity(n2tts);
        } else {
            new AlertDialog.Builder(mContext)
                    .setMessage(mContext.getString(R.string.n2tts_not_found_message))
                    .setPositiveButton(mContext.getResources().getString(R.string.go_play_stroe),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=jp.kddilabs.n2tts&hl=ja");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    mContext.startActivity(intent);
                                }
                            })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), null).show();
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }
}
