package de.clubber_stuttgart.clubber.BusinessLogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.clubber_stuttgart.clubber.UI.Club;
import de.clubber_stuttgart.clubber.UI.Event;

public class DataBaseHelper extends SQLiteOpenHelper {

    final private String LOG = "DataBaseHelper";


    private static final String DATABASE_NAME = "clubberDB";

    static final String TABLE_NAME_EVENTS = "events";
    static final String TABLE_NAME_CLUBS = "clubs";

    private static final String E_ID = "id";
    private static final String E_DTE = "dte";
    private static final String E_NAME = "name" ;
    private static final String E_CLUB = "club";
    private static final String E_SRT_TIME = "srttime";
    private static final String E_BTN = "btn";
    private static final String E_GENRE = "genre";

    private static final String C_ID = "id";
    private static final String C_NAME = "name";
    private static final String C_ADRS = "adrs";
    private static final String C_TEL = "tel";
    private static final String C_WEB = "web";


    private final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_NAME_EVENTS + "(" + E_ID + " INTEGER PRIMARY KEY NOT NULL ," +
            E_DTE + " TEXT ," +
            E_NAME + " TEXT NOT NULL ," +
            E_CLUB + " TEXT NOT NULL ," +
            E_SRT_TIME + " TEXT NOT NULL ," +
            E_BTN + " TEXT ," +
            E_GENRE + " TEXT)";

    private final String CREATE_TABLE_CLUBS = "CREATE TABLE " + TABLE_NAME_CLUBS + "(" + C_ID + " INTEGER PRIMARY KEY  NOT NULL , " +
            C_NAME + " TEXT , " +
            C_ADRS + " TEXT NOT NULL , " +
            C_TEL + " TEXT , " +
            C_WEB + " TEXT NOT NULL)";

    private final String DROP_TABLE_CLUBS = "DROP IF TABLE EXISTS " + TABLE_NAME_CLUBS;
    private final String DROP_TABLE_EVENTS = "DROP IF TABLE EXISTS " + TABLE_NAME_EVENTS;

    public DataBaseHelper (Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG, "Creating tables...");

        db.execSQL(CREATE_TABLE_EVENTS);
        Log.d(LOG, "Table " + TABLE_NAME_EVENTS + " got created with following SQL-statement: " + CREATE_TABLE_EVENTS);

