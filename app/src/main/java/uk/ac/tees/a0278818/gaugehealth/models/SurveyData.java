package uk.ac.tees.a0278818.gaugehealth.models;

import java.util.HashMap;

public class SurveyData {
    public HashMap<String, String> user_response_to_questions;
    public String loggedIn_user_email,dayOfTheWeek,monthString,year,day;

    public SurveyData() {
    }

    public SurveyData(HashMap<String, String> userResponseMap, String currentUserEmailID,
                      String dayOfTheWeek, String monthString, String year, String day) {
        this.user_response_to_questions = userResponseMap;
        this.loggedIn_user_email = currentUserEmailID;
        this.dayOfTheWeek = dayOfTheWeek;
        this.monthString = monthString;
        this.year = year;
        this.day = day;
    }
}
