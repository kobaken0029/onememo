package com.pliseproject.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.pliseproject.R;
import com.pliseproject.activities.CreateMemoActivity;
import com.pliseproject.fragments.bases.BaseTextToSpeechFragment;
import com.pliseproject.managers.AppController;
import com.pliseproject.models.Memo;
import com.pliseproject.utils.UiUtil;

import static butterknife.ButterKnife.findById;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ViewMemoFragment extends BaseTextToSpeechFragment {
    private AppController appController;

    @InjectView(R.id.subject_textView)
    TextView subjectView;

    @InjectView(R.id.memo_textView)
    TextView memoView;

    @InjectView(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;

    @OnClick(R.id.edit_button)
    void onClickEditButton() {
        Intent intent = new Intent(activity, CreateMemoActivity.class);
        intent.putExtra("memo", memo);
        startActivity(intent);
        floatingActionsMenu.collapse();
    }

    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(activity, R.string.check_delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDeletedMemoId(memo.getId());
                appController.deleteMemo(memo);
                memo = null;
                loadMemos();
                loadMemo();
            }
        });
        floatingActionsMenu.collapse();
    }

    @OnClick(R.id.create_button)
    void onClickCreateButton() {
        activity.moveCreateMemoView();
        floatingActionsMenu.collapse();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appController = (AppController) activity.getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_memo, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity.getListView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        memo = (Memo) parent.getItemAtPosition(position);
                        subjectView.setText(memo.getSubject());
                        memoView.setText(memo.getMemo());
                        activity.getDrawerLayout().closeDrawer(Gravity.START);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMemo();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDeletedMemoId(ViewMemoFragment.this.memo.getId());
                ((AppController) activity.getApplication()).deleteMemo(memo);
                ViewMemoFragment.this.memo = null;
                loadMemos();
                loadMemo();
            }
        });
    }

    /**
     * メモを読み込む。
     */
    private void loadMemo() {
        if (activity.getListView().getCount() > 0) {
            if (memo == null || !activity.getMemos().contains(memo)) {
                memo = (Memo) activity.getListView().getItemAtPosition(0);
            } else {
                memo = appController.findMemo(memo.getId());
            }
            findById(activity, R.id.delete_button).setVisibility(View.VISIBLE);
            findById(activity, R.id.edit_button).setVisibility(View.VISIBLE);
        } else {
            memo = new Memo();
            memo.setSubject("あなたへ");
            memo.setMemo("メモ書いて！");

            findById(activity, R.id.delete_button).setVisibility(View.GONE);
            findById(activity, R.id.edit_button).setVisibility(View.GONE);
        }

        // 画面に値をセット
        subjectView.setText(memo.getSubject());
        memoView.setText(memo.getMemo());
    }
}
