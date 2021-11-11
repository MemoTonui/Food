package com.linda.food.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.linda.food.R;

import butterknife.BindView;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView image,logo;
    LottieAnimationView lottie;
    private static final int NUM_PAGES =3;
    ViewPager viewPager;
    private SlidePageAdapter pagerAdapter;
    Animation animation;
    private static int SPLASH_SCREEN_TIME =5500;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);
        image = findViewById(R.id.image);
        logo = findViewById(R.id.logo_transparent);
        lottie = findViewById(R.id.lottie);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        viewPager.startAnimation(animation);

        image.animate().translationY(-1000).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        lottie.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences =getSharedPreferences("SharedPreferences",MODE_PRIVATE);
                boolean isFirstTime =sharedPreferences.getBoolean("FirstTime",true);

                if(isFirstTime){
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putBoolean("FirstTime",false);
                    editor.commit();
                }else {
                    Intent intent = new Intent(IntroductoryActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN_TIME);
    }
    private class SlidePageAdapter extends FragmentStatePagerAdapter {

        public SlidePageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2:
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}