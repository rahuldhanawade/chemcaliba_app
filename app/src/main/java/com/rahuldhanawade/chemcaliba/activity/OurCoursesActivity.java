package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastInfo;

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
import com.rahuldhanawade.chemcaliba.adapter.OurCourcesAdapter;
import com.rahuldhanawade.chemcaliba.adapter.OurCourcesPOJO;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OurCoursesActivity extends BaseActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<OurCourcesPOJO> ourCourcesPOJOS = new ArrayList<>();
    OurCourcesAdapter adapter;
    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_our_courses);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle((fetchToolbarTitle) OurCoursesActivity.this,"Our Courses");

        Init();
    }

    private void Init() {
        nestedScrollView=findViewById(R.id.nested_view_our_course);
        recyclerView=findViewById(R.id.recy_our_course);
        progressBar=findViewById(R.id.progress_bar_our_course);

        adapter = new OurCourcesAdapter(OurCoursesActivity.this,ourCourcesPOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetOurCoursesList(page,limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetOurCoursesList(page,limit);
                }
            }
        });
    }

    private void GetOurCoursesList(int page, int limit) {
        String Courses_URL = ROOT_URL+"courses_list";

        Log.d("Courses_URL",""+Courses_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Courses_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Courses_URL",""+response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject responseObj=new JSONObject(response);
                    String courseData=responseObj.getString("courseData");
                    if(courseData==null || courseData.equals("[]") || courseData.equalsIgnoreCase("")){
                        DisplayToastInfo(getApplicationContext(),"No Data Found");
                    }else{
                        JSONArray data_array=new JSONArray(courseData);
                        for(int k=0; k< data_array.length();k++){
                            JSONObject dataObj=data_array.getJSONObject(k);
                            OurCourcesPOJO ourCourcesPOJOS1 = new OurCourcesPOJO();
                            ourCourcesPOJOS1.setCourseMasterId(dataObj.getString("course_master_id"));
                            ourCourcesPOJOS1.setCourseImage(dataObj.getString("course_image"));
                            ourCourcesPOJOS1.setCourseName(dataObj.getString("course_name"));
                            ourCourcesPOJOS1.setCourseCategoryName(dataObj.getString("course_category_name"));
                            ourCourcesPOJOS1.setCourseCategoryInfo(dataObj.getString("course_category_info"));
                            ourCourcesPOJOS1.setCourseDurationNumberOfDays(dataObj.getString("course_duration_number_of_days"));
                            ourCourcesPOJOS1.setCourseStartDate(dataObj.optString("course_start_date"));
                            ourCourcesPOJOS1.setCourseEndDate(dataObj.optString("course_end_date"));
                            ourCourcesPOJOS1.setCourseActualPrice(dataObj.getString("course_actual_price"));
                            ourCourcesPOJOS1.setCourseSellPrice(dataObj.getString("course_sell_price"));
                            ourCourcesPOJOS1.setIsAllowPurchaseAfterExpire(dataObj.getString("is_allow_purchase_after_expire"));
                            ourCourcesPOJOS1.setCourseStatus(dataObj.getString("course_status"));
                            ourCourcesPOJOS1.setIsAlreadyBought(dataObj.getBoolean("is_already_bought"));
                            ourCourcesPOJOS1.setIsCourseExpired(dataObj.getBoolean("is_course_expired"));
                            ourCourcesPOJOS.add(ourCourcesPOJOS1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new OurCourcesAdapter(OurCoursesActivity.this,ourCourcesPOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(OurCoursesActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(OurCoursesActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(OurCoursesActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(OurCoursesActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(OurCoursesActivity.this,"Parse Error (because of invalid json or xml).");
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
                Log.d("ourCourseData",""+map.toString());
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
        Intent i = new Intent(OurCoursesActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}