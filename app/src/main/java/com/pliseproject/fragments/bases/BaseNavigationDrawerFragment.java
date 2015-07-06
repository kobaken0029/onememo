package com.pliseproject.fragments.bases;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.pliseproject.R;
import com.pliseproject.activities.bases.BaseNavigationDrawerActivity;
import com.pliseproject.activities.CreateMemoActivity;
import com.pliseproject.activities.SetAlarmActivity;
import com.pliseproject.activities.ViewMemoActivity;
import com.pliseproject.managers.AppController;
import com.pliseproject.managers.DBAdapter;
import com.pliseproject.models.Memo;
import com.pliseproject.utils.DateUtil;
import com.pliseproject.utils.UiUtil;

/**
 * Created by koba on 2015/05/31.
 */
public class BaseNavigationDrawerFragment extends Fragment {
    protected BaseNavigationDrawerActivity activity;
    protected AppController appController;
    protected Memo memo;

    private int deletedMemoId;
    private RelativeLayout emptyRelativeLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseNavigationDrawerActivity) activity;
        this.appController = (AppController) this.activity.getApplication();
        emptyRelativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.drawer_empty, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity instanceof ViewMemoActivity) {
            memo = (Memo) activity.getIntent().getSerializableExtra("memo");
        }

        activity.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ViewMemoActivity.class);
                intent.putExtra("memo", (Memo) parent.getItemAtPosition(position));
                if (activity.isModifiedFlg()) {
                    appController.showDialogBeforeMoveMemoView(activity, intent);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (activity instanceof SetAlarmActivity) {
                        activity.setResult(Activity.RESULT_CANCELED, intent);
                    } else {
                        startActivity(intent);
                    }

                    activity.finish();
                }
                activity.getDrawerLayout().closeDrawer(Gravity.START);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMemos();
    }

    /**
     * メモ群を読み込む。
     */
    protected void loadMemos() {
        activity.getMemos().clear();

        AppController.dbAdapter.open();
        Cursor c = AppController.dbAdapter.getAllMemos();
        if (c.moveToFirst()) {
            do {
                Memo memo = new Memo(c.getInt(c.getColumnIndex(DBAdapter.COL_ID)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_SUBJECT)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_MEMO)),
                        DateUtil.convertStringToCalendar(c.getString(c.getColumnIndex(DBAdapter.COL_POST_TIME))),
                        c.getInt(c.getColumnIndex(DBAdapter.COL_POST_FLG)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_CREATE_AT)),
                        c.getString(c.getColumnIndex(DBAdapter.COL_UPDATE_AT)));
                activity.getMemos().add(memo);
            } while (c.moveToNext());
        }
        AppController.dbAdapter.close();
        activity.getMemoListAdapter().notifyDataSetChanged();

        if (activity.getListView().getCount() == 0) {
            activity.getDrawerLinearLayout().removeView(emptyRelativeLayout);
            activity.getDrawerLinearLayout().removeView(activity.getListView());
            activity.getDrawerLinearLayout().addView(emptyRelativeLayout);
        } else {
            activity.getDrawerLinearLayout().removeView(emptyRelativeLayout);
            activity.getDrawerLinearLayout().removeView(activity.getListView());
            activity.getDrawerLinearLayout().addView(activity.getListView());
        }
    }

    public boolean onContextItemSelected(MenuItem item, DialogInterface.OnClickListener listener) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Memo selectedMemo = activity.getMemos().get(info.position);

        switch (item.getItemId()) {
            // メモを編集する
            case BaseNavigationDrawerActivity.MENU_ITEM_ID_EDIT:
                Intent intent = new Intent(activity, CreateMemoActivity.class);
                intent.putExtra("memo", selectedMemo);
                if (((activity instanceof CreateMemoActivity) && activity.getIntent().getSerializableExtra("memo") != null)
                        || activity instanceof SetAlarmActivity) {
                    if (activity.isModifiedFlg()) {
                        appController.showDialogBeforeMoveMemoView(activity, intent);
                    } else {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (activity instanceof SetAlarmActivity) {
                            activity.setResult(Activity.RESULT_CANCELED, intent);
                        } else {
                            startActivity(intent);
                        }

                        activity.finish();
                    }
                } else if (activity instanceof ViewMemoActivity) {
                    startActivity(intent);
                }
                activity.getDrawerLayout().closeDrawer(Gravity.START);
                break;

            // メモを削除する
            case BaseNavigationDrawerActivity.MENU_ITEM_ID_DELETE:
                UiUtil.showDialog(activity, R.string.check_delete_message,
                        listener != null ? listener : new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletedMemoId = selectedMemo.getId();
                                ((AppController) activity.getApplication()).deleteMemo(selectedMemo);
                                loadMemos();
                                if (memo.getId() == deletedMemoId
                                        && activity instanceof CreateMemoActivity) {
                                    activity.finish();
                                }
                            }
                        });
                break;
        }
        return true;
    }

    public int getDeletedMemoId() {
        return deletedMemoId;
    }

    public void setDeletedMemoId(int deletedMemoId) {
        this.deletedMemoId = deletedMemoId;
    }
}
