package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastSuccess;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.returnValueChangeNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.fetchToolbarTitle;
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.MyValidator;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    private LoadingDialog loadingDialog;
    
    ImageView iv_profile_image;
    TextView tv_full_name_PD,tv_emailid_PD,tv_student_name__PD,tv_student_status_PD,tv_student_create_PD,tv_student_updated_PD;
    EditText edt_full_name_PD,edt_email_id_PD,edt_contact_PD,edt_address_PD,edt_aadhar_PD;
    LinearLayout linear_change_password_PD,linear_active_update,linear_update_details,linear_update,linear_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_profile);
        View inflated = stub.inflate();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        FetchToolTitle.fetchTitle(ProfileActivity.this,(fetchToolbarTitle) ProfileActivity.this,"Profile");

        loadingDialog = new LoadingDialog(ProfileActivity.this);

        Init();
    }

    private void Init() {

        iv_profile_image = findViewById(R.id.iv_profile_image);
        tv_full_name_PD = findViewById(R.id.tv_full_name_PD);
        tv_emailid_PD = findViewById(R.id.tv_emailid_PD);
        tv_student_name__PD = findViewById(R.id.tv_student_name__PD);
        tv_student_status_PD = findViewById(R.id.tv_student_status_PD);
        tv_student_create_PD = findViewById(R.id.tv_student_create_PD);
        tv_student_updated_PD = findViewById(R.id.tv_student_updated_PD);

        edt_full_name_PD = findViewById(R.id.edt_full_name_PD);
        edt_email_id_PD = findViewById(R.id.edt_email_id_PD);
        edt_contact_PD = findViewById(R.id.edt_contact_PD);
        edt_address_PD = findViewById(R.id.edt_address_PD);
        edt_aadhar_PD = findViewById(R.id.edt_aadhar_PD);

        linear_change_password_PD = findViewById(R.id.linear_change_password_PD);
        linear_active_update = findViewById(R.id.linear_active_update);
        linear_update_details = findViewById(R.id.linear_update_details);
        linear_update = findViewById(R.id.linear_update);
        linear_cancel = findViewById(R.id.linear_cancel);

        linear_active_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnableEditText(true);
                linear_active_update.setVisibility(View.GONE);
                linear_update_details.setVisibility(View.VISIBLE);
            }
        });
        linear_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidDetails()){
                    UpadteDetails();
                }
            }
        });
        linear_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnableEditText(false);
                linear_active_update.setVisibility(View.VISIBLE);
                linear_update_details.setVisibility(View.GONE);
                GetProfileData();
            }
        });
        linear_change_password_PD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        GetProfileData();
    }

    private void UpadteDetails() {

        String Str_full_name = edt_full_name_PD.getText().toString();
        String Str_contact = edt_contact_PD.getText().toString();
        String Str_address = edt_address_PD.getText().toString();
        String Str_aadhar = edt_aadhar_PD.getText().toString();

        loadingDialog.startLoadingDialog();

        String UpdateStdDetails_URL = ROOT_URL+"update_contact_details";

        Log.d("UpdateStdDetails_URL",""+UpdateStdDetails_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateStdDetails_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateStdDetails_URL", "onResponse: "+response);
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                DisplayToastSuccess(ProfileActivity.this,message);
                                setEnableEditText(false);
                                GetProfileData();
                                linear_active_update.setVisibility(View.VISIBLE);
                                linear_update_details.setVisibility(View.GONE);
                            }else{
                                DisplayToastError(ProfileActivity.this,message);
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
                            DisplayToastError(ProfileActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(ProfileActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(ProfileActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(ProfileActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(ProfileActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("full_name", Str_full_name);
                map.put("contact", Str_contact);
                map.put("address", Str_address);
                map.put("aadhar_number", Str_aadhar);
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                Log.d("UpdateStdDetails_URL",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void GetProfileData() {

        loadingDialog.startLoadingDialog();

        String StudentDetails_URL = ROOT_URL+"student_profile";

        Log.d("StudentDetails_URL",""+StudentDetails_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, StudentDetails_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                String profile_img_url = responseObj.getString("profile_img_url");
                                JSONObject StudentDetailsObj = responseObj.getJSONObject("student_info");
                                String full_name = StudentDetailsObj.getString("full_name");
                                String emailid = StudentDetailsObj.getString("emailid");
                                int student_status = StudentDetailsObj.getInt("status");
                                String created_date = StudentDetailsObj.getString("created_date");
                                String updated_date = StudentDetailsObj.getString("updated_date");
                                String contact = StudentDetailsObj.getString("contact");
                                String address = StudentDetailsObj.getString("address");
                                String aadhar_number = StudentDetailsObj.getString("aadhar_number");

                                Glide.with(getApplicationContext())
                                        .applyDefaultRequestOptions(new RequestOptions()
                                                .placeholder(R.drawable.ic_man_user)
                                                .error(R.drawable.ic_man_user))
                                        .load(profile_img_url)
                                        .into(iv_profile_image);

                                tv_full_name_PD.setText(full_name);
                                tv_emailid_PD.setText(emailid);

                                tv_student_name__PD.setText(full_name);
                                if(student_status == 2){
                                    tv_student_status_PD.setText("ACTIVE");
                                }else{
                                    tv_student_status_PD.setText("INACTIVE");
                                }
                                tv_student_create_PD.setText(created_date);
                                tv_student_updated_PD.setText(updated_date);

                                edt_full_name_PD.setText(returnValueChangeNull(full_name));
                                edt_email_id_PD.setText(returnValueChangeNull(emailid));
                                edt_contact_PD.setText(returnValueChangeNull(contact));
                                edt_address_PD.setText(returnValueChangeNull(address));
                                edt_aadhar_PD.setText(returnValueChangeNull(aadhar_number));

                                setEnableEditText(false);

                            }else{
                                DisplayToastError(ProfileActivity.this,message);
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
                            DisplayToastError(ProfileActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(ProfileActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(ProfileActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(ProfileActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(ProfileActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                Log.d("StudentDetailsParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setEnableEditText(boolean bool) {

        edt_full_name_PD.setEnabled(bool);
        edt_email_id_PD.setEnabled(false);
        edt_contact_PD.setEnabled(bool);
        edt_address_PD.setEnabled(bool);
        edt_aadhar_PD.setEnabled(bool);

    }

    private boolean isValidDetails() {
        boolean result = true;

        if(edt_email_id_PD.getText().length() > 0){
            if (!MyValidator.isValidEmail(edt_email_id_PD)) {
                edt_email_id_PD.requestFocus();
                DisplayToastError(ProfileActivity.this,"Please Enter Valid Email Id");
                result = false;
            }
        }

        if(edt_contact_PD.getText().length() > 0){
            if (!MyValidator.isValidMobile(edt_contact_PD)) {
                edt_contact_PD.requestFocus();
                DisplayToastError(ProfileActivity.this,"Please Enter Valid Contact No");
                result = false;
            }
        }

        if(edt_address_PD.getText().length() > 0){
            if (!MyValidator.isValidField(edt_address_PD)) {
                edt_address_PD.requestFocus();
                DisplayToastError(ProfileActivity.this,"Please Enter Valid Address");
                result = false;
            }
        }

        if(edt_aadhar_PD.getText().length() > 0){
            if (!MyValidator.isValidAadhaar(edt_aadhar_PD)) {
                edt_aadhar_PD.requestFocus();
                DisplayToastError(ProfileActivity.this,"Please Enter Valid Aadhar");
                result = false;
            }
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
    }
}