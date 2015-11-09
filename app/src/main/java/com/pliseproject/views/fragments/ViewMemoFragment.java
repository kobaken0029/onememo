package com.pliseproject.views.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.pliseproject.R;
import com.pliseproject.utils.UiUtil;
import com.pliseproject.models.Memo;
import com.pliseproject.views.activities.BaseActivity;
import com.pliseproject.views.activities.NavigationDrawerActivity;
import com.pliseproject.views.adapters.MemoListAdapter;

import static butterknife.ButterKnife.findById;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class ViewMemoFragment extends TextToSpeechFragment {
    private RelativeLayout emptyRelativeLayout;

    private MemoListAdapter mMemoListAdapter;


    @Bind(R.id.subject_textView)
    TextView subjectView;
    @Bind(R.id.memo_textView)
    TextView memoView;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu mFloatingActionsMenu;

    private ListView mListView;

    @OnClick(R.id.create_button)
    void onClickCreateButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.class.getName(), new Memo());
        MemoFragment f = new MemoFragment();
        f.setArguments(bundle);
        ((BaseActivity) getActivity()).replaceFragment(R.id.container, f);

        mFloatingActionsMenu.setVisibility(View.GONE);
        mFloatingActionsMenu.collapse();
    }

    @OnClick(R.id.edit_button)
    void onClickEditButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.class.getName(), mMemoHelper.getCurrentMemo());
        MemoFragment f = new MemoFragment();
        f.setArguments(bundle);
        ((BaseActivity) getActivity()).replaceFragment(R.id.container, f);

        mFloatingActionsMenu.setVisibility(View.GONE);
        mFloatingActionsMenu.collapse();
    }

    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(getActivity(), R.string.check_delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMemoHelper.delete(getActivity(), mMemoHelper.getCurrentMemo());
                mMemoListAdapter.setMemos(mMemoHelper.findAll());
                mMemoListAdapter.notifyDataSetChanged();
            }
        });
        mFloatingActionsMenu.collapse();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        emptyRelativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.drawer_empty, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_memo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mMemoHelper.setCurrentMemo((Memo) parent.getItemAtPosition(position));
                        loadMemo();
                        ((NavigationDrawerActivity) getActivity()).getDrawerLayout().closeDrawer(GravityCompat.START);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMemos();
        loadMemo();
    }

    private void initialize() {
        mMemoListAdapter = new MemoListAdapter(getActivity(), mMemoHelper.findAll());
        mListView = findById(getActivity(), android.R.id.list);
        mListView.setAdapter(mMemoListAdapter);
    }

    /**
     * メモ群を読み込む。
     */
    protected void loadMemos() {
        mMemoListAdapter.setMemos(mMemoHelper.findAll());
        mMemoListAdapter.notifyDataSetChanged();
        LinearLayout view = findById(getActivity(), R.id.drawer);
        view.removeView(emptyRelativeLayout);
        view.removeView(mListView);

        if (mListView.getCount() == 0) {
            view.addView(emptyRelativeLayout);
        } else {
            view.addView(mListView);
        }
    }

    /**
     * メモを読み込む。
     */
    private void loadMemo() {
        Memo memo = mMemoHelper.getCurrentMemo();

        Bundle bundle = getArguments();
        if (bundle != null) {
            memo = (Memo) bundle.getSerializable(Memo.class.getName());
        }

        if (mMemoHelper.findAll().size() > 0) {
            memo = memo == null
                    ? (Memo) mListView.getItemAtPosition(0)
                    : memo;
            findById(getActivity(), R.id.delete_button).setVisibility(View.VISIBLE);
            findById(getActivity(), R.id.edit_button).setVisibility(View.VISIBLE);
        } else {
            findById(getActivity(), R.id.delete_button).setVisibility(View.GONE);
            findById(getActivity(), R.id.edit_button).setVisibility(View.GONE);
        }

        // 画面に値をセット
        subjectView.setText(memo == null ? "" : memo.getSubject());
        memoView.setText(memo == null ? "" : memo.getMemo());
    }
}
