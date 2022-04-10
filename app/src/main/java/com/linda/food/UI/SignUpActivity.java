package com.linda.food.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;
import com.linda.food.Constants.Constants;
import com.linda.food.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity  {
    @BindView(R.id.codePicker)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.otp)
    EditText phoneNumber;
    @BindView(R.id.finish)
    Button proceedButton;
    String phone;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        //Shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        //Checks minimum sdk version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        /*if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
        }*/

        countryCodePicker.registerCarrierNumberEditText(phoneNumber);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phoneNumber.getText().toString().trim())){
                    phoneNumber.setError("Please enter your phone number");
                    Toast.makeText(SignUpActivity.this,"Please enter your phone number",Toast.LENGTH_LONG).show();
                }
                else  if (phoneNumber.getText().toString().trim().length()!=10){
                    phoneNumber.setError("Please enter a correct phone number");
                    Toast.makeText(SignUpActivity.this,"Please enter a correct phone number",Toast.LENGTH_LONG).show();
                }
                else {

                    phone = countryCodePicker.getFullNumberWithPlus().trim();
                    addPhoneNumberToSharedPreferences(phone);
                    Intent intent = new Intent(SignUpActivity.this,OTPActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //Add phone number name to sharedpreferences
    private void addPhoneNumberToSharedPreferences(String phone){
        editor.putString(Constants.PREFERENCES_PHONE_NUMBER,phone).apply();
    }
}