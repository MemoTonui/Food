package com.linda.food.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.profile_image) ImageView profile;
    @BindView(R.id.fullname) TextView fullName;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.full_name_profile) TextInputEditText fullNameProfile;
    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.emailAddress) TextInputEditText emailAddress;
    @BindView(R.id.phone) TextInputEditText phoneNumber;
    @BindView(R.id.update) Button update;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String id;
    String myFullName;
    String myEmail;
    String myPhone;
    String myUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Profile.this);
        editor = sharedPreferences.edit();

       id = sharedPreferences.getString(Constants.PREFERENCES_USER_ID,null);

       getUser();

       update.setOnClickListener(this);


    }
    private  void getUser(){
        FoodzillaService service = FoodzillaClient.getClient();
        Call<User> userCall = service.getUser(id);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User user = response.body();
                    Glide.with(getApplicationContext()).load(user.getImgUrl()).apply(RequestOptions.circleCropTransform()).into(profile);
                    fullName.setText(user.getFullName());
                    email.setText(user.getEmail());
                    emailAddress.setText(user.getEmail());
                    fullNameProfile.setText(user.getFullName());
                    username.setText(user.getUsername());
                    phoneNumber.setText(user.getPhoneNumber());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == update){
            myFullName = fullNameProfile.getText().toString();
            myPhone = phoneNumber.getText().toString();
            myUsername = username.getText().toString();
            myEmail = emailAddress.getText().toString();

            User user = new User();
            user.setFullName(myFullName);
            user.setPhoneNumber(myPhone);
            user.setEmail(myEmail);
            user.setUsername(myUsername);

            FoodzillaService service = FoodzillaClient.getClient();
            Call<User> call = service.updateUser(id, user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Update Successful", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please try again later", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection", Toast.LENGTH_LONG).show();

                }
            });



        }
    }
}