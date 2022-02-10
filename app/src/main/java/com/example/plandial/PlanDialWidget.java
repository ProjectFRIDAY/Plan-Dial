package com.example.plandial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class PlanDialWidget extends AppWidgetProvider {
    private static final String TAG = "WIDGET";

    private static final int MAX_ITEM_CNT = 5;
    private static final int WIDGET_UPDATE_INTERVAL = 5000; // ms 단위
    private static final String ITEM_SEPARATOR = ",";
    private static final String FIELD_SEPARATOR = ":";

    private static Context mContext;
    private static String mItemList = "";
    private static AlarmManager mAlarmManager = null;
    private static PendingIntent mPendingIntent = null;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plan_dial_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pe = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_layout, pe); // 클릭시 main activity 실행

        mContext = context;
        int num = 1, visibleItemCnt = 0;
        if (mItemList != null) {
            String[] items = mItemList.split(ITEM_SEPARATOR);
            int cnt = (items.length < MAX_ITEM_CNT) ? items.length : MAX_ITEM_CNT;
            for ( ; num <= cnt; num++) {
                if (setItem(views, num, items[num - 1])) visibleItemCnt++;
            }
        }
        for ( ; num <= MAX_ITEM_CNT; num++) {
            setItem(views, num, null); // 나머지 item 감추기
        }

        views.setViewVisibility(R.id.appwidget_empty_text,
                                (visibleItemCnt == 0) ? View.VISIBLE : View.GONE);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static int getResId(String name, String defType) {
        // name 에 해당하는 id가 없는 경우 0을 리턴함
        return mContext.getResources().getIdentifier(name, defType, mContext.getPackageName());
    }

    private static int getResId(String name) {
        return getResId(name, "id");
    }

    private static int getResDrawableId(String name) {
        return getResId(name, "drawable");
    }

    private static boolean setItem(RemoteViews views, int num, String item) {
        int resId, imgResId = -1;
        String text = "";

        Log.d(TAG, "PlanDialWidget.setItem() : num="+num+" item="+item);
        if (item != null) {
            String[] arr = item.split(FIELD_SEPARATOR);
            if (arr.length >= 1) text = arr[0];
            if (arr.length >= 2) {
                imgResId = getResDrawableId(arr[1]); // image resource가 없는 경우 resImgId = 0임
            }
        }

        resId = getResId("widget_item"+num+"_layout"); // item LinearLayout id 얻기
        if (imgResId < 0 && text.length() == 0) { // "이름:리소스" 형태가 아닌 "" or ":"인 경우
            views.setViewVisibility(resId, View.GONE); // item layout 감추기
            return false; // item 이미지가 없는 경우 해당 item이 표시되지 않도록 하고 return 함
        }
        views.setViewVisibility(resId, View.VISIBLE); // item layout 보이기

        resId = getResId("widget_item"+num+"_img"); // item의 ImageView id 얻기
        if (imgResId <= 0) {
            views.setViewVisibility(resId, View.INVISIBLE); // imageView 감추기(GONE이 아니기 때문에 자리차지함)
        } else {
            views.setViewVisibility(resId, View.VISIBLE); // imageView 보이기
            views.setImageViewResource(resId, imgResId);
        }

        resId = getResId("widget_item"+num+"_text"); // item의 TextView id 얻기
        views.setTextViewText(resId, text);
        return true;
    }

    private String getUrgentDialList() {
        DialManager dialManager = DialManager.getInstance();
        ArrayList<AlertDial> urgentDialList = dialManager.getUrgentDials(DialManager.URGENT_BOUND);
        String strItemList = "";
        int cnt = 0;

        for (Dial dial : urgentDialList) {
            if (strItemList.length() > 0) strItemList += ITEM_SEPARATOR;
            strItemList += dial.getName() + FIELD_SEPARATOR + dial.getIcon();
            if (++cnt >= MAX_ITEM_CNT) break;
        }

        return strItemList;
    }

    private int[] getWidgetId(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context.getPackageName(), PlanDialWidget.class.getName());
        return appWidgetManager.getAppWidgetIds(widget);
    }

    private void alarmCancel() {
        if (mAlarmManager != null && mPendingIntent != null) { // 기존 알람 취소
            mPendingIntent.cancel();
            mAlarmManager.cancel(mPendingIntent);
            mPendingIntent = null;
            mAlarmManager = null;
        }
    }

    private void alarmUpdate(Context context) {
        Intent intent = getForceUpdateIntent(context);
        mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL, mPendingIntent);
    }

    private static Intent getForceUpdateIntent(Context context) {
        Intent in = new Intent(context, PlanDialWidget.class);
        in.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        in.putExtra("forceUpdate", 1);
        return in;
    }

    public static void WakeUp(Context context) {
        Log.d(TAG, "PlanDialWidget.WakeUp()");
        context.sendBroadcast(getForceUpdateIntent(context));
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

            alarmCancel(); // 기존 알람 취소

            int[] widgetIds = getWidgetId(context);
            if (widgetIds == null || widgetIds.length == 0) { // widget 이 존재하지 않을 때
                mItemList = ""; // item list 초기화
            } else { // widget이 존재할 때만 실행
                String strItemList = getUrgentDialList();

                Log.d(TAG, "PlanDialWidget.onReceive() : strItemList=" + strItemList);

                alarmUpdate(context); // 알람 갱신

                if (!strItemList.equals(mItemList)) { // 임박 리스트가 변경되었을 때만 갱신
                    mItemList = strItemList;
                    Log.d(TAG, "PlanDialWidget.onReceive() : mItemList=" + mItemList);

                    // onUpdate()를 직접 호출하지 않으면 update 주기가 되기 전까진 변경하지 않음
                    this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);
                    return;
                }

                int forceUpdate = intent.getIntExtra("forceUpdate", 0);
                Log.d(TAG, "PlanDialWidget.onReceive() : forceUpdate=" + forceUpdate);
                if (forceUpdate == 1) return; // 알람이나 WakeUp() 함수에 의해 실행되는 경우 super class의 onReceive() 실행 안되도록 함
            }
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d(TAG, "PlanDialWidget.onReceive() : BOOT_COMPLETED");

            int[] widgetIds = getWidgetId(context);
            if (widgetIds != null && widgetIds.length > 0) { // widget이 존재할 때만 실행
                WakeUp(context); // 부팅 완료후 update
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