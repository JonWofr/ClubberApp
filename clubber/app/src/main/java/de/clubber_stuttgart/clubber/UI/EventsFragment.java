package de.clubber_stuttgart.clubber.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.clubber_stuttgart.clubber.BusinessLogic.DBConnectionService;
import de.clubber_stuttgart.clubber.BusinessLogic.DataBaseHelper;
import de.clubber_stuttgart.clubber.UI.CardEventAdapter;
import de.clubber_stuttgart.clubber.UI.Event;
import de.clubber_stuttgart.clubber.R;


public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    final private String LOG = "EventFragment";
    private BroadcastReceiver dbConnectionServiceHasFinished;
    private Context context;
    private SwipeRefreshLayout refresh;
    private RecyclerView eRecyclerView;
    private ImageView eImageView;
    private TextView eTextView;



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

        //get the RecyclerView
        eRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        eImageView = (ImageView) view.findViewById(R.id.sad_smiley);
        eTextView = (TextView) view.findViewById(R.id.no_events);
        //When Broadcast is received UI is updated
        dbConnectionServiceHasFinished = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOG, "Broadcast received. UI of EventsFragment is about to be updated");
                reloadFragment();
            }
        };

        //get the swipeLayout
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_events);
        //set RefreshListener
        refresh.setOnRefreshListener(this);
        Log.i(LOG,"OnRefreshListener has been set");
        //ad color sheme
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary));

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        //contains information about a potentially selected date
        Bundle bundle = getArguments();
        //creates an Array List of event items
        ArrayList<Event> eventList = dataBaseHelper.getEventArrayList(bundle);

        boolean networkAccess = DBConnectionService.networkAccess;
        Log.i(LOG, "Check if there is network access... result: " + networkAccess);

        if (eventList.isEmpty()){
            Log.d(LOG, "There are no entries in the database for given query");
            if (!networkAccess){
                Toast.makeText(context, "Es sind keine Events für die Suche vorhanden, bitte stelle eine Internetverbindung her, um sicher zu gehen, dass die Daten aktuell sind.", Toast.LENGTH_LONG).show();
            }
            eRecyclerView.setVisibility(View.GONE);
            eImageView.setVisibility(View.VISIBLE);
            eTextView.setVisibility(View.VISIBLE);
        }
        else {
            if (!networkAccess){
                Log.i(LOG, "There are entries in the database but they might not be up to date");
                //prints information about the lack of network access
                Toast.makeText(context, "Achtung, keine Internetverbindung. Events eventuell unvollständig", Toast.LENGTH_LONG).show();
            }
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
        //refresh stops after the Toast appears
        refresh.setRefreshing(false);
        Log.d(LOG," stop onRefreshListener when Toast appears");
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
