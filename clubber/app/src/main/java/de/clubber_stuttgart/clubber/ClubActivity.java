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

        ArrayList<Club> clubList = JsonController.getClubList();
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        String[] from = {"clubName","clubAdrs","clubTel","clubLink"};
        int[] to = {R.id.club_name, R.id.club_adrs, R.id.club_tel, R.id.club_web};




        for(Club club : clubList){
            HashMap<String, String> map = new HashMap<>();
            String[] input = {club.name, club.adrs, club.tel, club.web};
            for(int i = 0; i < from.length; i++) {
                map.put(from[i],input[i]);
            }
            fillMaps.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, fillMaps, R.layout.adapter_view_layout_club, from, to);
        listViewClubs.setAdapter(simpleAdapter);
    }

}
