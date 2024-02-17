package uk.ac.tees.a0278818.gaugehealth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailFragment extends Fragment {
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView userAuthenticatedTxt;
    private String userOldEmail, userNewEmail, userPwd;
    private Button updateEmailBtn, verifyUserBtn;
    private EditText editTextNewEmail, editTextPwd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_email, container, false);

        progressBar = view.findViewById(R.id.email_verify_pgBar);
        editTextPwd = view.findViewById(R.id.update_email_password_editText);
        editTextNewEmail = view.findViewById(R.id.update_new_email_editText);
        userAuthenticatedTxt = view.findViewById(R.id.update_email_header2);
        updateEmailBtn = view.findViewById(R.id.update_email_btn);
        verifyUserBtn = view.findViewById(R.id.update_email_verifyBtn);

        updateEmailBtn.setEnabled(false); // make update email button disabled before verification
        editTextNewEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Set old email ID on TextView
        userOldEmail = firebaseUser.getEmail();
        TextView textviewOldEmail = view.findViewById(R.id.old_email_textView);
        textviewOldEmail.setText(userOldEmail);

        if (firebaseUser.equals("")){
            Toast.makeText(getContext(), "Something went wrong! User details not available.", Toast.LENGTH_LONG).show();
        }else {
            reAuthenticate(firebaseUser);
        }

        return view;
    }

    //reAuthenticate/verify user before updating email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        verifyUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtain password for authentication
                userPwd = editTextPwd.getText().toString().trim();

                if (TextUtils.isEmpty(userPwd)){
                    Toast.makeText(getContext(), "Password is required to continue", Toast.LENGTH_LONG).show();
                    editTextPwd.setError("Please enter your password for authentication");
                    editTextPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Password has been verified." +
                                        "You can update email now.", Toast.LENGTH_LONG).show();

                                //Set TextView to show that user has been authenticated
                                userAuthenticatedTxt.setText("You are authenticated. You can update your email now.");

                                // Disable editText for password and enable editText for new email and update button
                                editTextNewEmail.setEnabled(true);
                                editTextPwd.setEnabled(false);
                                verifyUserBtn.setEnabled(false);
                                updateEmailBtn.setEnabled(true);

                                //Change color of update email button
                                updateEmailBtn.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),
                                        R.color.green));

                                updateEmailBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = editTextNewEmail.getText().toString();
                                        if (TextUtils.isEmpty(userNewEmail)){
                                            Toast.makeText(getContext(), "New Email is required", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter new email");
                                            editTextNewEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()){
                                            Toast.makeText(getContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please provide valid email");
                                            editTextNewEmail.requestFocus();
                                        } else if (userOldEmail.matches(userNewEmail)){
                                            Toast.makeText(getContext(), "New Email cannot be same as old email", Toast.LENGTH_LONG).show();
                                            editTextNewEmail.setError("Please enter new email");
                                            editTextNewEmail.requestFocus();
                                        } else {
                                            progressBar.setVisibility(View.VISIBLE);
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    //Verify email
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(getContext(), "Email has been updated. Please verify your new email", Toast.LENGTH_LONG).show();

                    ProfileFragment profileFragment = new ProfileFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, profileFragment,"null")
                            .addToBackStack(null)
                            .commit();
                } else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}