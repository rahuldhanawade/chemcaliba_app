package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.CHAP_DOC_URL;
import static com.rahuldhanawade.chemcaliba.RestClient.RestClient.ROOT_URL;
import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.devs.readmoreoption.ReadMoreOption;
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

public class CoursesDetailsActivity extends BaseActivity {

    private static final String TAG = CoursesDetailsActivity.class.getSimpleName();
    private LoadingDialog loadingDialog;

    TextView tv_course_active_cd,tv_course_category_name_cd,tv_course_name_cd,tv_discription_cd,
            tv_course_start_date_cd, tv_course_end_date_cd,tv_duration_cd,tv_valid_date_cd,tv_actual_price_cd,tv_offer_price_cd;
    LinearLayout linear_course_doclink_cd,linear_video_list,linear_course_list,linear_buy_view;
    ImageView iv_course_img;
    String course_id = "", is_bought = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub stub = (ViewStub) findViewById(R.id.base_layout);
        stub.setLayoutResource(R.layout.activity_courses_details);
        View inflated = stub.inflate();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        FetchToolTitle.fetchTitle(CoursesDetailsActivity.this,(fetchToolbarTitle) CoursesDetailsActivity.this,"Course Details");

        loadingDialog = new LoadingDialog(CoursesDetailsActivity.this);

        course_id = getIntent().getStringExtra("course_id");
        is_bought = getIntent().getStringExtra("is_bought");

