package uk.ac.tees.a0278818.gaugehealth.Survey;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.a0278818.gaugehealth.R;
import uk.ac.tees.a0278818.gaugehealth.adapters.ResponseAdapter;
import uk.ac.tees.a0278818.gaugehealth.models.ResponseCardData;
import uk.ac.tees.a0278818.gaugehealth.utils.ResponseClickListener;

public class ResponseFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    ResponseAdapter responseAdapter;
    RecyclerView recyclerView;
    ResponseClickListener responseClickListener;
    TextView responseTime_TextView;
    String createdDateTimeKeyStr;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseUserResponseRef;
    private String userEmail, userID;
    private final HashMap<String, HashMap<String, Object>> responsesHashMapFromDb = new HashMap<>();
    private List<ResponseCardData> responseCardDataList = new ArrayList<>();


    public ResponseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the responses for the current logged in user from the database
        // Get the current logged in user
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        
        if (firebaseUser == null){
            Toast.makeText(ResponseFragment.this.getContext(),
                    "Something went wrong, user profile not available", Toast.LENGTH_SHORT).show();
        } else {
            userEmail = firebaseUser.getEmail();
            userID = firebaseUser.getUid();
        }
        
        mDatabaseUserResponseRef = FirebaseDatabase.getInstance().getReference("User_Responses");
        mDatabaseUserResponseRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    long count = snapshot.getChildrenCount();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        HashMap<String, Object> eachResponseDetails = (HashMap<String, Object>) snap.getValue();
                        responsesHashMapFromDb.put(snap.getKey(), eachResponseDetails);
                    } try {
                        responseCardDataList = getResponseDataFromDb();
                    } catch (ParseException e){
                        throw new RuntimeException(e);
                    }

                    recyclerView = getActivity().findViewById(R.id.tasksRecyclerView);
                    responseClickListener = new ResponseClickListener() {
                        @Override
                        public void click(View v, int index) {
                            super.click(v, index);
                            responseTime_TextView = (TextView) v.findViewById(R.id.responseTime_txtview);
                            createdDateTimeKeyStr = (String) responseTime_TextView.getTag();
                            // Open fragment with response details
                            ResponseSummaryFragment respSumFragment = new ResponseSummaryFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            Bundle result = new Bundle();
                            result.putString("selectedResponseDateTimeKey", createdDateTimeKeyStr);
                            respSumFragment.setArguments(result);
                            fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), respSumFragment, "findThisFragment")
                                    .addToBackStack(null).commit();
                        }
                    };
                    //-----------View Survey ActionButton EnD--------------
                    responseAdapter = new ResponseAdapter(responseCardDataList, getActivity().getApplication(), responseClickListener);
                    recyclerView.setAdapter(responseAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ResponseFragment.this.getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResponseFragment.this.getContext(), "Something went wrong! Firebase Data not accessible", Toast.LENGTH_LONG).show();
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_response, container, false);
    }

    private List<ResponseCardData> getResponseDataFromDb() throws ParseException{
        List<ResponseCardData> list = new ArrayList<>();
        List<String> dateTimeList = new ArrayList<>();
        for (Map.Entry<String, HashMap<String, Object>> entry : responsesHashMapFromDb.entrySet()) {
            String createdDateTime = entry.getKey();
            dateTimeList.add(createdDateTime);
        }
        // Sort the ArrayList in descending order
        Collections.sort(dateTimeList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // Parse the dates from the string and compare them in reverse order
                return -1 * o2.compareTo(o1);
            }
        });

        // Loop the sorted list and match the date, then add it to the recyclerView list
        for (String sortedDateTime : dateTimeList) {
            for (Map.Entry<String, HashMap<String, Object>> entry : responsesHashMapFromDb.entrySet()){
                String createdDateTime = entry.getKey();
                if (createdDateTime.equalsIgnoreCase(sortedDateTime)){
                    String dayOfWeek = "";
                    String day = "";
                    String month = "";
                    String year = "";
                    String createdByMail = "";
                    String whiteSpaceMetaChar = "\\s";
                    String time = createdDateTime.split(whiteSpaceMetaChar)[1];

                    HashMap<String, Object> respobseDetailsHashMap = entry.getValue();
                    for (Map.Entry<String, Object> stringObjectEntry : respobseDetailsHashMap.entrySet()) {
                        if (stringObjectEntry.getKey().equalsIgnoreCase("dayOfTheWeek")){
                            dayOfWeek = (String) stringObjectEntry.getValue();
                        }else if (stringObjectEntry.getKey().equalsIgnoreCase("day")) {
                            day = (String) stringObjectEntry.getValue();
                        } else if (stringObjectEntry.getKey().equalsIgnoreCase("monthString")) {
                            month = (String) stringObjectEntry.getValue();
                        } else if (stringObjectEntry.getKey().equalsIgnoreCase("year")) {
                            year = (String) stringObjectEntry.getValue();
                        } else if (stringObjectEntry.getKey().equalsIgnoreCase("loggedIn_user_email")) {
                            createdByMail = (String) stringObjectEntry.getValue();
                        }
                    }
                    list.add(new ResponseCardData(dayOfWeek, day, month, year, "Time: " +
                            time, "Created By: " + createdByMail, createdDateTime));
                }
            }
        }
        return list;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("NavItemClicked", "View Survey");
        return false;
    }
}