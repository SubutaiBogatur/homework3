package com.example.aleksandr.homework3;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

public class LoadPictureService extends Service {

    private final String shuttle = "https://www.nasa.gov/images/content/152546main_Landing2-lg.jpg";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    LoadTask task;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MainActivity.TAG, "Service started");
        File f = new File(getFilesDir(), MainActivity.PATH_TO_IMAGE);
        if (!f.exists()) {
            Log.d(MainActivity.TAG, "Picture wasn't downloaded yet");
            task = new LoadTask(shuttle, f, this.getApplicationContext());
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Log.d(MainActivity.TAG, "Picture was already downloaded");
            sendBroadcast(new Intent(MainActivity.pictureReadyIntent));
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (task != null)
            task.cancel(true);
        super.onDestroy();
    }
}