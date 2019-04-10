package de.clubber_stuttgart.clubber.business_logic;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DBConnectionService extends IntentService {

    public DBConnectionService() {
        super("DBConnectionService");
    }

    final private String LOG = "DBConnectionService";

    @Override
    protected void onHandleIntent(Intent intent) {
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
    }
}



