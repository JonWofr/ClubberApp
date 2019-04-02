package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ArrayList<CardItemEvent> card_items = new ArrayList<>();
        card_items.add(new CardItemEvent("Ladies Night", "#mixed","28.03","20:00","Schräglage"));
        card_items.add(new CardItemEvent("Halloween Special", "#mixed","28.03","20:00","Perkins Park"));
        card_items.add(new CardItemEvent("Tanz in den Mai", "#HipHop","28.03","20:00","7Grad"));
        card_items.add(new CardItemEvent("Ladies Night", "#mixed","28.03","20:00","Schräglage"));
        card_items.add(new CardItemEvent("Ladies Night", "#mixed","28.03","20:00","Schräglage"));
        card_items.add(new CardItemEvent("Ladies Night", "#mixed","28.03","20:00","Schräglage"));


        mRecyclerView = findViewById(R.id.recycler_view);
        //wenn man weiß, dass sich die Größe des RecyclerView nicht verändern wird
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CardEventAdapter(card_items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


}
