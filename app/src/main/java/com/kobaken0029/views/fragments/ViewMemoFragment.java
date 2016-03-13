package com.kobaken0029.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.interfaces.ViewMemoHandler;
import com.kobaken0029.models.Memo;
import com.kobaken0029.views.activities.BaseActivity;
import com.kobaken0029.views.viewmodels.ViewMemoViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

/**
 * メモを閲覧するFragment。
 */
public class ViewMemoFragment extends TextToSpeechFragment implements ViewMemoHandler {
    /** タグ。 */
    public static final String TAG = ViewMemoFragment.class.getName();

    @Bind(R.id.subject_textView)
    TextView subjectView;
    @Bind(R.id.memo_textView)
    TextView memoView;

    private ViewMemoViewModel mViewMemoViewModel;

    private NavigationActivityHandler handler;

    public interface NavigationActivityHandler {
        void modifyDrawerView();
        long getSelectedMemoId(Bundle bundle);
    }

    /**
     * インスタンス生成。
     *
     * @return ViewMemoFragmentのインスタンス
     */
    public static ViewMemoFragment newInstance() {
        return newInstance(null);
    }

    /**
     * インスタンス生成。
     *
     * @param memo メモ
     * @return ViewMemoFragmentのインスタンス
     */
    public static ViewMemoFragment newInstance(Memo memo) {
        ViewMemoFragment fragment = new ViewMemoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Memo.TAG, memo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R.id.memomiya)
    void onClickMemomiya() {
        if (mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }

        Memo memo = mMemoHelper.find(mViewMemoViewModel.getMemoId());
        if (memo != null) {
            StringBuilder str = new StringBuilder();
            str.append("");
            if (!TextUtils.isEmpty(memo.getSubject())) {
                str.append(memo.getSubject());
                str.append("。");
            }
            if (!TextUtils.isEmpty(memo.getMemo())) {
                str.append(memo.getMemo());
            }
            ttsSpeak(str.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_memo, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!(getActivity() instanceof NavigationActivityHandler)) {
            throw new ClassCastException();
        }
        handler = (NavigationActivityHandler) getActivity();
        mViewMemoViewModel.setMemoId(handler.getSelectedMemoId(getArguments()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = findById(getActivity(), R.id.toolbar_menu);
        mToolbarHelper.change((BaseActivity) getActivity(), toolbar, R.string.read_view, false);

        Memo memo = mMemoHelper.find(mViewMemoViewModel.getMemoId());
        if (memo != null) {
            mViewMemoViewModel.updateMemoView(memo, !mMemoHelper.isEmpty(memo));
        } else {
            if (handler != null) {
                handler.modifyDrawerView();
            }
        }
    }

    @Override
    void bindView() {
        if (mViewMemoViewModel == null) {
            mViewMemoViewModel = new ViewMemoViewModel();
        }
        mViewMemoViewModel.setSubjectView(subjectView);
        mViewMemoViewModel.setMemoView(memoView);
    }

    @Override
    public void onClickedDeleteButton(List<Memo> memos) {
        if (!memos.isEmpty()) {
            Memo target = memos.get(0);
            mViewMemoViewModel.updateMemoView(target, true);
        } else {
            mViewMemoViewModel.updateMemoView(null, false);
        }
    }

    @Override
    public void onClickedItemMemoList(Memo memo) {
        mViewMemoViewModel.updateMemoView(memo, true);
    }
}
