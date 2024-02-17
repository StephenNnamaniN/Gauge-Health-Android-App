package uk.ac.tees.a0278818.gaugehealth.models;

public class ResponseCardData {
    public String dayOfTheWeek, day,month,year,time,createdBy, createdDateTimeKey;
    public ResponseCardData(String dayOfTheWeek, String day, String month, String year, String time, String createdBy, String createdDateTimeKey) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.day = day;
        this.month = month;
        this.year = year;
        this.time = time;
        this.createdBy = createdBy;
        this.createdDateTimeKey = createdDateTimeKey;
    }
}
