package com.pliseproject.models;

import java.io.Serializable;
import java.util.Calendar;

/**
 * メモクラス。
 */
public class Memo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String subject;
    private String memo;
    private Calendar postTime;
    private int postFlg;
    private String createAt;
    private String updateAt;

    public Memo() {

    }

    /**
     * コンストラクタ。
     *
     * @param id       メモID
     * @param subject  件名
     * @param memo     メモ本文
     * @param postTime 通知時刻
     * @param postFlg  通知フラグ
     * @param createAt 作成日時
     * @param updateAt 更新日時
     */
    public Memo(int id, String subject, String memo, Calendar postTime, int postFlg, String createAt, String updateAt) {
        this.setId(id);
        this.setSubject(subject);
        this.setMemo(memo);
        this.setPostTime(postTime);
        this.setPostFlg(postFlg);
        this.setCreateAt(createAt);
        this.setUpdateAt(updateAt);
    }

    /**
     * メモIDを取得します。
     *
     * @return メモID
     */
    public int getId() {
        return id;
    }

    /**
     * メモIDをセットします。
     *
     * @param id メモID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 件名を取得します。
     *
     * @return 件名
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 件名をセットします。
     *
     * @param subject 件名
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * メモ本文を取得します。
     *
     * @return メモ本文
     */
    public String getMemo() {
        return memo;
    }

    /**
     * メモ本文をセットします。
     *
     * @param memo メモ本文
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 通知時刻を取得します。
     *
     * @return 通知時刻
     */
    public Calendar getPostTime() {
        return postTime;
    }

    /**
     * 通知時刻をセットします。
     *
     * @param postTime 通知時刻
     */
    public void setPostTime(Calendar postTime) {
        this.postTime = postTime;
    }

    /**
     * 通知の有無を判定します。
     *
     * @return 通知有無
     */
    public int getPostFlg() {
        return postFlg;
    }

    /**
     * 通知の有無をセットします。
     *
     * @param postFlg 通知有無
     */
    public void setPostFlg(int postFlg) {
        this.postFlg = postFlg;
    }

    /**
     * 作成日時を取得します。
     *
     * @return 作成日時
     */
    public String getCreateAt() {
        return createAt;
    }

    /**
     * 作成日時をセットします。
     *
     * @param createAt 作成日時
     */
    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    /**
     * 更新日時を取得します。
     *
     * @return 更新日時
     */
    public String getUpdateAt() {
        return updateAt;
    }

    /**
     * 更新日時をセットします。
     *
     * @param updateAt 更新日時
     */
    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
