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
import com.rahuldhanawade.chemcaliba.activity.baseActivity.fetchToolbarTitle;
import com.rahuldhanawade.chemcaliba.adapter.EnrolledAdapter;
import com.rahuldhanawade.chemcaliba.adapter.EnrolledPOJO;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnrolledActivity extends BaseActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<EnrolledPOJO> enrolledPOJOS = new ArrayList<>();
    EnrolledAdapter adapter;
    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_enrolled);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(EnrolledActivity.this,(fetchToolbarTitle) EnrolledActivity.this,"Enrolled Courses");

        Init();
    }

    private void Init() {

        nestedScrollView=findViewById(R.id.nested_view_enrolled_course);
        recyclerView=findViewById(R.id.recy_enrolled_course);
        progressBar=findViewById(R.id.progress_bar_enrolled_course);

        adapter = new EnrolledAdapter(EnrolledActivity.this,enrolledPOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetEnrolledList(page,limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetEnrolledList(page,limit);
                }
            }
        });
    }

    private void GetEnrolledList(int page, int limit) {

        String Enrolled_URL = ROOT_URL+"enrolled_course_list";

        Log.d("Enrolled_URL",""+Enrolled_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Enrolled_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Enrolled_URL",""+response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject responseObj=new JSONObject(response);
                    String courseData=responseObj.getString("courseData");
                    if(courseData==null || courseData.equals("[]") || courseData.equalsIgnoreCase("")){
                        DisplayToastInfo(getApplicationContext(),"You have not purchased any course or your course is expired");
                    }else{
                        JSONArray data_array=new JSONArray(courseData);
                        for(int k=0; k< data_array.length();k++){
                            JSONObject dataObj=data_array.getJSONObject(k);
                            EnrolledPOJO enrolledPOJOS1 = new EnrolledPOJO();
                            enrolledPOJOS1.setEnrollment_master_id(dataObj.getString("enrollment_master_id"));
                            enrolledPOJOS1.setStudent_id(dataObj.getString("student_id"));
                            enrolledPOJOS1.setCourse_master_id(dataObj.getString("course_master_id"));
                            enrolledPOJOS1.setNo_of_days(dataObj.getString("no_of_days"));
                            enrolledPOJOS1.setValid_upto(dataObj.getString("valid_upto"));
                            enrolledPOJOS1.setEnrollment_date(dataObj.getString("enrollment_date"));
                            enrolledPOJOS1.setCourse_name(dataObj.getString("course_name"));
                            enrolledPOJOS1.setCourse_image(dataObj.getString("course_image"));
                            enrolledPOJOS1.setCourse_category_name(dataObj.getString("course_category_name"));
                            enrolledPOJOS1.setCourse_category_info(dataObj.getString("course_category_info"));
                            enrolledPOJOS1.setCourse_validity(dataObj.getString("course_validity"));
                            enrolledPOJOS1.setCourse_enrollment_date(dataObj.getString("course_enrollment_date"));
                            enrolledPOJOS.add(enrolledPOJOS1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new EnrolledAdapter(EnrolledActivity.this,enrolledPOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(EnrolledActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(EnrolledActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(EnrolledActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(EnrolledActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(EnrolledActivity.this,"Parse Error (because of invalid json or xml).");
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
                Log.d("enrolledData",""+map.toString());
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
        Intent i = new Intent(EnrolledActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}