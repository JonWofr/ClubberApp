package de.clubber_stuttgart.clubber;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HTTPHelper extends AsyncTask {

    private String idEvent;
    private String idClub;
    private  Context context;

    //is called after execute() call and calls method requestResponseServer(). This method runs in its own thread
    @Override
    protected Object doInBackground(Object[] objects) {
        return requestResponseServer(idEvent, idClub);
    }

    //method to be called on a HTTPHelper object to initiate the AsyncTask methods
    public void initiateServerCommunication(String idEvent, String idClub, Context context){
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
            //ToDo: Achtung kann nicht null sein --> was wenn json file mehr oder weniger leer ist und DB up to date ist?
        }else{
            //ToDo: Struktur der Objekte überlegen. Brauchen wir diese noch oder können wir direkt von jsonObject einspeichern?

            Log.i(this.getClass().toString(),"New data is about to be added to the local db");
            JsonController.createList(o.toString());
            ArrayList<Event> eventList = JsonController.getEventList();
            ArrayList<Club> clubList = JsonController.getClubList();

            //context is needed to create this object, got passed inside this class via initiateServerCommunication()
            DataBaseHelper dbHelper = new DataBaseHelper(context);

            //inserts every event object into the db
            for (Event event : eventList){
                dbHelper.insertEventEntry(event);
            }
            //inserts every club object into the db
            for (Club club : clubList){
                dbHelper.insertClubEntry(club);
            }
        }
    }

    //this method is necessary to establish the connection to the server, send data to the server and retrieve responses, which are dependant
    //on the sent requests
    private String requestResponseServer(String idEvent, String idClub){
        //URL which leads to the php script on the server. Data for the request is sent via GET
        String urlString = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=" + idEvent + "&idClub=" + idClub;
        //TODO hier das erst Argument für die Loggin methoden im kopf der klasse deklariern und konsistent machenn
        Log.i(this.getClass().toString(), urlString);
        //this stringbuilder will be appended gradually and consists of the response of the server
        StringBuilder jsonString = new StringBuilder();
        try {
            URL url = new URL(urlString);
            //connect to the server
            HttpURLConnection jsonResponse = (HttpURLConnection) url.openConnection();
            //TODO weitermachen bruh?!
            System.out.println(jsonResponse);

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
            Log.i(this.getClass().toString(), "Data of the server: " + jsonString);
        }catch (MalformedURLException e) {
            Log.w(this.getClass().toString(), "URL is invalid");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        return jsonString.toString();
    }
}

