package com.room17.mygdxgame;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class MyWidgetProvider //{}
	extends AppWidgetProvider {

	private int total = 0;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		for (int widgetId : appWidgetIds) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			total = MainActivity.getStep();

			if (total == 0) {
				// Set the text
				remoteViews.setTextViewText(R.id.update,
						"Please make a move or open the game.");
			} else {
				// Set the text
				remoteViews.setTextViewText(R.id.update, total
						+ " steps so far.");
			}

			// Register an onClickListener
			Intent intent = new Intent(context, MyWidgetProvider.class);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}
