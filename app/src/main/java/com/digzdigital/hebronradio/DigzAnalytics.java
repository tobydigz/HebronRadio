package com.digzdigital.hebronradio;

/**
 * Created by Digz on 01/04/2016.
 */

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class DigzAnalytics extends Application{
    private Tracker digzTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (digzTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            digzTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return digzTracker;
    }
}
