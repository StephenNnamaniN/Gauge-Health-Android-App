package uk.ac.tees.a0278818.gaugehealth.Survey;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import uk.ac.tees.a0278818.gaugehealth.R;
import uk.ac.tees.a0278818.gaugehealth.adapters.ResponseSummaryAdapter;
import uk.ac.tees.a0278818.gaugehealth.databinding.FragmentResponseSummaryBinding;
import uk.ac.tees.a0278818.gaugehealth.models.ResponseSummaryCardData;
import uk.ac.tees.a0278818.gaugehealth.utils.ResponseClickListener;

public class ResponseSummaryFragment extends Fragment {
    FragmentResponseSummaryBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    RecyclerView recyclerView;
    ResponseSummaryAdapter responseSummaryAdapter;
    ResponseClickListener responseClickListener;
    private DatabaseReference mDatabaseUserResponseRef, mDatabaseSurveyQuestionRef;
    private String userEmail, userId;
    private final HashMap<String, HashMap<String, Object>> responsesHashMapDb = new HashMap<>();
    private final HashMap<Integer, String> questionsMap = new HashMap<>();
    private List<ResponseSummaryCardData> responseSummaryCardDataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResponseSummaryBinding.inflate(getLayoutInflater());

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseSummaryFragment.this.getParentFragmentManager()
                        .popBackStackImmediate();
            }
        });

        // Retrieving data using bundle
        Bundle bundle = getArguments();
        String selectedResponseDateTime = String.valueOf(bundle.getString("selectedResponseDataTimeKey"));

        // Get the responses for the current logged in user from the database
        // Get the current logged in user
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(ResponseSummaryFragment.this.getContext(),
                    "Something went wrong, user profile not available", Toast.LENGTH_SHORT).show();
        } else {
            userEmail = firebaseUser.getEmail();
            userId = firebaseUser.getUid();
        }

        //---------get the questions from the database-----------
        mDatabaseSurveyQuestionRef = FirebaseDatabase.getInstance().getReference("Survey_Questions");
        mDatabaseSurveyQuestionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    long count = snapshot.getChildrenCount();

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        questionsMap.put(Integer.valueOf(snap.getKey()), snap.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResponseSummaryFragment.this.getContext(), "Something went wrong! Firebase Data not accessible", Toast.LENGTH_LONG).show();
            }
        });

        //-------------Get user responses from database---------
        mDatabaseUserResponseRef = FirebaseDatabase.getInstance().getReference("User_Responses");
        assert firebaseUser != null;
        mDatabaseUserResponseRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snap : snapshot.getChildren()){
                        if (snap.getKey().equalsIgnoreCase(selectedResponseDateTime)) {
                            Log.d("ResponseSummaryFragment", "Response Created DateTime-> " + snap.getKey() + "\n Details :\n " + snap.getValue());
                            HashMap<String, Object> eachResponseDetails = (HashMap<String, Object>) snap.getValue();
                            responsesHashMapDb.put(snapshot.getKey(), eachResponseDetails);
                        }
                    }
                    responseSummaryCardDataList = getUserResponseDataFromDB();
                    Log.d("ResponseSummaryFragment","Items in the list "+responseSummaryCardDataList.size());
                    recyclerView = getActivity().findViewById(R.id.responseSummaryRecyclerView);
                    responseSummaryAdapter = new ResponseSummaryAdapter(responseSummaryCardDataList, getActivity().getApplication());
                    recyclerView.setAdapter(responseSummaryAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ResponseSummaryFragment.this.getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResponseSummaryFragment.this.getContext(), "Something went wrong! Firebase Data not accessible", Toast.LENGTH_LONG).show();
            }
        });

        return binding.getRoot();
    }

    private List<ResponseSummaryCardData> getUserResponseDataFromDB() {
        List<ResponseSummaryCardData> list = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, Object>> entry : responsesHashMapDb.entrySet()) {
            HashMap<String, Object> responseDetailsHashMap = (HashMap<String, Object>) entry.getValue();
            //Get all the details from the survey
            for (Map.Entry<String, Object> stringObjectEntry : responseDetailsHashMap.entrySet()) {
                // This section to get only the survey questions and answer
                if (stringObjectEntry.getKey().equalsIgnoreCase("user_response_to_questions")) {
                    HashMap<String, String> responseSummaryMap = (HashMap<String, String>) stringObjectEntry.getValue();

                    // Sort the questions in ascending order
                    String question = "", response = "", questionNumber = "";
                    for (int questionCounter = 1; questionCounter <= responseSummaryMap.size(); questionCounter++){
                        for (Map.Entry<String, String> eachResponseSummaryMap : responseSummaryMap.entrySet()) {
                            question = eachResponseSummaryMap.getKey();
                            response = eachResponseSummaryMap.getValue();
                            questionNumber = getQuestionNumberFromQuestion(question);
                            if(Integer.valueOf(questionNumber).equals(questionCounter)) {
                                break;
                            }
                        }
                        list.add(new ResponseSummaryCardData("Q." + question, "Response: " + response, questionNumber));
                    }
                }
            }
        }
        return list;
    }

    private String getQuestionNumberFromQuestion(String currentQuestion) {
        String questionNumber = "";
        for (Map.Entry<Integer, String> eachQuestion : questionsMap.entrySet()){
            if (eachQuestion.getValue().equalsIgnoreCase(currentQuestion)){
                questionNumber = String.valueOf(eachQuestion.getKey());
                break;
            }
        }
        return questionNumber;
    }
}