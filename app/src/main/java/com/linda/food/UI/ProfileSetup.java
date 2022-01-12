package com.linda.food.UI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.linda.food.Constants.Constants;
import com.linda.food.Network.FoodzillaClient;
import com.linda.food.Network.FoodzillaService;
import com.linda.food.R;
import com.linda.food.models.User;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSetup extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.profilePic) ImageView profilePic;
    @BindView(R.id.fab)  FloatingActionButton fab;
    @BindView(R.id.save) Button save;
    @BindView(R.id.location) EditText location;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    String fullName;
    String phone;
    String email;
    String username;
    String myLocation;
    Uri filePath;
    String imgUrl;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ProfileSetup.this);
        editor = sharedPreferences.edit();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fab.setOnClickListener(this);
        save.setOnClickListener(this);
        location.setOnClickListener(this);
        if (isServicesOkay()){
            init();
        }

    }

    private void init(){
        startActivity(new Intent(ProfileSetup.this,MapActivity.class));
    }
    private void setupProfile(){
        fullName = sharedPreferences.getString(Constants.PREFERENCES_FULL_NAME, null);
        phone = sharedPreferences.getString(Constants.PREFERENCES_PHONE_NUMBER, null);
        email = sharedPreferences.getString(Constants.PREFERENCES_USER_EMAIL, null);
        username = sharedPreferences.getString(Constants.PREFERENCES_USER_NAME, null);
        myLocation = location.getText().toString();

        User user = new User();
        user.setEmail(email);
        user.setImgUrl(imgUrl);
        user.setUsername(username);
        user.setPhoneNumber(phone);
        user.setFullName(fullName);
        user.setLocation(myLocation);

        FoodzillaService service = FoodzillaClient.getClient();
        Call<User> userCall = service.createUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ProfileSetup.this,"Profile created", Toast.LENGTH_LONG).show();
                    addUserIdToPreferences(response.body().getId());
                    startActivity(new Intent(ProfileSetup.this, MainActivity.class));
                }
                else {
                    Toast.makeText(ProfileSetup.this,"Unable to create Profile", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileSetup.this,"Please Check Your Internet Connection", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        //imgUrl = uri.getPath();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
            Glide.with(getApplicationContext()).load(bitmap).apply(RequestOptions.circleCropTransform()).into(profilePic);

        } catch (IOException e) {
            e.printStackTrace();
        }
        uploadImage();
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileSetup.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT).show();
                                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imgUrl = uri.toString();
                                        }
                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(ProfileSetup.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v== fab){
            ImagePicker.Companion.with(ProfileSetup.this).start();
        }
        if (v == save){
            setupProfile();
        }
        if (v == location){

        }
    }
    private void addUserIdToPreferences(String id){
        editor.putString(Constants.PREFERENCES_USER_ID,id).apply();
    }

    public boolean isServicesOkay() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ProfileSetup.this);

        if (available == ConnectionResult.SUCCESS){
            //Everything is okay
            return true;
        }
        else  if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //There's a problem and it can be fixed
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ProfileSetup.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            //Can'tresolve this error
            Toast.makeText(getApplicationContext(),"Your device is not compatible", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}