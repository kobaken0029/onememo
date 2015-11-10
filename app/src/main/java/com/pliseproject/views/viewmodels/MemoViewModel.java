package com.pliseproject.views.viewmodels;

import android.widget.EditText;

public class MemoViewModel {
    EditText subjectEditText;
    EditText memoEditText;

    public EditText getSubjectEditText() {
        return subjectEditText;
    }

    public void setSubjectEditText(EditText subjectEditText) {
        this.subjectEditText = subjectEditText;
    }

    public EditText getMemoEditText() {
        return memoEditText;
    }

    public void setMemoEditText(EditText memoEditText) {
        this.memoEditText = memoEditText;
    }

}
