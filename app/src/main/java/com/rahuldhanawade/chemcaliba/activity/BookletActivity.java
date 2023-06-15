package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.CHAP_DOC_URL;
import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.webkit.URLUtil;
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
import com.rahuldhanawade.chemcaliba.activity.baseActivity.BaseActivity;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.FetchToolTitle;
import com.rahuldhanawade.chemcaliba.activity.baseActivity.fetchToolbarTitle;
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;
import com.rahuldhanawade.chemcaliba.utills.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookletActivity extends BaseActivity {

    private LoadingDialog loadingDialog;

    TextView tv_course_name_BT;
    LinearLayout linear_course_list_BT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_booklet);
        View inflated = stub.inflate();
        FetchToolTitle.fetchTitle((fetchToolbarTitle) BookletActivity.this,"Booklets/Assesments");

        loadingDialog = new LoadingDialog(BookletActivity.this);
        
        Init();
    }

    private void Init() {

        tv_course_name_BT = findViewById(R.id.tv_course_name_BT);
        linear_course_list_BT = findViewById(R.id.linear_course_list_BT);

        GetBookLets();
    }

    private void GetBookLets() {
        
        loadingDialog.startLoadingDialog();

        String Booklets_URL = ROOT_URL+"booklets";

        Log.d("Booklets_URL",""+Booklets_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Booklets_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                JSONArray validEnrollmentListArray = responseObj.getJSONArray("validEnrollmentList");
                                for(int i = 0; i < validEnrollmentListArray.length(); i++){
                                    JSONObject dataObj = validEnrollmentListArray.getJSONObject(i);
                                    String course_name = dataObj.getString("course_name");
                                    JSONArray CourseChapterArray = dataObj.getJSONArray("course_chapter");

                                    tv_course_name_BT.setText(course_name);

                                    setCourseDocLinks(CourseChapterArray);
                                }
                            }else{
                                DisplayToastError(BookletActivity.this,message);
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
                            DisplayToastError(BookletActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(BookletActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(BookletActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(BookletActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(BookletActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                Log.d("Booklets_URLParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setCourseDocLinks(JSONArray courseChapterArray) {
        if(courseChapterArray.length() > 0){
            try {

                for(int i=0; i< courseChapterArray.length();i++){
                    JSONObject data_obj = courseChapterArray.getJSONObject(i);
                    String chapter_master_id = data_obj.getString("chapter_master_id");
                    String chapter_name = data_obj.getString("chapter_name");
                    JSONArray ChapterDocArray = data_obj.getJSONArray("chapter_doc");
                    JSONArray SubChapterArray = data_obj.getJSONArray("sub_chapters");

                    LayoutInflater maininflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View rowView = maininflater.inflate(R.layout.layout_item_title, null);

                    ImageView iv_exp_arrow = rowView.findViewById(R.id.iv_exp_arrow);
                    TextView tv_Exp_title = rowView.findViewById(R.id.tv_Exp_title);
                    LinearLayout linear_exp_header = rowView.findViewById(R.id.linear_exp_header);
                    LinearLayout linear_exp_video_links = rowView.findViewById(R.id.linear_exp_video_links);
                    LinearLayout linear_exp_subchap_links = rowView.findViewById(R.id.linear_exp_subchap_links);
                    linear_exp_header.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(linear_exp_video_links.getVisibility() == View.VISIBLE){
                                iv_exp_arrow.setImageResource(R.drawable.ic_down_circle);
                                linear_exp_video_links.setVisibility(View.GONE);
                                linear_exp_subchap_links.setVisibility(View.GONE);
                            }else{
                                iv_exp_arrow.setImageResource(R.drawable.ic_circle_up);
                                linear_exp_video_links.setVisibility(View.VISIBLE);
                                linear_exp_subchap_links.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    tv_Exp_title.setText(chapter_name);

                    for(int j=0; j< ChapterDocArray.length();j++){
                        JSONObject Chapobj = ChapterDocArray.getJSONObject(j);
                        String chapter_document_master_id = Chapobj.getString("chapter_document_master_id");
                        String document_title = Chapobj.getString("document_title");
                        String document_file = Chapobj.getString("document_file");
                        String doc_created_date = Chapobj.getString("doc_created_date");

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View innerrowView = inflater.inflate(R.layout.layout_item_list, null);

                        ImageView iv_img_layout = innerrowView.findViewById(R.id.iv_img_layout);
                        LinearLayout linear_item_lay = innerrowView.findViewById(R.id.linear_item_lay);
                        TextView tv_title_lay = innerrowView.findViewById(R.id.tv_title_lay);
                        TextView tv_created_date_lay = innerrowView.findViewById(R.id.tv_created_date_lay);

                        iv_img_layout.setImageResource(R.drawable.ic_file);

                        tv_title_lay.setText(document_title);
                        tv_created_date_lay.setText(doc_created_date);

                        linear_item_lay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String doc_url = CHAP_DOC_URL+"chapter/"+chapter_master_id+"/chapter-documents/"+chapter_document_master_id+"/"+document_file;
                                if(URLUtil.isValidUrl(doc_url)){
                                    Intent i = new Intent(BookletActivity.this,PDFViewActivity.class);
                                    i.putExtra("pdf_file_name",document_title);
                                    i.putExtra("pdf_file_url",doc_url);
                                    startActivity(i);
                                }else{
                                    DisplayToastError(getApplicationContext(),"Sorry no url found please try later");
                                }
                            }
                        });

                        linear_exp_video_links.addView(innerrowView);

                    }

                    if(SubChapterArray.length() > 0){
                        for(int h=0; h< SubChapterArray.length();h++){
                            JSONObject subdata_obj = SubChapterArray.getJSONObject(h);
                            String sub_chapter_master_id = subdata_obj.getString("sub_chapter_master_id");
                            String sub_chapter_name = subdata_obj.getString("sub_chapter_name");
                            JSONArray SubChapterDocArray = subdata_obj.getJSONArray("sub_chapter_doc");

                            LayoutInflater subinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View subrowView = subinflater.inflate(R.layout.layout_item_title, null);

                            ImageView iv_exp_arrow_sub = subrowView.findViewById(R.id.iv_exp_arrow);
                            TextView tv_Exp_title_sub = subrowView.findViewById(R.id.tv_Exp_title);
                            LinearLayout linear_exp_header_sub = subrowView.findViewById(R.id.linear_exp_header);
                            LinearLayout linear_exp_video_links_sub = subrowView.findViewById(R.id.linear_exp_video_links);
                            linear_exp_header_sub.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                            tv_Exp_title_sub.setText(sub_chapter_name);
                            linear_exp_header_sub.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(linear_exp_video_links_sub.getVisibility() == View.VISIBLE){
                                        iv_exp_arrow_sub.setImageResource(R.drawable.ic_down_circle);
                                        linear_exp_video_links_sub.setVisibility(View.GONE);
                                    }else{
                                        iv_exp_arrow_sub.setImageResource(R.drawable.ic_circle_up);
                                        linear_exp_video_links_sub.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            for(int j=0; j< SubChapterDocArray.length();j++){
                                JSONObject Chapobj = SubChapterDocArray.getJSONObject(j);
                                String sub_chapter_document_master_id = Chapobj.getString("sub_chapter_document_master_id");
                                String document_title = Chapobj.getString("document_title");
                                String document_file = Chapobj.getString("document_file");
                                String doc_created_date = Chapobj.getString("sub_doc_created_date");

                                LayoutInflater subInnerinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View subinnerrowView = subInnerinflater.inflate(R.layout.layout_item_list, null);

                                ImageView iv_img_layout = subinnerrowView.findViewById(R.id.iv_img_layout);
                                LinearLayout linear_item_lay = subinnerrowView.findViewById(R.id.linear_item_lay);
                                TextView tv_title_lay = subinnerrowView.findViewById(R.id.tv_title_lay);
                                TextView tv_created_date_lay = subinnerrowView.findViewById(R.id.tv_created_date_lay);

                                iv_img_layout.setImageResource(R.drawable.ic_file);

                                tv_title_lay.setText(document_title);
                                tv_created_date_lay.setText(doc_created_date);

                                linear_item_lay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String sub_doc_url = CHAP_DOC_URL+"sub-chapter/"+sub_chapter_master_id+"/sub-chapter-documents/"+sub_chapter_document_master_id+"/"+document_file;
                                        if(URLUtil.isValidUrl(sub_doc_url)){
                                            Intent i = new Intent(BookletActivity.this,PDFViewActivity.class);
                                            i.putExtra("pdf_file_name",document_title);
                                            i.putExtra("pdf_file_url",sub_doc_url);
                                            startActivity(i);
                                        }else{
                                            DisplayToastError(getApplicationContext(),"Sorry no url found please try later");
                                        }
                                    }
                                });

                                linear_exp_video_links_sub.addView(subinnerrowView);

                            }

                            linear_exp_subchap_links.addView(subrowView);
                        }
                    }

                    linear_course_list_BT.addView(rowView);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}