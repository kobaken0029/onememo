package com.kobaken0029.utils;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class N2ttsUtil {
    public static final String N2TTS_PACKAGE_NAME = "jp.kddilabs.n2tts";

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
}
