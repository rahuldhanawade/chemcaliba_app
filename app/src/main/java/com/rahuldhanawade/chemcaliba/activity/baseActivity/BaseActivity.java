package com.rahuldhanawade.chemcaliba.activity.baseActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.MainActivity;
import com.rahuldhanawade.chemcaliba.activity.SplashScreen;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, fetchToolbarTitle{

    Toolbar toolbar;
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        navigationView();
    }

    private void navigationView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //TextView nav_header_userId = navigationView.findViewById(R.id.nav_header_userId);
        View hView =  navigationView.getHeaderView(0);

        TextView nav_header_userName = (TextView)hView.findViewById(R.id.nav_header_userName);
        TextView nav_user_email = (TextView)hView.findViewById(R.id.nav_Email);

        nav_header_userName.setText("Rahul Dhanawade");
        nav_user_email.setText("rd@test.com");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_home){
        } else if(id == R.id.nav_our_courses){
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_enrolled_courses) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_test_results){
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_test_schedule){
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_announccments){
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_pt_meeting){
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_holidays){
            Intent i=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if(id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        UtilitySharedPreferences.clearPref(getApplicationContext());
        Intent i = new Intent(getApplicationContext(), SplashScreen.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }

    @Override
    public void updateToobarTitle(String title) {
        Log.d(TAG, "updateToobarTitle: "+title);
        toolbar.setTitle(title);
    }
}