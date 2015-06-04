package com.pliseproject.activities.bases;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pliseproject.R;
import com.pliseproject.activities.CreateMemoActivity;
import com.pliseproject.activities.SetAlarmActivity;
import com.pliseproject.activities.ViewMemoActivity;
import com.pliseproject.managers.AppController;
import com.pliseproject.models.Memo;
import com.pliseproject.receivers.MyBroadcastReceiver;
import com.pliseproject.views.adapters.MyListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class BaseNavigationDrawerActivity extends ActionBarActivity {
    public static final int MENU_ITEM_ID_EDIT = 1;
    public static final int MENU_ITEM_ID_DELETE = 2;

    protected AppController appController;

    private MyBroadcastReceiver mBroadcastReceiver;
    private List<Memo> memos;
    private MyListAdapter myListAdapter;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(android.R.id.list)
    ListView listView;

    @InjectView(R.id.drawer)
    LinearLayout drawerLinearLayout;

    @OnClick(R.id.drawer_create_memo)
    void onClickDrawerCreateMemo() {
        moveCreateMemoView();
        drawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = (AppController) getApplication();
        mBroadcastReceiver = new MyBroadcastReceiver(getApplicationContext(), this);
        mBroadcastReceiver.registerReceiver();

        memos = new ArrayList<>();
        myListAdapter = new MyListAdapter(this, memos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(myListAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_ITEM_ID_EDIT, 0, getString(R.string.edit));
        menu.add(0, MENU_ITEM_ID_DELETE, 0, getString(R.string.delete));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_setting:
                ((AppController) getApplication()).settingReadVoice();
                break;
            case R.id.menu_end:
                mBroadcastReceiver.sendReceiver();
                break;
        }

        return true;
    }

    /**
     * 作成画面に遷移する。
     */
    public void moveCreateMemoView() {
        if (((this instanceof CreateMemoActivity) && getIntent().getSerializableExtra("memo") != null)
                || this instanceof SetAlarmActivity) {
            appController.showDialogBeforeMoveMemoView(this, new Intent(this, CreateMemoActivity.class));
        } else if (this instanceof ViewMemoActivity) {
            startActivity(new Intent(this, CreateMemoActivity.class));
        }
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public MyListAdapter getMyListAdapter() {
        return myListAdapter;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ListView getListView() {
        return listView;
    }

    public LinearLayout getDrawerLinearLayout() {
        return drawerLinearLayout;
    }
}
