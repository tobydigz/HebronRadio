package com.digzdigital.hebronradio;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Digz on 28/10/2015.
 */
public class HebronWidget extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        //TODO Update the Widget UI
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.hebron_widget);
//            remoteViews.setTextViewText(R.id.textView, number);

            Intent intent = new Intent(context, HebronWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.plysngwid, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.stpsngwid, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        //TODO Handle deletion of the widget
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context)
    {
        //TODO The widget has been disabled
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context)
    {
        //TODO The widget has been enabled
        super.onEnabled(context);
    }
}
