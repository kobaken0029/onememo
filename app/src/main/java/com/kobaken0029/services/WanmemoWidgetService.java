package com.kobaken0029.services;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kobaken0029.R;
import com.kobaken0029.helpers.MemoHelper;
import com.kobaken0029.helpers.impls.MemoHelperImpl;
import com.kobaken0029.models.Memo;
import com.kobaken0029.utils.DateUtil;

import java.util.List;

public class WanmemoWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WanmemoWidgetFactory();
    }

    private class WanmemoWidgetFactory implements RemoteViewsFactory {
        private MemoHelper memoHelper;
        private List<Memo> memos;

        @Override
        public void onCreate() {
            memoHelper = new MemoHelperImpl();
            memos = memoHelper.findAll();
        }

        @Override
        public void onDataSetChanged() {
            memos = memoHelper.findAll();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (memos.size() <= 0) {
                return null;
            }

            Memo memo = memos.get(position);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.row_widget_memo_list);
            remoteViews.setTextViewText(R.id.subject_text, memo.getSubject());
            remoteViews.setTextViewText(
                    R.id.last_update_text,
                    DateUtil.convertToString(
                            DateUtil.MONTH_DAY,
                            DateUtil.convertStringToDate(DateUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, memo.getUpdateAt())
                    )
            );
            remoteViews.setTextViewText(R.id.memo_text, memo.getMemo());

            Intent intent = new Intent();
            intent.putExtra(Memo.ID, memo.getId());
            remoteViews.setOnClickFillInIntent(R.id.container, intent);

            return remoteViews;
        }

        @Override
        public int getCount() {
            return memos.size();
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
