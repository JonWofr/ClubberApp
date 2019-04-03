package de.clubber_stuttgart.clubber;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Database name
    private static final String DATABASE_NAME = "clubberDB";
    //Database tablenames
    private static final String TABLE_NAME_EVENTS = "events";
    private static final String TABLE_NAME_CLUBS = "clubs";

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
    private String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_NAME_EVENTS + "(" + E_ID + " INTEGER PRIMARYKEY NOT NULL ," +
            E_DTE + " TEXT ," +
            E_NAME + " TEXT NOT NULL ," +
            E_CLUB + " TEXT NOT NULL ," +
            E_SRT_TIME + " TEXT NOT NULL ," +
            E_BTN + " TEXT ," +
            E_GENRE + " TEXT)";

    //SQL statement for creating clubs table
    private String CREATE_TABLE_CLUBS = "CREATE TABLE " + TABLE_NAME_CLUBS + "(" + C_ID + " INTEGER PRIMARY KEY , " +
            C_NAME + " TEXT , " +
            C_ADRS + " TEXT NOT NULL , " +
            C_TEL + " TEXT , " +
            C_WEB + " TEXT NOT NULL)";

    private String dropTableClubs = "DROP TABLE " + TABLE_NAME_CLUBS;
    private String dropTableEvents = "DROP TABLE " + TABLE_NAME_EVENTS;



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



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTableClubs);
        db.execSQL(dropTableEvents);
        onCreate(db);
    }


    protected void insertEventEntry(Event event){
        ContentValues values = new ContentValues();
        values.put(E_ID, event.id);
        values.put(E_DTE, event.dte);
        values.put(E_NAME, event.name);
        values.put(E_CLUB, event.club);
        values.put(E_SRT_TIME, event.srttime);
        values.put(E_BTN, event.btn);
        values.put(E_GENRE, event.genre);
    }

    protected void insertClubEntry (Club club){
        ContentValues values = new ContentValues();
        values.put(C_ID, club.id);
        values.put(C_NAME, club.name);
        values.put(C_ADRS, club.adrs);
        values.put(C_TEL, club.tel);
        values.put(C_WEB, club.web);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME_CLUBS, null, values);
        db.close();
    }

}
