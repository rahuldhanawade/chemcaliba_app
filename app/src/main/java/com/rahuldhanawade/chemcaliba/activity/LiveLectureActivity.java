package com.rahuldhanawade.chemcaliba.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.fetchToolbarTitle;

public class LiveLectureActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_live_lecture);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle((fetchToolbarTitle) LiveLectureActivity.this,"Live Lecture");
    }
}