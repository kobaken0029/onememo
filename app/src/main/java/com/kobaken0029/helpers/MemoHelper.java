package com.kobaken0029.helpers;

import android.content.Context;

import com.kobaken0029.models.Memo;
import com.kobaken0029.views.adapters.MemoListAdapter;
import com.kobaken0029.views.viewmodels.DrawerViewModel;
import com.kobaken0029.views.viewmodels.ViewMemoViewModel;

import java.util.List;

public interface MemoHelper {
    Memo find(long id);
    List<Memo> findAll();
    Memo create(String subject, String mainText);
    Memo update(Context mContext, Memo memo);
    void delete(Context mContext, Memo memo);
    void loadMemos(MemoListAdapter adapter, DrawerViewModel viewModel);
    void loadMemo(ViewMemoViewModel viewModel);
    boolean isMemoEmpty(Memo memo);
    boolean exists();
    Memo getCurrentMemo();
    void setCurrentMemo(Memo memo);
}
