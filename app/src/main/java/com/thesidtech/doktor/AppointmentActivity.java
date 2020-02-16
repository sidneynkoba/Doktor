package com.thesidtech.doktor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AppointmentActivity extends AppCompatActivity {

    WebView appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        appointment = findViewById(R.id.appointment_web);
        appointment.setWebViewClient(new WebViewClient());
        appointment.loadUrl("https://destinyinternationalschool.com/doctor");

        WebSettings webSettings = appointment.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        appointment.reload();

    }

    @Override
    public void onBackPressed() {
        if (appointment.canGoBack()) {
            appointment.goBack();
        } else {
            super.onBackPressed();
        }
    }

}



