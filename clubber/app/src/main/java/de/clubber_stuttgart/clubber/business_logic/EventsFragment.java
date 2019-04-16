package de.clubber_stuttgart.clubber.business_logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import de.clubber_stuttgart.clubber.CardEventAdapter;
import de.clubber_stuttgart.clubber.Event;
import de.clubber_stuttgart.clubber.R;


public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    final private String LOG = "EventFragment";
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
                Log.d(LOG, "Broadcast received. UI of EventsFragment is about to be updated");
                reloadFragment();
            }
        };

        //get the swipeLayout
        SwipeRefreshLayout refresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_events);
        //set RefreshListener
        refresh.setOnRefreshListener(this);
        Log.i(LOG,"OnRefreshListener has been set");
        //ad color sheme
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));

        //creates an Array List of event items
        ArrayList<Event> eventList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        Log.d(LOG, db.getPath());

        Bundle bundle = getArguments();

        Cursor cursor;

        //Intent does not have to contain any selected date (for example if the events tab is reached via the tab bar)
        if (bundle != null && bundle.containsKey("selectedDate")) {
            //custom query
            Log.i(LOG, "Events will be filtered for date " + bundle.getString("selectedDate"));
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
        boolean networkAccess = DBConnectionService.networkAccess;
        Log.i(LOG, "Check if there is network access... result: " + networkAccess);

        if (!networkAccess) {
            if (eventList.isEmpty()) {
                Log.w(LOG, "There are no entries in the database");
                //ToDo: Hier evtl. eher eine TextView einfügen.!!!!!!!!!!!!!
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

    //gets called when you pull down the events list = it reloads the Data in the RecyclerView
    @Override
    public void onRefresh() {
        Log.i(LOG, "recyclerView has been pulled down, trying to refresh...");
        Intent serviceIntent = new Intent(context, DBConnectionService.class);
        context.startService(serviceIntent);

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
