package com.rahuldhanawade.chemcaliba.activity.baseActivity;


import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastInfo;

import android.content.Context;
import android.util.Log;
import android.view.View;

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
import com.rahuldhanawade.chemcaliba.activity.EnrolledActivity;
import com.rahuldhanawade.chemcaliba.adapter.EnrolledAdapter;
import com.rahuldhanawade.chemcaliba.adapter.EnrolledPOJO;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FetchToolTitle {
    public static void fetchTitle(Context context, fetchToolbarTitle fetchToolbarTitle, String title){
        fetchToolbarTitle.updateToobarTitle(title);
        updateEndormentCount(context,fetchToolbarTitle);
    }

    public static void updateEndormentCount(Context context, fetchToolbarTitle fetchToolbarTitle){

        String Enrolled_URL = ROOT_URL+"enrolled_course_list";

        Log.d("Enrolled_URL",""+Enrolled_URL);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Enrolled_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Enrolled_URL",""+response);
                try {
                    JSONObject responseObj=new JSONObject(response);
                    JSONArray courseData=responseObj.getJSONArray("courseData");
                    if(courseData.length() > 0){
                        fetchToolbarTitle.updateEndormentCount(true);
                        UtilitySharedPreferences.setPrefs(context, "is_enrolled", String.valueOf(true));
                    }else {
                        fetchToolbarTitle.updateEndormentCount(false);
                        UtilitySharedPreferences.setPrefs(context, "is_enrolled", String.valueOf(false));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fetchToolbarTitle.updateEndormentCount(false);
                UtilitySharedPreferences.setPrefs(context, "is_enrolled", String.valueOf(false));
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    DisplayToastError(context,"Server is not connected to internet.");
                } else if (error instanceof AuthFailureError) {
                    DisplayToastError(context,"Server couldn't find the authenticated request.");
                } else if (error instanceof ServerError) {
                    DisplayToastError(context,"Server is not responding.Please try Again Later");
                } else if (error instanceof NetworkError) {
                    DisplayToastError(context,"Your device is not connected to internet.");
                } else if (error instanceof ParseError) {
                    DisplayToastError(context,"Parse Error (because of invalid json or xml).");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(context,"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(context,"emailid"));
                map.put("limit", String.valueOf(10));
                map.put("page_num", String.valueOf(1));
                Log.d("enrolledData",""+map.toString());
                return map;
            }

        };
        int socketTimeout = 50000; //50 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    };
}
