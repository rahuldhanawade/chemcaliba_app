package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayPopUpInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

public class MainActivity extends BaseActivity {

    LinearLayout linear_live_lect,linear_enrolled_courses,linear_test_results,linear_test_schedule,linear_booklets;
    String is_enrolled = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_main);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(MainActivity.this,MainActivity.this,"Dashboard");

        is_enrolled = UtilitySharedPreferences.getPrefs(MainActivity.this,"is_enrolled");

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

        linear_test_schedule = findViewById(R.id.linear_test_schedule);
        linear_test_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_enrolled.equals("true")){
                    Intent i = new Intent(MainActivity.this, TestScheduleActivity.class);
                    startActivity(i);
                }else{
                    DisplayPopUpInfo(MainActivity.this, "You have not purchased any course or your course is expired");
                }
            }
        });

        linear_booklets = findViewById(R.id.linear_booklets);
        linear_booklets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, BookletActivity.class);
                startActivity(i);
            }
        });
    }
}
