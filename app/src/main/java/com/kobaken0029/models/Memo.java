package com.kobaken0029.models;

import com.kobaken0029.models.db.OneMemoDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;

import java.util.Date;

/**
 * メモクラス。
 */
@Table(databaseName = OneMemoDatabase.NAME, tableName = Memo.NAME)
public class Memo extends BaseEntity {
    /** タグ。 */
    public static final String TAG = Memo.class.getName();

    /** テーブル名。 */
    public static final String NAME = "memos";

    /** ID。 */
    public static final String ID = "id";

    /** メモ。 */
    public static final String SUBJECT = "subject";

    /** メモ。 */
    public static final String MEMO = "memo";

    @Column
    @Unique
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String subject;
    @Column
    String memo;
    @Column
    Date postTime;
    @Column
    int postFlg;
    @Column
    String createAt;
    @Column
    String updateAt;

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
    public Memo(long id, String subject, String memo, Date postTime, int postFlg,
                String createAt, String updateAt) {
        this.setId(id);
        this.setSubject(subject);
        this.setMemo(memo);
        this.setPostTime(postTime);
        this.setPostFlg(postFlg);
        this.setCreateAt(createAt);
        this.setUpdateAt(updateAt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public int getPostFlg() {
        return postFlg;
    }

    public void setPostFlg(int postFlg) {
        this.postFlg = postFlg;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
