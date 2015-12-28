package com.kobaken0029.helpers.impls;

import android.support.test.runner.AndroidJUnit4;

import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.models.Memo;
import com.raizlabs.android.dbflow.sql.language.Select;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.kobaken0029.models.Memo.ID;
import static com.kobaken0029.models.Memo.MEMO;
import static com.kobaken0029.models.Memo.SUBJECT;

@RunWith(AndroidJUnit4.class)
public class MemoHelperImplTest extends TestCase {
    private final MemoHelper mMemoHelper = new MemoHelperImpl();
    private final List<HashMap<String, Object>> params = Arrays.asList(
            new HashMap<String, Object>() {
                {
                    put(ID, 1l);
                    put(SUBJECT, "件名1");
                    put(MEMO, "メモ本文1");
                }
            },
            new HashMap<String, Object>() {
                {
                    put(ID, 2l);
                    put(SUBJECT, "件名2");
                    put(MEMO, "メモ本文2");
                }
            },
            new HashMap<String, Object>() {
                {
                    put(ID, 3l);
                    put(SUBJECT, "件名3");
                    put(MEMO, "メモ本文3");
                }
            }
    );

    @BeforeClass
    public static void doBeforeClass() {
        List<Memo> memos = new Select().from(Memo.class).queryList();
        for (Memo memo : memos) {
            memo.delete();
        }
    }

    @After
    public void doAfter() throws Exception {
        List<Memo> memos = new Select().from(Memo.class).queryList();
        for (Memo memo : memos) {
            memo.delete();
        }
    }

    /**
     * findが成功すること。
     *
     * @throws Exception 例外
     */
    @Test
    public void testFind() throws Exception {
        {
            Memo memo = setParams(new Memo(), 0);
            memo.save();
            Memo target = mMemoHelper.find(memo.getId());
            assertionMemo(target, 0);
        }

        {
            Memo target = mMemoHelper.find(0);
            assertNull(target);
        }
    }

    /**
     * findAllが成功すること。
     *
     * @throws Exception 例外
     */
    @Test
    public void testFindAll() throws Exception {
        List<Memo> expected = createMemos();
        for (int i = 0; i < expected.size(); i++) {
            expected.get(i).save();
        }

        List<Memo> actualMemos = mMemoHelper.findAll();
        for (int i = 0; i < actualMemos.size(); i++) {
            assertionMemo(actualMemos.get(i), i);
        }
    }

    /**
     * createが成功すること。
     *
     * @throws Exception 例外
     */
    @Test
    public void testCreate() throws Exception {
        Memo target = mMemoHelper.create((String) params.get(0).get(SUBJECT), (String) params.get(0).get(MEMO));
        assertionMemo(target, 0);
    }

    @Test
    public void testUpdate() throws Exception {
        fail();
    }

    @Test
    public void testDelete() throws Exception {
        fail();
    }

    @Test
    public void testLoadMemos() throws Exception {
        fail();
    }

    @Test
    public void testIsEmpty() throws Exception {
        {
            assertFalse(mMemoHelper.exists());
        }

        {
            Memo memo = setParams(new Memo(), 0);
            memo.save();
            assertTrue(mMemoHelper.exists());
        }
    }

    @Test
    public void testExists() throws Exception {
        fail();
    }

    /**
     * メモ群を作成する。
     *
     * @return メモ群
     */
    private List<Memo> createMemos() {
        List<Memo> memos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Memo memo = new Memo();
            memo.setSubject((String) params.get(i).get(SUBJECT));
            memo.setMemo((String) params.get(i).get(MEMO));
            memos.add(memo);
        }
        return memos;
    }

    /**
     * パラメータをセットする。
     *
     * @param memo メモ
     * @return パラメータがセットされたメモ
     */
    private Memo setParams(Memo memo, int paramsId) {
        memo.setSubject((String) params.get(paramsId).get(SUBJECT));
        memo.setMemo((String) params.get(paramsId).get(MEMO));
        return memo;
    }

    /**
     * メモの件名、本文が一致するか検証する。
     *
     * @param target 対象メモ
     */
    private void assertionMemo(Memo target, int paramsId) {
        assertEquals(params.get(paramsId).get(SUBJECT), target.getSubject());
        assertEquals(params.get(paramsId).get(MEMO), target.getMemo());
    }
}