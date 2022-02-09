package com.example.plandial;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class PlanDialWidget extends AppWidgetProvider {
    private static final int MAX_ITEM_CNT = 5;

    private static String itemList = null;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plan_dial_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pe = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_layout, pe);

        int num = 1, visibleItemCnt = 0;
        if (itemList != null) {
            String[] items = itemList.split("/");
            int cnt = Math.min(items.length, MAX_ITEM_CNT);
            for (; num <= cnt; num++) {
                if (setItem(context, views, num, items[num - 1])) visibleItemCnt++;
            }
        }
        for (; num <= MAX_ITEM_CNT; num++) {
            setItem(context, views, num, null); // 나머지 item 감추기
        }

        views.setViewVisibility(R.id.appwidget_empty_text,
                (visibleItemCnt == 0) ? View.VISIBLE : View.GONE);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static int getResId(Context context, String name, String defType) {
        // name 에 해당하는 id가 없는 경우 0을 리턴함
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    static int getResId(Context context, String name) {
        return getResId(context, name, "id");
    }

    static int getResDrawableId(Context context, String name) {
        return getResId(context, name, "drawable");
    }

    static boolean setItem(Context context, RemoteViews views, int num, String item) {
        int resId, imgResId = -1;
        String text = "";

        Log.d("WIDGET", "num=" + num + " item=" + item);
        if (item != null) {
            String[] arr = item.split(":");
            if (arr.length >= 1) text = arr[0];
            if (arr.length >= 2) {
                imgResId = getResDrawableId(context, arr[1]); // image resource가 없는 경우 resImgId = 0임
            }
        }

        resId = getResId(context, "widget_item" + num + "_layout"); // item LinearLayout id 얻기
        if (imgResId < 0 && text.length() == 0) { // "이름:리소스" 형태가 아닌 "" or ":"인 경우
            views.setViewVisibility(resId, View.GONE); // item layout 감추기
            return false; // item 이미지가 없는 경우 해당 item이 표시되지 않도록 하고 return 함
        }
        views.setViewVisibility(resId, View.VISIBLE); // item layout 보이기

        resId = getResId(context, "widget_item" + num + "_img"); // item의 ImageView id 얻기
        if (imgResId <= 0) {
            views.setViewVisibility(resId, View.INVISIBLE); // imageView 감추기(GONE이 아니기 때문에 자리차지함)
        } else {
            views.setViewVisibility(resId, View.VISIBLE); // imageView 보이기
            views.setImageViewResource(resId, imgResId);
        }

        resId = getResId(context, "widget_item" + num + "_text"); // item의 TextView id 얻기
        views.setTextViewText(resId, text);
        return true;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            String strItem = intent.getStringExtra("itemList");
            Log.d("WIDGET", "itemList=" + itemList);
            if (strItem != null) {
                itemList = strItem;
                // 아래 부분에서 onUpdate()를 직접 호출하지 않으면 update 주기가 되기 전까진 변경되지 않음
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName widget = new ComponentName(context.getPackageName(), PlanDialWidget.class.getName());
                int[] widgetIds = appWidgetManager.getAppWidgetIds(widget);
                if (widgetIds != null && widgetIds.length > 0) {
                    this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);
                }
                return;
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}