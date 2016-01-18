package com.kobaken0029.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;

import java.util.ArrayList;
import java.util.List;

import static butterknife.ButterKnife.findById;

/**
 * メモリストのAdapter。
 */
public class MemoListAdapter extends BaseAdapter {
    private List<Memo> memos;
    private LayoutInflater mInflater;

    /**
     * ホルダクラス。
     */
    class ViewHolder {
        TextView subjectTextView;
        TextView lastUpdateTextView;

        ViewHolder(View view) {
            subjectTextView = findById(view, R.id.memo_text);
            lastUpdateTextView = findById(view, R.id.last_update_text);
        }
    }

    /**
     * コンストラクタ。
     *
     * @param context コンテキスト
     * @param memos   メモ群
     */
    public MemoListAdapter(Context context, List<Memo> memos) {
        this.memos = memos;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Memo memo = (Memo) getItem(position);
        if (memo != null) {
            holder.subjectTextView.setText(memo.getSubject());
            holder.lastUpdateTextView.setText(memo.getUpdateAt());
        }

        return convertView;
    }

    public void setMemos(List<Memo> memos) {
        this.memos = new ArrayList<>(memos);
    }
}
