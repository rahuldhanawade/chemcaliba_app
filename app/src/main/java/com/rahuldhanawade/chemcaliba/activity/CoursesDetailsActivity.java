package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastSuccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CoursesDetailsActivity extends BaseActivity {

    private LoadingDialog loadingDialog;

    TextView tv_course_active_cd,tv_course_category_name_cd,tv_course_category_info_cd,tv_course_name_cd,tv_course_start_date_cd,
            tv_course_end_date_cd,tv_duration_cd,tv_valid_date_cd;
    String course_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_courses_details);
        View inflated = stub.inflate();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        FetchToolTitle.fetchTitle((fetchToolbarTitle) CoursesDetailsActivity.this,"Course Details");

        loadingDialog = new LoadingDialog(CoursesDetailsActivity.this);

        course_id = getIntent().getStringExtra("course_id");

        Init();
    }

    private void Init() {
        tv_course_active_cd = findViewById(R.id.tv_course_active_cd);
        tv_course_category_name_cd = findViewById(R.id.tv_course_category_name_cd);
        tv_course_category_info_cd = findViewById(R.id.tv_course_category_info_cd);
        tv_course_name_cd = findViewById(R.id.tv_course_name_cd);
        tv_course_start_date_cd = findViewById(R.id.tv_course_start_date_cd);
        tv_course_end_date_cd = findViewById(R.id.tv_course_end_date_cd);
        tv_duration_cd = findViewById(R.id.tv_duration_cd);
        tv_valid_date_cd = findViewById(R.id.tv_valid_date_cd);
        
        GetCourseDetails();
    }

    private void GetCourseDetails() {
        loadingDialog.startLoadingDialog();

        String CourseDetails_URL = ROOT_URL+"view_course_details";

        Log.d("CourseDetails_URL",""+CourseDetails_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CourseDetails_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                JSONObject CourseDetailsObj = responseObj.getJSONObject("course_details");
                                boolean is_course_expired = CourseDetailsObj.getBoolean("is_course_expired");
                                String course_category_name = CourseDetailsObj.getString("course_category_name");
                                String course_category_info = CourseDetailsObj.getString("course_category_info");
                                String course_name = CourseDetailsObj.getString("course_name");
                                String course_start_date = CourseDetailsObj.getString("course_start_date");
                                String course_end_date = CourseDetailsObj.getString("course_end_date");
                                String course_duration = CourseDetailsObj.getString("course_duration_number_of_days");

                                if(is_course_expired){
                                    tv_course_active_cd.setVisibility(View.VISIBLE);
                                }else{
                                    tv_course_active_cd.setVisibility(View.GONE);
                                }
                                tv_course_category_name_cd.setText(course_category_name);
                                tv_course_category_info_cd.setText(course_category_info);
                                tv_course_name_cd.setText(course_name);
                                tv_course_start_date_cd.setText(course_start_date);
                                tv_course_end_date_cd.setText(course_end_date);
                                tv_duration_cd.setText(course_duration);
                                tv_valid_date_cd.setText(course_end_date);

                            }else{
                                DisplayToastError(CoursesDetailsActivity.this,message);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismissDialog();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                map.put("course_id", course_id);
                Log.d("CourseDetailsParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}