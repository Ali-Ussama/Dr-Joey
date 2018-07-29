package com.example.aliosama.sillynamemaker.Activities.SignUp_In;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliosama.sillynamemaker.Activities.NavDrawerActivity;
import com.example.aliosama.sillynamemaker.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static java.lang.Thread.sleep;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    GoogleSignInClient mGoogleSignInClient;
    Button GooglesignInButton;
    Button JoeySingInButton;
    TextView SignUpTextView;
    int RC_SIGN_IN = 1;
    String TAG = "TAG";
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton FacebookloginButton;
    boolean isLoggedIn;
    GoogleSignInAccount account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
            FacebookSdk.sdkInitialize(this);

            setContentView(R.layout.activity_sign_in);

            Init();

            GooglesignInButton.setOnClickListener(this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init(){
        try {
            SignUpTextView = findViewById(R.id.activity_sign_in_SignUpTextView);
            Typeface RobotMedium = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Medium.ttf");
            JoeySingInButton = findViewById(R.id.sign_in_joey);
            JoeySingInButton.setTypeface(RobotMedium);
            FacebookloginButton = findViewById(R.id.facebook_login_button);
            // Set the dimensions of the sign-in button.
            GooglesignInButton = findViewById(R.id.sign_in_button);
            GooglesignInButton.setTypeface(RobotMedium);

            SignUpTextView.setOnClickListener(this);
            // Facebook Sign In
            callbackManager = CallbackManager.Factory.create();
            FacebookloginButton.setReadPermissions("email", "public_profile");
            // If you are using in a fragment, call FacebookloginButton.setFragment(this);

            // Callback registration
            FacebookloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    AccessToken accessToken = loginResult.getAccessToken();
                    isLoggedIn = accessToken != null && !accessToken.isExpired();

                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d(TAG, "facebook:onError", exception);
                    exception.printStackTrace();
                }
            });

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

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.sign_in_button:
                    GoogleSignIn();
                    break;
                case R.id.activity_sign_in_SignUpTextView:
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                    finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void GoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void JoeySignIn(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                Log.i(TAG+"updateUI", "DisplayName :" + user.getDisplayName());
                Log.i(TAG+"updateUI", "Email:" + user.getEmail());
                Log.i(TAG+"updateUI", "PhoneNumber:" + user.getPhoneNumber());
                Log.i(TAG+"updateUI", "Uid :" + user.getUid());
                Log.i(TAG+"updateUI", "ProviderId:" + user.getProviderId());
                startActivity(new Intent(SignInActivity.this, NavDrawerActivity.class));
//                startActivity(new Intent(SignInActivity.this, FirebaseChatbotActivity.class));

            }else{
                Log.i(TAG+"updateUI", "user is Null :");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        try{
            Log.d(TAG, "handleFacebookAccessToken:" + token);

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
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
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("SingInApiException", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);
    }
}
