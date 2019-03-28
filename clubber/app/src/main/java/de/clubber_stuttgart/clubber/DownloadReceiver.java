package de.clubber_stuttgart.clubber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DownloadReceiver extends BroadcastReceiver {

    //ToDo: Achtung, Kommentar könnte sich noch bezüglich onStart und onStop ändern.
    //receives DOWNLOAD_COMPLETE broadcast, which is registered in the onStart method (and unregistered in the onStop method)


    @Override
    public void onReceive(Context context, Intent intent) {

        File file = new File(context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json");
        //ToDo: Andere Lösung finden mit queueId, da auch nach dem Download der Json neue Downloads durchgeführt werden könnten und das Programm ausgeführt werden würde.

        //checks if this is the download from our download service.
        if (file.exists()) {
            Log.d("BroadcastReceiver", "Json File download complete!");

            String result = "";
            String filename = context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json";
            try {
                //BufferedReader is said to be more performance friendly
                BufferedReader br = new BufferedReader(new FileReader(filename));
                //the StringBuilder ist pretty much just a String
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
                result = sb.toString();
            } catch (Exception e) {
                //ToDo: ACHTUNG HIER WIRD NICHTS BEHANDELT, WENN DIE DATEI NICHT EXISTIERT!
                e.printStackTrace();
            }
            System.out.println(result);


        }else{

            //ToDo: Was wenn es sich nicht um unseren Download handelt?

        }
    }
}

