package com.example.aldrian.musicin2firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aldrian.musicin2firebase.SessionManager.SessionManager;
import com.example.aldrian.musicin2firebase.musician.MusicianActivity;
import com.example.aldrian.musicin2firebase.owner.OwnerActivity;
import com.example.aldrian.musicin2firebase.users.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {
    protected int splashTime = 2000;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessionManager = new SessionManager(getApplicationContext());
        Thread splashThread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(splashTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(sessionManager.checkLogin()) {
                        if (sessionManager.getCurrentUser().getRole().equals("musician")) {
                            sessionManager.checkLogin(MusicianActivity.class);
                        } else if (sessionManager.getCurrentUser().getRole().equals("owner")) {
                            sessionManager.checkLogin(OwnerActivity.class);
                        }
                    }
                    else{
                        startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));

                    }
                    finish();

                }
            }
        };
        splashThread.start();
    }
}
