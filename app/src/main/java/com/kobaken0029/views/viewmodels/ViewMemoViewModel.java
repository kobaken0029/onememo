package com.kobaken0029.views.viewmodels;

import android.widget.TextView;

import com.kobaken0029.models.Memo;

public class ViewMemoViewModel {
    private Long memoId;
    private TextView subjectView;
    private TextView memoView;

    public boolean stateChanged(Memo memo) {
        return !memo.getId().equals(memoId)
                || !memo.getSubject().equals(subjectView.getText().toString())
                || !memo.getMemo().equals(memoView.getText().toString());
    }

    public void setMemoView(Memo memo, boolean exist) {
        if (exist) {
            memoId = memo.getId();
            subjectView.setText(memo.getSubject());
            memoView.setText(memo.getMemo());
        } else {
            subjectView.setText("");
            memoView.setText("");
        }
    }

    public Long getMemoId() {
        return memoId;
    }

    public void setMemoId(Long memoId) {
        this.memoId = memoId;
    }

    public TextView getSubjectView() {
        return subjectView;
    }

    public void setSubjectView(TextView subjectView) {
        this.subjectView = subjectView;
    }

    public TextView getMemoView() {
        return memoView;
    }

    public void setMemoView(TextView memoView) {
        this.memoView = memoView;
    }
}
