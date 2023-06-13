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
import com.rahuldhanawade.chemcaliba.adapter.EnrolledAdapter;
import com.rahuldhanawade.chemcaliba.adapter.EnrolledPOJO;
import com.rahuldhanawade.chemcaliba.adapter.HolidayAdapter;
import com.rahuldhanawade.chemcaliba.adapter.HolidayPOJO;
import com.rahuldhanawade.chemcaliba.adapter.PTMeetingAdapter;
import com.rahuldhanawade.chemcaliba.adapter.PTMeetingPOJO;
import com.rahuldhanawade.chemcaliba.adapter.TestResultAdapter;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HolidayActivity extends BaseActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<HolidayPOJO> holidayPOJOS = new ArrayList<>();
    HolidayAdapter adapter;
    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_holiday);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(HolidayActivity.this,"Test Results");

        Init();
    }

    private void Init() {
        nestedScrollView=findViewById(R.id.nested_view_HD);
        recyclerView=findViewById(R.id.recy_HD);
        progressBar=findViewById(R.id.progress_bar_HD);

        adapter = new HolidayAdapter(HolidayActivity.this,holidayPOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetHolidayList(page,limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetHolidayList(page,limit);
                }
            }
        });
    }

    private void GetHolidayList(int page, int limit) {

        String Holiday_URL = ROOT_URL+"holiday_info";

        Log.d("Holiday_URL",""+Holiday_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Holiday_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Holiday_URL",""+response);
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
                            HolidayPOJO holidayPOJO1 = new HolidayPOJO();
                            holidayPOJO1.setHolidayInformationMasterId(dataObj.getInt("holiday_information_master_id"));
                            holidayPOJO1.setHolidayInformationTitle(dataObj.getString("holiday_information_title"));
                            holidayPOJO1.setHolidayInformationFromDate(dataObj.getString("holiday_information_from_date"));
                            holidayPOJO1.setHolidayInformationToDate(dataObj.getString("holiday_information_to_date"));
                            holidayPOJO1.setCreatedDate(dataObj.getString("created_date"));
                            holidayPOJO1.setCourseCategoryName(dataObj.getString("course_category_name"));
                            holidayPOJOS.add(holidayPOJO1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new HolidayAdapter(HolidayActivity.this,holidayPOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(HolidayActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(HolidayActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(HolidayActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(HolidayActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(HolidayActivity.this,"Parse Error (because of invalid json or xml).");
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
                Log.d("Holiday_URLData",""+map.toString());
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
        Intent i = new Intent(HolidayActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}