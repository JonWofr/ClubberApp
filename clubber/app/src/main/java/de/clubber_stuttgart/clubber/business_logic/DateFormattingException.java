package de.clubber_stuttgart.clubber.business_logic;

class DateFormattingException extends RuntimeException {

    DateFormattingException(){
        super("The date could not be converted. Wrong formatting");
    }
}
