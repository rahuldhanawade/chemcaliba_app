package com.rahuldhanawade.chemcaliba.activity;

import static android.content.ContentValues.TAG;
import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayPopUpInfo;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastSuccess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.RestClient.RestClient;
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubscribeProductActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    
    private LoadingDialog loadingDialog;

    ImageView iv_course_img_SP;
    TextView tv_product_name_SP,tv_unit_price_SP,tv_course_start_date_SP,tv_course_end_date_SP,tv_duration_SP,
            tv_valid_date_SP,tv_discount_SP,tv_total_SP,tv_remove_SP,tv_refund_policy;
    LinearLayout linear_coupon_SP,linear_buy_now;
    EditText edt_coupon_SP;
    String Str_product_id = "", Str_course_end_date = "", Str_actual_price = "", Str_amount = "", Str_merchant_order_id = "",
            Str_discount_value = "",Str_discount_type = "",  Str_coupon_master_id = "", Str_coupon_discount_percent = "", Str_description = "";

    private JSONObject options;
    CheckBox chk_refund_policy;
    boolean is_refund = false;

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
        tv_discount_SP = findViewById(R.id.tv_discount_SP);
        tv_total_SP = findViewById(R.id.tv_total_SP);
        tv_remove_SP = findViewById(R.id.tv_remove_SP);
        tv_refund_policy = findViewById(R.id.tv_refund_policy);
        chk_refund_policy = findViewById(R.id.chk_refund_policy);

        tv_refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://chemcaliba.com/refund-policy/"));
                startActivity(i);
            }
        });

        chk_refund_policy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_refund = b;
            }
        });

        tv_remove_SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetProductDetails();
                tv_remove_SP.setVisibility(View.GONE);
            }
        });

        edt_coupon_SP = findViewById(R.id.edt_coupon_SP);

        linear_coupon_SP = findViewById(R.id.linear_coupon_SP);

        linear_coupon_SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_coupon_SP.isEnabled()){
                    ApplyCoupon();
                }
            }
        });

        linear_buy_now = findViewById(R.id.linear_buy_now);

        linear_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_refund){
                    getPaymentDetails();
                }else{
                    DisplayPopUpInfo(SubscribeProductActivity.this,"Please Accept Terms & Conditions");
                }
            }
        });

        GetProductDetails();
    }

    private void startPayment() {

        Log.d(TAG, "startPayment: "+Str_amount);
        long amount = Math.round(Float.parseFloat(Str_amount) * 100);

        Checkout checkout = new Checkout();
        checkout.setKeyID(RestClient.RAZORPAY_KEY);
        int image = R.mipmap.ic_launcher_foreground; // Can be any drawable
        checkout.setImage(image);
        checkout.setFullScreenDisable(true);
        try {
            options = new JSONObject();
            options.put("description", "");
            options.put("name", "Chemcaliba Student Portal");
            options.put("currency", "INR");
            options.put("prefill.contact", UtilitySharedPreferences.getPrefs(getApplicationContext(),"contact"));
            options.put("prefill.email", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
            options.put("amount", amount);
            checkout.open(SubscribeProductActivity.this, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
            e.printStackTrace();
        }
    }

    private void ApplyCoupon() {

        String Str_coup_code = edt_coupon_SP.getText().toString();

        loadingDialog.startLoadingDialog();

        String ApplyCoupon_URL = ROOT_URL+"apply_coupon";

        Log.d("ApplyCoupon_URL",""+ApplyCoupon_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApplyCoupon_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                JSONObject CouponResultObj = responseObj.getJSONObject("coupon_result");
                                JSONObject CartDetailsObj = CouponResultObj.getJSONObject("cart_details");
                                Str_discount_value = CartDetailsObj.getString("discount_value");
                                String price_after_discount = CartDetailsObj.getString("price_after_discount");
                                Str_discount_type = CartDetailsObj.optString("discount_type");
                                Str_coupon_master_id = CartDetailsObj.getString("discount_coupon_master_id");
                                Str_coupon_discount_percent = CartDetailsObj.optString("coupon_discount_percent");

                                tv_discount_SP.setText("₹"+Str_discount_value);
                                tv_total_SP.setText("₹"+price_after_discount);
                                Str_amount = price_after_discount;

                                edt_coupon_SP.setEnabled(false);
                                tv_remove_SP.setVisibility(View.VISIBLE);

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
                map.put("coupon_code", Str_coup_code);
                Log.d("ApplyCoupon_URLParam",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                                String product_img_url = CartDetailsObj.getString("product_img_url");
                                String product_name = CartDetailsObj.getString("product_name");
                                String price_after_discount = CartDetailsObj.getString("price_after_discount");
                                String course_start_date = CartDetailsObj.getString("course_start_date");
                                Str_course_end_date = CartDetailsObj.getString("course_end_date");
                                String course_duration_number_of_days = CartDetailsObj.getString("course_duration_number_of_days");

                                Glide.with(getApplicationContext())
                                        .applyDefaultRequestOptions(new RequestOptions()
                                                .placeholder(R.drawable.banner_img)
                                                .error(R.drawable.banner_img))
                                        .load(product_img_url)
                                        .into(iv_course_img_SP);

                                Str_actual_price = price_after_discount;

                                tv_product_name_SP.setText(product_name);
                                tv_unit_price_SP.setText(price_after_discount);
                                tv_course_start_date_SP.setText(course_start_date);
                                tv_course_end_date_SP.setText(Str_course_end_date);
                                tv_duration_SP.setText(course_duration_number_of_days + " Days");
                                tv_valid_date_SP.setText(Str_course_end_date);
                                tv_total_SP.setText("₹"+price_after_discount);
                                Str_amount = price_after_discount;
                                tv_discount_SP.setText("");
                                edt_coupon_SP.setEnabled(true);
                                edt_coupon_SP.setText("");

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

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d(TAG, "onPaymentSuccess: "+s);
        Log.d(TAG, "onPaymentSuccess: "+paymentData.getPaymentId());
        Log.d(TAG, "onPaymentSuccess: "+paymentData.getData());
        updatePaymentDetails(paymentData);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d(TAG, "onPaymentError: "+s);
        Log.d(TAG, "onPaymentError: "+paymentData.getPaymentId());
        Log.d(TAG, "onPaymentError: "+paymentData.getData());
        DisplayPopUpInfo(SubscribeProductActivity.this, "Payment Failed! Please contact support for more details");
    }

    private void updatePaymentDetails(PaymentData paymentData) {

        loadingDialog.startLoadingDialog();

        String Payment_URL = ROOT_URL+"app_payment_response";

        Log.d("Payment_URL",""+Payment_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Payment_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                DisplayToastSuccess(SubscribeProductActivity.this,message);
                                Intent i = new Intent(SubscribeProductActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
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
                map.put("razor_payment_id", paymentData.getPaymentId());
                map.put("merchant_order_id", Str_merchant_order_id);
                map.put("merchant_amount", Str_amount);
                map.put("payment_status", "captured");
                map.put("payment_method", "App");
                map.put("payment_response", paymentData.getData().toString());
                map.put("discount_coupon_master_id", Str_coupon_master_id);
                map.put("course_actual_price", Str_actual_price);
                map.put("discount_type", Str_discount_type);
                map.put("disount_percent", Str_coupon_discount_percent);
                map.put("discount_value", Str_discount_value);
                map.put("course_end_date", Str_course_end_date);
                Log.d("Payment_URLParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getPaymentDetails() {

        loadingDialog.startLoadingDialog();

        String PaymentParams_URL = ROOT_URL+"payment_params";

        Log.d("PaymentParams_URL",""+PaymentParams_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PaymentParams_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                Str_merchant_order_id = responseObj.getString("merchant_order_id");
                                Str_description = responseObj.getString("description");
                                startPayment();
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
                map.put("amount", Str_amount);
                Log.d("PaymentParams_URL",""+map.toString());
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