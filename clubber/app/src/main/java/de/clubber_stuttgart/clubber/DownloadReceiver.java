package de.clubber_stuttgart.clubber;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DownloadReceiver extends BroadcastReceiver {



    //ToDo: Achtung, Kommentar könnte sich noch bezüglich onStart und onStop ändern.
    //receives DOWNLOAD_COMPLETE broadcast, which is registered in the onStart method (and unregistered in the onStop method)
    @Override
    public void onReceive(Context context, Intent intent) {

        long downloadedFileId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
        long downloadedJsonId = DownloadServiceJson.getQueueId();
        File file = new File(context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json");


        //1st check: Has our file been downloaded while runtime at all
        //2nd check: Has the download the same id as the downloaded json file
        //3rd check (just in case): is our file existing in the right directory
        if(downloadedJsonId != 0.0 && downloadedFileId == downloadedJsonId && file.exists()) {
            Log.i("BroadcastReceiver", "Json File download complete!");
            //takes json file, reads it and converts it to a string
            createJsonString(context);

        }else{
            //We ignore the broadcast...
        }
    }

    void createJsonString(Context context){
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
            JsonController.createList(result);
            ArrayList<Event> eventList = JsonController.getEventList();
            ArrayList<Club> clubList = JsonController.getClubList();

            DataBaseHelper dbHelper = new DataBaseHelper(context);

            for (Event event : eventList){
                dbHelper.insertEventEntry(event);
            }

            for (Club club : clubList){
                dbHelper.insertClubEntry(club);
            }


            //ToDo: besseren Ort finden um die Backup-Datei zu löschen, am besten nachdem validität der json geprüft wurde.
            //deleting backup File, we don't need it anymore because we downloaded a fresh one
            Log.i("BroadcastReceiver","delete backup file...");
            JsonController.deleteOldJsonFiles("backup_data",context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath());
            Log.i("BroadcastReceiver","File has been read, Backup file deleted");

        }

        catch (Exception e) {
            //ToDo: Hier muss der Error spezifischer abgefangen werden + Backup File muss wieder in normale Datei umgewandelt werden, da beim Download etwas schief gegangen sein könnte.
            //ToDo: --> wir müssen die Datei erneut runterladen und es nochmal versuchen, wenn das nicht passt, nehmen wir die Backup datei.

            //We delete the potentially faulty file and rename the backup_data to the actual data.json file.
            JsonController.deleteOldJsonFiles("data",context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath());
            JsonController.renameJson("backup_data","data",context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath());

            //Something went wrong, so we try to download the file again
            //ToDo: lade die File erneut herunter
            Log.w("DownloadReceiver ", "An error occured " + e);
        }
    }
}

