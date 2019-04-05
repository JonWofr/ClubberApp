package de.clubber_stuttgart.clubber;

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

public class HTTPHelper extends AsyncTask {

    private String idEvent;
    private String idClub;

    @Override
    protected Object doInBackground(Object[] objects) {
        return requestResponseServer(idEvent, idClub);
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    //method to be called on a HTTPHelper object to initiate the AsyncTask methods
    public void initiateServerCommunication(String idEvent, String idClub){
        this.idEvent = idEvent;
        this.idClub = idClub;
        this.execute();
    }

    private String requestResponseServer(String idEvent, String idClub){
        URL url;
        //TODO Send data and get response inside here
        String urlString = "https://clubber-stuttgart.de/script/scriptDB.php?idEvent=" + idEvent + "&idClub=" + idClub;
        //TODO hier das erst Argument für die Loggin methoden im kopf der klasse deklariern und konsistent machenn
        Log.i(this.getClass().toString(), urlString);
        try {
            url = new URL(urlString);
            HttpURLConnection jsonResponse = (HttpURLConnection) url.openConnection();
            //TODO weitermachen
            System.out.println(jsonResponse);

            InputStream in = new BufferedInputStream(jsonResponse.getInputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
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
        return "";
    }
}

