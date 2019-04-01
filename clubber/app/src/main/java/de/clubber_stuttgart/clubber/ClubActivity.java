package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        ListView listViewClubs = findViewById(R.id.clubListView);

        //get the current eventList
        List<HashMap<String, String>> fillMaps = JsonController.getClubList();

        //These are the key names, which save the values, which are to be displayed
        String[] from = {"name","adrs","tel","webLink"};
        //The values should be displayed in specified TextViews
        int[] to = {R.id.club_name, R.id.club_adrs, R.id.club_tel, R.id.club_link};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, fillMaps, R.layout.adapter_view_layout_club, from, to);
        listViewClubs.setAdapter(simpleAdapter);
    }
}
