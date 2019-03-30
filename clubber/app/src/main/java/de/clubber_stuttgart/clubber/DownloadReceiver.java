package de.clubber_stuttgart.clubber;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DownloadReceiver extends BroadcastReceiver {



    //ToDo: Achtung, Kommentar könnte sich noch bezüglich onStart und onStop ändern.
    //receives DOWNLOAD_COMPLETE broadcast, which is registered in the onStart method (and unregistered in the onStop method)

    @Override
    public void onReceive(Context context, Intent intent) {

        long downloadedFileId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
        long downloadedJsonId = DownloadServiceJson.getQueueId();
        File file = new File(context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json");

        //ToDo if statement

        //ToDo was wenn datei während der runtime abgefragt wird? Die queueid wäre dann nicht mehr 0.0

        //1st check: Has our file been downloaded while runtime at all
        //2nd check: Has the download the same id as the downloaded json file
        //3rd check (just in case): is our file existing in the right directory
        if(downloadedJsonId != 0.0 && downloadedFileId == downloadedJsonId && file.exists()) {

            Log.d("BroadcastReceiver", "Json File download complete!");

            String filename = context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json";
            try {
                //BufferedReader is said to be more performance friendly than reading the whole file at once
                BufferedReader br = new BufferedReader(new FileReader(filename));
                //the StringBuilder is a not as performance heavy as appending a String gradually
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                //reads every line of the json file on at a time
                while (line != null) {
                    //StringBuilder is concattinated correspondingly
                    sb.append(line);
                    //jumps to next line
                    line = br.readLine();
                }
                //conversion from StringBuilder to String
                String result = sb.toString();
                JsonController con = new JsonController();
                con.createList(result);
            }

            catch (Exception e) {
                Log.w("DownloadReceiver: ", "And error occured " + e);
            }

        }else{
        }
    }
}

