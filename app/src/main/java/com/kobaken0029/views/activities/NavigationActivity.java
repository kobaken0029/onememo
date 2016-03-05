package com.kobaken0029.views.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kobaken0029.R;
import com.kobaken0029.interfaces.MemoHandler;
import com.kobaken0029.interfaces.ViewMemoHandler;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.DateUtil;
import com.kobaken0029.utils.N2ttsUtil;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.interfaces.NavigationOnClickListener;
import com.kobaken0029.interfaces.OnMenuItemClickListener;
import com.kobaken0029.views.adapters.MemoListAdapter;
import com.kobaken0029.views.fragments.MemoFragment;
import com.kobaken0029.views.fragments.ViewMemoFragment;
import com.kobaken0029.views.viewmodels.DrawerViewModel;
import com.kobaken0029.views.viewmodels.FloatingActionViewModel;
import com.kobaken0029.views.widget.OneMemoWidgetProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * NavigationViewが存在するActivity。
 */
public class NavigationActivity extends BaseActivity
        implements NavigationOnClickListener, OnMenuItemClickListener, ViewMemoFragment.NavigationActivityHandler {
    /** タグ。 */
    public static final String TAG = NavigationActivity.class.getName();

    /** プリファレンスID。 */
    public static final String SHARED_PREFERENCES_ID = "memo_position";

    /** プリファレンスKey。 */
    public static final String SHARED_PREFERENCES_MEMO_POSITION_KEY = "position";

    private static final int MEMO_NEW = 0;
    private static final int MEMO_EXISTING = 1;

    @Bind(R.id.toolbar_menu)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_header_layout)
    RelativeLayout drawerHeaderRelativeLayout;
    @Bind(R.id.search_text)
    EditText drawerSearchMemoEditText;
    @Bind(R.id.memo_list)
    ListView mListView;
    @Bind(R.id.drawer)
    LinearLayout drawer;

    @Bind(R.id.multiple_actions)
    FloatingActionsMenu mFloatingActionMenu;
    @Bind(R.id.save_button_in_create_view)
    FloatingActionButton mStoreInCreateViewFab;
    @Bind(R.id.go_to_alarm_setting_button)
    FloatingActionButton mAlertFab;
    @Bind(R.id.save_button)
    FloatingActionButton mStoreFab;
    @Bind(R.id.delete_button)
    FloatingActionButton mDeleteFab;
    @Bind(R.id.go_to_edit_button)
    FloatingActionButton mEditFab;
    @Bind(R.id.go_to_create_button)
    FloatingActionButton mCreateFab;

    private long mCurrentMemoId;
    private MemoListAdapter mMemoListAdapter;

    private DrawerViewModel mDrawerViewModel;
    private FloatingActionViewModel mFloatingActionViewModel;

    private ViewMemoHandler mViewMemoHandler;

    @IntDef({MEMO_NEW, MEMO_EXISTING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface MemoState {}

    /**
     * Fragmentを取り出す。
     *
     * @param memoId メモID
     */
    private void popBackStackToViewMemoFragment(long memoId) {
        mCurrentMemoId = memoId;
        mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
        mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
        getFragmentManager().popBackStack();
    }

    /**
     * メモ作成画面に遷移する。
     *
     * @param state メモの状態
     */
    private void showMemoFragment(@MemoState int state) {
        Memo memo = state == MEMO_NEW ? new Memo() : mMemoHelper.find(mCurrentMemoId);
        MemoFragment fragment = MemoFragment.newInstance(memo);
        replaceFragment(R.id.container, fragment, MemoFragment.TAG);
        mFloatingActionViewModel.stateMemoFragment(state == MEMO_NEW);
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * メモ保存ボタン押下時のコールバック。
     *
     * @param state メモの状態
     */
    private void onClickStoreButton(@MemoState int state) {
        MemoHandler memoHandler = (MemoFragment) getSupportFragmentManager().findFragmentByTag(MemoFragment.TAG);
        Memo memo = memoHandler.saveMemo(state == MEMO_NEW ? new Memo() : mMemoHelper.find(mCurrentMemoId));
        popBackStackToViewMemoFragment(memo.getId());
        updateAppWidget();
    }

    /**
     * 新規作成ボタン押下時のコールバック。
     */
    @OnClick(R.id.go_to_create_button)
    void onClickCreateButton() {
        showMemoFragment(MEMO_NEW);
    }

    /**
     * 編集ボタン押下時のコールバック。
     */
    @OnClick(R.id.go_to_edit_button)
    void onClickEditButton() {
        showMemoFragment(MEMO_EXISTING);
    }

    /**
     * 削除ボタン押下時のコールバック。
     */
    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(this, R.string.check_delete_message, (dialog, which) -> {
            Memo deletedMemo = mMemoHelper.find(mCurrentMemoId);
            mMemoHelper.delete(NavigationActivity.this, deletedMemo);
            mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
            mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);

            if (mViewMemoHandler == null) {
                mViewMemoHandler = (ViewMemoFragment) getSupportFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
            }
            mViewMemoHandler.onClickedDeleteButton(mMemoHelper.findAll());
            updateAppWidget();
        });
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * 新規作成画面のFAB押下時のコールバック。
     */
    @OnClick(R.id.save_button_in_create_view)
    void onClickStoreMemoInCreateViewButton() {
        onClickStoreButton(MEMO_NEW);
    }

    /**
     * 保存ボタン押下時のコールバック。
     */
    @OnClick(R.id.save_button)
    void onClickStoreMemoButton() {
        onClickStoreButton(MEMO_EXISTING);
    }

    /**
     * 通知設定ボタン押下時のコールバック。
     */
    @OnClick(R.id.go_to_alarm_setting_button)
    void onClickSetAlertButton() {
        startActivityForResult(
                AlarmSettingActivity.createIntent(this, mMemoHelper.find(mCurrentMemoId)),
                AlarmSettingActivity.SET_ALARM_ACTIVITY
        );
        mFloatingActionViewModel.collapse();
    }

    /**
     * ナビゲーションドロワー内の新規作成ボタン押下時のコールバック。
     */
    @OnClick(R.id.drawer_create_memo)
    void onClickDrawerCreateMemo() {
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) == null) {
            showMemoFragment(MEMO_NEW);
        } else {
            mFloatingActionMenu.collapse();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * メモリストのアイテム押下時のコールバック。
     *
     * @param parent   親View
     * @param position 位置
     */
    @OnItemClick(R.id.memo_list)
    void onClickItemMemoList(AdapterView<?> parent, int position) {
        // メモの位置をプリファレンスに保存
        savePositionToSharedPreferences(position);

        // 現在のメモIDを取得
        mCurrentMemoId = ((Memo) parent.getItemAtPosition(position)).getId();

        // メモ作成画面の場合
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) != null) {
            getFragmentManager().popBackStack();
        }

        ViewMemoFragment viewMemoFragment = (ViewMemoFragment) getSupportFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
        if (viewMemoFragment != null) {
            if (mViewMemoHandler == null) {
                mViewMemoHandler = viewMemoFragment;
            }
            mViewMemoHandler.onClickedItemMemoList(mMemoHelper.find(mCurrentMemoId));
        }

        mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
        mFloatingActionMenu.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        bindView();
        mToolbarHelper.init(this, toolbar, R.string.read_view, false, true);

        // メモを全件取得
        List<Memo> memos = mMemoHelper.exists() ? mMemoHelper.findAll() : new ArrayList<>();
        mMemoListAdapter = new MemoListAdapter(this, memos);
        mListView.setAdapter(mMemoListAdapter);

        Intent intent = getIntent();
        ViewMemoFragment viewMemoFragment;
        if (mMemoHelper.exists()) {
            // Notificationから得られたメモを取得
            Memo memo = mMemoHelper.find(intent.getLongExtra(Memo.ID, 0L));
            if (memo == null) {
                // プリファレンスからメモの位置を取得
                int position = getPosition();
                if (position >= memos.size()) {
                    position = memos.size() - 1;
                }

                // 位置からメモを取得
                memo = memos.get(position);
            }
            mCurrentMemoId = memo.getId();
            viewMemoFragment = ViewMemoFragment.newInstance(memo);
        } else {
            viewMemoFragment = ViewMemoFragment.newInstance();
        }

        if (savedInstanceState == null) {
            addFragment(R.id.container, viewMemoFragment, ViewMemoFragment.TAG);
            if (intent.getBooleanExtra(MemoFragment.TAG, false)) {
                showMemoFragment(MEMO_NEW);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 時刻に応じて、ナビゲーションドロワー内のヘッダーの背景を変える
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlarmSettingActivity.SET_ALARM_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                // 通知時間が設定されたメモを取得
                Memo settingMemo = (Memo) data.getSerializableExtra(Memo.TAG);
                mMemoHelper.update(settingMemo);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) != null) {
            getFragmentManager().popBackStack();
            mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
            mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    /**
     * ViewModelにViewをバインドする。
     */
    private void bindView() {
        if (mDrawerViewModel == null) {
            mDrawerViewModel = new DrawerViewModel();
            mDrawerViewModel.setDrawerLayout(drawerLayout);
            mDrawerViewModel.setDrawer(drawer);
            mDrawerViewModel.setHeaderLayout(drawerHeaderRelativeLayout);
            mDrawerViewModel.setMemoListView(mListView);
            mDrawerViewModel.setMemoListEmptyLayout((RelativeLayout) getLayoutInflater().inflate(R.layout.drawer_empty, null));
        }

        if (mFloatingActionViewModel == null) {
            mFloatingActionViewModel = new FloatingActionViewModel();
            mFloatingActionViewModel.setFloatingActionMenu(mFloatingActionMenu);
            mFloatingActionViewModel.setStoreInCreateViewFab(mStoreInCreateViewFab);
            mFloatingActionViewModel.setAlertFab(mAlertFab);
            mFloatingActionViewModel.setStoreFab(mStoreFab);
            mFloatingActionViewModel.setDeleteFab(mDeleteFab);
            mFloatingActionViewModel.setEditFab(mEditFab);
            mFloatingActionViewModel.setCreateFab(mCreateFab);
        }
        mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());

        drawerSearchMemoEditText.setOnKeyListener((v, code, e) -> {
            if ((e.getAction() == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER)) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                drawerSearchMemoEditText.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN
                        );

                return true;
            }
            return false;
        });

        drawerSearchMemoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });
    }

    /**
     * メモ本文、件名で部分一致でメモを検索してリストに適用する。
     */
    private void search(String target) {
        List<Memo> searchedMemos = mMemoHelper.findByMemoOrSubject(target);
        mMemoListAdapter.setMemos(searchedMemos);
        mDrawerViewModel.modify(!searchedMemos.isEmpty());
    }

    /**
     * Widgetを更新する。
     */
    private void updateAppWidget() {
        Intent intent = new Intent(getApplicationContext(), OneMemoWidgetProvider.class);
        intent.setAction(OneMemoWidgetProvider.ACTION_UPDATE);
        try {
            PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * メモの位置をSharedPreferencesに保存する。
     *
     * @param position メモの位置
     */
    private void savePositionToSharedPreferences(int position) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SHARED_PREFERENCES_MEMO_POSITION_KEY, position);
        editor.apply();
    }

    /**
     * メモの位置をSharedPreferencesから取得する。
     *
     * @return メモの位置
     */
    private int getPosition() {
        return getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE)
                .getInt(SHARED_PREFERENCES_MEMO_POSITION_KEY, 0);
    }

    @Override
    public void onClicked() {
        mDrawerViewModel.getDrawerLayout().openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onMenuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_setting:
                N2ttsUtil.settingReadVoice(this);
                break;
        }
        return true;
    }

    @Override
    public void modifyDrawerView() {
        mDrawerViewModel.modify(false);
    }

    @Override
    public long getSelectedMemoId(Bundle bundle) {
        long id;
        if (bundle != null) {
            id = mCurrentMemoId;
        } else if (mMemoHelper.exists()) {
            id = Stream.of(mMemoHelper.findAll())
                    .sorted((o1, o2) -> (int) (o2.getId() - o1.getId()))
                    .collect(Collectors.toList()).get(0).getId();
        } else {
            id = 0L;
        }
        return id;
    }
}
