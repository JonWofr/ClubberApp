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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {

    final private String LOG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent startEventActIntent = new Intent(getApplicationContext(),EventActivity.class);
        final Intent startClubActIntent = new Intent(getApplicationContext(), ClubActivity.class);

        //ToDo: Das hier in eine Methode schreiben, da wir das auch ausführen müssen, wenn der Nutzer wieder Internet hat oder einen Refresh durchführen will.
        Log.i(LOG,"Checking if network is available...");
        if (isNetworkAvailable()) {

            Log.i(LOG,"Network is available");
            Log.i(LOG, "initiating setup and update of the database...");
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            String[] highestIds = dataBaseHelper.selectHighestIds();
            //init call to start async task which updates the database if needed.
            Log.i(LOG, "Requesting data from webserver database...");
            Log.d(LOG, "Requesting data from event table from following id:" + highestIds[0]);
            Log.d(LOG, "Requesting data from club table from following id:" + highestIds[1]);
            new HTTPHelper().initiateServerCommunication(highestIds[0], highestIds[1], this);


        } else {
            //ToDo: Broadcast empfangen, falls Netzwerk erreichbar? oder beim runterziehen der Listen Datenbank aktualisieren?
            Log.i(LOG,"There is no Network available");
            Log.d(LOG, "putExtra() on the Activity Intents to let them know there is no network available...");

            //gives the intent some more information about the connection --> "carefull activity! You need to consider this to give the user information on the UI"
            String noNetwork = "noNetwork";
            startEventActIntent.putExtra(noNetwork,true);
            startClubActIntent.putExtra(noNetwork,true);
        }


        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startEventActIntent);
            }
        });


        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startClubActIntent);
            }
        });


    }
    //ToDo: Überprüfen, ob external Storage erreichbar ist (benötigen wir das? Eigentlich schreiben wir auf internal Storage. --> Prüfen, ob das einen Unterschied macht)

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}



