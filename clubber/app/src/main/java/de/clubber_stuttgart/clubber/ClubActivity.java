package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class ClubActivity extends Activity {

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
