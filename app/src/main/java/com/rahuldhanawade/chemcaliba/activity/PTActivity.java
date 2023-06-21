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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.rahuldhanawade.chemcaliba.adapter.PTMeetingAdapter;
import com.rahuldhanawade.chemcaliba.adapter.PTMeetingPOJO;
import com.rahuldhanawade.chemcaliba.utills.MyValidator;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PTActivity extends BaseActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<PTMeetingPOJO> ptMeetingPOJOS = new ArrayList<>();
    PTMeetingAdapter adapter;
    int page = 1, limit = 10;

    EditText edt_search_PT;
    LinearLayout linear_search_PT;
    String str_search = "";
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_ptactivity);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(PTActivity.this,PTActivity.this,"PT Meeting");

        Init();
    }

    private void Init() {
        nestedScrollView=findViewById(R.id.nested_view_PT);
        recyclerView=findViewById(R.id.recy_PT);
        progressBar=findViewById(R.id.progress_bar_PT);

        edt_search_PT=findViewById(R.id.edt_search_PT);
        linear_search_PT=findViewById(R.id.linear_search_PT);

        linear_search_PT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyValidator.isValidField(edt_search_PT)){
                    DisplayToastError(getApplicationContext(),"Enter Something");
                }else{
                    str_search = edt_search_PT.getText().toString();
                    page = 1;
                    ptMeetingPOJOS.clear();
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.VISIBLE);
                    GetPTMeetingList(page,limit,str_search);
                }
            }
        });

        clear = findViewById(R.id.iv_clear_PT);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search_PT.setText("");
                str_search="";
                page = 1;
                ptMeetingPOJOS.clear();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                GetPTMeetingList(page,limit,str_search);
            }
        });

        adapter = new PTMeetingAdapter(PTActivity.this,ptMeetingPOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetPTMeetingList(page,limit, str_search);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetPTMeetingList(page,limit, str_search);
                }
            }
        });
    }

    private void GetPTMeetingList(int page, int limit, String str_search) {

        String PTMeeting_URL = ROOT_URL+"parent_teachers_meetings";

        Log.d("PTMeeting_URL",""+PTMeeting_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, PTMeeting_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PTMeeting_URL",""+response);
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
                            PTMeetingPOJO ptMeetingPOJO1 = new PTMeetingPOJO();
                            ptMeetingPOJO1.setPtMeetingsMasterId(dataObj.getInt("pt_meetings_master_id"));
                            ptMeetingPOJO1.setPtMeetingsTitle(dataObj.getString("pt_meetings_title"));
                            ptMeetingPOJO1.setPtMeetingsDateTime(dataObj.getString("pt_meetings_date_time"));
                            ptMeetingPOJO1.setPtMeetingsDateTime(dataObj.getString("pt_meetings_date_time"));
                            ptMeetingPOJO1.setCreatedDate(dataObj.getString("created_date"));
                            ptMeetingPOJO1.setCourseCategoryName(dataObj.getString("course_category_name"));
                            ptMeetingPOJOS.add(ptMeetingPOJO1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new PTMeetingAdapter(PTActivity.this,ptMeetingPOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(PTActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(PTActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(PTActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(PTActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(PTActivity.this,"Parse Error (because of invalid json or xml).");
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
                map.put("searchTerm", str_search);
                Log.d("PTMeeting_URLData",""+map.toString());
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
        Intent i = new Intent(PTActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}