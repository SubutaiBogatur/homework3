package com.example.aleksandr.homework3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aleksandr Tukallo on 28.11.16.
 */
public class LoadTask extends AsyncTask<Void, Void, Void> {

    Context context;
    String url;
    File f;

    LoadTask(String u, File ff, Context context) {
        this.context = context;
        f = ff;
        url = u;
    }

    protected Void doInBackground(Void[] voids) {
        Log.d(MainActivity.TAG, "Async task started");
        HttpURLConnection connection = null;
        InputStream in = null;
        OutputStream out = null;
        boolean taskFinished = false;

        context.sendBroadcast(new Intent(MainActivity.downloadingStartedIntent));
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            in = new BufferedInputStream(connection.getInputStream());
            out = new FileOutputStream(f);

            int cnt;
            while ((cnt = in.read()) != -1 && !isCancelled()) {
                out.write(cnt);
            }
            Log.d(MainActivity.TAG, "Picture downloaded and saved to the file");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(MainActivity.TAG, "Unable to download the picture");
            f.delete();
        }

        try {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.sendBroadcast(new Intent(MainActivity.pictureReadyIntent));
        return null;
    }
}