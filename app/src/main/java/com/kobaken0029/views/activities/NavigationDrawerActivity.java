package com.kobaken0029.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
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
    public Long currentMemoId;

    private void replaceMemoFragment(Bundle bundle, boolean newMemo) {
        MemoFragment f = new MemoFragment();
        f.setArguments(bundle);
        replaceFragment(R.id.container, f, MemoFragment.TAG);
        mFloatingActionViewModel.stateMemoFragment(newMemo);
        mFloatingActionViewModel.collapse();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void popBackStackToViewMemoFragment(Memo memo) {
        currentMemoId = memo.getId();
        getFragmentManager().popBackStack();

        ((ViewMemoFragment) getFragmentManager().findFragmentByTag(ViewMemoFragment.TAG))
                .getViewMemoViewModel()
                .setMemoView(mMemoHelper.find(currentMemoId), mMemoHelper.isEmpty(mMemoHelper.find(currentMemoId)));

        mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
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
        bundle.putSerializable(Memo.TAG, mMemoHelper.find(currentMemoId));
        replaceMemoFragment(bundle, false);
    }

    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(this, R.string.check_delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Memo target = mMemoHelper.find(currentMemoId);
                mMemoHelper.delete(NavigationDrawerActivity.this, target);
                mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
                ViewMemoFragment f = (ViewMemoFragment) getFragmentManager().findFragmentByTag(ViewMemoFragment.TAG);
                if (f != null) {
                    mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
                    f.getViewMemoViewModel().setMemoView(target, false);
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
        intent.putExtra(Memo.TAG, mMemoHelper.find(currentMemoId));
        startActivityForResult(intent, SetAlarmActivity.SET_ALARM_ACTIVITY);
        mFloatingActionViewModel.collapse();
    }

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
            mMemoHelper.update(NavigationDrawerActivity.this, memo);
        }

        popBackStackToViewMemoFragment(memo);
    }

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

    @OnItemClick(R.id.memo_list)
    void onClickItemMemoList(AdapterView<?> parent, int position) {
        currentMemoId = ((Memo) parent.getItemAtPosition(position)).getId();

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
        setContentView(R.layout.activity_navigation_drawer);
        ButterKnife.bind(this);
        bindView();
        mToolbarHelper.init(this, toolbar, R.string.read_view, false, true);

        List<Memo> memos = mMemoHelper.findAll();
        boolean notExists = !mMemoHelper.exists();
        if (notExists) {
            memos = new ArrayList<>();
        }
        mMemoListAdapter = new MemoListAdapter(this, memos);
        mListView.setAdapter(mMemoListAdapter);

        if (savedInstanceState == null) {
            mFloatingActionViewModel.stateViewMemoFragment(notExists);

            ViewMemoFragment f = new ViewMemoFragment();
            if (mMemoHelper.exists()) {
                Memo memo = memos.get(0);
                currentMemoId = memo.getId();

                Bundle bundle = new Bundle();
                bundle.putSerializable(Memo.TAG, memo);
                f.setArguments(bundle);
            }
            addFragment(R.id.container, f, ViewMemoFragment.TAG);
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
        if (getFragmentManager().findFragmentByTag(MemoFragment.TAG) != null) {
            getFragmentManager().popBackStack();
            mMemoHelper.loadMemos(mMemoListAdapter, mDrawerViewModel);
            mFloatingActionViewModel.stateViewMemoFragment(!mMemoHelper.exists());
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

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
    }

    public DrawerViewModel getDrawerViewModel() {
        return mDrawerViewModel;
    }

    public FloatingActionViewModel getFloatingActionViewModel() {
        return mFloatingActionViewModel;
    }
}
