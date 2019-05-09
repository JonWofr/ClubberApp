package de.clubber_stuttgart.clubber.BusinessLogic;

import org.junit.Test;

import de.clubber_stuttgart.clubber.Exception.DateFormattingException;

import static org.junit.Assert.*;

public class SelectDateTest {

    //Test which formats every date since a.d.. Ongoing until 3rd millennium
    @Test
    public void formatDate() {
        for (int year = 1; year <= 3000; year++) {
            String yearNumeric = String.valueOf(year);
            if (yearNumeric.length() == 1) {
                yearNumeric = "000" + yearNumeric;
            } else if (yearNumeric.length() == 2) {
                yearNumeric = "00" + yearNumeric;
            } else if (yearNumeric.length() == 3) {
                yearNumeric = "0" + yearNumeric;
            }

            String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            for (int month = 1; month <= 12; month++) {
                String monthString = months[month - 1];
                String monthNumeric = String.valueOf(month);
                if (monthNumeric.length() == 1) {
                    monthNumeric = 0 + monthNumeric;
                }

                for (int day = 1; day <= 31; day++) {
                    String dayNumeric = String.valueOf(day);
                    if (dayNumeric.length() == 1) {
                        dayNumeric = 0 + dayNumeric;
                    }

                    assertEquals(yearNumeric + "-" + monthNumeric + "-" + dayNumeric, SelectDate.formatDate(monthString + " " + dayNumeric + ", " + yearNumeric));

                }
            }
        }
    }

    @Test(expected = DateFormattingException.class)
    public void formatDateCheckIfExceptionIsThrown(){
        //wrong month value
        SelectDate.formatDate("February 05, 2019");
        //wrong day value
        SelectDate.formatDate("Mar 2nd, 2018");
        //wrong year value
        SelectDate.formatDate("Mar 02, twothousand and nineteen");
    }
}