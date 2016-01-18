package com.kobaken0029.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.kobaken0029.R;

/**
 * UIに関するUtil。
 */
public class UiUtil {
    /**
     * ダイアログを表示する。
     *
     * @param context  コンテキスト
     * @param msgId    メッセージID
     * @param listener クリックリスナー
     */
    public static void showDialog(final Context context, final int msgId,
                                  final DialogInterface.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(() ->
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme))
                        .setMessage(context.getString(msgId))
                        .setPositiveButton(context.getResources().getString(R.string.yes),
                                listener != null ? listener : (dialog, id) -> dialog.cancel())
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel())
                        .setCancelable(false)
                        .show());
    }

    /**
     * ダイアログを表示する。
     *
     * @param context  コンテキスト
     * @param msg      メッセージ
     * @param listener クリックリスナー
     */
    public static void showDialog(final Context context, final String msg,
                                  final DialogInterface.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(() ->
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme))
                        .setMessage(msg)
                        .setPositiveButton(context.getResources().getString(R.string.yes),
                                listener != null ? listener : (dialog, id) -> dialog.cancel())
                        .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel())
                        .setCancelable(false)
                        .show());
    }

    /**
     * トーストを表示させる。
     *
     * @param context コンテキスト
     * @param msgId   メッセージID
     */
    public static void showToast(final Context context, final int msgId) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, context.getResources().getString(msgId), Toast.LENGTH_SHORT).show());
    }

    /**
     * トーストを表示させる。
     *
     * @param context コンテキスト
     * @param msg     メッセージ
     */
    public static void showToast(final Context context, final String msg) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
    }
}
