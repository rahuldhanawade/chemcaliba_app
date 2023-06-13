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
import com.rahuldhanawade.chemcaliba.adapter.TestResultPOJO;
import com.rahuldhanawade.chemcaliba.adapter.TestScheduleAdapter;
import com.rahuldhanawade.chemcaliba.adapter.TestSchedulePOJO;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestScheduleActivity extends BaseActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<TestSchedulePOJO> testSchedulePOJOS = new ArrayList<>();
    TestScheduleAdapter adapter;
    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_test_schedule);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(TestScheduleActivity.this,"Test Schedule");

        Init();
    }

    private void Init() {
        nestedScrollView=findViewById(R.id.nested_view_test_schedule);
        recyclerView=findViewById(R.id.recy_test_schedule);
        progressBar=findViewById(R.id.progress_bar_test_schedule);

        adapter = new TestScheduleAdapter(TestScheduleActivity.this,testSchedulePOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetTestScheduleList(page,limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetTestScheduleList(page,limit);
                }
            }
        });
        
    }

    private void GetTestScheduleList(int page, int limit) {
        String TestSchedule_URL = ROOT_URL+"test_schedules";

        Log.d("TestSchedule_URL",""+TestSchedule_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, TestSchedule_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TestSchedule_URL",""+response);
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
                            TestSchedulePOJO testSchedulePOJO1 = new TestSchedulePOJO();
                            testSchedulePOJO1.setTestScheduleMasterId(dataObj.getInt("test_schedule_master_id"));
                            testSchedulePOJO1.setTestScheduleTitle(dataObj.getString("test_schedule_title"));
                            testSchedulePOJO1.setCreatedDate(dataObj.getString("created_date"));
                            testSchedulePOJO1.setCourseCategoryName(dataObj.getString("course_category_name"));
                            testSchedulePOJO1.setTestScheduleDateTime(dataObj.getString("test_schedule_date_time"));
                            testSchedulePOJO1.setTestScheduleLink(dataObj.getString("test_schedule_link"));
                            testSchedulePOJOS.add(testSchedulePOJO1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new TestScheduleAdapter(TestScheduleActivity.this,testSchedulePOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(TestScheduleActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(TestScheduleActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(TestScheduleActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(TestScheduleActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(TestScheduleActivity.this,"Parse Error (because of invalid json or xml).");
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
                map.put("searchTerm", "");
                Log.d("TestSchedule_URLData",""+map.toString());
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
        Intent i = new Intent(TestScheduleActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}