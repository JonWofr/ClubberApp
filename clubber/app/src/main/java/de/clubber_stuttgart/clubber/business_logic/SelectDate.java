package de.clubber_stuttgart.clubber.business_logic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.text.NumberFormat;
import java.util.Locale;

import de.clubber_stuttgart.clubber.R;

public class SelectDate implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;

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


    }
}
