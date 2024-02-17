package uk.ac.tees.a0278818.gaugehealth.Survey;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.a0278818.gaugehealth.R;
import uk.ac.tees.a0278818.gaugehealth.databinding.FragmentSurveyBinding;
import uk.ac.tees.a0278818.gaugehealth.models.SurveyData;


public class SurveyFragment extends Fragment {
    FragmentSurveyBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseSurveyQuestionRef, mDatabaseUserResponseRef;
    private String userEmail, userID;
    private TextView questionNumberTxt, questionTxt, previousQuestionTxt, nextQuestionTxt;
    private Button allOfTheTimeBtn, mostOfTheTimeBtn, someOfTheTimeBtn, noneOfTheTimeBtn, dontKnowBtn, submitBtn;
    private ConstraintLayout previousConstraintLayout, nextConstraintLayout;
    private final HashMap<Integer, String> questionMap = new HashMap<>();
    private final HashMap<String, String> respondsToQuestionMap = new HashMap<>();



    public SurveyFragment() {
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
        binding = FragmentSurveyBinding.inflate(getLayoutInflater());

        // Let's declare all the necessary widgets
        questionNumberTxt = binding.questionNumber;
        questionTxt = binding.question;
        previousQuestionTxt = binding.prevText;
        nextQuestionTxt = binding.nextText;
        allOfTheTimeBtn = binding.answer1Btn;
        mostOfTheTimeBtn = binding.answer2Btn;
        someOfTheTimeBtn = binding.answer3Btn;
        noneOfTheTimeBtn = binding.answer4Btn;
        dontKnowBtn = binding.answer5Btn;
        submitBtn = binding.submitBtn;

        // Button Layout for Previous and Next
        previousConstraintLayout = binding.prevConstraintLayout;
        nextConstraintLayout = binding.nextConstraintLayout;

        // When the fragment is created we want the first question to show
        // Hence, we will hide the previous button and the submit button; the previous button comes up only from the second question
        // And the submit button appears on the last question
        binding.prevConstraintLayout.setVisibility(View.INVISIBLE);
        binding.submitBtn.setVisibility(View.INVISIBLE);

        // Get the current logged in user
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        // Let's check if user was loaded properly
        if (firebaseUser == null) {
            Toast.makeText(SurveyFragment.this.getContext(),
                    "Something went wrong, user profile not available", Toast.LENGTH_SHORT).show();
        } else {
            userEmail = firebaseUser.getEmail();
            userID = firebaseUser.getUid();
            Log.d("SurveyFragment", "Logged in user email:" + userEmail + " User Id:" + userID);
        }
        // Get current Date and Time
        // Get the questions from the database
        mDatabaseSurveyQuestionRef = FirebaseDatabase.getInstance().getReference("Survey_Questions");
        mDatabaseSurveyQuestionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    long count = snapshot.getChildrenCount();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Log.d("SurveyFragment", snap.getKey() + "--" + snap.getValue(String.class));
                        questionMap.put(Integer.valueOf(snap.getKey()), snap.getValue(String.class));
                    }
                    // Display the first question and question number
                    questionTxt.setText(questionMap.get(1));
                    questionNumberTxt.setText("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SurveyFragment.this.getContext(), "Something went wrong! Firebase Data not accessible", Toast.LENGTH_LONG).show();
            }
        });

        // ------Navigation button events--------
        // Create navigation to previous questions
        previousQuestionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Let's check what is the current question number
                String currentQuestionNumber = (String) questionNumberTxt.getText();
                // we hide the previous button if this is the first question
                if (Integer.valueOf(currentQuestionNumber) -1 == 1){
                    previousConstraintLayout.setVisibility(View.INVISIBLE);
                }
                // We also hide the submit button if the current question is less than the count of questions
                // replace the question and number
                if (Integer.valueOf(currentQuestionNumber) - 1 < questionMap.size()) {
                    nextConstraintLayout.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.INVISIBLE);
                    questionTxt.setText(questionMap.get(Integer.valueOf(currentQuestionNumber)-1));
                    questionNumberTxt.setText(String.valueOf(Integer.valueOf(currentQuestionNumber) -1));
                } else {
                    questionTxt.setText(questionMap.get(Integer.valueOf(currentQuestionNumber) -1));
                    questionNumberTxt.setText(String.valueOf(Integer.valueOf(currentQuestionNumber) -1));
                }
                setSelectedAnswerBackgroundChange(getResponseForPreviousQuestion((String) questionTxt.getText()), v);
            }
        });

        // Navigate to next question
        nextQuestionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make all answer buttons background color default
                allOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
                mostOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
                someOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
                noneOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
                dontKnowBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));

                // Check if user has responded to previous question
                boolean userHasResponded = checkUserResponseToPreviousQuestion((String) questionTxt.getText());
                if (userHasResponded) {
                    //check what is the current question number
                    String currentQuestionNumber = (String) questionNumberTxt.getText();
                    // Show the previous button if this is not the first question to navigate
                    if (Integer.valueOf(currentQuestionNumber) + 1 > 1) {
                        previousConstraintLayout.setVisibility(View.VISIBLE);
                    }
                    // show the submit button if this is the last question
                    // Replace the question and question number with the next.
                    if (Integer.valueOf(currentQuestionNumber) + 1 == questionMap.size()) {
                        nextConstraintLayout.setVisibility(View.INVISIBLE);
                        submitBtn.setVisibility(View.VISIBLE);
                        questionTxt.setText(questionMap.get(Integer.valueOf(currentQuestionNumber) + 1));
                        questionNumberTxt.setText(String.valueOf(Integer.valueOf(currentQuestionNumber) + 1));
                    } else {
                        questionTxt.setText(questionMap.get(Integer.valueOf(currentQuestionNumber) + 1));
                        questionNumberTxt.setText(String.valueOf(Integer.valueOf(currentQuestionNumber) + 1));
                    }
                    setSelectedAnswerBackgroundChange(getResponseForPreviousQuestion((String) questionTxt.getText()),v);
                } else {
                    Toast.makeText(SurveyFragment.this.getContext(),
                            "Please choose any one response for this question.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // -----------Response button events------------
        allOfTheTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String allOfTheTimeStr = getString(R.string.answer_1);
                Log.d("SurveyFragment", allOfTheTimeStr);
                setResponseToQuestions(allOfTheTimeStr, (String) questionTxt.getText());
                setSelectedAnswerBackgroundChange(allOfTheTimeStr,v);
            }
        });

        mostOfTheTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mostOfTheTimeStr = getString(R.string.answer_2);
                Log.d("SurveyFragment", mostOfTheTimeStr);
                setResponseToQuestions(mostOfTheTimeStr, (String) questionTxt.getText());
                setSelectedAnswerBackgroundChange(mostOfTheTimeStr,v);
            }
        });

        someOfTheTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String someOfTheTimeStr = getString(R.string.answer_3);
                Log.d("SurveyFragment", someOfTheTimeStr);
                setResponseToQuestions(someOfTheTimeStr, (String) questionTxt.getText());
                setSelectedAnswerBackgroundChange(someOfTheTimeStr,v);
            }
        });

        noneOfTheTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noneOfTheTimeStr = getString(R.string.answer_4);
                Log.d("SurveyFragment", noneOfTheTimeStr);
                setResponseToQuestions(noneOfTheTimeStr, (String) questionTxt.getText());
                setSelectedAnswerBackgroundChange(noneOfTheTimeStr,v);
            }
        });
        dontKnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dontKnowStr = getString(R.string.answer_5);
                Log.d("SurveyFragment", dontKnowStr);
                setResponseToQuestions(dontKnowStr, (String) questionTxt.getText());
                setSelectedAnswerBackgroundChange(dontKnowStr,v);

            }
        });

        //-------Submit button events-----------
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if user responded to last question
                Log.d("SurveyFragment", "Submit button clicked");
                boolean userHasResponded = checkUserResponseToPreviousQuestion((String) questionTxt.getText());
                if (userHasResponded) {
                    // Create a HashMap for question as key and response as value
                    // include current date and time
                    // include current user emailId
                    HashMap<String, String> userResponseToQuestionHashMap = new HashMap<>();
                    for (Map.Entry<Integer, String> entry : questionMap.entrySet()){
                        // for each question
                        String question = entry.getValue();
                        // Check the question in the responseToQuestion and get the response.
                        // Finally put the question and response to the userResponseToQuestionMap
                        if (respondsToQuestionMap.containsKey(question)){
                            userResponseToQuestionHashMap.put(question,
                                    respondsToQuestionMap.get(question));
                        }
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date = new Date();
                    String currentDateAndTimeStr = simpleDateFormat.format(date);
                    String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
                    String day = (String) DateFormat.format("dd", date);
                    String monthString = (String) DateFormat.format("MMM", date);
                    String year = (String) DateFormat.format("yyyy", date);

                    SurveyData surveyData = new SurveyData(userResponseToQuestionHashMap,
                            userEmail, dayOfTheWeek, monthString, year, day);
                    // Store the details in the response database
                    // Get reference to Firebase Realtime Database [User_Responses] node
                    mDatabaseUserResponseRef = FirebaseDatabase.getInstance().getReference("User_Responses");
                    mDatabaseUserResponseRef.child(firebaseUser.getUid()).child(currentDateAndTimeStr)
                            .setValue(surveyData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SurveyFragment.this.getContext(),
                                            "Survey successfully completed!", Toast.LENGTH_LONG).show();
                                    // Open responses fragment after each successful survey submission
                                    // Create new fragment transaction
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.fragment_container, ResponseFragment.class, null)
                                            .setReorderingAllowed(true)
                                            .addToBackStack("responses")
                                            .commit();
                                }
                            }). addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SurveyFragment.this.getContext(),
                                            "Something went wrong while submitting the survey. Please try again.",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    Toast.makeText(SurveyFragment.this.getContext(),
                            "Please choose any one response for this question.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return binding.getRoot();
    }
    // Function to check if user has choose any response to current question before going to next question
    private boolean checkUserResponseToPreviousQuestion(String currentQuestion){
        return respondsToQuestionMap.containsKey(currentQuestion.trim());
    }

    private String getResponseForPreviousQuestion(String prevQuestion) {
        String prev_response = "";
        if (respondsToQuestionMap.containsKey(prevQuestion)) {
            prev_response = respondsToQuestionMap.get(prevQuestion);
        }
        return prev_response;
    }

    // This is a generic method that attaches respond to the current question.
    private void setResponseToQuestions(String responseType, String currentQuestion) {
        //Add or update responseToQuestionMap HashMap with the selected choice
        // Check if the question is already added to the map then update current response
        if (respondsToQuestionMap.containsKey(currentQuestion.trim())) {
            respondsToQuestionMap.put(currentQuestion.trim(), responseType);
        } else {
            respondsToQuestionMap.put(currentQuestion.trim(), responseType);
        }
        // Just to display the values in the map
        for (Map.Entry<String, String> entry : respondsToQuestionMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("SurveyFragment", "Q: " + key + " -- Ans: " + value);
        }
    }

    private void setSelectedAnswerBackgroundChange(String selectedAnswerStr, View v) {
        if (selectedAnswerStr.equalsIgnoreCase(getString(R.string.answer_1))) {
            allOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button_selected));
            mostOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            someOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            noneOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            dontKnowBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
        } else if(selectedAnswerStr.equalsIgnoreCase(getString(R.string.answer_2))) {
            allOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            mostOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button_selected));
            someOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            noneOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            dontKnowBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
        } else if(selectedAnswerStr.equalsIgnoreCase(getString(R.string.answer_3))){
            allOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            mostOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            someOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button_selected));
            noneOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            dontKnowBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));

        }else if(selectedAnswerStr.equalsIgnoreCase(getString(R.string.answer_4))){
            allOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            mostOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            someOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            noneOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button_selected));
            dontKnowBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
        }else if(selectedAnswerStr.equalsIgnoreCase(getString(R.string.answer_5))){
            allOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            mostOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            someOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            noneOfTheTimeBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button));
            dontKnowBtn.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.answer_button_selected));
        }
    }
}