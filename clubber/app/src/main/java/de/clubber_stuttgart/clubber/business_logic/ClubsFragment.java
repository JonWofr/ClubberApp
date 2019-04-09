package de.clubber_stuttgart.clubber.business_logic;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
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

    final private String LOG = "ClubActivity";

    private RecyclerView cRecyclerView;
    //Adapter is Bridge between Data and the Recycler View - only provides as many items as we currently need
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;
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
        //selectionArgs where blabla is something (e.g. date)
        //ToDo: orderby einfügen
        Cursor cursor = db.query(true, DataBaseHelper.TABLE_NAME_CLUBS, null, null, null, null, null, "name asc", null);

        while (cursor.moveToNext()) {
            clubList.add(new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }


        //ToDo: Das hier wird nicht so gehen, muss mit bundle gelöst werden..
        boolean noNetwork = getActivity().getIntent().getBooleanExtra("noNetwork", false);
        Log.i(LOG, "Check if there is network access... result: " + !noNetwork);

        if (noNetwork) {
            if (clubList.isEmpty()) {
                Log.w(LOG, "There are no entries in the database");
                //ToDo: Hier evtl. eher eine TextView einfügen.
                Toast.makeText(context, "Keine Clubs vorhanden, bitte stelle eine Internetverbindung her.", Toast.LENGTH_LONG).show();
            } else {
                Log.i(LOG, "There are entries in the database but they might not be up to date");
                //prints information about the lack of network access
                Toast.makeText(context, "Achtung, keine Internetverbindung. Clubs eventuell unvollständig", Toast.LENGTH_LONG).show();
            }
        }

        //connect with the recyclerView from the Layout
        cRecyclerView = view.findViewById(R.id.recycler_view2);
        // if the RecyclerView wont change in size it makes the performance better
        cRecyclerView.setHasFixedSize(true);
        //asign LayoutManager
        cLayoutManager = new LinearLayoutManager(context);
        //asign Adapter - pass the Arraylist into it
        cAdapter = new CardClubAdapter(clubList);
        //pass LayoutManger to RecyclerView
        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);



        return view;
    }

}
