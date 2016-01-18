package com.kobaken0029.views.viewmodels;

import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * FABのViewModel。
 */
public class FloatingActionViewModel {
    private FloatingActionsMenu floatingActionMenu;
    private FloatingActionButton storeInCreateViewFab;
    private FloatingActionButton alertFab;
    private FloatingActionButton storeFab;
    private FloatingActionButton deleteFab;
    private FloatingActionButton editFab;
    private FloatingActionButton createFab;

    /**
     * FAMを閉じる。
     */
    public void collapse() {
        floatingActionMenu.collapse();
    }

    /**
     * 閲覧画面の状態にする。
     *
     * @param memoEmpty メモが空の場合true
     */
    public void stateViewMemoFragment(boolean memoEmpty) {
        floatingActionMenu.setVisibility(View.VISIBLE);
        storeInCreateViewFab.setVisibility(View.GONE);
        alertFab.setVisibility(memoEmpty ? View.GONE : View.VISIBLE);
        storeFab.setVisibility(View.GONE);
        deleteFab.setVisibility(memoEmpty ? View.GONE : View.VISIBLE);
        editFab.setVisibility(memoEmpty ? View.GONE : View.VISIBLE);
        createFab.setVisibility(View.VISIBLE);
    }

    /**
     * メモ作成・編集画面の状態にする。
     *
     * @param newMemo 新規メモの場合true
     */
    public void stateMemoFragment(boolean newMemo) {
        floatingActionMenu.setVisibility(newMemo ? View.GONE : View.VISIBLE);
        storeInCreateViewFab.setVisibility(newMemo ? View.VISIBLE : View.GONE);
        alertFab.setVisibility(View.VISIBLE);
        storeFab.setVisibility(View.VISIBLE);
        deleteFab.setVisibility(View.GONE);
        editFab.setVisibility(View.GONE);
        createFab.setVisibility(View.GONE);
    }

    public FloatingActionsMenu getFloatingActionMenu() {
        return floatingActionMenu;
    }

    public void setFloatingActionMenu(FloatingActionsMenu floatingActionMenu) {
        this.floatingActionMenu = floatingActionMenu;
    }

    public FloatingActionButton getStoreInCreateViewFab() {
        return storeInCreateViewFab;
    }

    public void setStoreInCreateViewFab(FloatingActionButton storeInCreateViewFab) {
        this.storeInCreateViewFab = storeInCreateViewFab;
    }

    public FloatingActionButton getAlertFab() {
        return alertFab;
    }

    public void setAlertFab(FloatingActionButton alertFab) {
        this.alertFab = alertFab;
    }

    public FloatingActionButton getStoreFab() {
        return storeFab;
    }

    public void setStoreFab(FloatingActionButton storeFab) {
        this.storeFab = storeFab;
    }

    public FloatingActionButton getDeleteFab() {
        return deleteFab;
    }

    public void setDeleteFab(FloatingActionButton deleteFab) {
        this.deleteFab = deleteFab;
    }

    public FloatingActionButton getEditFab() {
        return editFab;
    }

    public void setEditFab(FloatingActionButton editFab) {
        this.editFab = editFab;
    }

    public FloatingActionButton getCreateFab() {
        return createFab;
    }

    public void setCreateFab(FloatingActionButton createFab) {
        this.createFab = createFab;
    }
}
