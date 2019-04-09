package de.clubber_stuttgart.clubber.business_logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DataBaseHelper extends SQLiteOpenHelper {

    final private String LOG = "HTTPHelper";

    //Database name
    private static final String DATABASE_NAME = "clubberDB";
    //Database tablenames (are protected private so ClubActivity and EventActivity are able to get the names of the tables)
    static final String TABLE_NAME_EVENTS = "events";
    static final String TABLE_NAME_CLUBS = "clubs";

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
    private final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_NAME_EVENTS + "(" + E_ID + " INTEGER PRIMARY KEY NOT NULL ," +
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

        Log.d(LOG, "Creating tables...");

        //creates events and clubs tables
        db.execSQL(CREATE_TABLE_EVENTS);
        Log.d(LOG, "Table " + TABLE_NAME_EVENTS + " got created with following SQL-statement: " + CREATE_TABLE_EVENTS);

        db.execSQL(CREATE_TABLE_CLUBS);
        Log.d(LOG, "Table " + TABLE_NAME_CLUBS + " got created with following SQL-statement: " + CREATE_TABLE_CLUBS);


    }


    //deletes the tables and creates them all over again
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ToDo: hier löschen der Tabellen einfügen und bei den anderen beiden Methoden rausnehmen.
        db.execSQL(DROP_TABLE_CLUBS);
        db.execSQL(DROP_TABLE_EVENTS);
        Log.d(LOG, "Table " + TABLE_NAME_CLUBS + " and table " + TABLE_NAME_EVENTS + " have been deleted.");
        onCreate(db);
    }


    void insertEventEntry(JSONObject event){
        //inserts values for event table
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


        //ToDo: wie hängt onUpgrade damit zusammen?
        //gets the DB and calls the onCreate method
        SQLiteDatabase db = this.getWritableDatabase();
        //runs insert command as SQL
        long resultCode = db.insert(TABLE_NAME_EVENTS, null, values);
        db.close();

        if (resultCode == -1){
            Log.w(LOG, "The row of event data has not been inserted into the database");
        }
    }

    void insertClubEntry(JSONObject club){
        //inserts values for clubs table
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

        //ToDo: wie hängt onUpgrade damit zusammen?
        //gets the DB and calls the onCreate method
        SQLiteDatabase db = this.getWritableDatabase();
        //runs insert command as SQL
        long resultCode = db.insert(TABLE_NAME_CLUBS, null, values);
        db.close();

        if (resultCode == -1){
            Log.w(LOG, "The row of club data has not been inserted into the database");
        }
    }

    //method to execute a SQL statement, which contains 'alter'
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
        //get highest id of table events. If the table does not exist at this moment String eId will be null
        Cursor cursor = db.query(true, TABLE_NAME_EVENTS, new String[]{"MAX(" + E_ID + ")"}, null, null, null, null, null, null);
        cursor.moveToNext();
        String eId = cursor.getString(0);

        cursor.close();
        //TODO Sofern die Tabellen noch nicht bestehen zu diesem Zeitpunkt werden die Strings null und null wird an die URL, welche zu dem Server
        //TODO geschickt wird, angehängt. Das Programm stürzt nicht ab und der Server antwortet sogar darauf, jedoch kann dies zu unerwünschtem
        //TODO Verhalten führen.

        //get highest id of table events. If the table does not exist at this moment String eId will be null
        cursor = db.query(true, TABLE_NAME_CLUBS, new String[]{"MAX(" + C_ID + ")"}, null, null, null, null, null, null);
        cursor.moveToNext();
        String cId = cursor.getString(0);

        cursor.close();
        db.close();
        //save highest ids of both tables inside String array and return it
        return new String[]{eId, cId};
    }

    public void deleteOldEntries (){
        //TODO Hier könnte ein Problem entstehen, wenn die Methode am 01. eines Monats aufgerufen wird, da die Methode nicht wissen kann, dass der Tag vor dem 01. nicht der 00. ist
        //TODO Hier könnte ein weiteres Problem entstehen, wenn der Tag dd nicht zweistellig ist (z.B. 05). Beim Parsen zu einem Integer wird höchstwahrscheinlich eine 5 und nicht eine 05 erstellt, wodurch beim sql statement probleme auftreten können
        String currentDate = java.time.LocalDate.now().toString();
        String day = currentDate.substring(currentDate.length()- 2, currentDate.length());
        String yearAndMonth = currentDate.substring(0, 8);
        int dayMinusOne = Integer.parseInt(day) - 1;
        String dateOfYesterday = yearAndMonth + dayMinusOne;
        Log.i(this.getClass().toString(), "Every event entry in the local db, which is older than " + dateOfYesterday + " will be deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from " + TABLE_NAME_EVENTS + " where " + E_DTE + " < " + dateOfYesterday);
        db.close();
    }

}
