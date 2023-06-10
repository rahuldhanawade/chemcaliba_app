package com.rahuldhanawade.chemcaliba.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;

public class MainActivity extends BaseActivity {

    LinearLayout linear_live_lect,linear_enrolled_courses,linear_test_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_main);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(MainActivity.this,"Dashboard");

        init();
    }

    public void init(){
        linear_live_lect = findViewById(R.id.linear_live_lect);
        linear_live_lect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LiveLectureActivity.class);
                startActivity(i);
            }
        });

        linear_enrolled_courses = findViewById(R.id.linear_enrolled_courses);
        linear_enrolled_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EnrolledActivity.class);
                startActivity(i);
            }
        });

        linear_test_results = findViewById(R.id.linear_test_results);
        linear_test_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TestResultActivity.class);
                startActivity(i);
            }
        });
    }
}
