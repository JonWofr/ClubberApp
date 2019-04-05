package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClubActivity extends Activity {

    private RecyclerView cRecyclerView;
    //Adapter is Bridge between Data and the Recycler View - only provides as many items as we currently need
    private RecyclerView.Adapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        // ToDo: das Beispiel Club Event durch die Verbindung mit der Datenbank ersetzten
        //this is just an Club item example
        Club c = new Club(1,"Rakete","Haussmanstraße 43, 70186 Stuttgart","0711 467588", "www.rakete-stuttgart.de");

        //creates the Array list of Club items
        ArrayList<Club> clubList = new ArrayList<>();
        //just an example to test the Club xml and Adapter
        clubList.add(c);
        clubList.add(c);
        clubList.add(c);

        //connect with the recyclerView from the Layout
        cRecyclerView=findViewById(R.id.recycler_view2);
        // if the RecyclerView wont change in size it makes the performance better
        cRecyclerView.setHasFixedSize(true);
        //asign LayoutManager
        cLayoutManager = new LinearLayoutManager(this);
        //asign Adapter - pass the Arraylist into it
        cAdapter = new CardClubAdapter(clubList);
        //pass LayoutManger to RecyclerView
        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);


        /*ListView listViewClubs = findViewById(R.id.clubListView);

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

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, fillMaps, R.layout.card_item_club, from, to);
        listViewClubs.setAdapter(simpleAdapter);*/
    }

}
