package com.kobaken0029.interfaces;

import com.kobaken0029.models.Memo;

import java.util.List;

public interface ViewMemoHandler {
    void onClickedDeleteButton(List<Memo> memos);
    void onClickedItemMemoList(Memo memo);
}
