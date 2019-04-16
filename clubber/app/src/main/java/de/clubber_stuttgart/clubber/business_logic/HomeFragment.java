package de.clubber_stuttgart.clubber.business_logic;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import de.clubber_stuttgart.clubber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    final private String LOG = "HomeFragment";
    public EditText datePicker;
    private String selectedDate;
    DatePickerDialog.OnDateSetListener setListener;
    private Context context;
    private Button filterDate;




    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity().getApplicationContext();

        if (MainActivity.initSetupDatabase) {
            Log.i(LOG,"initial setup of the database. App is being started for the first time");
            Intent serviceIntent = new Intent(context, DBConnectionService.class);
            context.startService(serviceIntent);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        //hides the keyboard
        datePicker.setInputType(InputType.TYPE_NULL);

        //start the datePicker Dialog
        SelectDate date= new SelectDate(datePicker, getContext());
        Log.i(LOG,"calls SelectDate class and opens Datepickerdialog");

        filterDate = (Button) view.findViewById(R.id.eventBtnWithDate);
        filterDate.setOnClickListener(this);

        return view;

    }


    //to open filtered Events Fragment
    @Override
    public void onClick(View v) {

        Fragment fragment = null;
        switch (v.getId()) {
            //if our button for submitting the date is clicked, this will happen
            case R.id.eventBtnWithDate:
                fragment = new EventsFragment();
                Log.i(LOG,"The datepicker button has been taped...");
                try {
                    EditText dateInput = getView().findViewById(R.id.datePicker);
                    String date = dateInput.getText().toString();
                    //if the date is empty, the button has been clicked without a chosen date. We print out a toast.
                    //if there is a date, we pass the date through the MainActivity and replace the HomeFragment with the EventFragment
                    if(!(date.equals(""))){
                        date = formatDate(date);
                        Log.d(LOG,"A date has been picked, replacing HomeFragment with EventsFragment...");
                        MainActivity.setDateInBundle(fragment, date);
                        replaceFragment(fragment);
                    }else {
                        Log.d(LOG, "No date has been picked");
                        Toast.makeText(context, "Du musst noch ein Datum auswählen", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }catch (NullPointerException e){
                    Log.w(LOG,"No datepicker view");
                }
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public String formatDate (String date){
        //ensures compatibility with db-query, because date formats are stored in a us-format (yyyy-mm-dd)
        String[] dates = date.split(",");
        String day = dates[0].substring(dates[0].length()-2, dates[0].length());
        String month = dates[0].substring(0, dates[0].length()-3);
        String year = dates[1].substring(dates[1].length()-4, dates[1].length());
        switch (month){
            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
        }
        date = year + "-" + month + "-" + day;
        return date;
    }

}
