package com.linda.food.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.linda.food.R;

public class LoginTabFragment extends Fragment implements View.OnClickListener {

    EditText email;

    EditText password;

    TextView forgotPassword;

    Button loginButton;

    TextView noAccount;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private int SIGN_IN = 1;
    private ProgressDialog mAuthProgressDialog;
    public static final String TAG =LoginTabFragment.class.getSimpleName();

    float v = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        email = root.findViewById(R.id.emailAddress);
        password = root.findViewById(R.id.pass);
        forgotPassword = root.findViewById(R.id.forgot_password);
        loginButton = root.findViewById(R.id.login);
        noAccount = root.findViewById(R.id.noAccount);

        //Set on click listener
        loginButton.setOnClickListener(this::onClick);
        //Animate
        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPassword.setTranslationX(800);
        loginButton.setTranslationX(800);
        noAccount.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forgotPassword.setAlpha(v);
        loginButton.setAlpha(v);
        noAccount.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        noAccount.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


        //Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }


        };
        createAuthProgressDialog();
        return root;
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase");
        mAuthProgressDialog.setCancelable(false);
    }

    //Onclick methods
    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            if(loginWithPassword()) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            else {
                return;
            }
        }
    }
    //Method to login with password
    private boolean loginWithPassword() {
        String myEmail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (myEmail.equals("")) {
            email.setError("Please Enter Your Email");
            return false;
        }
        if (pass.equals("")) {
            password.setError("Please Enter Your Password");
            return false;
        }
        mAuthProgressDialog.show();
        mAuth.signInWithEmailAndPassword(myEmail, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthProgressDialog.dismiss();


                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail", task.getException());
                    Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    Log.d(TAG, "signInWithEmailComplete" + task.isSuccessful());
                    Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}

