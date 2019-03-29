package de.clubber_stuttgart.clubber;

import android.app.DownloadManager;
import android.app.IntentService;

import android.content.Intent;

import android.net.Uri;


public class DownloadServiceJson extends IntentService {

    private static long queueId;
    DownloadManager dm;

    public DownloadServiceJson() {
        super("DownloadServiceJson");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //ToDo: Achtung, Kommentar könnte sich noch bezüglich der MainActivity ändern
        //starts the download for the json file. MainActivity triggers this service after it deleted the old json files
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://clubber-stuttgart.de/script/data.json"));
        request.setDestinationInExternalFilesDir(this, getFilesDir().getAbsolutePath(), "data.json");
        request.setVisibleInDownloadsUi(false);
        queueId = dm.enqueue(request);
    }

    //A getter method, which returns the private id of the downloaded file
    protected static long getQueueId(){
        return queueId;
    }
}

