package com.example.aleksandr.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    TextView text;
    ProgressBar pb;

    static final String PATH_TO_IMAGE = "image.jpg";
    static final String TAG = "Main activity, hw3";
    static final String pictureReadyIntent = "picture ready";
    static final String downloadingStartedIntent = "downloading started";

    ScreenOnBroadcastReciever receiver1 = new ScreenOnBroadcastReciever();
    BroadcastReceiver receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(MainActivity.TAG, "Picture loaded, updating UI");
            updateUIWithPicture();
        }
    };
    BroadcastReceiver receiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(MainActivity.TAG, "Downloading started, updating UI");
            updateUILoadingStarted();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.picture);
        image.setVisibility(View.INVISIBLE);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        text = (TextView) findViewById(R.id.textView);

        registerReceiver(receiver1, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(receiver2, new IntentFilter(pictureReadyIntent));
        registerReceiver(receiver3, new IntentFilter(downloadingStartedIntent));

        Log.d(MainActivity.TAG, "Three receivers are registered");
    }

    @Override
    protected void onDestroy() {
        Log.d(MainActivity.TAG, "onDestroy called");
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(receiver3);
//        File f = new File(getFilesDir(), PATH_TO_IMAGE);
//        f.delete(); //f is destroyed here just for fun. To see progress bar more often.
        super.onDestroy();
    }

    void updateUILoadingStarted() {
        text.setText(R.string.download_text);
        pb.setVisibility(View.VISIBLE);
    }

    void updateUIWithPicture() {
        Log.d(MainActivity.TAG, "Updating UI");
        pb.setVisibility(View.INVISIBLE);
        File f = new File(getFilesDir(), PATH_TO_IMAGE);
        boolean errorHappened = true;

        if (f.exists()) {
            try {
                image.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(f)));
                errorHappened = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (errorHappened) {
            text.setText(getString(R.string.error_text));
        } else {
            text.setText(getString(R.string.success_text));
            image.setVisibility(View.VISIBLE);
        }
    }
}
