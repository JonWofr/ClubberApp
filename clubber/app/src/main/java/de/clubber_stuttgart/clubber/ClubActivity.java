package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ClubActivity extends Activity {

    final private String LOG = "ClubActivity";
    private RecyclerView cRecyclerView;
    //Adapter is Bridge between Data and the Recycler View - only provides as many items as we currently need
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;
    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        //creates an Array List of event items
        ArrayList<Club> clubList = new ArrayList<>();
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        //columns --> which should be selected (null = all)
        //selection null return all the rows (WHERE statement)
        //selectionArgs where blabla is something (e.g. date)
        //ToDo: orderby einfügen
        Cursor cursor = db.query(true, DataBaseHelper.TABLE_NAME_CLUBS, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            clubList.add(new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }


        boolean noNetwork = getIntent().getBooleanExtra("noNetwork", false);
        Log.i(LOG, "Check if there is network access... result: " + !noNetwork);

        if (noNetwork) {
            if (clubList.isEmpty()) {
                Log.w(LOG, "There are no entries in the database");
                //ToDo: Hier evtl. eher eine TextView einfügen.
                Toast.makeText(this, "Keine Clubs vorhanden, bitte stelle eine Internetverbindung her.", Toast.LENGTH_LONG).show();
            } else {
                Log.i(LOG, "There are entries in the database but they might not be up to date");
                //prints information about the lack of network access
                Toast.makeText(this, "Achtung, keine Internetverbindung. Clubs eventuell unvollständig", Toast.LENGTH_LONG).show();
            }
        }

        //connect with the recyclerView from the Layout
        cRecyclerView = findViewById(R.id.recycler_view2);
        // if the RecyclerView wont change in size it makes the performance better
        cRecyclerView.setHasFixedSize(true);
        //asign LayoutManager
        cLayoutManager = new LinearLayoutManager(this);
        //asign Adapter - pass the Arraylist into it
        cAdapter = new CardClubAdapter(clubList);
        //pass LayoutManger to RecyclerView
        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);


    }

}
