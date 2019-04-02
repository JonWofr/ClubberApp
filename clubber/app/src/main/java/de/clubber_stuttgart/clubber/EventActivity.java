package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

public class EventActivity extends Activity {
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
    }
}