        db.execSQL(CREATE_TABLE_CLUBS);
        Log.d(LOG, "Table " + TABLE_NAME_CLUBS + " got created with following SQL-statement: " + CREATE_TABLE_CLUBS);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CLUBS);
        db.execSQL(DROP_TABLE_EVENTS);
        Log.d(LOG, "Table " + TABLE_NAME_CLUBS + " and table " + TABLE_NAME_EVENTS + " have been deleted.");
        onCreate(db);
    }


    void insertEventEntry(JSONObject event){
        ContentValues values = new ContentValues();
        try {
            values.put(E_ID, event.getInt(E_ID));
            values.put(E_DTE, event.getString(E_DTE));
            values.put(E_NAME, event.getString(E_NAME));
            values.put(E_CLUB, event.getString(E_CLUB));
            values.put(E_SRT_TIME, event.getString(E_SRT_TIME));
            values.put(E_BTN, event.getString(E_BTN));
            values.put(E_GENRE, event.getString(E_GENRE));
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        SQLiteDatabase db = this.getWritableDatabase();
        long resultCode = db.insert(TABLE_NAME_EVENTS, null, values);
        db.close();

        if (resultCode == -1) {
            Log.w(LOG, "The row of event data has not been inserted into the database");
        }
    }

    void insertClubEntry(JSONObject club){
        ContentValues values = new ContentValues();
        try {
        values.put(C_ID, club.getInt(C_ID));
        values.put(C_NAME, club.getString(C_NAME));
        values.put(C_ADRS, club.getString(C_ADRS));
        values.put(C_TEL, club.getString(C_TEL));
        values.put(C_WEB, club.getString(C_WEB));
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long resultCode = db.insert(TABLE_NAME_CLUBS, null, values);
        db.close();

        if (resultCode == -1){
            Log.w(LOG, "The row of club data has not been inserted into the database");
        }
    }

    public void alterTable (String command){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(command);
        }
        catch (SQLException e){
            Log.w(LOG, "The SQLite alter-statement is not valid");
        }
    }

    //method to fetch the highest id saved in the local db of table events and club. Is called inside MainActivity
    String[] selectHighestIds (){
        SQLiteDatabase db = this.getWritableDatabase();
        //get highest id of table events. If the table does not exist at this moment String eId will be null and the whole json will be returned
        Cursor cursor = db.query(true, TABLE_NAME_EVENTS, new String[]{"MAX(" + E_ID + ")"}, null, null, null, null, null, null);
        cursor.moveToNext();
        String eId = cursor.getString(0);

        cursor.close();

        //get highest id of table events. If the table does not exist at this moment String eId will be null and the whole json will be returned
        cursor = db.query(true, TABLE_NAME_CLUBS, new String[]{"MAX(" + C_ID + ")"}, null, null, null, null, null, null);
        cursor.moveToNext();
        String cId = cursor.getString(0);

        cursor.close();
        db.close();
        //save highest ids of both tables inside String array and return it. If the tables do not exist yet the values inside the array are null, null. This is actually no problem at all, because at the serverside the server knows what to do with that
        return new String[]{eId, cId};
    }

    void deleteOldEntries (){
        //ToDo: Fix your shit!!!!!!!!!!
        //Get the date of yesterday
        String currentDate = java.time.LocalDate.now().toString();
        String day = currentDate.substring(currentDate.length()- 2, currentDate.length());
        String yearAndMonthString = currentDate.substring(0, 8);
        int dayMinusOne = Integer.parseInt(day) - 1;
        String dayMinusOneString = String.valueOf(dayMinusOne);
        if (dayMinusOneString.length() == 1){
            dayMinusOneString = 0 + String.valueOf(dayMinusOne);
        }
        String dateOfYesterday = yearAndMonthString + dayMinusOneString;
        //delete every event entry, which is older than yesterday
        Log.i(LOG, "Every event entry in the local db, which is older than " + dateOfYesterday + " will be deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeletedCount = db.delete(TABLE_NAME_EVENTS, 	E_DTE + " < '" + dateOfYesterday + "'", null);
        Log.i(LOG, rowsDeletedCount + " entries have been deleted because corresponding events were outdated");
        db.close();
    }

    public ArrayList<Event> getEventArrayList(Bundle bundle){

        ArrayList<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        //Intent does not have to contain any selected date (for example if the events tab is reached via the tab bar)
        if (bundle != null && bundle.containsKey("selectedDate")) {
            //custom query
            Log.i(LOG, "Events will be filtered for date " + bundle.getString("selectedDate"));
            cursor = db.query(DataBaseHelper.TABLE_NAME_EVENTS, null, "dte = ?", new String[]{bundle.getString("selectedDate")}, null, null, "dte, srttime asc", null);
        } else {
            //default query
            cursor = db.query(DataBaseHelper.TABLE_NAME_EVENTS, null, null, null, null, null, "dte, srttime asc", null);
        }

        while (cursor.moveToNext()) {
            eventList.add(new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
        }
        cursor.close();

        return eventList;
    }

    public ArrayList<Club> getClubArrayList(){
        ArrayList<Club> clubList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        //columns --> which should be selected (null = all)
        //selection null return all the rows (WHERE statement)
        //selectionArgs where column is something (e.g. date)
        Cursor cursor = db.query(true, DataBaseHelper.TABLE_NAME_CLUBS, null, null, null, null, null, "name asc", null);

        while (cursor.moveToNext()) {
            clubList.add(new Club(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }
        cursor.close();

        return clubList;
    }
}
