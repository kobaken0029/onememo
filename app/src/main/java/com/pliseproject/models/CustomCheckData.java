package com.pliseproject.models;

/**
 * カスタムクラス。
 */
public class CustomCheckData {
    private String text;
    private boolean checkFlag;

    /**
     * コンストラクタ。
     *
     * @param text      テキスト
     * @param checkFlag チェックボックス真偽
     */
    public CustomCheckData(String text, boolean checkFlag) {
        this.setText(text);
        this.setCheckFlag(checkFlag);
    }

    /**
     * リストのテキストを取得します。
     *
     * @return テキスト
     */
    public String getText() {
        return text;
    }

    /**
     * リストのテキストをセットします。
     *
     * @param text テキスト
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * チェックボックスの真偽を取得します。
     *
     * @return 真偽
     */
    public boolean isCheckFlag() {
        return checkFlag;
    }

    /**
     * チェックボックスの真偽をセットします。
     */
    public void setCheckFlag(boolean checkFlag) {
        this.checkFlag = checkFlag;
    }
}