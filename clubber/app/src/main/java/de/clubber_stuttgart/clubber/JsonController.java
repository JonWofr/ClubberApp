package de.clubber_stuttgart.clubber;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonController extends Thread {

    private String jsonString;
    //lists to be used in "Veranstaltungen" und "Clubs" section of the app
    private static List<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
    private static List<HashMap<String, String>> clubList = new ArrayList<HashMap<String, String>>();

    //runs in own thread
    @Override
    public void run() {
        try {
            //JSONObject will be created with String given due to createList() method
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.i("JsonController --> ", "The JSONObject has been succesfully created.");

            //Jsons's file content are two arrays "events" and "clubs"
            JSONArray jsonEventArray = jsonObject.getJSONArray("events");
            JSONArray jsonClubArray = jsonObject.getJSONArray("clubs");

            //Each array consists of either event or club objects. Here we save each Json object into an object of Hashmap with corresponding key-value pairs
            for (int i = 0; i < jsonEventArray.length(); i++) {

                JSONObject jsonEventObject = jsonEventArray.getJSONObject(i);

                HashMap<String, String> eventHash = new HashMap <String, String>();

                //Fetching all data from the Json object
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

                //Putting data into Hashmap
                eventHash.put("dte", dte);
                eventHash.put("name", name);
                eventHash.put("club", club);
                eventHash.put("adrs", adrs);
                eventHash.put("srttime", srttime);
                eventHash.put("endtime", endtime);
                eventHash.put("price", price);
                eventHash.put("soldas", soldas);
                eventHash.put("buttonLink", buttonLink);
                eventHash.put("genre", genre);
                eventHash.put("occ", occ);

                //Adding Hashmap to the List, which consists of as many Hashmaps as there are Event objects
                eventList.add(eventHash);
                Log.i("JsonController --> ", "Event \"" + eventHash.get("name") + "\" has been successfully added to the list.");
            }

            //Each array consists of either event or club objects. Here we save each Json object into an object of Hashmap with corresponding key-value pairs
            for (int i = 0; i < jsonClubArray.length(); i++) {

                JSONObject jsonClubObject = jsonClubArray.getJSONObject(i);

                HashMap<String, String> clubHash = new HashMap <String, String>();

                //Fetching all data from the Json object
                String name = jsonClubObject.getString("name");
                String adrs = jsonClubObject.getString("adrs");
                String tel = jsonClubObject.getString("tel");
                String webLink = jsonClubObject.getString("web");

                //Putting data into Hashmap
                clubHash.put("name", name);
                clubHash.put("adrs", adrs);
                clubHash.put("tel", tel);
                clubHash.put("webLink", webLink);

                //Adding Hashmap to the List, which consists of as many Hashmaps as there are Club objects
                clubList.add(clubHash);

                Log.i("JsonController --> ", "Club \"" + clubHash.get("name") + "\" has been successfully added to the list.");
            }
        }
        //TODO catch if var jsonString has not been initialized (e.g.) if smartphone has no internet connection and run() got called
        catch (JSONException jsonE) {
            Log.w("JsonController --> ", "The json file does not contain valid json, errortext: " + jsonE);
        }

    }

    //ToDo: static
    //called when the download inside DownloadReceiver is finished and the buffer read its contents
    public void createList(String jsonString) {
        this.jsonString = jsonString;
        this.run();
    }

    //Getter for the current eventList
    public static List getEventList() {
        return eventList;
    }

    //Getter for the current clubList
    public static List getClubList() {
        return clubList;
    }

    static void deleteOldJsonFiles(String filename, String path) {
        try {
            String iter = "";
            int i = 0;
            //we delete every old json file to make room in order to replace it with a new one
            while (true) {
                File file = new File(path + "/" + filename + iter + ".json");
                //only deletes if file exists. If there is no file to delete it will break out of the loop.
                if (!Files.deleteIfExists(file.toPath())) {
                    if (i == 0) {
                        Log.i("deleteOldJsonFiles", "No files to delete. Method ends");
                    } else {
                        Log.i("deleteOldJsonFiles", "No files left to delete. Method ends.");
                    }
                    break;
                } else {
                    i++;
                    //iterates ending of the filename if there are multiple files called data.json
                    iter = "-" + i;
                }
            }
        } catch (IOException e) {
            Log.e("mainActivity", "An IOException in deleteIfExists function has occured");
            e.printStackTrace();
        }
    }

    static void renameJson(String oldName, String newName, String path) {
        File from = new File(path, oldName + ".json");
        File to = new File(path, newName + ".json");
        if (from.exists()) {
            if (from.renameTo(to)) {
                Log.i("renameJson", "Successful, backup created");
            } else {
                Log.i("renameJson", "Unsuccessful, no backup created");

                //ToDo: Das hier tun
                Log.i("MainActivity", "should try to download json without backup");
            }
        } else {
            Log.i("renameJson", "File doesn't exist");
            Log.i("MainActivity", "should download json");
        }
    }
}
