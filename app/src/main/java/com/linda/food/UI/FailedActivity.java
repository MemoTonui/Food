package com.linda.food.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.linda.food.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FailedActivity extends AppCompatActivity {
    @BindView(R.id.home) Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        ButterKnife.bind(this);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FailedActivity.this,MainActivity.class));
            }
        });
    }
}