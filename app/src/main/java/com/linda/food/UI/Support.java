package com.linda.food.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.linda.food.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Support extends AppCompatActivity {
    @BindView(R.id.content) EditText content;
    @BindView(R.id.submit) Button submit;
    @BindView(R.id.subject) EditText subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myEmail = "tonuidesigns@gmail.com";
                String messageToSend = content.getText().toString();
                //String myEmail = email.getText().toString();
                String mySubject = subject.getText().toString();
                String [] recipients = myEmail.split(",");

                if (messageToSend.equals("")){
                    content.setError("Please add a message");
                    return;
                } if (mySubject.equals("")){
                    subject.setError("Please Add a subject");
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,mySubject);
                intent.putExtra(Intent.EXTRA_TEXT, messageToSend);

                intent.setType("message/rfc822");

                if(intent.resolveActivity(getPackageManager())!= null){
                    startActivity(Intent.createChooser(intent,"Choose An Email Client"));
                    Toast.makeText(Support.this, "Email Sent!", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Support.this, "Your device does not support this action", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}