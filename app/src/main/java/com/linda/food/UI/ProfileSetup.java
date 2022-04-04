package com.linda.food.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.drjacky.imagepicker.ImagePicker;
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

import static com.linda.food.Constants.Constants.IMAGE_REQUEST;

public class ProfileSetup extends AppCompatActivity implements View.OnClickListener {
    @BindView (R.id.firstName) EditText firstName;
    @BindView (R.id.lastName) EditText lastName;
    @BindView (R.id.username) EditText username;
    @BindView (R.id.email) EditText emailAddress;
    @BindView(R.id.profilePic) ImageView profilePic;
    @BindView(R.id.fab)  FloatingActionButton fab;
    @BindView(R.id.save) Button save;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = ProfileSetup.class.getSimpleName();


    String fullName;
    String phone;
    String email;
    String myUsername;
    String firebaseUid;

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



        //Onclick listeners
        fab.setOnClickListener(this);
        save.setOnClickListener(this);


    }

    private void init(){
        startActivity(new Intent(ProfileSetup.this,MapActivity.class));
    }
    private void setupProfile(){
        fullName = firstName.getText().toString()+ " " + lastName.getText().toString();
        phone = sharedPreferences.getString(Constants.PREFERENCES_PHONE_NUMBER, null);
        email = emailAddress.getText().toString();
        myUsername = username.getText().toString();
        firebaseUid = sharedPreferences.getString(Constants.PREFERENCES_FIREBASE_USER_ID, null);
        if (fullName.isEmpty()){
            firstName.setError("Please Enter Your First Name");
        }
            else if (email.isEmpty()){
                emailAddress.setError("Please Enter Your Email");
            }
        else
        if (myUsername.isEmpty()){
            username.setError("Please Enter Your User Name");
        }

            User user = new User();
            user.setEmail(email);
            user.setImgUrl(imgUrl);
            user.setUsername(myUsername);
            user.setPhoneNumber(phone);
            user.setFullName(fullName);
            user.setFirebaseUid(firebaseUid);

        FoodzillaService service = FoodzillaClient.getClient();
        ProgressDialog pd = new ProgressDialog(ProfileSetup.this);
        pd.setMessage("Please Wait...");
        pd.show();
        Call<User> userCall = service.createUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ProfileSetup.this,"Profile created", Toast.LENGTH_LONG).show();
                    addUserIdToPreferences(response.body().getId());
                    startActivity(new Intent(ProfileSetup.this, MainActivity.class));
                    pd.hide();
                }
                else {
                    FoodzillaService client = FoodzillaClient.getClient();
                    Call<User> phoneCall = client.getUserByPhoneNumber(phone);
                    phoneCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()){
                                User user = response.body();
                                System.out.println("HERRREEEEEEEEEEEEEEEEEEEEEE" + user.getId());
                                addUserIdToPreferences(user.getId());
                                user.setFirebaseUid(firebaseUid);
                                FoodzillaService myService = FoodzillaClient.getClient();
                                Call<User> myCall = myService.updateUser(user.getId(), user);
                                myCall.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Update Successful", Toast.LENGTH_LONG).show();
                                            pd.hide();
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

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                    Toast.makeText(ProfileSetup.this,"You already have a profile", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProfileSetup.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileSetup.this,"Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                pd.hide();
            }
        });
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }*/


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
            ImagePicker.Companion.with(ProfileSetup.this).start(IMAGE_REQUEST);

        }
        if (v == save){
            setupProfile();
        }

    }
    private void addUserIdToPreferences(String id){
        editor.putString(Constants.PREFERENCES_USER_ID,id).apply();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");

        switch (requestCode) {
            case IMAGE_REQUEST:{
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
        }

    }
}