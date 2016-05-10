package com.sam_chordas.android.stockhawk;

import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.DetailStockActivity;

/**
 * A collections widget for displaying the stocks
 */
public class MyStocksListWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "stockhawk " + MyStocksListWidget.class.getSimpleName();

    /**
     * updates a widget for a given id.  Helper method for onUpdate.
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId      the id of the widget to update
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, MyStocksListRemoteViewService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_stocks_list_widget);

        views.setRemoteAdapter(appWidgetId, R.id.widget_listview, intent);

        Intent intentTemplate = new Intent(context, DetailStockActivity.class);

        PendingIntent pendingIntentTemplete = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_listview, pendingIntentTemplete);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * stub method.  Currently not implemented
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }


    /**
     * stub method.  Currently not implemented
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (StockTaskService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds =
                    appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            Log.i(LOG_TAG, "notifyAppWidgetViewDataChanged called");
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
        }
    }
}