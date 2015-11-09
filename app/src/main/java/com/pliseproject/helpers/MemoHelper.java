package com.pliseproject.helpers;

import android.content.Context;

import com.pliseproject.models.Memo;

import java.util.List;

public interface MemoHelper {
    Memo find(long id);
    List<Memo> findAll();
    Memo create(String subject, String mainText);
    Memo update(Context mContext, Memo memo);
    void delete(Context mContext, Memo memo);
    Memo getCurrentMemo();
    void setCurrentMemo(Memo memo);
}
