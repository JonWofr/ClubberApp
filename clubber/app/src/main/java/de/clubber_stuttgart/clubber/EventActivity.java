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

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ListView listView = (ListView) findViewById(R.id.eventListView);

        //get the current eventList
        List<HashMap<String, String>> eventList= JsonController.getEventList();

        //These are the key names, which save the values, which are to be displayed
        String[] from = new String[]{"dte", "name", "club", "adrs", "srttime" };
        //The values should be displayed in specified TextViews
        int[] to = new int[]{R.id.eventTextView1, R.id.eventTextView2, R.id.eventTextView3, R.id.eventTextView4, R.id.eventTextView5};

        SimpleAdapter adapter = new SimpleAdapter(this,eventList, R.layout.activity_event, from, to);
        listView.setAdapter(adapter);
    }*/

    //ToDo: die Navigation funktioniert nicht:  Error inflating class android.support.design.widget.BottomNavigationView

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DataBaseHelper dataBaseHelper = new DataBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ArrayList<Event> card_items = new ArrayList<>();
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        Log.d("EventActivity",db.getPath());

        //columns --> which should be selected (null = all)
        //selection null return all the rows (WHERE statement)
        //selectionArgs where blabla is something (e.g. date)
        //ToDo: orderby einfügen
        Cursor cursor = db.query(DataBaseHelper.TABLE_NAME_EVENTS, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            card_items.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6)));
        }



        mRecyclerView = findViewById(R.id.recycler_view);
        //wenn man weiß, dass sich die Größe des RecyclerView nicht verändern wird
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CardEventAdapter(card_items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }





}
