package uk.ac.tees.a0278818.gaugehealth;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.a0278818.gaugehealth.Survey.SurveyMenu;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.Item;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.ItemViewModel;
import uk.ac.tees.a0278818.gaugehealth.data.Repository;
import uk.ac.tees.a0278818.gaugehealth.emotionfile.CheckEmotions;
import uk.ac.tees.a0278818.gaugehealth.menufrag.BloodPressureFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.EmergencyFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.ExerciseFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.MedicalHelpFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.OxySaturationFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.PharmacyFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.PulseFragment;
import uk.ac.tees.a0278818.gaugehealth.menufrag.TemperatureFragment;

public class HomeFragment extends Fragment {
    private ItemViewModel itemViewModel;
    private List<Item> itemList;
    CardView survey, emotion, bloodPressure, temperature,
            oxygenSaturation, pulse, help, exercise, emergency, pharmacy;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemViewModel = new ViewModelProvider(requireActivity())
                .get(ItemViewModel.class);
        itemViewModel.setSelectedItems(itemList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        survey = view.findViewById(R.id.survey_cardView);
        emotion = view.findViewById(R.id.emotion_cardView);
        bloodPressure = view.findViewById(R.id.blood_Pressure_cardView);
        temperature = view.findViewById(R.id.temperature_cardView);
        oxygenSaturation = view.findViewById(R.id.oxygen_cardView);
        pulse = view.findViewById(R.id.pulse_cardView);
        help = view.findViewById(R.id.help_cardView);
        exercise = view.findViewById(R.id.exercise_cardView);
        emergency = view.findViewById(R.id.emergency_cardView);
        pharmacy = view.findViewById(R.id.pharmacy_cardView);

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyMenu surveyMenu = new SurveyMenu();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, surveyMenu, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CheckEmotions.class));
            }
        });

        bloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloodPressureFragment bloodPressureFragment = new BloodPressureFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, bloodPressureFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemperatureFragment temperatureFragment = new TemperatureFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, temperatureFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        oxygenSaturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OxySaturationFragment oxySaturationFragment = new OxySaturationFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, oxySaturationFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        pulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PulseFragment pulseFragment = new PulseFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pulseFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicalHelpFragment helpFragment = new MedicalHelpFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, helpFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseFragment exerciseFragment = new ExerciseFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, exerciseFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmergencyFragment emergencyFragment = new EmergencyFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, emergencyFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PharmacyFragment pharmacyFragment = new PharmacyFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pharmacyFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}