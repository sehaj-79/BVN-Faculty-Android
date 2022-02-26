package Model;


public class Events extends NoticeId{

    private String Audience, Date, EventName, Guest, Location, Organizer, Time;


    public Events(String Audience,String Date,String EventName,String Guest,String Location,String Organizer,String Time){
        this.Audience = this.Audience;
        this.Date = this.Date;
        this.EventName = this.EventName;
        this.Guest = this.Guest;
        this.Location = this.Location;
        this.Organizer = this.Organizer;
        this.Time = this.Time;

    }

    public Events(){
    }


    public String getAudience() {
        return Audience;
    }

    public void setAudience(String audience) {
        Audience = audience;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getGuest() {
        return Guest;
    }

    public void setGuest(String guest) {
        Guest = guest;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getOrganizer() {
        return Organizer;
    }

    public void setOrganizer(String organizer) {
        Organizer = organizer;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}