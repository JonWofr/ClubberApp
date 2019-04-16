package de.clubber_stuttgart.clubber.business_logic;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import de.clubber_stuttgart.clubber.CardClubAdapter;
import de.clubber_stuttgart.clubber.Club;
import de.clubber_stuttgart.clubber.R;


public class ClubsFragment extends Fragment {

    final private String LOG = "ClubFragment";
    private Context context;


    public ClubsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity().getApplicationContext();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clubs, container, false);

        //creates an Array List of event items
        ArrayList<Club> clubList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        //columns --> which should be selected (null = all)
        //selection null return all the rows (WHERE statement)
        //selectionArgs where column is something (e.g. date)
        Cursor cursor = db.query(true, DataBaseHelper.TABLE_NAME_CLUBS, null, null, null, null, null, "name asc", null);

        while (cursor.moveToNext()) {
            clubList.add(new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }

        cursor.close();

        boolean networkAccess = DBConnectionService.networkAccess;
        Log.i(LOG, "Check if there is network access... result: " + networkAccess);

        if (!networkAccess) {
            if (clubList.isEmpty()) {
                Log.w(LOG, "There are no entries in the database");
                //ToDo: Hier evtl. eher eine TextView einfügen.
                Toast.makeText(context, "Keine Clubs vorhanden, bitte stelle eine Internetverbindung her.", Toast.LENGTH_LONG).show();
            } else {
                Log.i(LOG, "There are entries in the database but they might not be up to date");
                createRecyclerView(view, clubList);
                //prints information about the lack of network access
                Toast.makeText(context, "Achtung, keine Internetverbindung. Clubs eventuell unvollständig", Toast.LENGTH_LONG).show();
            }
        } else{
            createRecyclerView(view, clubList);
        }
        return view;
    }

    private void createRecyclerView(View view, ArrayList<Club> clubList){
        //connect with the recyclerView from the Layout
        RecyclerView cRecyclerView = view.findViewById(R.id.recycler_view2);
        // if the RecyclerView wont change in size it makes the performance better
        cRecyclerView.setHasFixedSize(true);
        //asign LayoutManager
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(context);
        //asign Adapter - pass the Arraylist into it
        //Adapter is Bridge between Data and the Recycler View - only provides as many items as we currently need
        RecyclerView.Adapter cAdapter = new CardClubAdapter(clubList);
        //pass LayoutManger to RecyclerView
        cRecyclerView.setLayoutManager(cLayoutManager);
        Log.d(LOG,"LayoutManager has been set");
        cRecyclerView.setAdapter(cAdapter);
        Log.d(LOG,"Adapter has been set");
    }
}
