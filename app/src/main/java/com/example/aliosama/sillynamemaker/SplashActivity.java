package com.example.aliosama.sillynamemaker;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aliosama.sillynamemaker.Activities.SignUp_In.SignUpSignInNavActivity;

public class SplashActivity extends AppCompatActivity {

    TextView dr;
    TextView joey;
    final int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_splash);

            dr = findViewById(R.id.dr);
            joey = findViewById(R.id.joey);
            Typeface NexafontLight = Typeface.createFromAsset(this.getAssets(), "fonts/Nexa-Light.otf");
            Typeface NexafontBold = Typeface.createFromAsset(this.getAssets(), "fonts/Nexa-Bold.otf");
            dr.setTypeface(NexafontLight);
            joey.setTypeface(NexafontBold);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, SignUpSignInNavActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
