package uk.ac.tees.a0278818.gaugehealth;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileFragment extends Fragment {

    private EditText updateFullName, updateDob, updateMobile;
    private RadioGroup updateGenderRdGroup;
    private RadioButton updateSelectedGender;
    private String txtFullName, txtDob, txtMobile, txtGender;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        progressBar = view.findViewById(R.id.update_profile_progBar);
        updateFullName = view.findViewById(R.id.editText_profile_name);
        updateDob = view.findViewById(R.id.editText_profile_dob);
        updateMobile = view.findViewById(R.id.editText_profile_mobile);
        updateGenderRdGroup = view.findViewById(R.id.update_profile_radioGp);
        
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        
        // Show Profile Data
        assert firebaseUser != null;
        showProfile(firebaseUser);

        //Update Profile Pic
        Button updatePicBtn = view.findViewById(R.id.update_profile_pic);
        updatePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage uploadImageFragment = new UploadImage();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, uploadImageFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button updateEmailBtn = view.findViewById(R.id.update_profile_email);
        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEmailFragment updateEmailFragment = new UpdateEmailFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, updateEmailFragment, "null")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Setting up DatePicker on EditText
        updateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extracting saved dd, mm, yyyy into different variables by creating an array delimiter
                String[] textDob = txtDob.split("/");

                int day = Integer.parseInt(textDob[0]);
                int month = Integer.parseInt(textDob[1]) - 1; // - 1 takes care of month index starting from 0
                int year = Integer.parseInt(textDob[2]);

                DatePickerDialog picker;
                picker = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        updateDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //Now that all the data has been extracted and set let us update profile
        Button updateProfileBtn = view.findViewById(R.id.update_profile_btn);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });

        return view;
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = updateGenderRdGroup.getCheckedRadioButtonId();
        updateSelectedGender = requireView().findViewById(selectedGenderID);

        // Validate mobile number using Matcher and Pattern (Regular Expression)
        String mobileRegex = "(?:(?:(?:\\+|00)44[\\s\\-\\.]?)?(?:(?:\\(?0\\)?)[\\s\\-\\.]?)?(?:\\d[\\s\\-\\.]?){10})|(?=\\(?\\d*\\)?[\\x20\\-\\d]*)(\\(?\\)?\\x20*\\-*\\d){11}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(txtMobile);

        // Here we make sure that all fields and filled correctly by the user.
        if (TextUtils.isEmpty(txtFullName)) {
            Toast.makeText(getContext(), "Please enter your full name", Toast.LENGTH_LONG).show();
            updateFullName.setError("Full Name is required");
            updateFullName.requestFocus();
        }  else if (TextUtils.isEmpty(txtDob)) {
            Toast.makeText(getContext(), "Please enter date of birth", Toast.LENGTH_LONG).show();
            updateDob.setError("Date of Birth is required");
            updateDob.requestFocus();
        } else if (TextUtils.isEmpty(updateSelectedGender.getText())) {
            Toast.makeText(getContext(), "Please select your gender", Toast.LENGTH_LONG).show();
            updateSelectedGender.setError("Gender is required");
            updateSelectedGender.requestFocus();
        } else if (TextUtils.isEmpty(txtMobile)) {
            Toast.makeText(getContext(), "Please enter your mobile no.", Toast.LENGTH_LONG).show();
            updateMobile.setError("Mobile No. should be 10 digits");
            updateMobile.requestFocus();
        } else if (txtMobile.length() != 11){
            Toast.makeText(getContext(), "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
            updateMobile.setError("Mobile no. should be 11 digits");
           updateMobile.requestFocus();
        } else if (!mobileMatcher.find()){
            Toast.makeText(getContext(), "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
            updateMobile.setError("Mobile no. is invalid");
            updateMobile.requestFocus();
        }  else {
            //Here we can obtain all the updated data entered by the user
            txtGender = updateSelectedGender.getText().toString().trim();
            txtFullName = updateFullName.getText().toString().trim();
            txtDob = updateDob.getText().toString().trim();
            txtMobile = updateMobile.getText().toString().trim();

            //Enter user data into Firebase Realtime Database
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtDob, txtGender, txtMobile);

            //Extract user reference from Database for "Registered Users"
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userID = firebaseUser.getUid();

            progressBar.setVisibility(View.VISIBLE);
            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        //Setting new display name for user
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txtFullName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(getContext(), "Updates Successful!", Toast.LENGTH_LONG).show();

                        // We return to ProfileFragment after successful user profile update
                        ProfileFragment profileFragment = new ProfileFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, profileFragment, "null")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        try{
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIdRegistered = firebaseUser.getUid();

        //Extracting user reference from database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIdRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    txtFullName = firebaseUser.getDisplayName();
                    txtDob = readUserDetails.doB;
                    txtMobile = readUserDetails.mobile;
                    txtGender = readUserDetails.gender;

                    updateFullName.setText(txtFullName);
                    updateDob.setText(txtDob);
                    updateMobile.setText(txtMobile);

                    //We will show gender through Radio Button
                    if (txtGender.equals("Male")){
                        updateSelectedGender = getView().findViewById(R.id.update_radio_male);
                    } else {
                        updateSelectedGender = getView().findViewById(R.id.update_radio_female);
                    }
                    updateSelectedGender.setChecked(true);
                } else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}