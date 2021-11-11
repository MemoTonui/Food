package com.linda.food.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.linda.food.R;
import com.linda.food.adapters.LoginAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    float v =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Use this to bind all views
        tabLayout=findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        //Add Tabs to the tab layout
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Registration"));

        //Set the tab layout's gravity
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Create object of adapter class
        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        //Add on page change listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //animate

        tabLayout.setTranslationY(300);


        tabLayout.setAlpha(v);

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
    }
}