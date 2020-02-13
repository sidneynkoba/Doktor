package com.thesidtech.doktor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class FindDrugActivity extends AppCompatActivity {

    private WebView find_drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_drug);

        find_drug = findViewById(R.id.find_drug_web);
        find_drug.setWebViewClient(new WebViewClient());
        find_drug.loadUrl("https://destinyinternationalschool.com/doc/index.php");

        WebSettings webSettings = find_drug.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        find_drug.reload();

    }

    @Override
    public void onBackPressed() {
        if (find_drug.canGoBack()) {
            find_drug.goBack();
        } else {
            super.onBackPressed();
        }
    }

}


