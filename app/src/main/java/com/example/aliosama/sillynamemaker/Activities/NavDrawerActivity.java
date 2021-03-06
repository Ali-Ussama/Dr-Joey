package com.example.aliosama.sillynamemaker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.aliosama.sillynamemaker.Activities.SignUp_In.SignUpSignInNavActivity;
import com.example.aliosama.sillynamemaker.Fragments.ChatFragment;
import com.example.aliosama.sillynamemaker.Fragments.History.HistoryFragment;
import com.example.aliosama.sillynamemaker.Fragments.PersonalityTypes.PersonalityTypesFragment;
import com.example.aliosama.sillynamemaker.R;
import com.example.aliosama.sillynamemaker.databinding.ActivityNavDrawerBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int ItemSelected = 0;

    ActivityNavDrawerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        binding.navView.setCheckedItem(R.id.nav_chat);
        setMenuCounter(R.id.nav_chat, 4);
        loadFragment();
    }

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) binding.navView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    private Fragment getFragment() {
        Fragment fragment = null;
        switch (ItemSelected) {
            case 0:
                fragment = new ChatFragment();
                getSupportActionBar().setTitle(getString(R.string.home));
                break;
            case 1:
                fragment = new HistoryFragment();
                getSupportActionBar().setTitle(getString(R.string.historu));
                break;
            case 2:
                fragment = new PersonalityTypesFragment();
                getSupportActionBar().setTitle(getString(R.string.personality_types));
                break;
        }
        return fragment;
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.nav_content, getFragment())
                .commit();
    }

    private void getActivity() {

    }

    private void loadActivity() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            ItemSelected = 0;
            loadFragment();
            super.onBackPressed();
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.nav_drawer, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setCheckable(true);
        if (id == R.id.nav_chat) {
            item.setChecked(true);
            if (ItemSelected != 0) {
                ItemSelected = 0;
                loadFragment();
            }

        } else if (id == R.id.nav_history) {
            item.setChecked(true);
            if (ItemSelected != 1) {
                ItemSelected = 1;
                loadFragment();
            }

        } else if (id == R.id.nav_personalityTypes) {
            item.setChecked(true);
            if (ItemSelected != 2) {
                ItemSelected = 2;
                loadFragment();
            }
        } else if (id == R.id.nav_share) {
            item.setChecked(true);

        } else if (id == R.id.nav_contactUs) {

        } else if (id == R.id.nav_SignOut) {
            if (ItemSelected != 6) {
                ItemSelected = 6;
                item.setChecked(true);
                FirebasesingOut();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void FirebasesingOut() {
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {

                mAuth.signOut();
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();


                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isComplete() && task.isSuccessful()) {
                                    Log.i("Nav Sign Out", "Google Sign out complete Successfully");
                                }
                            }
                        });
                startActivity(new Intent(NavDrawerActivity.this, SignUpSignInNavActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
