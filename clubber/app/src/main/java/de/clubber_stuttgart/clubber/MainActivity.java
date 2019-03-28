package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends Activity {

    BroadcastReceiver downloadReceiver;

    @Override
    public void onStart(){
        super.onStart();
        //registers an IntentFilter dynamically for DownloadReceiver. When a Download is finished, the DownloadReceiver.class will listen to it
        IntentFilter downloadCompleteFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, downloadCompleteFilter);
    }

    @Override
    public void onStop(){
        super.onStop();
        //unregisters the receiver
        unregisterReceiver(downloadReceiver);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
