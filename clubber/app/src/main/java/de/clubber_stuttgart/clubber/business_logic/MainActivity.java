package de.clubber_stuttgart.clubber.business_logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.clubber_stuttgart.clubber.R;

public class MainActivity extends Activity {

    final private String LOG = "MainActivity";
    private Intent startEventActIntent;
    private Intent startClubActIntent;
    private EditText txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set default date to current date
        txt = findViewById(R.id.editText);
        String currentDate = java.time.LocalDate.now().toString();
        txt.setText(currentDate);

        startEventActIntent = new Intent(this, EventActivity.class);
        startClubActIntent = new Intent(this, ClubActivity.class);

        initDBConnectionService();


        final Button eventBtn = findViewById(R.id.eventBtn);
        eventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startEventActIntent);
            }
        });

        Button eventWithDate = findViewById(R.id.button);
        eventWithDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startEventActIntent.putExtra("selectedDate", txt.getText().toString());
                startActivity(startEventActIntent);
            }
        });


        Button clubBtn = findViewById(R.id.button2);
        clubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startClubActIntent);
            }
        });

        Button refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG,"refresh button has been clicked, trying to refresh...");
                initDBConnectionService();
                //ToDo: Rückmeldung an den user, ob er schon up to date ist und ob der refresh erfolgreich war.
            }
        });


        //ToDo: Überprüfen, ob external Storage erreichbar ist (benötigen wir das? Eigentlich schreiben wir auf internal Storage. --> Prüfen, ob das einen Unterschied macht)

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void initDBConnectionService(){
        Log.i(LOG,"Checking if network is available...");
        String noNetwork = "noNetwork";

        if (isNetworkAvailable()) {
            Log.i(LOG,"Network is available");
            startEventActIntent.putExtra(noNetwork,false);
            startClubActIntent.putExtra(noNetwork,false);

            Intent serviceIntent = new Intent(this,DBConnectionService.class);
            this.startService(serviceIntent);

        } else {
            Log.i(LOG, "no network available");
            //gives the intent some more information about the connection --> "carefull activity! You need to consider this to give the user information on the UI"
            startEventActIntent.putExtra(noNetwork,true);
            startClubActIntent.putExtra(noNetwork,true);
        }
    }

}



