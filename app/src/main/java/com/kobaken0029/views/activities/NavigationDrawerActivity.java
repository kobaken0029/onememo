package com.kobaken0029.views.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.DateUtil;
import com.kobaken0029.utils.PackageUtil;
import com.kobaken0029.utils.UiUtil;
import com.kobaken0029.views.adapters.MemoListAdapter;
import com.kobaken0029.views.fragments.MemoFragment;
import com.kobaken0029.views.fragments.ViewMemoFragment;
import com.kobaken0029.views.viewmodels.DrawerViewModel;
import com.kobaken0029.views.viewmodels.FloatingActionViewModel;
import com.kobaken0029.views.viewmodels.MemoViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static butterknife.ButterKnife.findById;

public class NavigationDrawerActivity extends BaseActivity {
    public static final String TAG = NavigationDrawerActivity.class.getName();

    @Bind(R.id.toolbar_menu)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_header_layout)
    RelativeLayout drawerHeaderRelativeLayout;
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

    private void replaceMemoFragment(Bundle bundle, boolean newMemo) {
        MemoFragment f = new MemoFragment();
        f.setArguments(bundle);
        replaceFragment(R.id.container, f, MemoFragment.TAG);
        mFloatingActionViewModel.stateMemoFragment(newMemo);
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void popBackStackToViewMemoFragment(Memo memo) {
        mMemoHelper.setCurrentMemo(memo);
        getFragmentManager().popBackStack();
        mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.create_button)
    void onClickCreateButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.TAG, new Memo());
        replaceMemoFragment(bundle, true);
    }

    @OnClick(R.id.edit_button)
    void onClickEditButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.TAG, mMemoHelper.getCurrentMemo());
        replaceMemoFragment(bundle, false);
    }

    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(this, R.string.check_delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMemoHelper.delete(NavigationDrawerActivity.this, mMemoHelper.getCurrentMemo());
                mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
                ViewMemoFragment f = (ViewMemoFragment) getFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
                if (f != null) {
                    f.refresh();
                    f.getViewMemoViewModel().reset();
                }
            }
        });
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.store_button_in_create_view)
    void onClickStoreMemoInCreateViewButton() {
        MemoViewModel viewModel = ((MemoFragment) getFragmentManager().findFragmentByTag(MemoFragment.TAG)).getMemoViewModel();
        popBackStackToViewMemoFragment(mMemoHelper.create(
                viewModel.getSubjectEditText().getText().toString(),
                viewModel.getMemoEditText().getText().toString()));
    }

    @OnClick(R.id.alert_button)
    void onClickSetAlertButton() {
        Intent intent = new Intent(this, SetAlarmActivity.class);
        intent.putExtra(Memo.TAG, mMemoHelper.getCurrentMemo());
        startActivityForResult(intent, SetAlarmActivity.SET_ALARM_ACTIVITY);
        mFloatingActionViewModel.collapse();
    }

    @OnClick(R.id.store_button)
    void onClickStoreMemoButton() {
        MemoFragment f = (MemoFragment) getFragmentManager().findFragmentByTag(MemoFragment.TAG);
        MemoViewModel viewModel = f.getMemoViewModel();

        Memo memo = mMemoHelper.getCurrentMemo();
        if (mMemoHelper.isMemoEmpty(memo) || memo.getId() == f.getDeletedMemoId()) {
            memo = mMemoHelper.create(
                    viewModel.getSubjectEditText().getText().toString(),
                    viewModel.getMemoEditText().getText().toString());
        } else {
            memo.setSubject(viewModel.getSubjectEditText().getText().toString());
            memo.setMemo(viewModel.getMemoEditText().getText().toString());
            mMemoHelper.update(NavigationDrawerActivity.this, memo);
        }
        popBackStackToViewMemoFragment(memo);
    }

    @OnClick(R.id.drawer_create_memo)
    void onClickDrawerCreateMemo() {
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) == null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Memo.TAG, new Memo());
            MemoFragment f = new MemoFragment();
            f.setArguments(bundle);
            replaceFragment(R.id.container, f, MemoFragment.class.getName());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @OnItemClick(R.id.memo_list)
    void onClickItemMemoList(AdapterView<?> parent, int position) {
        mMemoHelper.setCurrentMemo((Memo) parent.getItemAtPosition(position));

        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) != null) {
            getFragmentManager().popBackStack();
        } else {
            ViewMemoFragment f = (ViewMemoFragment) getFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
            if (f != null) {
                mMemoHelper.loadMemo(f.getViewMemoViewModel());
            }
        }

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
        bindView();
        mToolbarHelper.init(this, toolbar, R.string.read_view, false, true, mMenuItemClickListener);

        List<Memo> memos = mMemoHelper.findAll();
        if (memos == null) {
            memos = new ArrayList<>();
        }
        mMemoListAdapter = new MemoListAdapter(this, memos);
        mListView.setAdapter(mMemoListAdapter);

        if (savedInstanceState == null) {
            mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
            addFragment(R.id.container, new ViewMemoFragment(), ViewMemoFragment.class.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(findById(this, R.id.memo_list));

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
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(MemoFragment.class.getName()) != null) {
            getFragmentManager().popBackStack();
            mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void bindView() {
        mDrawerViewModel = new DrawerViewModel();
        mDrawerViewModel.setDrawerLayout(drawerLayout);
        mDrawerViewModel.setDrawer(drawer);
        mDrawerViewModel.setHeaderLayout(drawerHeaderRelativeLayout);
        mDrawerViewModel.setMemoListView(mListView);
        mDrawerViewModel.setMemoListEmptyLayout((RelativeLayout) getLayoutInflater().inflate(R.layout.drawer_empty, null));

        mFloatingActionViewModel = new FloatingActionViewModel();
        mFloatingActionViewModel.setFloatingActionMenu(mFloatingActionMenu);
        mFloatingActionViewModel.setStoreInCreateViewFab(mStoreInCreateViewFab);
        mFloatingActionViewModel.setAlertFab(mAlertFab);
        mFloatingActionViewModel.setStoreFab(mStoreFab);
        mFloatingActionViewModel.setDeleteFab(mDeleteFab);
        mFloatingActionViewModel.setEditFab(mEditFab);
        mFloatingActionViewModel.setCreateFab(mCreateFab);
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

    public MemoListAdapter getMemoListAdapter() {
        return mMemoListAdapter;
    }

    public DrawerViewModel getDrawerViewModel() {
        return mDrawerViewModel;
    }

    public FloatingActionViewModel getFloatingActionViewModel() {
        return mFloatingActionViewModel;
    }
}
