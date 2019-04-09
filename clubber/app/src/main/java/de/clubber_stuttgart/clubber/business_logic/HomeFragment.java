package de.clubber_stuttgart.clubber.business_logic;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import de.clubber_stuttgart.clubber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    final private String LOG = "MainActivity";
    private Intent startEventActIntent;
    private Intent startClubActIntent;
    private EditText datePicker;
    private String selectedDate;
    DatePickerDialog.OnDateSetListener setListener;
    private Context context;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity().getApplicationContext();

        //startEventActIntent = new Intent(context, EventActivity.class);
        //startClubActIntent = new Intent(context, ClubActivity.class);

        initDBConnectionService();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initDBConnectionService(){
        Log.i(LOG,"Checking if network is available...");
        String noNetwork = "noNetwork";

        if (isNetworkAvailable()) {
            Log.i(LOG,"Network is available");
            //startEventActIntent.putExtra(noNetwork,false);
            //startClubActIntent.putExtra(noNetwork,false);

            Intent serviceIntent = new Intent(context, DBConnectionService.class);
            context.startService(serviceIntent);

        } else {
            Log.i(LOG, "no network available");
            //gives the intent some more information about the connection --> "carefull activity! You need to consider this to give the user information on the UI"
            //startEventActIntent.putExtra(noNetwork,true);
            //startClubActIntent.putExtra(noNetwork,true);
        }
    }

    public void formatDate (String year, String month, String day){
        //ensures compatibility with db-query, because date formats are stored in a us-format (yyyy-mm-dd)

        if (month.length() == 1){
            month = "0" + month;
        }

        if (day.length() == 1){
            day = "0" + day;
        }

        selectedDate = year + "-" + month + "-" + day;
        Log.i(this.getClass().toString(), "The user selected " + selectedDate + " as date");
        //this is for better readability for european users
        String europeanDateFormat = day + "." + month + "." + year;
        datePicker.setText(europeanDateFormat);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        //hides the keyboard
        datePicker.setInputType(InputType.TYPE_NULL);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        /*
        start the datePicker Dialog
         */


        //TODO Datepicker .show() Methode kann anscheinend nicht in einem Fragment funktionieren
        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        //method is used to correctly format the date to be able to do a sql query and to parse it into a form, which has better readability
                        formatDate(String.valueOf(year), String.valueOf(month), String.valueOf(day));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        final Button eventBtnWithoutDate = view.findViewById(R.id.eventBtnWithoutDate);
        eventBtnWithoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks if a date has been set previously (after the user navigated to the events with a date selected) and deletes the date
                Bundle bundle = startEventActIntent.getExtras();
                if (bundle.containsKey("selectedDate")) {
                    startEventActIntent.removeExtra("selectedDate");
                }
                startActivity(startEventActIntent);
            }
        });

        Button eventBtnWithDate = view.findViewById(R.id.eventBtnWithDate);
        eventBtnWithDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //only set a date if one got selected
                if (selectedDate != null) {
                    startEventActIntent.putExtra("selectedDate", selectedDate);
                }
                startActivity(startEventActIntent);
            }
        });


        Button clubBtn = view.findViewById(R.id.clubBtn);
        clubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startClubActIntent);
            }
        });

        Button refreshBtn = view.findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "refresh button has been clicked, trying to refresh...");
                initDBConnectionService();
                //ToDo: Rückmeldung an den user, ob er schon up to date ist und ob der refresh erfolgreich war.
            }
        });

        //ToDo: Überprüfen, ob external Storage erreichbar ist (benötigen wir das? Eigentlich schreiben wir auf internal Storage. --> Prüfen, ob das einen Unterschied macht)


        return view;

    }






}