package com.kobaken0029.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.kobaken0029.R;

public class N2ttsUtil {
    public static final String N2TTS_PACKAGE_NAME = "jp.kddilabs.n2tts";
    public static final String N2TTS_CLASS_NAME = ".TtsServiceSettings";
    public static final String N2TTS_ACTION = "android.intent.category.LAUNCHER";
    public static final String N2TTS_URI_GOOGLE_PLAY_STORE_JA = "https://play.google.com/store/apps/details?id=jp.kddilabs.n2tts&hl=ja";
    public static final int N2TTS_FLAGS = 0x10000000;

    /**
     * コンストラクタ。
     */
    private N2ttsUtil() {
    }

    /**
     * packageの有無をチェックします。
     * @param packageName packageの名前
     * @return 存在した場合、true、
     */
    public static boolean packageCheck(String packageName, PackageManager manager) {
        try {
            manager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * 音声読上げ設定画面へ遷移します。
     */
    public static void settingReadVoice(final Context mContext) {
        if (N2ttsUtil.packageCheck(N2TTS_PACKAGE_NAME, mContext.getPackageManager())) {
            Intent n2tts = new Intent(Intent.ACTION_MAIN);
            n2tts.setAction(N2TTS_ACTION);
            n2tts.setClassName(N2TTS_PACKAGE_NAME, N2TTS_PACKAGE_NAME + N2TTS_CLASS_NAME);
            n2tts.setFlags(N2TTS_FLAGS);
            mContext.startActivity(n2tts);
        } else {
            new AlertDialog.Builder(mContext)
                    .setMessage(mContext.getString(R.string.n2tts_not_found_message))
                    .setPositiveButton(mContext.getResources().getString(R.string.go_play_stroe),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = Uri.parse(N2TTS_URI_GOOGLE_PLAY_STORE_JA);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    mContext.startActivity(intent);
                                }
                            })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), null).show();
        }
    }
}