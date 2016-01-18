package com.kobaken0029.helpers;

import android.content.Context;

import com.kobaken0029.models.Memo;
import com.kobaken0029.views.adapters.MemoListAdapter;
import com.kobaken0029.views.viewmodels.DrawerViewModel;

import java.util.List;

/**
 * メモに関するヘルパークラス。
 */
public interface MemoHelper {
    /**
     * メモをIDから取得する。
     * @param id メモID
     * @return メモ
     */
    Memo find(long id);

    /**
     * メモを全件取得する。
     * @return すべてのメモ群
     */
    List<Memo> findAll();

    /**
     * メモを作成する。
     * @param subject 件名
     * @param mainText メモ本文
     * @return メモ
     */
    Memo create(String subject, String mainText);

    /**
     * メモを更新する。
     * @param mContext コンテキスト
     * @param memo 対象メモ
     * @return メモ
     */
    Memo update(Context mContext, Memo memo);

    /**
     * メモを削除する。
     * @param mContext コンテキスト
     * @param memo 対象メモ
     */
    void delete(Context mContext, Memo memo);

    /**
     * メモ群を読み込む。
     * @param adapter メモリストのアダプタ
     * @param viewModel ドロワー情報
     */
    void loadMemos(MemoListAdapter adapter, DrawerViewModel viewModel);

    /**
     * メモがからかどうか判定する。
     * @param memo 対象メモ
     * @return 空の場合true
     */
    boolean isEmpty(Memo memo);

    /**
     * メモが存在するかどうか判定する。
     * @return 存在する場合true
     */
    boolean exists();
}
