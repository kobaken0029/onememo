package com.kobaken0029.interfaces;

import com.kobaken0029.models.Memo;

public interface AlarmSettingHandler {
    void setPostedMemo(Memo target);
    void saveSetting();
}
