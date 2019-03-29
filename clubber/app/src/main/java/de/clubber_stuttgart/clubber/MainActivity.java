package de.clubber_stuttgart.clubber;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainActivity extends Activity {
    //ToDo: Pfade mit Variablen vereinfachen
    //variable is needed to create the absolute path for the json file. (package path)
    //String dirType = this.getFilesDir().getAbsolutePath();
    //path where json file is saved to
    //String pathJson = this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath() + "data.json";

    BroadcastReceiver downloadReceiver = new DownloadReceiver();

    @Override
    public void onStart(){
        super.onStart();
        //ToDo: Woanders registrieren?
        //registers an IntentFilter dynamically for DownloadReceiver. When a Download is finished, the DownloadReceiver.class will listen to it
        IntentFilter downloadCompleteFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, downloadCompleteFilter);
    }

    @Override
    public void onStop(){
        super.onStop();
        //ToDo: Kann eigentlich schon früher deregistriert werden.
        //unregisters the receiver
        unregisterReceiver(downloadReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToDo: Permission richtig abfragen
        //ToDo: Überprüfen, ob external Storage erreichbar ist

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        deleteOldJsonFiles();

        //triggers the download service, which downloads a json file from our webserver. It runs in its own thread to prevent performance issues.
        Intent intent = new Intent(this,DownloadServiceJson.class);
        this.startService(intent);


    }


    //ToDo: Projektstruktur überdenken, wollen wir Methoden wie diese in der MainActivity stehen haben?
    //ToDo: API bezüglich der Files.class Methoden überdenken
    void deleteOldJsonFiles(){
        try {
            String iter = "";
            int i = 0;
            //we delete every old json file to make room in order to replace it with a new one
            while (true) {
                File file = new File(this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data" + iter + ".json");
                if (!Files.deleteIfExists(file.toPath())) {
                    break;
                } else {
                    i++;
                    //iterates ending of the filename if there are multiple files called Data.json
                    iter = "-" + i;
                }
            }
        } catch (IOException e){
            Log.e("mainActivity","An IOException in deleteIfExists function has occured");
            e.printStackTrace();
        }
    }
}
