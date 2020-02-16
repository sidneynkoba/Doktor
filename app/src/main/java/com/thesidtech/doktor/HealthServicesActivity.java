package com.thesidtech.doktor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class HealthServicesActivity extends AppCompatActivity {

    private WebView health_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_services);

        health_services = findViewById(R.id.health_services_web);
        health_services.setWebViewClient(new WebViewClient());
        health_services.loadUrl("https://destinyinternationalschool.com/dokta-search/index.php");

        WebSettings webSettings = health_services.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        health_services.reload();

    }

    @Override
    public void onBackPressed() {
        if (health_services.canGoBack()) {
            health_services.goBack();
        } else {
            super.onBackPressed();
        }
    }

}