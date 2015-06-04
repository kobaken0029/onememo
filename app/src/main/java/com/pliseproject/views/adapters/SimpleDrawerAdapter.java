package com.pliseproject.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pliseproject.R;

import java.util.List;
import java.util.Map;

import static butterknife.ButterKnife.findById;


/**
 * NavigationDrawerのAdapterクラス。
 */
public class SimpleDrawerAdapter extends RecyclerView.Adapter<SimpleDrawerAdapter.ViewHolder> {
    /**
     * Drawerのホルダークラス。
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTextView2;
        public ImageView mImageView;
        public ImageView mIconImageView;
        public View divider;
        public ListView mListView;

        /**
         * コンストラクタ。
         *
         * @param itemView view
         * @param viewType viewType
         * @param context  コンテキスト
         */
        public ViewHolder(View itemView, int viewType, Context context) {
            super(itemView);

            // viewTypeによって値を設定
            switch (viewType) {
                case R.array.type_header_before_login:
                    mTextView = findById(itemView, R.id.email);
                    mImageView = findById(itemView, R.id.icon);
                    findById(itemView, R.id.drawer_header_layout)
                            .setBackgroundColor(context.getResources().getColor(R.color.action_bar));
                    break;
                case R.array.type_header_after_login:
                    mTextView = findById(itemView, R.id.name);
                    mTextView2 = findById(itemView, R.id.email);
                    mImageView = findById(itemView, R.id.icon);
                    findById(itemView, R.id.drawer_header_layout)
                            .setBackgroundColor(context.getResources().getColor(R.color.action_bar));
                    break;
                case R.array.type_menu:
                    mTextView = findById(itemView, R.id.menu_text);
                    mIconImageView = findById(itemView, R.id.image);
                    break;
                case R.array.type_divider:
                    divider = findById(itemView, R.id.divider);
                    break;
                case R.array.type_sub_header:
                    mTextView = findById(itemView, R.id.sub_header_text);
                    break;
                case R.array.type_sub_menu:
                    mTextView = findById(itemView, R.id.menu_text);
                case R.array.type_memo_list:
                    mListView = findById(itemView, android.R.id.list);
                default:
                    break;
            }
        }
    }

    private Context mContext;

    // MainActivityから渡されるデータ
    private List<Map<String, Object>> mDrawerMenuArr;

    // ListViewのアダプター
    private MyListAdapter mMyListAdapter;

    /**
     * コンストラクタ。
     *
     * @param context   コンテキスト
     * @param arrayList 対象のデータ
     */
    public SimpleDrawerAdapter(Context context, List<Map<String, Object>> arrayList, MyListAdapter myListAdapter) {
        mContext = context;
        mDrawerMenuArr = arrayList;
        mMyListAdapter = myListAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        switch (viewType) {
            case R.array.type_header_before_login:
            case R.array.type_header_after_login:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
                itemView.setClickable(false);
                break;
            case R.array.type_menu:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu, parent, false);
                itemView.setClickable(true);
                break;
            case R.array.type_divider:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_divider, parent, false);
                itemView.setClickable(false);
                break;
            case R.array.type_sub_header:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_sub_header, parent, false);
                itemView.setClickable(false);
                break;
            case R.array.type_sub_menu:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_sub_menu, parent, false);
                itemView.setClickable(true);
                break;
            case R.array.type_memo_list:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_memo_list, parent, false);
            default:
                break;
        }

        // アイテム選択時の Ripple Drawable を有効にする
        // Android 4 系端末で確認すると、Ripple効果は付かないが、選択色のみ適用される
        TypedValue outValue = new TypedValue();
        parent.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        itemView.setBackgroundResource(outValue.resourceId);

        return new ViewHolder(itemView, viewType, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String, Object> menu = mDrawerMenuArr.get(position);

        // 各アイテムのViewに、データをバインドする
        switch (holder.getItemViewType()) {
            case R.array.type_header_before_login:
                holder.mTextView.setText(menu.get("name").toString());
                holder.mImageView.setImageDrawable((Drawable) menu.get("icon"));
                break;
            case R.array.type_header_after_login:
                holder.mTextView.setText(menu.get("name").toString());
                holder.mTextView2.setText(menu.get("email").toString());
                holder.mImageView.setImageDrawable((Drawable) menu.get("icon"));
                break;
            case R.array.type_menu:
                holder.mTextView.setText(menu.get("text").toString());
                holder.mIconImageView.setImageDrawable((Drawable) menu.get("icon"));
                break;
            case R.array.type_divider:
                holder.divider.setBackgroundColor(Color.parseColor(menu.get("color").toString()));
                break;
            case R.array.type_sub_header:
            case R.array.type_sub_menu:
                holder.mTextView.setText(menu.get("text").toString());
                break;
            case R.array.type_memo_list:
                holder.mListView.setAdapter(mMyListAdapter);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerMenuArr.size();
    }

    @Override
    public int getItemViewType(int position) {
        // viewTypeを返す
        switch (mDrawerMenuArr.get(position).get("type").toString()) {
            case "header_before_login":
                return R.array.type_header_before_login;
            case "header_after_login":
                return R.array.type_header_after_login;
            case "menu":
                return R.array.type_menu;
            case "divider":
                return R.array.type_divider;
            case "sub_header":
                return R.array.type_sub_header;
            case "sub_menu":
                return R.array.type_sub_menu;
            case "memo_list":
                return R.array.type_memo_list;
            default:
                break;
        }
        return super.getItemViewType(position);
    }

}