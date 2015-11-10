package com.pliseproject.helpers;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pliseproject.models.Memo;
import com.pliseproject.views.activities.NavigationDrawerActivity;
import com.pliseproject.views.adapters.MemoListAdapter;
import com.pliseproject.views.viewmodels.DrawerViewModel;
import com.pliseproject.views.viewmodels.ViewMemoViewModel;

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
