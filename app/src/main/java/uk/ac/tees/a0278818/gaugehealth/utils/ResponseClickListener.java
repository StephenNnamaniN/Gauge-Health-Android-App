package uk.ac.tees.a0278818.gaugehealth.utils;

import android.util.Log;
import android.view.View;

public class ResponseClickListener {
    // here index is index
    // of item clicked
    public void click(View v, int index) {
        Log.d("ResponseClickListener", "Response " +index + " clicked.");
    }

    //Helper method for view survey button
    public void view_survey_click(View v,int index){
        Log.d("ResponseClickListener", "Response " +index + " clicked.");
    }
}
