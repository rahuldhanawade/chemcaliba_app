package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastSuccess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.MyValidator;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    EditText edt_old_pass_CP,edt_new_pass_CP,edt_confirm_new_pass_CP;
    LinearLayout linear_update_CP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        loadingDialog = new LoadingDialog(ChangePasswordActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_change_password);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Change password");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Init();
    }

    private void Init() {

        edt_old_pass_CP = findViewById(R.id.edt_old_pass_CP);
        edt_new_pass_CP = findViewById(R.id.edt_new_pass_CP);
        edt_confirm_new_pass_CP = findViewById(R.id.edt_confirm_new_pass_CP);
        linear_update_CP = findViewById(R.id.linear_update_CP);

        linear_update_CP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidNewPassword()){
                    ChangePassword();
                }
            }
        });

    }

    private void ChangePassword() {

        String Str_old_pass = edt_old_pass_CP.getText().toString();
        String Str_new_pass = edt_new_pass_CP.getText().toString();
        String Str_conf_new_pass = edt_confirm_new_pass_CP.getText().toString();

        loadingDialog.startLoadingDialog();

        String ChangePassword_URL = ROOT_URL+"change_password";

        Log.d("ChangePassword_URL",""+ChangePassword_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ChangePassword_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                DisplayToastSuccess(ChangePasswordActivity.this,message);
                                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }else{
                                DisplayToastError(ChangePasswordActivity.this,message);
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
                            DisplayToastError(ChangePasswordActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(ChangePasswordActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(ChangePasswordActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(ChangePasswordActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(ChangePasswordActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("current_password", Str_old_pass);
                map.put("new_password", Str_new_pass);
                map.put("confirm_password", Str_conf_new_pass);
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                Log.d("ChangePass_URLParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean isValidNewPassword() {
        boolean result = true;


        if (!MyValidator.isValidField(edt_old_pass_CP)) {
            edt_old_pass_CP.requestFocus();
            DisplayToastError(ChangePasswordActivity.this,"Please Enter Valid Old Password");
            result = false;
        }

        if (!MyValidator.isValidPassword(edt_new_pass_CP)) {
            edt_new_pass_CP.requestFocus();
            DisplayToastError(ChangePasswordActivity.this,"Please Enter Valid New Password");
            result = false;
        }

        if (!MyValidator.isValidPassword(edt_confirm_new_pass_CP)) {
            edt_confirm_new_pass_CP.requestFocus();
            DisplayToastError(ChangePasswordActivity.this,"Please Enter Valid Confirm Password");
            result = false;
        }

        if (!edt_new_pass_CP.getText().toString().equals(edt_confirm_new_pass_CP.getText().toString())) {
            DisplayToastError(ChangePasswordActivity.this,"New Password and Confirm Password doesn't match");
            result = false;
        }

        return result;
    }
}