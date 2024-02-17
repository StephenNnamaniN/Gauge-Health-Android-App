package uk.ac.tees.a0278818.gaugehealth;

import android.os.Bundle;
import android.text.TextUtils;
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

public class UpdatePasswordFragment extends Fragment {
    private FirebaseAuth authProfile;
    private EditText editTextCurrPwd, editTextNewPwd, editTextConfirmNewPwd;
    private TextView authenticatedTextView;
    private Button changePwdBtn, authenticateBtn;
    private ProgressBar progressBar;
    private String userCurrPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        editTextNewPwd = view.findViewById(R.id.update_new_password_editText);
        editTextCurrPwd = view.findViewById(R.id.update_password_editText);
        editTextConfirmNewPwd = view.findViewById(R.id.update_new_password_editText2);
        authenticatedTextView = view.findViewById(R.id.update_password_header2);
        progressBar = view.findViewById(R.id.password_verify_pgBar);
        authenticateBtn = view.findViewById(R.id.update_password_verifyBtn);
        changePwdBtn = view.findViewById(R.id.update_email_btn);

        //Disable editText for new password, confirm password and change password btn until user is authenticate
        editTextNewPwd.setEnabled(false);
        editTextConfirmNewPwd.setEnabled(false);
        changePwdBtn.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")){
            Toast.makeText(getContext(), "Something went wrong! User's details not available",
                    Toast.LENGTH_LONG).show();
            ProfileFragment profileFragment = new ProfileFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, profileFragment,"null")
                    .addToBackStack(null)
                    .commit();
        } else {
            reAuthenticate(firebaseUser);
        }

        return view;
    }

    //ReAuthenticate user before changing password
    private void reAuthenticate(FirebaseUser firebaseUser) {
        authenticateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCurrPwd = editTextCurrPwd.getText().toString();

                if (TextUtils.isEmpty(userCurrPwd)){
                    Toast.makeText(getContext(), "Password is needed", Toast.LENGTH_LONG).show();
                    editTextCurrPwd.setError("Please enter your current password to authenticate");
                    editTextCurrPwd.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);

                    //ReAuthenticate user now
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userCurrPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                //Disable editText for current password. Enable EditText for new password and confirm new password
                                editTextCurrPwd.setEnabled(false);
                                editTextNewPwd.setEnabled(true);
                                editTextConfirmNewPwd.setEnabled(true);

                                //Enable change pwd Button. Disable authenticate button
                                authenticateBtn.setEnabled(false);
                                changePwdBtn.setEnabled(true);

                                //Set TextView to show user is authenticate/verified
                                authenticatedTextView.setText("You are authenticated/verified." +
                                        "You can change password now!");
                                Toast.makeText(getContext(), "Password has been verified." +
                                        "Change password now", Toast.LENGTH_LONG).show();

                                //Update color of change password button
                                changePwdBtn.setBackgroundTintList(ContextCompat.getColorStateList(
                                        getContext(), R.color.green));
                                
                                changePwdBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {
        String userNewPwd = editTextNewPwd.getText().toString();
        String userConfirmPwd = editTextConfirmNewPwd.getText().toString();

        if (TextUtils.isEmpty(userNewPwd)) {
            Toast.makeText(getContext(), "New Password is needed", Toast.LENGTH_LONG).show();
            editTextNewPwd.setError("Please enter your new password");
            editTextNewPwd.requestFocus();
        } else if (TextUtils.isEmpty(userConfirmPwd)){
            Toast.makeText(getContext(), "Please confirm your new password", Toast.LENGTH_LONG).show();
            editTextNewPwd.setError("Please re-enter your new password");
            editTextNewPwd.requestFocus();
        } else if (!userNewPwd.matches(userConfirmPwd)){
            Toast.makeText(getContext(), "New Password is needed", Toast.LENGTH_LONG).show();
            editTextNewPwd.setError("Please enter your new password");
            editTextNewPwd.requestFocus();
        } else if (userCurrPwd.matches(userNewPwd)){
            Toast.makeText(getContext(), "New Password cannot be same as old password", Toast.LENGTH_LONG).show();
            editTextNewPwd.setError("Please enter a new password");
            editTextNewPwd.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(userNewPwd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Password has been change", Toast.LENGTH_LONG).show();
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
}