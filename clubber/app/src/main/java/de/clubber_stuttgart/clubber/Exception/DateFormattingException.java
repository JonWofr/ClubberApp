package de.clubber_stuttgart.clubber.Exception;

public class DateFormattingException extends RuntimeException {

    public DateFormattingException(){
        super("The date could not be converted. Wrong formatting");
    }
}
