package com.example.aliosama.sillynamemaker.Activities.SignUp_In;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.aliosama.sillynamemaker.Activities.NavDrawerActivity;
import com.example.aliosama.sillynamemaker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpSignInNavActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_sing_in_nav);

        try {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null){
                // Check if user's email is verified
                boolean emailVerified = user.isEmailVerified();
                if (emailVerified)
                    updateUI(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SignUpSingInOnClick(View view){
        int Id = view.getId();
        switch (Id){
            case R.id.activity_sign_up_sign_in_sinupbtn:
                Intent intent = new Intent(SignUpSignInNavActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_sign_up_sign_in_loginbtn:
                Intent i = new Intent(SignUpSignInNavActivity.this,SignInActivity.class);
                startActivity(i);
                break;
        }
    }
    private void updateUI(FirebaseUser user) {

        try {
            if (user != null){
                Log.i(TAG+"updateUI", "DisplayName :" + user.getDisplayName());
                Log.i(TAG+"updateUI", "Email:" + user.getEmail());
                Log.i(TAG+"updateUI", "PhoneNumber:" + user.getPhoneNumber());
                Log.i(TAG+"updateUI", "Uid :" + user.getUid());
                Log.i(TAG+"updateUI", "ProviderId:" + user.getProviderId());
                startActivity(new Intent(SignUpSignInNavActivity.this, NavDrawerActivity.class));

            }else{
                Log.i(TAG+"updateUI", "user is Null :");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
