package uk.ac.tees.a0278818.gaugehealth.Survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import uk.ac.tees.a0278818.gaugehealth.R;


public class SurveyMenu extends Fragment {
    CardView takeSurvey, responses;

    public SurveyMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_menu, container, false);

        takeSurvey = view.findViewById(R.id.take_survey_cardView);
        responses = view.findViewById(R.id.response_cardView);

        takeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyFragment surveyFragment = new SurveyFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, surveyFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        responses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseFragment responseFragment = new ResponseFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, responseFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}