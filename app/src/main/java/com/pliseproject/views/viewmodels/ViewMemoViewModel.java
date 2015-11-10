package com.pliseproject.views.viewmodels;

import android.widget.TextView;

public class ViewMemoViewModel {
    private TextView subjectView;
    private TextView memoView;

    public void reset() {
        subjectView.setText("");
        memoView.setText("");
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
