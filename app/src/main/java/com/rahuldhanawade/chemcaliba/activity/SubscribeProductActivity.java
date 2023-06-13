package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayPopUpInfo;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubscribeProductActivity extends AppCompatActivity {
    
    private LoadingDialog loadingDialog;

    ImageView iv_course_img_SP;
    TextView tv_product_name_SP,tv_unit_price_SP,tv_course_start_date_SP,tv_course_end_date_SP,tv_duration_SP,
            tv_valid_date_SP,tv_total_SP,tv_remove_SP;
    LinearLayout linear_buy_now;
    String Str_product_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_product);

        loadingDialog = new LoadingDialog(SubscribeProductActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_subscribe_SP);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Subscribe Course");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Str_product_id = getIntent().getStringExtra("product_id");
        
        Init();
    }

    private void Init() {

        iv_course_img_SP = findViewById(R.id.iv_course_img_SP);

        tv_product_name_SP = findViewById(R.id.tv_product_name_SP);
        tv_unit_price_SP = findViewById(R.id.tv_unit_price_SP);
        tv_course_start_date_SP = findViewById(R.id.tv_course_start_date_SP);
        tv_course_end_date_SP = findViewById(R.id.tv_course_end_date_SP);
        tv_duration_SP = findViewById(R.id.tv_duration_SP);
        tv_valid_date_SP = findViewById(R.id.tv_valid_date_SP);
        tv_total_SP = findViewById(R.id.tv_total_SP);
        tv_remove_SP = findViewById(R.id.tv_remove_SP);

        linear_buy_now = findViewById(R.id.linear_buy_now);

        linear_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayPopUpInfo(SubscribeProductActivity.this, "Please contact support for subscription of this Course");
            }
        });

        GetProductDetails();
    }

    private void GetProductDetails() {

        loadingDialog.startLoadingDialog();

        String BuyProduct_URL = ROOT_URL+"buy_product";

        Log.d("BuyProduct_URL",""+BuyProduct_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuyProduct_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                JSONObject CartDetailsObj = responseObj.getJSONObject("cart_details");
                                String course_image = CartDetailsObj.getString("course_image");
                                String product_name = CartDetailsObj.getString("product_name");
                                String price_after_discount = CartDetailsObj.getString("price_after_discount");
                                String course_start_date = CartDetailsObj.getString("course_start_date");
                                String course_end_date = CartDetailsObj.getString("course_end_date");
                                String course_duration_number_of_days = CartDetailsObj.getString("course_duration_number_of_days");

                                Glide.with(getApplicationContext())
                                        .applyDefaultRequestOptions(new RequestOptions()
                                                .placeholder(R.drawable.logo_chemcaliba)
                                                .error(R.drawable.logo_chemcaliba))
                                        .load(course_image)
                                        .into(iv_course_img_SP);

                                tv_product_name_SP.setText(product_name);
                                tv_unit_price_SP.setText(price_after_discount);
                                tv_course_start_date_SP.setText(course_start_date);
                                tv_course_end_date_SP.setText(course_end_date);
                                tv_duration_SP.setText(course_duration_number_of_days);
                                tv_valid_date_SP.setText(course_end_date);
                                tv_total_SP.setText(price_after_discount);

                            }else{
                                DisplayToastError(SubscribeProductActivity.this,message);
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
                            DisplayToastError(SubscribeProductActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(SubscribeProductActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(SubscribeProductActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(SubscribeProductActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(SubscribeProductActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                map.put("product_id", Str_product_id);
                Log.d("BuyProduct_URLParamas",""+map.toString());
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