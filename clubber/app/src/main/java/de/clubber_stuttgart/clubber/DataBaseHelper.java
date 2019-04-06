package de.clubber_stuttgart.clubber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Database name
    protected static final String DATABASE_NAME = "clubberDB";
    //Database tablenames
    protected static final String TABLE_NAME_EVENTS = "events";
    protected static final String TABLE_NAME_CLUBS = "clubs";

    //Colum names from events table
    private static final String E_ID = "id";
    private static final String E_DTE = "dte";
    private static final String E_NAME = "name" ;
    private static final String E_CLUB = "club";
    private static final String E_SRT_TIME = "srttime";
    private static final String E_BTN = "btn";
    private static final String E_GENRE = "genre";

    //Colum names from clubs table
    private static final String C_ID = "id";
    private static final String C_NAME = "name";
    private static final String C_ADRS = "adrs";
    private static final String C_TEL = "tel";
    private static final String C_WEB = "web";


    //SQL statement for creating events table
    private final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_NAME_EVENTS + "(" + E_ID + " INTEGER PRIMARYKEY NOT NULL ," +
            E_DTE + " TEXT ," +
            E_NAME + " TEXT NOT NULL ," +
            E_CLUB + " TEXT NOT NULL ," +
            E_SRT_TIME + " TEXT NOT NULL ," +
            E_BTN + " TEXT ," +
            E_GENRE + " TEXT)";

    //SQL statement for creating clubs table
    private final String CREATE_TABLE_CLUBS = "CREATE TABLE " + TABLE_NAME_CLUBS + "(" + C_ID + " INTEGER PRIMARY KEY  NOT NULL , " +
            C_NAME + " TEXT , " +
            C_ADRS + " TEXT NOT NULL , " +
            C_TEL + " TEXT , " +
            C_WEB + " TEXT NOT NULL)";

    //SQL statement to drop the specified tables
    private final String DROP_TABLE_CLUBS = "DROP IF TABLE EXISTS " + TABLE_NAME_CLUBS;
    private final String DROP_TABLE_EVENTS = "DROP IF TABLE EXISTS " + TABLE_NAME_EVENTS;



    DataBaseHelper (Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //creates events and clubs tables

        db.execSQL(CREATE_TABLE_EVENTS);
        Log.d("DataBaseHelper", "Table " + TABLE_NAME_EVENTS + " got created with following SQL-statement: " + CREATE_TABLE_EVENTS);

        db.execSQL(CREATE_TABLE_CLUBS);
        Log.d("DataBaseHelper", "Table " + TABLE_NAME_CLUBS + " got created with following SQL-statement: " + CREATE_TABLE_CLUBS);


    }


    //deletes the tables and creates them all over again
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ToDo: hier löschen der Tabellen einfügen und bei den anderen beiden Methoden rausnehmen.
        db.execSQL(DROP_TABLE_CLUBS);
        db.execSQL(DROP_TABLE_EVENTS);
        Log.d(this.getClass().toString(), "Table " + TABLE_NAME_CLUBS + " and table " + TABLE_NAME_EVENTS + " have been deleted.");
        onCreate(db);
    }


    protected void insertEventEntry(Event event){
        //inserts values for event table
        ContentValues values = new ContentValues();
        values.put(E_ID, event.id);
        values.put(E_DTE, event.dte);
        values.put(E_NAME, event.name);
        values.put(E_CLUB, event.club);
        values.put(E_SRT_TIME, event.srttime);
        values.put(E_BTN, event.btn);
        values.put(E_GENRE, event.genre);

        //ToDo: wie hängt onUpgrade damit zusammen?
        //gets the DB and calls the onCreate method
        SQLiteDatabase db = this.getWritableDatabase();
        //runs insert command as SQL
        long resultCode = db.insert(TABLE_NAME_EVENTS, null, values);
        db.close();

        if (resultCode == -1){
            Log.w(this.getClass().toString(), "The row of event data has not been inserted into the database");
        }
        else {
            Log.d(this.getClass().toString(), "The row of event data has successfully been inserted into the database");
        }
    }

    protected void insertClubEntry (Club club){
        //inserts values for clubs table
        ContentValues values = new ContentValues();
        values.put(C_ID, club.id);
        values.put(C_NAME, club.name);
        values.put(C_ADRS, club.adrs);
        values.put(C_TEL, club.tel);
        values.put(C_WEB, club.web);


        //ToDo: wie hängt onUpgrade damit zusammen?
        //gets the DB and calls the onCreate method
        SQLiteDatabase db = this.getWritableDatabase();
        //runs insert command as SQL
        long resultCode = db.insert(TABLE_NAME_CLUBS, null, values);
        db.close();

        if (resultCode == -1){
            Log.w(this.getClass().toString(), "The row of club data has not been inserted into the database");
        }
        else {
            Log.d(this.getClass().toString(), "The row of club data has successfully been inserted into the database");
        }
    }

    //method to execute a SQL statement, which contains 'alter'
    public void alterTable (String command){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(command);
        }
        catch (SQLException e){
            Log.w(this.getClass().toString(), "The SQLite alter-statement is not valid");
        }
    }

    //method to fetch the highest id saved in the local db of table events and club. Is called inside MainActivity
    public String[] selectHighestIds (){
        SQLiteDatabase db = this.getWritableDatabase();
        //get highest id of table events
        Cursor cursor = db.query(true, TABLE_NAME_EVENTS, new String[]{"MAX(" + E_ID + ")"}, null, null, null, null, null, null);
        cursor.moveToNext();
        String eId = cursor.getString(0);
        //get highest id of table events
        Log.d(this.getClass().toString(), "The highest id of " + TABLE_NAME_EVENTS + " is " + eId);
        cursor = db.query(true, TABLE_NAME_CLUBS, new String[]{"MAX(" + C_ID + ")"}, null, null, null, null, null, null);
        cursor.moveToNext();
        String cId = cursor.getString(0);
        Log.d(this.getClass().toString(), "The highest id of " + TABLE_NAME_CLUBS + " is " + cId);
        db.close();
        //save highest ids of both tables inside String array and return it
        return new String[]{eId, cId};
    }

}
