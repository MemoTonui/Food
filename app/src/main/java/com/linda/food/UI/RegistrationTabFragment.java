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




public class RegistrationTabFragment extends Fragment implements View.OnClickListener{
     EditText username;
    EditText email;
    EditText fullName;
    EditText phone;
    EditText password;
    EditText confirmPassword;
    Button registerButton;
    TextView haveAnAccount;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mAuthProgressDialog;
    private int SIGN_IN = 1;
    public static final String TAG =RegistrationTabFragment.class.getSimpleName();
    private String mName;

    float v = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.registration_tab_fragment,container,false);
        username =(EditText) root.findViewById(R.id.username);
        email = root.findViewById(R.id.email);
        fullName = root.findViewById(R.id.fullname);
        phone = root.findViewById(R.id.phone);
        password = root.findViewById(R.id.password);
        confirmPassword = root.findViewById(R.id.confirmPassword);
        registerButton = root.findViewById(R.id.register);
        haveAnAccount = root.findViewById(R.id.haveAnAccount);
        //set onClick Lister
        registerButton.setOnClickListener(this::onClick);

        //Instantiate FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        createAuthStateListener();
        createAuthProgressDialog();

        //Animate
        username.setTranslationX(800);
        fullName.setTranslationX(800);
        email.setTranslationX(800);
        phone.setTranslationX(800);
        password.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        registerButton.setTranslationX(800);
        haveAnAccount.setTranslationX(800);

        username.setAlpha(v);
        fullName.setAlpha(v);
        email.setAlpha(v);
        phone.setAlpha(v);
        password.setAlpha(v);
        confirmPassword.setAlpha(v);
        registerButton.setAlpha(v);
        haveAnAccount.setAlpha(v);

        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        fullName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1300).start();
        registerButton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1500).start();
        haveAnAccount.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1700).start();
        return root;
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase");
        mAuthProgressDialog.setCancelable(false);
    }

    private void createAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!= null){
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };

    }

    private boolean register(){
        String myUsername = username.getText().toString().trim();
        String myEmail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String myFullName = fullName.getText().toString().trim();

        if(myUsername.equals("")){
            username.setError("Please Input a Username");
            return false;
        }
        if(myEmail.equals("")){
            email.setError("Please input your email");
            return false;
        }
        if(pass.equals("")){
            password.setError("Please Input a password");
            return false;
        }
        if(confirmPass.equals("")){
            confirmPassword.setError("Please confirm your password");
            return false;
        }
        if(phoneNumber.equals("")){
            phone.setError("Please input your phone number");
            return false;
        }
        if(myFullName.equals("")){
            password.setError("Please Input your Full Name");
            return false;
        }
        isValid(pass,confirmPass);
        mAuthProgressDialog.show();
        mAuth.createUserWithEmailAndPassword(myEmail,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthProgressDialog.dismiss();
                Log.d(TAG, "createAccountWithEmailComplete" + task.isSuccessful());
                Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_LONG).show();

                if (!task.isSuccessful()) {
                    Log.w(TAG, "createAccountWithEmail", task.getException());
                    Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        return true;
    }

    private void isValid(String pass, String confirmPass){
        if(pass.length() <=8){
            password.setError("Your Password is too short");
            return;
        }
        if(!pass.toLowerCase().equals(confirmPass.toLowerCase())){
            confirmPassword.setError("This doesn't match your password ");
        }
    }
    @Override
    public void onClick(View view) {
        if(view == registerButton){
          if(register()) {
              startActivity(new Intent(getContext(), MainActivity.class));
          }
        else {
            return;
          }
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
