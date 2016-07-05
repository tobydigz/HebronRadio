package com.digzdigital.hebronradio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by Digz on 05/11/2015.
 */
public class MediaControlReceiver  extends BroadcastReceiver{

    public static final String ACTION_MEDIA_BUTTON = "com.digzdigital.hebronradio.ACTION_MEDIA_BUTTON";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction()))
        {
            Intent internalIntent = new Intent(ACTION_MEDIA_BUTTON);
            internalIntent.putExtras(intent.getExtras());
            context.sendBroadcast(internalIntent);
        }
    }
}


