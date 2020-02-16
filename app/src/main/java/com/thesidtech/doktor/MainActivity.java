package com.thesidtech.doktor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    CardView appointment_card, consultation_card,health_services_card, find_drug_card;
    ImageButton smsButton, mailButton, callButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        refresh(1000);

        appointment_card = findViewById(R.id.appointment_card);
        appointment_card.setOnClickListener(this);
        consultation_card = findViewById(R.id.consultation_card);
        consultation_card.setOnClickListener(this);
        health_services_card = findViewById(R.id.health_services_card);
        health_services_card.setOnClickListener(this);
        find_drug_card = findViewById(R.id.find_drug_card);
        find_drug_card.setOnClickListener(this);

        callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(this);
        mailButton = findViewById(R.id.mailButton);
        mailButton.setOnClickListener(this);
        smsButton = findViewById(R.id.smsButton);
        smsButton.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView date = findViewById(R.id.date);
        date.setText(currentDate);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appointment_card:
                startActivity(new Intent(MainActivity.this, AppointmentActivity.class));
                break;

            case R.id.consultation_card:
                startActivity(new Intent(MainActivity.this, ConsultationActivity.class));
                break;

            case R.id.health_services_card:
                startActivity(new Intent(MainActivity.this, HealthServicesActivity.class));
                break;

            case R.id.find_drug_card:
                startActivity(new Intent(MainActivity.this, FindDrugActivity.class));
                break;

            case R.id.callButton:
                String phone = "+256777164339";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(phoneIntent);
                break;

            case R.id.mailButton:
                Intent sendMailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:sidneyknightmark@gmail.com"));
                sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello...You Have Mail From App");
                sendMailIntent.putExtra(Intent.EXTRA_TEXT, "Replace this text with your message");
                sendMailIntent.setType("message/rfc822");
                Intent mailer = Intent.createChooser(sendMailIntent, null);
                startActivity(mailer);
                break;

            case R.id.smsButton:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "+256777164339");
                smsIntent.putExtra("sms_body","Body of Message");
                startActivity(smsIntent);
                break;

        }
    }

//    public void onClickDial (View view) {
//        String phone = "+256704367954";
//        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//        startActivity(phoneIntent);
//
//    }
//
//    public void onClickSendMail (View view){
//        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
//        //mailIntent.setData(Uri.parse("mailto:"));
//        mailIntent.setType("plain/text");
//        mailIntent.putExtra(Intent.EXTRA_EMAIL, "info@doktor.com");
//        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "APP USER INQUIRY!");
//        mailIntent.putExtra(Intent.EXTRA_TEXT, "Hello doktor....");
//        try {
//
//            startActivity(Intent.createChooser(mailIntent, "Send Email"));
//
//        }catch (Exception e){
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    public void onClickSMS (View view){
//        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//        smsIntent.setType("vnd.android-dir/mms-sms");
//        smsIntent.putExtra("address", "12125551212");
//        smsIntent.putExtra("sms_body","Body of Message");
//        startActivity(smsIntent);
//
//    }



//    private void checkNetworkConnectionStatus() {
//
//        boolean wifiConnected;
//        boolean mobileConnected;
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
//        if (activeInfo != null && activeInfo.isConnected()){
//            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
//            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
//
//            if (wifiConnected){
////                Toast.makeText(this,"Your are connected",Toast.LENGTH_SHORT).show();
//            }else {
//                if (mobileConnected){
////                    Toast.makeText(this,"Your are connected",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }else{
//            //no iternet connection
//            startActivity(new Intent(MainActivity.this, NetworkActivity.class));
//
//        }
//    }



//    private void refresh(int millisecond){
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                checkNetworkConnectionStatus();
//            }
//        };
//        handler.postDelayed(runnable, millisecond);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        refresh(1000);
//    }

}
