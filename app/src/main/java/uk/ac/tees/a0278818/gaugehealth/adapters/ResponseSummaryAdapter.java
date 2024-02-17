package uk.ac.tees.a0278818.gaugehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import uk.ac.tees.a0278818.gaugehealth.R;
import uk.ac.tees.a0278818.gaugehealth.Survey.ResponseSummaryCardViewHolder;
import uk.ac.tees.a0278818.gaugehealth.models.ResponseSummaryCardData;

public class ResponseSummaryAdapter extends RecyclerView.Adapter<ResponseSummaryCardViewHolder> {
    List<ResponseSummaryCardData> responseSummaryCardDataList;
    Context context;

    public ResponseSummaryAdapter(List<ResponseSummaryCardData> responseSummaryCardDataList, Context context) {
        this.responseSummaryCardDataList = responseSummaryCardDataList;
        this.context = context;
    }
    @NonNull
    @Override
    public ResponseSummaryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_response_summary_card_view_holder, parent, false);
        ResponseSummaryCardViewHolder responseSummaryCardViewHolder = new ResponseSummaryCardViewHolder(view);
        return responseSummaryCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseSummaryCardViewHolder holder, int position) {
        final int index = holder.getAdapterPosition();
        holder.responseQuestionNum_TextView.setText(responseSummaryCardDataList.get(position).responseQuestionNumber);
        holder.responseQuestion_TextView.setText(responseSummaryCardDataList.get(position).responseQuestion);
        holder.responseQuestionAnswer_TextView.setText(responseSummaryCardDataList.get(position).responseAnswer);
    }

    @Override
    public int getItemCount() {
        return responseSummaryCardDataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
