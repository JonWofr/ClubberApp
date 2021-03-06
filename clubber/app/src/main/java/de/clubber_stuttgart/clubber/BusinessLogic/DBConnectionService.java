package de.clubber_stuttgart.clubber.BusinessLogic;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import de.clubber_stuttgart.clubber.UI.MainActivity;


public class DBConnectionService extends IntentService {

    public DBConnectionService() {
        super("DBConnectionService");
    }

    final private String LOG = "DBConnectionService";

    //to check internet connection in other fragments (needed to give user feedback)
    public static boolean hasNetworkAccess;


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(LOG,"Checking if network is available...");

        hasNetworkAccess = isNetworkAvailable();
        if (hasNetworkAccess) {
            Log.i(LOG,"Network is available");
            Log.i(LOG, "initiating setup and update of the database...");
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            //deletes all entries older than yesterday
            dataBaseHelper.deleteOldEntries();
            String[] highestIds = dataBaseHelper.selectHighestIds();
            //init call to start async task which updates the database if needed.
            Log.i(LOG, "Requesting data from webserver database...");
            Log.d(LOG, "Requesting data from event table from following id:" + highestIds[0]);
            Log.d(LOG, "Requesting data from club table from following id:" + highestIds[1]);
            new HTTPHelper().initiateServerCommunication(highestIds[0], highestIds[1], this);
        }else{
            Log.i(LOG, "no network available");
            //gives the fragments some more information about the connection --> "carefull! You need to consider this to give the user information on the UI"
            hasNetworkAccess = false;
        }
    }

    @Override
    public void onDestroy() {
        if(!MainActivity.initialStartRequestResponseServer && !hasNetworkAccess){
            Log.i(LOG,"refresh not possible because no network");
            Toast.makeText(this, "keine Aktualisierung möglich, bitte stelle eine Internetverbindung her", Toast.LENGTH_LONG).show();
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}



