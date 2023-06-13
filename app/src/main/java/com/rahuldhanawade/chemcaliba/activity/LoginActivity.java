package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastSuccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;
    LinearLayout linear_login;
    TextView tv_sign_up,tv_reset_LA;
    EditText EdtEmail_Mobile,EdtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(LoginActivity.this);

        Init();
    }

    public void Init(){

        EdtEmail_Mobile = findViewById(R.id.EdtEmail_Mobile);
        EdtPassword = findViewById(R.id.EdtPassword);

//        EdtEmail_Mobile.setText("jeetendratyagi40@gmail.com");
//        EdtPassword.setText("M4)m1bxyr");

        linear_login = findViewById(R.id.linear_login);
        tv_sign_up = findViewById(R.id.tv_sign_up);
        tv_reset_LA = findViewById(R.id.tv_reset_LA);

        linear_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    UserLogin();
                }
            }
        });
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationScreen.class);
                startActivity(i);
            }
        });
        tv_reset_LA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ResetActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean isValid() {
        boolean result = true;

        if (!MyValidator.isValidEmail(EdtEmail_Mobile)) {
            EdtEmail_Mobile.requestFocus();
            DisplayToastError(LoginActivity.this,"Please Enter Valid Email Id");
            result = false;
        }

        if (!MyValidator.isValidField(EdtPassword)) {
            EdtPassword.requestFocus();
            DisplayToastError(LoginActivity.this,"Please Enter Valid Password");
            result = false;
        }

        return result;
    }
    public void UserLogin(){

        String Str_email = EdtEmail_Mobile.getText().toString();
        String Str_password = EdtPassword.getText().toString();


        loadingDialog.startLoadingDialog();

        String LOGIN_URL = ROOT_URL+"login";

        Log.d("LOGIN_URL",""+LOGIN_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                String student_id = responseObj.getString("student_id");
                                String full_name = responseObj.getString("full_name");
                                String emailid = responseObj.getString("emailid");

                                UtilitySharedPreferences.setPrefs(getApplicationContext(), "status", String.valueOf(status));
                                UtilitySharedPreferences.setPrefs(getApplicationContext(), "student_id", student_id);
                                UtilitySharedPreferences.setPrefs(getApplicationContext(), "full_name", full_name);
                                UtilitySharedPreferences.setPrefs(getApplicationContext(), "emailid", emailid);

                                DisplayToastSuccess(LoginActivity.this,message);

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }else{
                                DisplayToastError(LoginActivity.this,message);
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
                            DisplayToastError(LoginActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(LoginActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(LoginActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(LoginActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(LoginActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("emailid", Str_email);
                map.put("password", Str_password);
                Log.d("LoginParamas",""+map.toString());
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