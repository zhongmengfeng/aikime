
package com.ichi2yiji.compat;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.text.Html;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AnkiActivity;
import com.ichi2yiji.compat.customtabs.CustomTabActivityHelper;
import com.ichi2yiji.compat.customtabs.CustomTabsFallback;
import com.ichi2yiji.compat.customtabs.CustomTabsHelper;

/** Implementation of {@link Compat} for SDK level 16 */
@TargetApi(16)
public class CompatV16 extends CompatV15 implements Compat {

    @Override
    public void disableDatabaseWriteAheadLogging(SQLiteDatabase db) {
        db.disableWriteAheadLogging();
    }

    public boolean isWriteAheadLoggingEnabled(SQLiteDatabase db) {
        return db.isWriteAheadLoggingEnabled();
    }

    /*
     *  Return the input string in a form suitable for display on a HTML page.
     *
     * @param txt Text to be cleaned.
     * @return The input text, HTML-escpaped
    */
    @Override
    public String detagged(String txt) {
        return Html.escapeHtml(txt);
    }

    /*
    *  Update dimensions of widget from V16 on (elder versions do not support widget measuring)
    */
    @Override
    public void updateWidgetDimensions(Context context, RemoteViews updateViews, Class<?> cls) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, cls));
        for (int id : ids) {
            final float scale = context.getResources().getDisplayMetrics().density;
            Bundle options = manager.getAppWidgetOptions(id);
            float width, height;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
                height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
            } else {
                width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
                height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            }
            int horizontal, vertical;
            float text;
            if ((width / height) > 0.8) {
                horizontal = (int) (((width - (height * 0.8))/2 + 4) * scale + 0.5f);
                vertical = (int) (4 * scale + 0.5f);
                text = (float)(Math.sqrt(height * 0.8 / width) * 18);
            } else {
                vertical = (int) (((height - (width * 1.25))/2 + 4) * scale + 0.5f);
                horizontal = (int) (4 * scale + 0.5f);
                text = (float)(Math.sqrt(width * 1.25 / height) * 18);
            }

            updateViews.setTextViewTextSize(R.id.widget_due, TypedValue.COMPLEX_UNIT_SP, text);
            updateViews.setTextViewTextSize(R.id.widget_eta, TypedValue.COMPLEX_UNIT_SP, text);
            updateViews.setViewPadding(R.id.ankidroid_widget_text_layout, horizontal, vertical, horizontal, vertical);
        }
    }


    @Override
    public void openUrl(AnkiActivity activity, Uri uri) {
        CustomTabActivityHelper helper = activity.getCustomTabActivityHelper();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(helper.getSession());
        builder.setToolbarColor(activity.getResources().getColor(R.color.theme_primary)).setShowTitle(true);
        builder.setStartAnimations(activity, R.anim.slide_right_in, R.anim.slide_left_out);
        builder.setExitAnimations(activity, R.anim.slide_left_in, R.anim.slide_right_out);
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_arrow_back_white_24dp));
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, uri, new CustomTabsFallback());
    }
}