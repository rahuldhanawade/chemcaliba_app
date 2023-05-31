package com.rahuldhanawade.chemcaliba.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rahuldhanawade.chemcaliba.R;

public class LoginActivity extends AppCompatActivity {

    LinearLayout linear_login;
    TextView tv_sign_up,tv_reset_LA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linear_login = findViewById(R.id.linear_login);
        linear_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        tv_sign_up = findViewById(R.id.tv_sign_up);
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationScreen.class);
                startActivity(i);
            }
        });
        tv_reset_LA = findViewById(R.id.tv_reset_LA);
        tv_reset_LA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(i);
            }
        });
    }
}