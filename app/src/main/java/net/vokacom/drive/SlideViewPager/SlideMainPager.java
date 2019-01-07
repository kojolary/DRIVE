package net.vokacom.drive.SlideViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

import net.vokacom.drive.ItemClasses.MenActivity;
import net.vokacom.drive.ItemClasses.NavigationActivity;
import net.vokacom.drive.MainActivity;
import net.vokacom.drive.R;

import java.util.Timer;
import java.util.TimerTask;

public class SlideMainPager extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerItem adapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    private  String[] images = {
            "https://app.tedxafariwaa.com/LoginandRegistration/images/4.jpg",
            "https://app.tedxafariwaa.com/LoginandRegistration/images/men/2.jpg",
            "https://app.tedxafariwaa.com/LoginandRegistration/images/22.jpg"
    };
    private Timer timer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //prevent screenshot or screen capture
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //end of it
        setContentView(R.layout.slide_pager_main);

        mAuth= FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(SlideMainPager.this,NavigationActivity.class));
                }

            }
        };


        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerItem(SlideMainPager.this,images);
        viewPager.setAdapter(adapter);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%images.length);
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,3000,3000);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (i==viewPager.getAdapter().getCount()-1){
                    Intent myIntent = new Intent(SlideMainPager.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

}
