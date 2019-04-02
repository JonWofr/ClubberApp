package de.clubber_stuttgart.clubber;

public class CardItemEvent {

    private String eventTitle;
    private String musicDirection;
    private String date;
    private String startTime;
    private String club;

    public CardItemEvent(String eventTitle,String musicDirection,  String date, String startTime, String club){
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


