package uk.ac.tees.a0278818.gaugehealth;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullName, email, dob, mobile, password, conPassword;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButtonSelected;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Here we set a title for the registration page and a toast to prompt the new user
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        Toast.makeText(RegisterActivity.this, "You can now register!", Toast.LENGTH_LONG).show();

        // Let's find all the views and attach them the appropriate objects
        fullName = findViewById(R.id.register_enter_fullName);
        email = findViewById(R.id.register_enter_email);
        dob = findViewById(R.id.register_enter_dob);
        mobile = findViewById(R.id.register_enter_mobile);
        password = findViewById(R.id.register_password2);
        conPassword = findViewById(R.id.register_confirm_password2);
        progressBar = findViewById(R.id.register_progBar);

        // Radio button for gender
        radioGroup = findViewById(R.id.radio_group);
        radioGroup.clearCheck();

        // Setting up DatePicker on EditText
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        // Here we define our register button locally since they are not going to be used by any other method
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroup.getCheckedRadioButtonId();
                radioButtonSelected = findViewById(selectedGenderId);

                // We obtain the data entered by the new user
                String fullNameTxt = fullName.getText().toString();
                String emailTxt = email.getText().toString();
                String dobTxt = dob.getText().toString();
                String mobileTxt = mobile.getText().toString();
                String passwordTxt = password.getText().toString();
                String conPasswordTxt = conPassword.getText().toString();
                String genderTxt; // we can't obtain the value before verifying if any button has been clicked

                // Validate mobile number using Matcher and Pattern (Regular Expression)
                String mobileRegex = "(?:(?:(?:\\+|00)44[\\s\\-\\.]?)?(?:(?:\\(?0\\)?)[\\s\\-\\.]?)?(?:\\d[\\s\\-\\.]?){10})|(?=\\(?\\d*\\)?[\\x20\\-\\d]*)(\\(?\\)?\\x20*\\-*\\d){11}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(mobileTxt);

                // Here we make sure that all fields and filled correctly by the user.
                if (TextUtils.isEmpty(fullNameTxt)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    fullName.setError("Full Name is required");
                    fullName.requestFocus();
                } else if (TextUtils.isEmpty(emailTxt)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    email.setError("Email is required");
                    email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    email.setError("Valid email is required");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(dobTxt)) {
                    Toast.makeText(RegisterActivity.this, "Please enter date of birth", Toast.LENGTH_LONG).show();
                    dob.setError("Date of Birth is required");
                    dob.requestFocus();
                } else if (radioGroup.getCheckedRadioButtonId() == -1 ) {
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    radioButtonSelected.setError("Gender is required");
                    radioButtonSelected.requestFocus();
                } else if (TextUtils.isEmpty(mobileTxt)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile no.", Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile No. should be 10 digits");
                    mobile.requestFocus();
                } else if (mobileTxt.length() != 11){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile no. should be 11 digits");
                    mobile.requestFocus();
                } else if (!mobileMatcher.find()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
                    mobile.setError("Mobile no. is invalid");
                    mobile.requestFocus();
                } else if (TextUtils.isEmpty(passwordTxt)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    password.setError("Password is required");
                    password.requestFocus();
                } else if (passwordTxt.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    password.setError("Password is too weak");
                    password.requestFocus();
                } else if (TextUtils.isEmpty(conPasswordTxt)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    conPassword.setError("Password confirmation is required");
                    conPassword.requestFocus();
                } else if (!passwordTxt.equals(conPasswordTxt)){
                    Toast.makeText(RegisterActivity.this, "Confirm password is not same as password", Toast.LENGTH_LONG).show();
                    conPassword.setError("Password confirmation is required");
                    conPassword.requestFocus();
                    // Clear entered passwords
                    password.clearComposingText();
                    conPassword.clearComposingText();
                } else {
                    genderTxt = radioButtonSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(fullNameTxt, emailTxt, mobileTxt, dobTxt, genderTxt, mobileTxt, passwordTxt);
                }
            }
        });
    }

    // Register user with input credentials
    private void registerUser(String fullNameTxt, String emailTxt, String mobileTxt, String dobTxt, String genderTxt, String mobileTxt1, String passwordTxt) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            // Update user display name
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fullNameTxt).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            // Let's store the user data in the Firebase Realtime Database
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(dobTxt, genderTxt, mobileTxt);
                            // Extracting user reference from database for "Registered Users"
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        // Send verification email
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify email",
                                                Toast.LENGTH_LONG).show();

                                        // Open the MainActivity after successful registration
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        // Here we write to prevent user from returning to RegisterActivity when back button is pressed.
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "User registration failed. Please try again",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    // Hide the progressBar whether registration is successful or not
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

//                            // Send email verification
//                            assert firebaseUser != null;
//                            firebaseUser.sendEmailVerification();
                        } else {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                password.setError("Your password is too week. Kindly use a mix of alphabets, numbers and special characters");
                                password.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                password.setError("Your email is invalid or already in use. Kindly re-enter.");
                                password.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                password.setError("User is already registered with this email. Use another email.");
                                password.requestFocus();
                            } catch (Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}