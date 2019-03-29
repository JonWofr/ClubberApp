package de.clubber_stuttgart.clubber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DownloadReceiver extends BroadcastReceiver {

    //the global JSONObject for the "Veranstaltungen-" and "Clubs-" tab
    private static JSONObject jsonobj;

    //ToDo: Achtung, Kommentar könnte sich noch bezüglich onStart und onStop ändern.
    //receives DOWNLOAD_COMPLETE broadcast, which is registered in the onStart method (and unregistered in the onStop method)

    @Override
    public void onReceive(Context context, Intent intent) {

        File file = new File(context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json");
        //ToDo: Andere Lösung finden mit queueId, da auch nach dem Download der Json neue Downloads durchgeführt werden könnten und das Programm ausgeführt werden würde.

        //checks if this is the download from our download service.
        if (file.exists()) {
            Log.d("BroadcastReceiver", "Json File download complete!");

            String result = "";
            String filename = context.getExternalFilesDir(context.getFilesDir().getAbsolutePath()).getAbsolutePath() + "/data.json";
            try {
                //BufferedReader is said to be more performance friendly than reading the whole file at once
                BufferedReader br = new BufferedReader(new FileReader(filename));
                //the StringBuilder is a not as performance heavy as appending a String gradually
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                //reads every line of the json file on at a time
                while (line != null) {
                    //StringBuilder is concattinated correspondingly
                    sb.append(line);
                    //jumps to next line
                    line = br.readLine();
                }
                //conversion from StringBuilder to String
                result = sb.toString();
                //String will be converted to the JSONObject for further handling
                jsonobj = new JSONObject(result);
                Log.i("DownloadReceiver: ", "The JSONObject has been succesfully created.");
            }
            //error handling
            catch (JSONException jsone){
                Log.w("DownloadReceiver: ","The json file does not contain valid json, errortext: " + jsone);
            }
            catch (Exception e) {
                Log.w("DownloadReceiver: ", "And error occured " + e);
            }

        }else{
            //ToDo: Was wenn es sich nicht um unseren Download handelt?
        }
    }

    public static JSONObject getJson(){
        return jsonobj;
    }
}

