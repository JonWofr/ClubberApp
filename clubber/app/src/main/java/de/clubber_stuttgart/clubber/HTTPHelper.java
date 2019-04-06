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

    @Override
    protected Object doInBackground(Object[] objects) {
        return requestResponseServer(idEvent, idClub);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    //method to be called on a HTTPHelper object to initiate the AsyncTask methods
    public void initiateServerCommunication(String idEvent, String idClub, Context context){
        this.context = context;
        this.idEvent = idEvent;
        this.idClub = idClub;
        this.execute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(o == null){
            //ToDo: Achtung kann nicht null sein --> was wenn json file mehr oder weniger leer ist und DB up to date ist?
        }else{
            //ToDo: Struktur der Objekte überlegen. Brauchen wir diese noch oder können wir direkt von jsonObject einspeichern?

            Log.i("HHTPhelper","WIR SPEICHERN DINGE EIN...");
            JsonController.createList(o.toString());
            ArrayList<Event> eventList = JsonController.getEventList();
            ArrayList<Club> clubList = JsonController.getClubList();

            DataBaseHelper dbHelper = new DataBaseHelper(context);

            for (Event event : eventList){
                dbHelper.insertEventEntry(event);
            }

            for (Club club : clubList){
                dbHelper.insertClubEntry(club);
            }
        }
    }

    private String requestResponseServer(String idEvent, String idClub){
        URL url;
        //TODO Send data and get response inside here
        String urlString = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=" + idEvent + "&idClub=" + idClub;
        //TODO hier das erst Argument für die Loggin methoden im kopf der klasse deklariern und konsistent machenn
        Log.i(this.getClass().toString(), urlString);
        StringBuilder jsonString = new StringBuilder();
        try {
            url = new URL(urlString);
            HttpURLConnection jsonResponse = (HttpURLConnection) url.openConnection();
            //TODO weitermachen bruh?!
            System.out.println(jsonResponse);

            InputStream in = new BufferedInputStream(jsonResponse.getInputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));

            String line = buf.readLine();
            //reads every line of the json file on at a time
            while (line != null) {
                //StringBuilder is concattinated correspondingly
                jsonString.append(line);
                //jumps to next line
                line = buf.readLine();
            }
            System.out.println(jsonString.toString());

        }catch (MalformedURLException e) {
            Log.w(this.getClass().toString(), "URL is invalid");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        return jsonString.toString();
    }
}

