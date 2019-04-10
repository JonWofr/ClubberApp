package de.clubber_stuttgart.clubber.business_logic;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

import de.clubber_stuttgart.clubber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    final private String LOG = "MainActivity";
    public EditText datePicker;
    private String selectedDate;
    DatePickerDialog.OnDateSetListener setListener;
    private Context context;

    static boolean networkAccess;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity().getApplicationContext();

        if (MainActivity.initSetupDatabase) {
            initDBConnectionService();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void initDBConnectionService(){
        Log.i(LOG,"Checking if network is available...");
        if (isNetworkAvailable()) {
            Log.i(LOG,"Network is available");
            networkAccess = true;

            Intent serviceIntent = new Intent(context, DBConnectionService.class);
            context.startService(serviceIntent);
        } else {
            Log.i(LOG, "no network available");
            //gives the fragments some more information about the connection --> "carefull! You need to consider this to give the user information on the UI"
            networkAccess = false;
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

        //ToDo: Teil des klassischen Date Picker Dialog
        /*Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);*/


        /*
        start the datePicker Dialog
         */


        //try to include Datepicker Dialog in Fragment
        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

               /* DialogFragment dialogFragment = new SelectDateFragment();
                dialogFragment.show(getFragmentManager(),"date");*/

            }
        });
        //TODO Datepicker .show() Methode kann anscheinend nicht in einem Fragment funktionieren
        /*datePicker.setOnClickListener(new View.OnClickListener() {

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
        });*/



        //ToDo: Überprüfen, ob external Storage erreichbar ist (benötigen wir das? Eigentlich schreiben wir auf internal Storage. --> Prüfen, ob das einen Unterschied macht)


        return view;

    }






}
