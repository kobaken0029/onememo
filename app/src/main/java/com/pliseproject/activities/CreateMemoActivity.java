package com.pliseproject.activities;

import com.pliseproject.R;
import com.pliseproject.activities.bases.BaseNavigationDrawerActivity;
import com.pliseproject.fragments.CreateMemoFragment;

import android.os.Bundle;
import android.view.MenuItem;

/**
 * メモ作成画面のActivityです。
 */
public class CreateMemoActivity extends BaseNavigationDrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);
        initToolbar(R.string.create_view);
    }

    @Override
    public void onBackPressed() {
        ((CreateMemoFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_fragment)).onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ((CreateMemoFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.create_fragment)).onBackPressed();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
}
