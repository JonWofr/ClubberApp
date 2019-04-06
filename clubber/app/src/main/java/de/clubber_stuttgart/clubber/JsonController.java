package de.clubber_stuttgart.clubber;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class JsonController extends Thread {

    //lists to be used in "Veranstaltungen" und "Clubs" section of the app
    private static ArrayList<Event> eventList = new ArrayList<Event>();
    private static ArrayList<Club> clubList = new ArrayList<Club>();
    private Context context;

    //called when the download inside DownloadReceiver is finished and the buffer reads its contents
    public static void createList(String jsonString) {
        try {
            //JSONObject will be created with String given due to createJsonString() method
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.i("JsonController", "The JSONObject has been succesfully created.");

            //Jsons's file content are two arrays "events" and "clubs"
            JSONArray jsonEventArray = jsonObject.getJSONArray("events");
            JSONArray jsonClubArray = jsonObject.getJSONArray("clubs");

            //each array consists of either event or club objects. Here we transform the Json event objects into normal java Event objects with corresponding attributes
            for (int i = 0; i < jsonEventArray.length(); i++) {

                JSONObject jsonEventObject = jsonEventArray.getJSONObject(i);


                //Fetching all data from the Json object
                int id = jsonEventObject.getInt("id");
                String dte = jsonEventObject.getString("dte");
                String name = jsonEventObject.getString("name");
                String club = jsonEventObject.getString("club");
                String srttime = jsonEventObject.getString("srttime");
                String btn = jsonEventObject.getString("btn");
                String genre = jsonEventObject.getString("genre");

                //we create a new Event object, which will be stored inside an ArrayList of type Event
                Event evn = new Event(id, dte, name, club, srttime, btn, genre);
                eventList.add(evn);
                Log.i("", "Event " + evn.name + " has been added to the list.");
            }


            //Each array consists of either event or club objects. Here we save each Json object into an object of Hashmap with corresponding key-value pairs
            for (int i = 0; i < jsonClubArray.length(); i++) {

                JSONObject jsonClubObject = jsonClubArray.getJSONObject(i);

                //Fetching all data from the Json object
                int id = jsonClubObject.getInt("id");
                String name = jsonClubObject.getString("name");
                String adrs = jsonClubObject.getString("adrs");
                String tel = jsonClubObject.getString("tel");
                String web = jsonClubObject.getString("web");

                //we create a new Club object, which will be stored inside an ArrayList of type Club
                Club clb = new Club(id, name, adrs, tel, web);
                clubList.add(clb);
                Log.i("", "Club " + clb.name + " has been added to the list.");
            }
        }
        //TODO catch if var jsonString has not been initialized (e.g.) if smartphone has no internet connection and run() got called
        catch (JSONException jsonE) {
            Log.w("JsonController", "The json file does not contain valid json, errortext: " + jsonE);
        }
    }


    //Getter for the current eventList
    public static ArrayList getEventList() {
        return eventList;
    }

    //Getter for the current clubList
    public static ArrayList getClubList() {
        return clubList;
    }



}
