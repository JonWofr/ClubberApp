package de.clubber_stuttgart.clubber.business_logic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SelectDate implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    final private String LOG = "SelectDate";
    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;

    //Contructor
    public SelectDate(EditText editText, Context ctx){
        this.ctx=ctx;
        this.editText = editText;
        this.editText.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onClick(View v) {
         DatePickerDialog dialog =new DatePickerDialog(ctx, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        Log.d(LOG,"set Context + Day,Month,Year of Calendar for EditdText");
         dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText(sdformat.format(myCalendar.getTime()));

        Log.i(LOG,"set and show Date in EditText");
    }

    //ensures compatibility with db-query, because date formats are stored in a us-format (yyyy-mm-dd)
    static String formatDate (String date) throws DateFormattingException {
        String[] dates = date.split(",");
        String day = dates[0].substring(dates[0].length()-2, dates[0].length());
        String month = dates[0].substring(0, dates[0].length()-3);
        String year = dates[1].substring(dates[1].length()-4, dates[1].length());

        //Tests if date is in correct format, otherwise custom exception is triggered
        try {
            Integer.parseInt(day);
            Integer.parseInt(year);
            switch (month) {
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
                default:
                    throw new DateFormattingException();
            }

        }
        catch (NumberFormatException numberFormatException){
            throw new DateFormattingException();
        }

        date = year + "-" + month + "-" + day;
        return date;
    }
}
