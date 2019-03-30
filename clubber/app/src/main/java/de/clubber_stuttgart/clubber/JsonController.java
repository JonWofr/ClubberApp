package de.clubber_stuttgart.clubber;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonController extends Thread {

    private String jsonString;
    //lists to be used in "Veranstaltungen" und "Clubs" section of the app
    private static ArrayList <Event> eventList = new ArrayList<Event>();
    private static ArrayList <Club> clubList = new ArrayList<Club>();

    //runs in own thread
    @Override
    public void run () {
        try {
            //JSONObject will be created with String given due to createList() method
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.i("JsonController: ", "The JSONObject has been succesfully created.");

            //Jsons's file content are two arrays "events" and "clubs"
            JSONArray jsonEventArray = jsonObject.getJSONArray("events");
            JSONArray jsonClubArray = jsonObject.getJSONArray("clubs");

            //each array consists of either event or club objects. Here we transform the Json event objects into normal java Event objects with corresponding attributes
            for (int i = 0; i < jsonEventArray.length(); i++){
                JSONObject jsonEventObject = jsonEventArray.getJSONObject(i);
                String dte = jsonEventObject.getString("dte");
                String name = jsonEventObject.getString("name");
                String club = jsonEventObject.getString("club");
                String adrs = jsonEventObject.getString("adrs");
                String srttime = jsonEventObject.getString("srttime");
                String endtime = jsonEventObject.getString("endtime");
                String price = jsonEventObject.getString("price");
                String soldas = jsonEventObject.getString("soldas");
                String buttonLink = jsonEventObject.getString("button");
                String genre = jsonEventObject.getString("genre");
                String occ = jsonEventObject.getString("occ");

                //we create a new Event object, which will be stored inside an ArrayList of type Event
                Event evn = new Event(dte, name, club, adrs, srttime, endtime, price, soldas, buttonLink, genre, occ);
                eventList.add(evn);
                Log.i("", "Event " + evn.name + " has been added to the list.");
            }

            //each array consists of either event or club objects. Here we transform the Json club objects into normal java Club objects with corresponding attributes
            for (int i = 0; i < jsonClubArray.length(); i++){
                JSONObject jsonClubObject = jsonClubArray.getJSONObject(i);
                String name = jsonClubObject.getString("name");
                String adrs = jsonClubObject.getString("adrs");
                String tel = jsonClubObject.getString("tel");
                String webLink = jsonClubObject.getString("web");

                //we create a new Club object, which will be stored inside an ArrayList of type Club
                Club clb = new Club(name, adrs, tel, webLink);
                clubList.add(clb);
                Log.i("", "Club " + clb.name + " has been added to the list.");
            }
            }
            //TODO catch if var jsonString has not been initialized (e.g.) if smartphone has no internet connection and run() got called
            catch (JSONException jsonE) {
            Log.w("DownloadReceiver: ", "The json file does not contain valid json, errortext: " + jsonE);
        }

    }

    //called when the download inside DownloadReceiver is finished and the buffer read its contents
    public void createList (String jsonString) {
        this.jsonString = jsonString;
        this.run();
    }

    //Getter for the current eventList
    public static ArrayList getEventList(){
        return eventList;
    }

    //Getter for the current clubList
    public static ArrayList getClubList(){
        return clubList;
    }
    }
