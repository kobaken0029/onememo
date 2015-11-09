package com.pliseproject.views.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    @Bind(R.id.subject_textView)
    TextView subjectView;
    @Bind(R.id.memo_textView)
    TextView memoView;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu mFloatingActionsMenu;

    private ListView mListView;

    private void replaceMemoFragment(Bundle bundle) {
        MemoFragment f = new MemoFragment();
        f.setArguments(bundle);
        ((BaseActivity) getActivity()).replaceFragment(R.id.container, f, MemoFragment.class.getName());

        mFloatingActionsMenu.collapse();
    }

    @OnClick(R.id.create_button)
    void onClickCreateButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.class.getName(), new Memo());
        replaceMemoFragment(bundle);
    }

    @OnClick(R.id.edit_button)
    void onClickEditButton() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.class.getName(), mMemoHelper.getCurrentMemo());
        replaceMemoFragment(bundle);
    }

    @OnClick(R.id.delete_button)
    void onClickDeleteButton() {
        UiUtil.showDialog(getActivity(), R.string.check_delete_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMemoHelper.delete(getActivity(), mMemoHelper.getCurrentMemo());
                loadMemos();
                loadMemo();
            }
        });
        mFloatingActionsMenu.collapse();
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
        if (savedInstanceState == null) {
            initialize();
        }

        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mMemoHelper.setCurrentMemo((Memo) parent.getItemAtPosition(position));

                        if (getFragmentManager().findFragmentByTag(MemoFragment.class.getName()) != null) {
                            getFragmentManager().popBackStack();
                        } else {
                            loadMemo();
                        }

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
        mListView = ((NavigationDrawerActivity) getActivity()).getMemoListView();
        mListView.setAdapter(((NavigationDrawerActivity) getActivity()).getMemoListAdapter());
    }

    /**
     * メモ群を読み込む。
     */
    protected void loadMemos() {
        MemoListAdapter adapter = ((NavigationDrawerActivity) getActivity()).getMemoListAdapter();
        adapter.setMemos(mMemoHelper.findAll());
        adapter.notifyDataSetChanged();
        LinearLayout view = ((NavigationDrawerActivity) getActivity()).getDrawer();
        View emptyRelativeLayout = ((NavigationDrawerActivity) getActivity()).getEmptyRelativeLayout();
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

        if (mMemoHelper.findAll().size() > 0) {
            memo = memo.getId() == null
                    ? (Memo) mListView.getItemAtPosition(0)
                    : memo;
            findById(getActivity(), R.id.delete_button).setVisibility(View.VISIBLE);
            findById(getActivity(), R.id.edit_button).setVisibility(View.VISIBLE);
        } else {
            findById(getActivity(), R.id.delete_button).setVisibility(View.GONE);
            findById(getActivity(), R.id.edit_button).setVisibility(View.GONE);
        }

        // 画面に値をセット
        subjectView.setText(memo.getId() == null ? "" : memo.getSubject());
        memoView.setText(memo.getId() == null ? "" : memo.getMemo());
    }
}
