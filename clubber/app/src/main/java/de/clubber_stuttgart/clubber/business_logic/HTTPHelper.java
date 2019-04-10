package de.clubber_stuttgart.clubber.business_logic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPHelper extends AsyncTask {

    private String idEvent;
    private String idClub;
    private Context context;
    final private String LOG = "HTTPHelper";

    //is called after execute() call and calls method requestResponseServer(). This method runs in its own thread
    @Override
    protected Object doInBackground(Object[] objects) {
        return requestResponseServer(idEvent, idClub);
    }

    //method to be called on a HTTPHelper object to initiate the AsyncTask methods
    void initiateServerCommunication(String idEvent, String idClub, Context context){
        this.context = context;
        this.idEvent = idEvent;
        this.idClub = idClub;
        this.execute();
    }

    //is called after doInBackground() finished its tasks and the return value of doInBackground() is passed to this method
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(o == null){
            //when null returned the user gets a response that the database is up to date
            //ToDo: Achtung kann momentan nicht null sein --> was wenn json file mehr oder weniger leer ist und DB up to date ist?
        }else{

            Log.i(LOG,"inserting received data to the database...");

            try {
                JSONObject jsonObject = new JSONObject(o.toString());
                Log.d(LOG, "A JSONObject has been created with the received data from the server");

                //Jsons's file content are two arrays "events" and "clubs"
                JSONArray jsonEventArray = jsonObject.getJSONArray("events");
                JSONArray jsonClubArray = jsonObject.getJSONArray("clubs");

                //context is needed to create this object, got passed inside this class via initiateServerCommunication()
                DataBaseHelper dbHelper = new DataBaseHelper(context);

                Log.i(LOG,"inserting event entries to the database...");
                //inserts every json event object into the db
                for (int countEntryE = 0; countEntryE < jsonEventArray.length(); countEntryE++){
                    dbHelper.insertEventEntry(jsonEventArray.getJSONObject(countEntryE));
                }
                Log.d(LOG, jsonEventArray.length() + " event/s has/have been inserted to the database");

                Log.i(LOG,"inserting club entries to the database...");
                //inserts every json club object into the db
                for (int countEntryC = 0; countEntryC < jsonClubArray.length(); countEntryC++){
                    dbHelper.insertClubEntry(jsonClubArray.getJSONObject(countEntryC));
                }
                Log.d(LOG, jsonClubArray.length() + " club/s has/have been inserted to the database");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //this method is necessary to establish the connection to the server, send data to the server and retrieve responses, which are dependant on the sent requests
    private String requestResponseServer(String idEvent, String idClub){
        //URL which leads to the php script on the server. Data for the request is sent via GET
        String urlString = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=" + idEvent + "&idClub=" + idClub;
        Log.d(LOG, "send request to following url: " + urlString);
        //this stringbuilder will be appended gradually and consists of the response of the server
        StringBuilder jsonString = new StringBuilder();
        try {
            URL url = new URL(urlString);
            //connect to the server
            HttpURLConnection jsonResponse = (HttpURLConnection) url.openConnection();
            Log.d(LOG,"Connection to " + jsonResponse + " established");
            //create inputstream which delivers server's response data
            InputStream in = new BufferedInputStream(jsonResponse.getInputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));

            //jumps to next line
            String line = buf.readLine();
            //reads every line of the data one at a time
            while (line != null) {
                //StringBuilder is concattinated correspondingly
                jsonString.append(line);
                //jumps to next line
                line = buf.readLine();
            }
        }catch (MalformedURLException e) {
            //ToDo: reicht hier eine Warnung oder stürzt die App danach ab?
            Log.w(LOG, "URL is invalid");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        Log.d(LOG, "Data of server response: " + jsonString);
        return jsonString.toString();
    }
}

