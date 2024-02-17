package uk.ac.tees.a0278818.gaugehealth.Survey;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.tees.a0278818.gaugehealth.R;

public class ResponseCardViewHolder extends RecyclerView.ViewHolder {


    public TextView day_TextView, month_TextView, date_TextView, time_TextView, createdBy_TextView, response_viewBtn;
    public View responseCardView;
    public ResponseCardViewHolder(@NonNull View itemView) {
        super(itemView);
        day_TextView = itemView.findViewById(R.id.dayTextView);
        month_TextView = itemView.findViewById(R.id.monthTextView);
        date_TextView= itemView.findViewById(R.id.dateTextView);
        time_TextView = itemView.findViewById(R.id.responseTime_txtview);
        createdBy_TextView = itemView.findViewById(R.id.responseUserName_txtView);
        response_viewBtn = itemView.findViewById(R.id.response_viewbtn);
        responseCardView = itemView;
    }
}