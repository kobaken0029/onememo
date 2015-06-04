package com.pliseproject.utils;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class packageUtil {
    public static final String N2TTS_PACKAGE_NAME = "jp.kddilabs.n2tts";
    public static final String N2TTS_PACKAGE_NOT_FOUND_MESSAGE = "音声出力エンジンのN2 TTSが見当たりません";

    /**
     * コンストラクタ。
     */
    private packageUtil() {
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
}
