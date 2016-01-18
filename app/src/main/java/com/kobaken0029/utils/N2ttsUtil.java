package com.kobaken0029.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.kobaken0029.R;

/**
 * N2TTSに関するUtil。
 */
public class N2ttsUtil {
    /** パッケージ名。 */
    public static final String N2TTS_PACKAGE_NAME = "jp.kddilabs.n2tts";

    /** クラス名。 */
    public static final String N2TTS_CLASS_NAME = ".TtsServiceSettings";

    /** アクション名。 */
    public static final String N2TTS_ACTION = "android.intent.category.LAUNCHER";

    /** Google playのURL。 */
    public static final String N2TTS_URI_GOOGLE_PLAY_STORE_JA = "https://play.google.com/store/apps/details?id=jp.kddilabs.n2tts&hl=ja";

    /** フラグ。 */
    public static final int N2TTS_FLAGS = 0x10000000;

    /**
     * コンストラクタ。
     */
    private N2ttsUtil() {
    }

    /**
     * パッケージの有無をチェックします。
     *
     * @param packageName パッケージ名
     * @return 存在した場合true
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
     *
     * @param mContext コンテキスト
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
                            (dialog, which) -> {
                                Uri uri = Uri.parse(N2TTS_URI_GOOGLE_PLAY_STORE_JA);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                mContext.startActivity(intent);
                            })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), null).show();
        }
    }
}
