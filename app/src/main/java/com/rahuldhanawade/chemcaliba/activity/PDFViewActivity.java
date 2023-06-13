package com.rahuldhanawade.chemcaliba.activity;

import static com.rahuldhanawade.chemcaliba.utills.CommonMethods.DisplayToastError;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rahuldhanawade.chemcaliba.R;
import com.rahuldhanawade.chemcaliba.utills.LoadingDialog;

public class PDFViewActivity extends AppCompatActivity {

    private LoadingDialog loadingDialog;
    ProgressDialog progressDialog;

    WebView webview;
    String pdfname = "",pdflink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_pdfview);

        loadingDialog = new LoadingDialog(PDFViewActivity.this);

        pdfname = getIntent().getStringExtra("pdf_file_name");
        pdflink = getIntent().getStringExtra("pdf_file_url");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_PDFV);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(pdfname);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void Init() {
        loadingDialog.startLoadingDialog();
        webview = findViewById(R.id.webview_PDFV);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webview, url);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                DisplayToastError(getApplicationContext(),"Sorry no url found please try later");
            }
        });
        webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdflink);
    }
}