        Init();
    }

    private void Init() {
        tv_course_active_cd = findViewById(R.id.tv_course_active_cd);
        tv_course_category_name_cd = findViewById(R.id.tv_course_category_name_cd);
        tv_course_name_cd = findViewById(R.id.tv_course_name_cd);
        tv_discription_cd = findViewById(R.id.tv_discription_cd);
        tv_course_start_date_cd = findViewById(R.id.tv_course_start_date_cd);
        tv_course_end_date_cd = findViewById(R.id.tv_course_end_date_cd);
        tv_duration_cd = findViewById(R.id.tv_duration_cd);
        tv_valid_date_cd = findViewById(R.id.tv_valid_date_cd);
        tv_actual_price_cd = findViewById(R.id.tv_actual_price_cd);
        tv_offer_price_cd = findViewById(R.id.tv_offer_price_cd);

        iv_course_img = findViewById(R.id.iv_course_img);

        linear_course_doclink_cd = findViewById(R.id.linear_course_doclink_cd);
        linear_video_list = findViewById(R.id.linear_video_list);
        linear_course_list = findViewById(R.id.linear_course_list);
        linear_buy_view = findViewById(R.id.linear_buy_view);

        if(is_bought.equals("1")){
            linear_course_doclink_cd.setVisibility(View.VISIBLE);
            linear_buy_view.setVisibility(View.GONE);
        }else if(is_bought.equals("0")){
            linear_course_doclink_cd.setVisibility(View.GONE);
            linear_buy_view.setVisibility(View.VISIBLE);
        }

        linear_buy_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoursesDetailsActivity.this, SubscribeProductActivity.class);
                i.putExtra("product_id",course_id);
                startActivity(i);
            }
        });

        GetCourseDetails();
    }

    private void GetCourseDetails() {

        loadingDialog.startLoadingDialog();

        String CourseDetails_URL = ROOT_URL+"view_course_details";

        Log.d("CourseDetails_URL",""+CourseDetails_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CourseDetails_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismissDialog();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            boolean status = responseObj.getBoolean("status");
                            String message = responseObj.getString("message");
                            if(status){
                                JSONObject CourseDetailsObj = responseObj.getJSONObject("course_details");
                                boolean is_course_expired = CourseDetailsObj.getBoolean("is_course_expired");
                                String course_category_name = CourseDetailsObj.getString("course_category_name");
                                String course_category_info = CourseDetailsObj.getString("course_info");
                                String course_name = CourseDetailsObj.getString("course_name");
                                String course_start_date = CourseDetailsObj.getString("course_start_date");
                                String course_end_date = CourseDetailsObj.getString("course_end_date");
                                String course_duration = CourseDetailsObj.getString("course_duration_number_of_days");
                                String course_img_url = CourseDetailsObj.getString("course_img_url");
                                String course_actual_price = CourseDetailsObj.getString("course_actual_price");
                                String course_sell_price = CourseDetailsObj.getString("course_sell_price");

                                if(is_course_expired || is_bought.equals("1")){
                                    if(is_course_expired){
                                        tv_course_active_cd.setVisibility(View.VISIBLE);
                                    }
                                    linear_buy_view.setVisibility(View.GONE);
                                }else{
                                    tv_course_active_cd.setVisibility(View.GONE);
                                    linear_buy_view.setVisibility(View.VISIBLE);
                                }
                                tv_course_category_name_cd.setText(course_category_name);
                                tv_course_name_cd.setText(course_name);

                                ReadMoreOption readMoreOption = new ReadMoreOption.Builder(getApplicationContext())
                                        .textLength(300)
                                        .moreLabel("SHOW MORE")
                                        .lessLabel("SHOW LESS")
                                        .moreLabelColor(Color.RED)
                                        .lessLabelColor(Color.BLUE)
                                        .labelUnderLine(true)
                                        .build();

                                readMoreOption.addReadMoreTo(tv_discription_cd, course_category_info);
//                                tv_discription_cd.setText(course_category_info);
                                tv_course_start_date_cd.setText(course_start_date);
                                tv_course_end_date_cd.setText(course_end_date);
                                tv_duration_cd.setText(course_duration + " Days");
                                tv_valid_date_cd.setText(course_end_date);
                                tv_actual_price_cd.setText("₹"+course_actual_price+" (Inc. Gst)");
                                tv_offer_price_cd.setText("₹"+course_sell_price+" (Inc. Gst)");

                                Glide.with(getApplicationContext())
                                        .applyDefaultRequestOptions(new RequestOptions()
                                                .placeholder(R.drawable.banner_img)
                                                .error(R.drawable.banner_img))
                                        .load(course_img_url)
                                        .into(iv_course_img);

                                JSONArray CourseVideoDetailsArray = responseObj.getJSONArray("course_video_details");
                                setVideoLinks(CourseVideoDetailsArray);

                                JSONArray CourseChapterDetailsArray = responseObj.getJSONArray("course_chapter_details");
                                setCourseDocLinks(CourseChapterDetailsArray);

                            }else{
                                DisplayToastError(CoursesDetailsActivity.this,message);
                                linear_course_doclink_cd.setVisibility(View.GONE);
                                linear_buy_view.setVisibility(View.GONE);
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
                        linear_course_doclink_cd.setVisibility(View.GONE);
                        linear_buy_view.setVisibility(View.GONE);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Server is not connected to internet.");
                        } else if (error instanceof AuthFailureError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Server couldn't find the authenticated request.");
                        } else if (error instanceof ServerError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Server is not responding.Please try Again Later");
                        } else if (error instanceof NetworkError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Your device is not connected to internet.");
                        } else if (error instanceof ParseError) {
                            DisplayToastError(CoursesDetailsActivity.this,"Parse Error (because of invalid json or xml).");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("student_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"student_id"));
                map.put("emailid", UtilitySharedPreferences.getPrefs(getApplicationContext(),"emailid"));
                map.put("course_id", course_id);
                map.put("is_bought", is_bought);
                Log.d("CourseDetailsParamas",""+map.toString());
                return map;
            }
        };

        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setVideoLinks(JSONArray courseVideoDetailsArray) {

        if(courseVideoDetailsArray.length() > 0){
            try {
                LayoutInflater maininflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = maininflater.inflate(R.layout.layout_item_title, null);
                ImageView iv_exp_arrow = rowView.findViewById(R.id.iv_exp_arrow);
                TextView tv_Exp_title = rowView.findViewById(R.id.tv_Exp_title);
                LinearLayout linear_exp_header = rowView.findViewById(R.id.linear_exp_header);
                LinearLayout linear_exp_video_links = rowView.findViewById(R.id.linear_exp_video_links);

                linear_exp_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(linear_exp_video_links.getVisibility() == View.VISIBLE){
                            iv_exp_arrow.setImageResource(R.drawable.ic_down_circle);
                            linear_exp_video_links.setVisibility(View.GONE);
                        }else{
                            iv_exp_arrow.setImageResource(R.drawable.ic_circle_up);
                            linear_exp_video_links.setVisibility(View.VISIBLE);
                        }
                    }
                });

                tv_Exp_title.setText("Video Lecture Links");

                for(int i=0; i< courseVideoDetailsArray.length();i++){
                    JSONObject data_obj = courseVideoDetailsArray.getJSONObject(i);
                    String video_title = data_obj.getString("video_title");
                    String video_link = data_obj.getString("video_link");
                    String created_date = data_obj.getString("created_date");

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View innerrowView = inflater.inflate(R.layout.layout_item_list, null);

                    LinearLayout linear_item_lay = innerrowView.findViewById(R.id.linear_item_lay);
                    TextView tv_title_lay = innerrowView.findViewById(R.id.tv_title_lay);
                    TextView tv_created_date_lay = innerrowView.findViewById(R.id.tv_created_date_lay);

                    tv_title_lay.setText(video_title);
                    tv_created_date_lay.setText(created_date);

                    linear_item_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(URLUtil.isValidUrl(video_link)){
                                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_link));
                                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(video_link));
                                try {
                                    startActivity(appIntent);
                                } catch (ActivityNotFoundException ex) {
                                    startActivity(webIntent);
                                }
                            }else{
                                DisplayToastError(getApplicationContext(),"Sorry no url found please try later");
                            }
                        }
                    });

                    linear_exp_video_links.addView(innerrowView);

                }

                linear_video_list.addView(rowView);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setCourseDocLinks(JSONArray courseChapterDetailsArray) {
        if(courseChapterDetailsArray.length() > 0){
            try {

                for(int i=0; i< courseChapterDetailsArray.length();i++){
                    JSONObject data_obj = courseChapterDetailsArray.getJSONObject(i);
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
                                Log.d(TAG, "onClick: "+doc_url);
                                if(URLUtil.isValidUrl(doc_url)){
                                    Intent i = new Intent(CoursesDetailsActivity.this,PDFViewActivity.class);
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
                                        Log.d(TAG, "onClick: "+sub_doc_url);
                                        if(URLUtil.isValidUrl(sub_doc_url)){
                                            Intent i = new Intent(CoursesDetailsActivity.this,PDFViewActivity.class);
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

                    linear_course_list.addView(rowView);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}