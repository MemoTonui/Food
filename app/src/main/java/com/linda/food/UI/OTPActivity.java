package com.linda.food.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.linda.food.Constants.Constants;
import com.linda.food.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OTPActivity extends AppCompatActivity {

    private String phoneNumber , mVerificationId;
    @BindView(R.id.otp)
    EditText otp;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.resend)
    TextView resend;
    @BindView(R.id.loader)
    MKLoader loader;


    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        phoneNumber = sharedPreferences.getString(Constants.PREFERENCES_PHONE_NUMBER, null);

        //Checks minimum sdk version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        firebaseAuth =  FirebaseAuth.getInstance();

        //method to send verification code
        sendVerificationCode();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(OTPActivity.this,"Please Enter OTP Received",Toast.LENGTH_LONG).show();
                }
                else if(otp.getText().toString().trim().length()!= 6){
                    Toast.makeText(OTPActivity.this,"Please Enter The Correct OTP",Toast.LENGTH_LONG).show();
                }
                else {
                    loader.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential =PhoneAuthProvider.getCredential(mVerificationId,otp.getText().toString().trim());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        //If the user takes ovr 60 seconds to enter OTP, resend text appears
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });
    }


    //method to send verification code
    private void sendVerificationCode() {

        new CountDownTimer(60000,1000){

            //While waiting for OTP set the timer instead of OTP text
            @Override
            public void onTick(long millisUntilFinished) {
                resend.setText((""+millisUntilFinished/1000));
                resend.setEnabled(false);
            }

            //After 60 seconds are over show the resend  text
            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);
            }
        }.start();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                // This callback will be invoked in two situations:
                                // 1 - Instant verification. In some cases the phone number can be instantly
                                //     verified without needing to send or enter a verification code.
                                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                //     detect the incoming verification SMS and perform verification without
                                //     user action.


                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // This callback is invoked in an invalid request for verification is made,
                                // for instance if the the phone number format is not valid.
                                Toast.makeText(OTPActivity.this,"Verification Failed", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number, we
                                // now need to ask the user to enter the code and then construct a credential
                                // by combining the code with a verification ID.
                                // Save verification ID and resending token so we can use them later
                                mVerificationId = verificationId;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //Sign in with phone method
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(OTPActivity.this, ProfileSetup.class));
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                            addFirebaseUIDToSharedPreferences(user.getUid());
                            // Update UI
                        } else {
                            Toast.makeText(OTPActivity.this,"Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    //Add firebase uid name to sharedpreferences
    private void addFirebaseUIDToSharedPreferences(String firebase){
        editor.putString(Constants.PREFERENCES_FIREBASE_USER_ID,firebase).apply();
    }
}