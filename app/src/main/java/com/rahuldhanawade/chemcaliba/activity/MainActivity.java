package com.rahuldhanawade.chemcaliba.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_main);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(MainActivity.this,"Dashboard");
    }
}