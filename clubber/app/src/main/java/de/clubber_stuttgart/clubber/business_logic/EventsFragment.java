package de.clubber_stuttgart.clubber.business_logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import de.clubber_stuttgart.clubber.CardEventAdapter;
import de.clubber_stuttgart.clubber.Event;
import de.clubber_stuttgart.clubber.R;


public class EventsFragment extends Fragment {

    //ToDo: die Navigation funktioniert nicht:  Error inflating class android.support.design.widget.BottomNavigationView
    final private String LOG = "EventActivity";
    private BroadcastReceiver dbConnectionServiceHasFinished;
    private Context context;


    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        //When Broadcast is received UI is updated
        dbConnectionServiceHasFinished = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(this.getClass().toString(), "Broadcast received UI of EventsFragment is about to be updated");
                reloadFragment();
            }
        };

        //on button click the server will be contacted and new event or club entries might be stored into the local db
        Button refreshBtn = view.findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Log.i(LOG, "refresh button has been clicked, trying to refresh...");
                                              //ToDo: Rückmeldung an den user, ob er schon up to date ist und ob der refresh erfolgreich war.
                                              Intent serviceIntent = new Intent(context, DBConnectionService.class);
                                              context.startService(serviceIntent);
                                          }
                                      }
        );


        //creates an Array List of event items
        ArrayList<Event> eventList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        Log.d(this.getClass().toString(), db.getPath());

        Bundle bundle = getActivity().getIntent().getExtras();
        Cursor cursor;

        //Intent does not have to contain any selected date (for example if the events tab is reached via the tab bar)
        if (bundle != null && bundle.containsKey("selectedDate")) {
            //custom query
            Log.i(this.getClass().toString(), "Events will be filtered for date " + bundle.getString("selectedDate"));
            cursor = db.query(DataBaseHelper.TABLE_NAME_EVENTS, null, "dte = ?", new String[]{bundle.getString("selectedDate")}, null, null, "dte, srttime asc", null);
        } else {
            //default query
            cursor = db.query(DataBaseHelper.TABLE_NAME_EVENTS, null, null, null, null, null, "dte, srttime asc", null);
        }

        while (cursor.moveToNext()) {
            eventList.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
        }
        cursor.close();


        //ToDo: Funktioniert das nach der Implementierung der Navigation; Kommentar entfernen und testen
        boolean networkAccess = getArguments().getBoolean("networkAccess");
        Log.i(LOG, "Check if there is network access... result: " + networkAccess);

        if (!networkAccess) {
            if (eventList.isEmpty()) {
                Log.w(LOG, "There are no entries in the database");
                //ToDo: Hier evtl. eher eine TextView einfügen.
                Toast.makeText(context, "Keine Events vorhanden, bitte stelle eine Internetverbindung her.", Toast.LENGTH_LONG).show();
            } else {
                Log.i(LOG, "There are entries in the database but they might not be up to date");
                createRecyclerView(view, eventList);
                //prints information about the lack of network access
                Toast.makeText(context, "Achtung, keine Internetverbindung. Events eventuell unvollständig", Toast.LENGTH_LONG).show();
            }
        } else {
            createRecyclerView(view, eventList);
        }
        return view;
    }

    //gets called every time this activity gets into focus
    @Override
    public void onResume() {
        super.onResume();

        //Register created Receiver object and give it the specified action
        LocalBroadcastManager.getInstance(context).registerReceiver(dbConnectionServiceHasFinished, new IntentFilter("notifyEventFragment"));
    }

    //gets called every time this activity gets out of focus
    @Override
    public void onPause() {
        super.onPause();

        //Unsubscribe the receiver from the BroadcastManager
        LocalBroadcastManager.getInstance(context).unregisterReceiver(dbConnectionServiceHasFinished);
    }

    private void createRecyclerView(View view, ArrayList<Event> eventList) {
        RecyclerView eRecyclerView = view.findViewById(R.id.recycler_view);
        //we know that the size of the list won't change and is comparably small
        eRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(context);
        RecyclerView.Adapter eAdapter = new CardEventAdapter(eventList, context);
        eRecyclerView.setLayoutManager(eLayoutManager);
        eRecyclerView.setAdapter(eAdapter);
    }

    //needed to update the ui
    private void reloadFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
