package com.example.aleksandr.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Aleksandr Tukallo on 27.11.16.
 */
public class ScreenOnBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MainActivity.TAG, "Screen broadcast received");
        Intent intent1 = new Intent(context, LoadPictureService.class);
        context.startService(intent1);
    }
}
