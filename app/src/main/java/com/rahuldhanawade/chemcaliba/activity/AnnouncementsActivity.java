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
import com.rahuldhanawade.chemcaliba.adapter.AnnouncementsPOJO;
import com.rahuldhanawade.chemcaliba.adapter.AnnouncementsAdapter;
import com.rahuldhanawade.chemcaliba.utills.MyValidator;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementsActivity extends BaseActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<AnnouncementsPOJO> announcementsPOJOS = new ArrayList<>();
    AnnouncementsAdapter adapter;
    int page = 1, limit = 10;
    
    EditText edt_search_announcements;
    LinearLayout linear_search_announcements;
    String str_search = "";
    ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_announcements);
        View inflated = stub.inflate();

        FetchToolTitle.fetchTitle(AnnouncementsActivity.this,AnnouncementsActivity.this,"Announcements");

        Init();
    }

    private void Init() {
        nestedScrollView=findViewById(R.id.nested_view_announcements);
        recyclerView=findViewById(R.id.recy_announcements);
        progressBar=findViewById(R.id.progress_bar_announcements);
        edt_search_announcements=findViewById(R.id.edt_search_announcements);
        linear_search_announcements=findViewById(R.id.linear_search_announcements);

        linear_search_announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyValidator.isValidField(edt_search_announcements)){
                    DisplayToastError(getApplicationContext(),"Enter Something");
                }else{
                    str_search = edt_search_announcements.getText().toString();
                    page = 1;
                    announcementsPOJOS.clear();
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.VISIBLE);
                    GetAnnouncements(page,limit,str_search);
                }
            }
        });

        clear = findViewById(R.id.iv_clear_announcements);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search_announcements.setText("");
                str_search="";
                page = 1;
                announcementsPOJOS.clear();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                GetAnnouncements(page,limit,str_search);
            }
        });

        adapter = new AnnouncementsAdapter(AnnouncementsActivity.this,announcementsPOJOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        GetAnnouncements(page,limit, str_search);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    GetAnnouncements(page,limit, str_search);
                }
            }
        });
    }

    private void GetAnnouncements(int page, int limit, String str_search) {

        String Announcements_URL = ROOT_URL+"announcements";

        Log.d("Announcements_URL",""+Announcements_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Announcements_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Announcements_URL",""+response);
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
                            AnnouncementsPOJO announcementsPOJO1 = new AnnouncementsPOJO();
                            announcementsPOJO1.setAnnouncementMasterId(dataObj.getInt("announcement_master_id"));
                            announcementsPOJO1.setAnnouncementTitle(dataObj.getString("announcement_title"));
                            announcementsPOJO1.setAnnouncementDescription(dataObj.getString("announcement_description"));
                            announcementsPOJO1.setCreatedDate(dataObj.getString("created_date"));
                            announcementsPOJO1.setCourseCategoryName(dataObj.getString("course_category_name"));
                            announcementsPOJOS.add(announcementsPOJO1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new AnnouncementsAdapter(AnnouncementsActivity.this,announcementsPOJOS);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(AnnouncementsActivity.this,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(AnnouncementsActivity.this,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(AnnouncementsActivity.this,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(AnnouncementsActivity.this,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(AnnouncementsActivity.this,"Parse Error (because of invalid json or xml).");
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
                Log.d("Announcements_URLData",""+map.toString());
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
        Intent i = new Intent(AnnouncementsActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}