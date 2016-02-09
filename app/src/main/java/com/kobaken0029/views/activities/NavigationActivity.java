package com.kobaken0029.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.DateUtil;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.adapters.MemoListAdapter;
import com.kobaken0029.views.fragments.MemoFragment;
import com.kobaken0029.views.fragments.ViewMemoFragment;
import com.kobaken0029.views.viewmodels.DrawerViewModel;
import com.kobaken0029.views.viewmodels.FloatingActionViewModel;
import com.kobaken0029.views.viewmodels.MemoViewModel;
import com.kobaken0029.views.viewmodels.ViewMemoViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * NavigationViewが存在するActivity。
 */
public class NavigationActivity extends BaseActivity {
    /**
     * タグ。
     */
    public static final String TAG = NavigationActivity.class.getName();

    /**
     * プリファレンスID。
     */
    public static final String SHARED_PREFERENCES_ID = "memo_position";

    /**
     * プリファレンスKey。
     */
    public static final String SHARED_PREFERENCES_MEMO_POSITION_KEY = "position";

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
    @Bind(R.id.store_button_in_create_view)
    FloatingActionButton mStoreInCreateViewFab;
    @Bind(R.id.alert_button)
    FloatingActionButton mAlertFab;
    @Bind(R.id.store_button)
    FloatingActionButton mStoreFab;
    @Bind(R.id.delete_button)
    FloatingActionButton mDeleteFab;
    @Bind(R.id.edit_button)
    FloatingActionButton mEditFab;
    @Bind(R.id.create_button)
    FloatingActionButton mCreateFab;

    private DrawerViewModel mDrawerViewModel;
    private FloatingActionViewModel mFloatingActionViewModel;

    private MemoListAdapter mMemoListAdapter;
    public long currentMemoId;

