package com.example.aliosama.sillynamemaker.Activities.SignUp_In;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aliosama.sillynamemaker.Activities.Personality.PersonalityTestActivity;
import com.example.aliosama.sillynamemaker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    GoogleSignInAccount account;
    int RC_SIGN_IN = 1;
    String TAG = "TAG";


    TextView SignInTextView;
    Button SignUpGoogleBtn;
    Button JoeySignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        try {
           Init();
           SignUpGoogleBtn.setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init(){
        Typeface RobotMedium = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
        SignUpGoogleBtn = findViewById(R.id.sign_up_google_button);
        SignUpGoogleBtn.setTypeface(RobotMedium);
        SignInTextView = findViewById(R.id.activity_sign_up_SignInTextView);
        JoeySignUp = findViewById(R.id.sign_up_joey);

        // Google Sign In
        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);
    }

    public void onSignUpClick(View view) {
        int Id = view.getId();
        try {
            switch (Id) {
                case R.id.activity_sign_up_SignInTextView:
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                    break;
                case R.id.sign_up_joey:
                    startActivity(new Intent(SignUpActivity.this, PersonalityTestActivity.class));
                    finish();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.i(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        try {
            Log.i(TAG, "FirebaseAuthWithGoogle:" );

            if (acct != null) {
                Log.i(TAG, "FirebaseAuthWithGoogle:" + acct.getDisplayName());
                Log.i(TAG, "FirebaseAuthWithGoogle:" + acct.getEmail());
                Log.i(TAG, "FirebaseAuthWithGoogle:" + acct.getGivenName());
                Log.i(TAG, "FirebaseAuthWithGoogle:" + acct.getFamilyName());
                Log.i(TAG, "FirebaseAuthWithGoogle:" + acct.getId());


                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.i(TAG, "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Email sent.");
                                                        }
                                                    }
                                                });
                                    }
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.i(TAG, "signInWithCredential:failure", task.getException());
                                    Snackbar.make(findViewById(R.id.SingInActivityLayout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                            }
                        });

            } else {
                Log.i(TAG, "firebaseAuthWithGoogle:" + "account is null");

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateUI(FirebaseUser user) {

        try {
            if (user != null){
                Log.i(TAG+"updateUI", "Email:" + user.getEmail());
                startActivity(new Intent(SignUpActivity.this, PersonalityTestActivity.class));
            }else{
                Log.i(TAG+"updateUI", "user is Null :");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
