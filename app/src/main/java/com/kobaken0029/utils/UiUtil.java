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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme))
                        .setMessage(context.getString(msgId))
                        .setPositiveButton(context.getResources().getString(R.string.yes),
                                listener != null ? listener : new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme))
                        .setMessage(msg)
                        .setPositiveButton(context.getResources().getString(R.string.yes),
                                listener != null ? listener : new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    /**
     * トーストを表示させる。
     *
     * @param context コンテキスト
     * @param msgId   メッセージID
     */
    public static void showToast(final Context context, final int msgId) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, context.getResources().getString(msgId),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * トーストを表示させる。
     *
     * @param context コンテキスト
     * @param msg     メッセージ
     */
    public static void showToast(final Context context, final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
