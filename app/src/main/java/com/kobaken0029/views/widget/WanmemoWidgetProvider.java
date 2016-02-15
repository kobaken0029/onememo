package com.kobaken0029.views.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.kobaken0029.R;
import com.kobaken0029.models.Memo;
import com.kobaken0029.services.WanmemoWidgetService;
import com.kobaken0029.views.activities.NavigationActivity;
import com.kobaken0029.views.fragments.MemoFragment;

public class WanmemoWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_UPDATE = "com.kobaken0029.android_appwidget.ACTION_UPDATE";
    private static final String ACTION_ITEM_CLICK = "com.kobaken0029.android_appwidget.ACTION_ITEM_CLICK";
    private static final String ACTION_CLICK = "com.kobaken0029.android_appwidget.ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            for (int appWidgetId : appWidgetIds) {
                // ウィジェットレイアウトの初期化
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                // ヘッダー
                Intent activityIntent = new Intent(context, NavigationActivity.class);
                PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
                remoteViews.setOnClickPendingIntent(R.id.header_text, activityPendingIntent);

                // 新規作成アイコン
                Intent clickIntent = new Intent(context, WanmemoWidgetProvider.class);
                clickIntent.setAction(ACTION_CLICK);
                PendingIntent createMemoPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                remoteViews.setOnClickPendingIntent(R.id.icon_container, createMemoPendingIntent);

                // リスト
                Intent widgetServiceIntent = new Intent(context, WanmemoWidgetService.class);
                remoteViews.setRemoteAdapter(R.id.memo_list, widgetServiceIntent);

                Intent itemClickIntent = new Intent(context, WanmemoWidgetProvider.class);
                itemClickIntent.setAction(ACTION_ITEM_CLICK);
                PendingIntent itemClickPendingIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        itemClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
                remoteViews.setPendingIntentTemplate(R.id.memo_list, itemClickPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
        }    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Intent navigationActivityIntent = new Intent(context, NavigationActivity.class);
        switch (intent.getAction()) {
            case ACTION_CLICK:
                navigationActivityIntent.putExtra(MemoFragment.TAG, true);
                navigationActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(navigationActivityIntent);
                break;
            case ACTION_ITEM_CLICK:
                navigationActivityIntent.putExtra(Memo.ID, intent.getLongExtra(Memo.ID, 0L));
                navigationActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(navigationActivityIntent);
                break;
            case ACTION_UPDATE:
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                ComponentName myWidget = new ComponentName(context, WanmemoWidgetProvider.class);
                int[] appWidgetIds = manager.getAppWidgetIds(myWidget);
                manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.memo_list);
                break;
            default:
                break;
        }
    }
}
