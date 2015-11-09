package com.pliseproject.models;

/**
 * カスタムクラス。
 */
public class CustomCheckData {
    private String text;
    private boolean checkFlag;

    public CustomCheckData() {
    }

    public CustomCheckData(String text, boolean checkFlag) {
        this.text = text;
        this.checkFlag = checkFlag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(boolean checkFlag) {
        this.checkFlag = checkFlag;
    }
}