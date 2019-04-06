package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    //ToDo: Pfade mit Variablen vereinfachen
    //variable is needed to create the absolute path for the json file. (package path)
    //String dirType = this.getFilesDir().getAbsolutePath();
    //path where json file is saved to
    //String pathJson = this.getExternalFilesDir( this.getFilesDir().getAbsolutePath()).getAbsolutePath() + "data.json";

    private static final int REQUEST_WRITE_ACCESS_CODE = 0;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonFileName = "data", newJsonFileName = "backup_data";


        if (isNetworkAvailable()) {

            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            String[] highestIds = dataBaseHelper.selectHighestIds();

            //init call to start async task which updates the database if needed.
            new HTTPHelper().initiateServerCommunication(highestIds[0],highestIds[1],this);



        } else {
            //ToDo: einfache Abfrage der lokalen DB und Fehlermeldung, falls noch keine Einträge vorhanden sind.
        }


        //ToDo: Überprüfen, ob external Storage erreichbar ist (benötigen wir das? Eigentlich schreiben wir auf internal Storage. --> Prüfen, ob das einen Unterschied macht)


        //ToDo: Eigentlich benötigen wir diese Permission momentan nicht, falls wir sie aus dem Programm nehmen sollten, unbedingt auch an die Manifest denken!
        //getPermissionToReadExternalStorage();


        //TESTING
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(itn);
            }
        });
        //TESTING

        //TESTING
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(getApplicationContext(), ClubActivity.class);
                startActivity(itn);
            }
        });
        //TESTING


    }



    //ToDo: Projektstruktur überdenken, wollen wir Methoden wie diese in der MainActivity stehen haben?
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}















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