    /**
     * Fragmentを置き換える。
     *
     * @param bundle  バンドル
     * @param newMemo 新規メモならtrue
     */
    private void replaceMemoFragment(Bundle bundle, boolean newMemo) {
        MemoFragment f = MemoFragment.newInstance();
        f.setArguments(bundle);
        replaceFragment(R.id.container, f, MemoFragment.TAG);
        mFloatingActionViewModel.stateMemoFragment(newMemo);
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Fragmentを取り出す。
     *
     * @param memoId メモID
     */
    private void popBackStackToViewMemoFragment(long memoId) {
        currentMemoId = memoId;
        mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
        mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
        getFragmentManager().popBackStack();
    }

    /**
     * 新規作成ボタン押下時のコールバック。
     */
    @OnClick(R.id.create_button)
    void onClickCreateButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.TAG, new Memo());
        replaceMemoFragment(bundle, true);
    }

    /**
     * 編集ボタン押下時のコールバック。
     */
    @OnClick(R.id.edit_button)
    void onClickEditButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.TAG, mMemoHelper.find(currentMemoId));
        replaceMemoFragment(bundle, false);
    }

    /**
     * 削除ボタン押下時のコールバック。
     */
    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(this, R.string.check_delete_message, (dialog, which) -> {
            Memo deletedMemo = mMemoHelper.find(currentMemoId);
            mMemoHelper.delete(NavigationActivity.this, deletedMemo);
            mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
            ViewMemoFragment f = (ViewMemoFragment) getFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
            if (f != null) {
                mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
                List<Memo> memos = mMemoHelper.findAll();
                ViewMemoViewModel viewModel = f.getViewMemoViewModel();
                if (!memos.isEmpty()) {
                    Memo target = memos.get(0);
                    viewModel.setMemoView(target, true);
                } else {
                    viewModel.setMemoView(null, false);
                }
            }
        });
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * 新規作成画面のFAB押下時のコールバック。
     */
    @OnClick(R.id.store_button_in_create_view)
    void onClickStoreMemoInCreateViewButton() {
        MemoViewModel viewModel = ((MemoFragment) getFragmentManager().findFragmentByTag(MemoFragment.TAG)).getMemoViewModel();
        Memo createdMemo = mMemoHelper.create(
                viewModel.getSubjectEditText().getText().toString(),
                viewModel.getMemoEditText().getText().toString());
        popBackStackToViewMemoFragment(createdMemo.getId());
    }

    /**
     * 通知設定ボタン押下時のコールバック。
     */
    @OnClick(R.id.alert_button)
    void onClickSetAlertButton() {
        Intent intent = new Intent(getApplicationContext(), SetAlarmActivity.class);
        intent.putExtra(Memo.TAG, mMemoHelper.find(currentMemoId));
        startActivityForResult(intent, SetAlarmActivity.SET_ALARM_ACTIVITY);
        mFloatingActionViewModel.collapse();
    }

    /**
     * 保存ボタン押下時のコールバック。
     */
    @OnClick(R.id.store_button)
    void onClickStoreMemoButton() {
        MemoFragment f = (MemoFragment) getFragmentManager().findFragmentByTag(MemoFragment.TAG);
        MemoViewModel viewModel = f.getMemoViewModel();

        Memo memo = mMemoHelper.find(currentMemoId);
        if (mMemoHelper.isEmpty(memo) || memo.getId() == f.getDeletedMemoId()) {
            memo = mMemoHelper.create(
                    viewModel.getSubjectEditText().getText().toString(),
                    viewModel.getMemoEditText().getText().toString());
        } else {
            memo.setSubject(viewModel.getSubjectEditText().getText().toString());
            memo.setMemo(viewModel.getMemoEditText().getText().toString());
            memo = mMemoHelper.update(memo);
        }

        popBackStackToViewMemoFragment(memo.getId());
    }

    /**
     * ナビゲーションドロワー内の新規作成ボタン押下時のコールバック。
     */
    @OnClick(R.id.drawer_create_memo)
    void onClickDrawerCreateMemo() {
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Memo.TAG, new Memo());
            replaceMemoFragment(bundle, true);
        } else {
            mFloatingActionMenu.collapse();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * ナビゲーションドロワー内の検索ボタン押下時のコールバック。
     */
    @OnClick(R.id.drawer_search_memo)
    void onClickDrawerSearchMemo() {
        search();
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
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SHARED_PREFERENCES_MEMO_POSITION_KEY, position);
        editor.commit();

        // 現在のメモIDを取得
        currentMemoId = ((Memo) parent.getItemAtPosition(position)).getId();

        // メモ作成画面の場合
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) != null) {
            getFragmentManager().popBackStack();
        }

        ViewMemoFragment f = (ViewMemoFragment) getFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
        if (f != null) {
            ViewMemoViewModel viewModel = f.getViewMemoViewModel();
            Memo memo = mMemoHelper.find(currentMemoId);
            viewModel.setMemoView(memo, true);
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

        mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());

        ViewMemoFragment f = ViewMemoFragment.newInstance();
        if (mMemoHelper.exists()) {
            // Notificationから得られたメモを取得
            Memo memo = mMemoHelper.find(getIntent().getLongExtra(Memo.ID, 0L));
            if (memo == null) {
                // プリファレンスを取得
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);

                // プリファレンスからメモの位置を取得
                int position = preferences.getInt(SHARED_PREFERENCES_MEMO_POSITION_KEY, 0);
                if (position >= memos.size()) {
                    position = memos.size() - 1;
                }

                // 位置からメモを取得
                memo = memos.get(position);
            }
            currentMemoId = memo.getId();

            Bundle bundle = new Bundle();
            bundle.putSerializable(Memo.TAG, memo);
            f.setArguments(bundle);
        }

        if (savedInstanceState == null) {
            addFragment(R.id.container, f, ViewMemoFragment.TAG);
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
        if (requestCode == SetAlarmActivity.SET_ALARM_ACTIVITY) {
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

        drawerSearchMemoEditText.setOnKeyListener((v, code, e) -> {
            if ((e.getAction() == KeyEvent.ACTION_DOWN) && (code == KeyEvent.KEYCODE_ENTER)) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                drawerSearchMemoEditText.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN
                        );

                search();

                return true;
            }
            return false;
        });
    }

    /**
     * メモ本文、件名で部分一致でメモを検索してリストに適用する。
     */
    private void search() {
        List<Memo> searchedMemos = mMemoHelper.findByMemoOrSubject(drawerSearchMemoEditText.getText().toString());
        mMemoListAdapter.setMemos(searchedMemos);
        mDrawerViewModel.modify(!searchedMemos.isEmpty());
    }

    public DrawerViewModel getDrawerViewModel() {
        return mDrawerViewModel;
    }

    public FloatingActionViewModel getFloatingActionViewModel() {
        return mFloatingActionViewModel;
    }
}
