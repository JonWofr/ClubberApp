package de.clubber_stuttgart.clubber;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DBConnectionService extends IntentService {

    public DBConnectionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
