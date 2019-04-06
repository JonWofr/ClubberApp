package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventActivity extends Activity {


    //ToDo: die Navigation funktioniert nicht:  Error inflating class android.support.design.widget.BottomNavigationView

    private RecyclerView eRecyclerView;
    private RecyclerView.Adapter eAdapter;
    private RecyclerView.LayoutManager eLayoutManager;

    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //creates an Array List of event items
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        Log.d(this.getClass().toString(), db.getPath());

        //columns --> which should be selected (null = all)
        //selection null return all the rows (WHERE statement)
        //selectionArgs where blabla is something (e.g. date)
        //ToDo: orderby einfügen
        Cursor cursor = db.query(true, DataBaseHelper.TABLE_NAME_EVENTS, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            eventList.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6)));
        }



        eRecyclerView = findViewById(R.id.recycler_view);
        //wenn man weiß, dass sich die Größe des RecyclerView nicht verändern wird
        eRecyclerView.setHasFixedSize(true);
        eLayoutManager = new LinearLayoutManager(this);
        eAdapter = new CardEventAdapter(eventList, getApplicationContext());
        eRecyclerView.setLayoutManager(eLayoutManager);
        eRecyclerView.setAdapter(eAdapter);

    }





}
