package de.clubber_stuttgart.clubber;

/**
 * Constructor is important for Adapter and EventActivity
 */
public class CardItemEvents {

    private String eventTitle;
    private String musicDirection;
    private String date;
    private String startTime;
    private String club;

    public CardItemEvents(String eventTitle,String musicDirection,  String date, String startTime, String club){
        this.eventTitle=eventTitle;
        this.musicDirection=musicDirection;
        this.date=date;
        this.startTime=startTime;
        this.club=club;
    }


    public String getEventTitle(){
        return eventTitle;
    }

    public String getMusicDirection(){
        return musicDirection;
    }

    public String getDate(){
        return date;
    }

    public String getStartTime(){
        return startTime;
    }

    public String getClub(){
        return club;
    }
}

