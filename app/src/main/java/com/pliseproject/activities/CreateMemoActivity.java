package com.pliseproject.activities;

import com.pliseproject.R;
import com.pliseproject.activities.bases.BaseNavigationDrawerActivity;
import com.pliseproject.models.Memo;
import com.pliseproject.utils.UiUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * メモ作成画面のActivityです。
 */
public class CreateMemoActivity extends BaseNavigationDrawerActivity {
    @InjectView(R.id.toolbar_menu)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);
        ButterKnife.inject(this);
        initToolbar();
    }

    /**
     * ツールバーを設定する。
     */
    private void initToolbar() {
        toolbar.setTitle(R.string.create_view);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            UiUtil.showDialog(this, getString(R.string.confirmation_of_not_saved),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UiUtil.showDialog(this, getString(R.string.confirmation_of_not_saved),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
