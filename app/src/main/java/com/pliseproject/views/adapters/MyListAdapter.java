package com.pliseproject.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pliseproject.R;
import com.pliseproject.models.Memo;

import java.util.List;

/**
 * メモリストのAdapterです。
 */
public class MyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Memo> memos;

    /**
     * コンストラクタ。
     *
     * @param context コンテキスト
     * @param memos   メモ群
     */
    public MyListAdapter(Context context, List<Memo> memos) {
        mContext = context;
        this.memos = memos;
    }

    @Override
    public int getCount() {
        return memos.size();
    }

    @Override
    public Object getItem(int position) {
        return memos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView subjectTextView;
        TextView lastUpdateTextView;
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row, null);
        }
        Memo memo = (Memo) getItem(position);

        if (memo != null) {
            subjectTextView = (TextView) v.findViewById(R.id.memo_text);
            lastUpdateTextView = (TextView) v
                    .findViewById(R.id.last_update_text);
            subjectTextView.setText(memo.getSubject());
            lastUpdateTextView.setText(memo.getUpdateAt());
        }

        return v;
    }
}
