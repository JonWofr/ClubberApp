package de.clubber_stuttgart.clubber;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class MainActivity extends Activity {
    //ToDo: Pfade mit Variablen vereinfachen
    //variable is needed to create the absolute path for the json file. (package path)
    //String dirType = this.getFilesDir().getAbsolutePath();
    //path where json file is saved to
    //String pathJson = this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath() + "data.json";

    BroadcastReceiver downloadReceiver = new DownloadReceiver();
    private static final int REQUEST_WRITE_ACCESS_CODE = 0;

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

        String jsonFileName = "data", newJsonFileName = "backup_data";


        if(isNetworkAvailable()){
            //creates backup, if anything goes wrong in the DownloadService, we still got the backup file
            JsonController.renameJson(jsonFileName, newJsonFileName, this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath());
            //deletes any old data.json files if there are any at all
            JsonController.deleteOldJsonFiles(this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath(), jsonFileName);
            //triggers the download service, which downloads a json file from our webserver. It runs in its own thread to prevent performance issues.
            Intent intent = new Intent(this,DownloadServiceJson.class);
            this.startService(intent);

        }else{
            File file = new File(this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath(), jsonFileName + ".json");
            if(file.exists()){
                //ToDo: alte data.json verwenden (Schritt Datei runterzuladen überspringen)
            }else{
                //ToDo: Alternative implementieren. Was machen wir wenn es keine Internetverbindung + keine alte data.json gibt?
            }

        }



        //ToDo: Überprüfen, ob external Storage erreichbar ist (benötigen wir das? Eigentlich schreiben wir auf internal Storage. --> Prüfen, ob das einen Unterschied macht)


        //ToDo: Eigentlich benötigen wir diese Permission momentan nicht, falls wir sie aus dem Programm nehmen sollten, unbedingt auch an die Manifest denken!
        //getPermissionToReadExternalStorage();




        //TESTING
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList <Event> e = JsonController.getEventList();
                Intent i = new Intent(getApplicationContext(), ClubActivity.class);
                startActivity(i);
                for (Event evn : e){
                    System.out.println(evn.name);
                }
            }
        });
        //TESTING


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    //ToDo: Projektstruktur überdenken, wollen wir Methoden wie diese in der MainActivity stehen haben?
    //ToDo: Backup File checken.












/*

    ToDO: Permissionabfrage löschen falls nicht benötigt.

    public void getPermissionToReadExternalStorage() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
                Toast.makeText(this, "Wir benötigen die Berechtigung, um dir Clubs und Events anzeigen zu können", Toast.LENGTH_SHORT).show();
            }
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_ACCESS_CODE);

        }
        else {
            //ToDo: implement me
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_WRITE_ACCESS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Storage granted", Toast.LENGTH_SHORT).show();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

                if (showRationale) {
                    Toast.makeText(this, "Wir benötigen die Berechtigung, um dir Clubs und Events anzeigen zu können", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Berechtigung Daten zu lesen und zu schreiben verweigert.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    */


}
