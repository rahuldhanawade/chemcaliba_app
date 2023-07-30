package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
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
import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.MyValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationScreen extends AppCompatActivity {

    private LoadingDialog loadingDialog;

    EditText edt_full_name,edt_email_id,edt_contact_no;
    TextView tv_tc;
    CheckBox chk_tc;
    LinearLayout linear_register;

    boolean is_checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        loadingDialog = new LoadingDialog(RegistrationScreen.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_score_form);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Register");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Init();
    }

    private void Init() {

        edt_full_name = findViewById(R.id.edt_full_name);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_contact_no = findViewById(R.id.edt_contact_no);
        tv_tc = findViewById(R.id.tv_tc);
        tv_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://chemcaliba.com/privacy-policy/"));
                startActivity(i);
            }
        });


        chk_tc = findViewById(R.id.chk_tc);
        chk_tc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                is_checked = b;
            }
        });

        linear_register = findViewById(R.id.linear_register);
        linear_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    if(is_checked){
                        UserRegister();
                    }else {
                        DisplayToastError(RegistrationScreen.this,"Please Accept Terms and Policy");
                    }
                }
            }
        });
    }

    private void UserRegister() {

        String Str_full_name = edt_full_name.getText().toString();
        String Str_email = edt_email_id.getText().toString();
        String Str_contact_no = edt_contact_no.getText().toString();

        loadingDialog.startLoadingDialog();

        String REGISTER_URL = ROOT_URL+"register";

        Log.d("REGISTER_URL",""+REGISTER_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                DisplayToastSuccess(RegistrationScreen.this,message);

                                Intent i = new Intent(RegistrationScreen.this, LoginActivity.class);
                                startActivity(i);
                            }else{
                                DisplayToastError(RegistrationScreen.this,message);
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
                            DisplayToastError(RegistrationScreen.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(RegistrationScreen.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(RegistrationScreen.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(RegistrationScreen.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(RegistrationScreen.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("full_name", Str_full_name);
                map.put("emailid", Str_email);
                map.put("contact", Str_contact_no);
                Log.d("RegisterParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean isValid() {
        boolean result = true;

        if (!MyValidator.isValidField(edt_full_name)) {
            edt_full_name.requestFocus();
            DisplayToastError(RegistrationScreen.this,"Please Enter Valid Full Name");
            result = false;
        }

        if (!MyValidator.isValidEmail(edt_email_id)) {
            edt_email_id.requestFocus();
            DisplayToastError(RegistrationScreen.this,"Please Enter Valid Email Id");
            result = false;
        }

        if (!MyValidator.isValidMobile(edt_contact_no)) {
            edt_contact_no.requestFocus();
            DisplayToastError(RegistrationScreen.this,"Please Enter Valid Contact No");
            result = false;
        }

        return result;
    }
}