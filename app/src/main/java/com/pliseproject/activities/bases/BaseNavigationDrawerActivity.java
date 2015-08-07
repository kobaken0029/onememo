package com.pliseproject.activities.bases;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pliseproject.R;
import com.pliseproject.activities.CreateMemoActivity;
import com.pliseproject.activities.SetAlarmActivity;
import com.pliseproject.activities.ViewMemoActivity;
import com.pliseproject.managers.AppController;
import com.pliseproject.models.Memo;
import com.pliseproject.receivers.MyBroadcastReceiver;
import com.pliseproject.utils.DateUtil;
import com.pliseproject.views.adapters.MemoListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BaseNavigationDrawerActivity extends AppCompatActivity {
    /**
     * コンテキストメニューの編集ID
     */
    public static final int MENU_ITEM_ID_EDIT = 1;

    /**
     * コンテキストメニューの削除ID
     */
    public static final int MENU_ITEM_ID_DELETE = 2;

    protected AppController appController;

    private MyBroadcastReceiver mBroadcastReceiver;
    private List<Memo> memos;
    private MemoListAdapter memoListAdapter;
    private boolean modifiedFlg;

    @InjectView(R.id.toolbar_menu)
    Toolbar toolbar;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(android.R.id.list)
    ListView listView;

    @InjectView(R.id.drawer)
    LinearLayout drawerLinearLayout;

    @InjectView(R.id.drawer_header_layout)
    RelativeLayout drawerHeaderRelativeLayout;

    @InjectView(R.id.icon_memomiya)
    ImageView drawerMemomiyaImageView;

    @InjectView(R.id.message_window)
    TextView messageWinsowTextView;

    @OnClick(R.id.drawer_create_memo)
    void onClickDrawerCreateMemo() {
        moveCreateMemoView();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = (AppController) getApplication();
        mBroadcastReceiver = new MyBroadcastReceiver(getApplicationContext(), this);
        mBroadcastReceiver.registerReceiver();

        memos = new ArrayList<>();
        memoListAdapter = new MemoListAdapter(this, memos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.setAdapter(memoListAdapter);
        registerForContextMenu(listView);

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

    /**
     * ツールバーを設定する。
     */
    protected void initToolbar(int titleResId) {
        toolbar.setTitle(titleResId);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
                ((AppController) getApplication()).settingReadVoice(this);
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
            Intent intent = new Intent(this, CreateMemoActivity.class);
            intent.putExtra("memo", getIntent().getSerializableExtra("memo"));
            if (modifiedFlg) {
                appController.showDialogBeforeMoveMemoView(this, intent);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);

                if (this instanceof SetAlarmActivity) {
                    setResult(Activity.RESULT_CANCELED, intent);
                } else {
                    startActivity(intent);
                }

                finish();
            }
        } else if (this instanceof ViewMemoActivity) {
            startActivity(new Intent(this, CreateMemoActivity.class));
        }
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public MemoListAdapter getMemoListAdapter() {
        return memoListAdapter;
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

    public ImageView getDrawerMemomiyaImageView() {
        return drawerMemomiyaImageView;
    }

    public TextView getMessageWindowTextView() {
        return messageWinsowTextView;
    }

    public boolean isModifiedFlg() {
        return modifiedFlg;
    }

    public void setModifiedFlg(boolean modifiedFlg) {
        this.modifiedFlg = modifiedFlg;
    }
}
