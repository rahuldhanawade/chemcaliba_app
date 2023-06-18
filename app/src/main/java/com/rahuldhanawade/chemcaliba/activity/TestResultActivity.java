package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;
import com.rahuldhanawade.chemcaliba.adapter.TestResultAdapter;
import com.rahuldhanawade.chemcaliba.adapter.EnrolledPOJO;
import com.rahuldhanawade.chemcaliba.adapter.TestResultAdapter;
import com.rahuldhanawade.chemcaliba.adapter.TestResultPOJO;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestResultActivity extends BaseActivity {


    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<TestResultPOJO> testResultPOJOS = new ArrayList<>();
    TestResultAdapter adapter;
    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_test_result);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(TestResultActivity.this,"Test Results");

        Init();
    }

    private void Init() {
        nestedScrollView=findViewById(R.id.nested_view_test_results);
        recyclerView=findViewById(R.id.recy_test_results);
        progressBar=findViewById(R.id.progress_bar_test_results);

        adapter = new TestResultAdapter(TestResultActivity.this,testResultPOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetTestResultList(page,limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetTestResultList(page,limit);
                }
            }
        });
    }

    private void GetTestResultList(int page, int limit) {

        String TestResult_URL = ROOT_URL+"test_results";

        Log.d("TestResult_URL",""+TestResult_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, TestResult_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TestResult_URL",""+response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject responseObj=new JSONObject(response);
                    String courseData=responseObj.getString("data");
                    if(courseData==null || courseData.equals("[]") || courseData.equalsIgnoreCase("")){
                        DisplayToastInfo(getApplicationContext(),"No Data Found");
                    }else{
                        JSONArray data_array=new JSONArray(courseData);
                        for(int k=0; k< data_array.length();k++){
                            JSONObject dataObj=data_array.getJSONObject(k);
                            TestResultPOJO testResultPOJO1 = new TestResultPOJO();
                            testResultPOJO1.setTestRecordsMasterId(dataObj.getInt("test_records_master_id"));
                            testResultPOJO1.setTestDate(dataObj.getString("test_date"));
                            testResultPOJO1.setMarksObtained(dataObj.getString("marks_obtained"));
                            testResultPOJO1.setTotalMarks(dataObj.getString("total_marks"));
                            testResultPOJO1.setNoOfRightQuestions(dataObj.getString("no_of_right_questions"));
                            testResultPOJO1.setNoOfWrongQuestions(dataObj.getString("no_of_wrong_questions"));
                            testResultPOJO1.setCreatedDate(dataObj.getString("created_date"));
                            testResultPOJO1.setIsAttempted(dataObj.getString("is_attempted"));
                            testResultPOJOS.add(testResultPOJO1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new TestResultAdapter(TestResultActivity.this,testResultPOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(TestResultActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(TestResultActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(TestResultActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(TestResultActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(TestResultActivity.this,"Parse Error (because of invalid json or xml).");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                map.put("limit", String.valueOf(limit));
                map.put("page_num", String.valueOf(page));
                Log.d("TestResultData",""+map.toString());
                return map;
            }

        };
        int socketTimeout = 50000; //50 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(TestResultActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}