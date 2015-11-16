package com.kobaken0029.views.viewmodels;

import android.widget.EditText;

import com.kobaken0029.models.Memo;

public class MemoViewModel {
    private Long memoId;
    private EditText subjectEditText;
    private EditText memoEditText;

    public EditText getSubjectEditText() {
        return subjectEditText;
    }

    public void setMemoView(Memo memo, boolean exist) {
        if (exist) {
            memoId = memo.getId();
            subjectEditText.setText(memo.getSubject());
            memoEditText.setText(memo.getMemo());
        } else {
            subjectEditText.setText("");
            memoEditText.setText("");
        }
    }

    public Long getMemoId() {
        return memoId;
    }

    public void setMemoId(Long memoId) {
        this.memoId = memoId;
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
