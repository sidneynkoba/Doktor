package com.thesidtech.doktor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConsultationActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView painLevel;
    private ImageView capturedCaseImage;
    private ImageButton caseImageBtn;
    private EditText fullName, phoneNo, caseDescription;
    private Button sendCase;
    private Spinner doctorType;
    String doctorNames[] = {"Select type of doctor","General physician","Specialist"};
    ArrayAdapter <String> types;
    private TextView selectedDoctorPrice;

    int maxPain = 10, minPain = 0;
    int currentProgress;

    private ProgressDialog progressDialog;

    String attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;

    String pickphonenumber,pickamount = "";
    private String selectedDoctor = "";
    private String selectedPrice = "";
    private String newPainLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        seekBar = findViewById(R.id.seekBar);
        painLevel = findViewById(R.id.painLevel);
        caseDescription = findViewById(R.id.caseDescription);
        phoneNo = findViewById(R.id.phoneNo);
        fullName = findViewById(R.id.fullName);
        capturedCaseImage = findViewById(R.id.captured_image);
        caseImageBtn = findViewById(R.id.caseImage_btn);
        sendCase = findViewById(R.id.sendCase);
        selectedDoctorPrice = findViewById(R.id.selectedDoctorPrice);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        doctorType = findViewById(R.id.doctor_of_type);
        types = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,doctorNames);
        doctorType.setAdapter(types);
        doctorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        selectedDoctor =  "Select type of doctor";
                        selectedDoctorPrice.setText("FEE");
                        pickamount = "";
                        selectedPrice = "FEE";
                        break;
                    case 1:
                        selectedDoctor =  "General practitioner";
                        selectedDoctorPrice.setText("UGX 20,000");
                        pickamount="20000";
                        selectedPrice = "UGX 20,000";
                        break;
                    case 2:
                        selectedDoctor =  "Specialist";
                        selectedDoctorPrice.setText("UGX 50,000");
                        pickamount="50000";
                        selectedPrice = "UGX 50,000";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBar.setMax(maxPain);
        seekBar.setProgress(minPain);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = progress;
                painLevel.setText(""+currentProgress);
                newPainLevel = ""+currentProgress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(ConsultationActivity.this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ConsultationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(ConsultationActivity.this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                },100);
            }
        }

        caseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();

            }
        });

        sendCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfEmpty();
            }
        });

    }

    private void  openCamera(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 100){
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            capturedCaseImage.setImageBitmap(capturedImage);

            //convert captured image into pdf
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(capturedImage.getWidth(),capturedImage.getHeight(),1).create();

            PdfDocument.Page page = pdfDocument.startPage(pi);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawPaint(paint);

            //scale bitmap
            capturedImage = Bitmap.createScaledBitmap(capturedImage,capturedImage.getWidth(),capturedImage.getHeight(),true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(capturedImage,0,0,null);

            pdfDocument.finishPage(page);

            //saving pdf
            File root = new File(Environment.getExternalStorageDirectory(),"PDF Folder 12");
            if (!root.exists()){
                root.mkdir();
            }
            String pdfName = "consultcase.pdf";
            File pdffile = new File(root,pdfName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(pdffile);
                pdfDocument.writeTo(fileOutputStream);
            }catch (IOException e){
                e.printStackTrace();
            }

            pdfDocument.close();
        }
    }

    public void sendCaseMail(){

        String mail = "sidneyknightmark@gmail.com";
        String message = "FULL NAME: "+fullName.getText().toString().trim()+
                "\nPHONE NUMBER: "+phoneNo.getText().toString().trim()+
                "\nCONSULTATION TYPE: "+selectedDoctor+" @ a fee of "+selectedPrice+
                "\nPAIN LEVEL: "+newPainLevel+" of 10"+
                "\nCASE DESCRIPTION:\n"+ caseDescription.getText().toString().trim();
        String subject = fullName.getText().toString().trim()+"'s Doktor Consultation";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();

    }

    public void checkIfEmpty(){
//        String painLevelData = ""+painLevel;
        String caseDescriptionData = caseDescription.getText().toString().trim();
        String phoneNoData = phoneNo.getText().toString().trim();
        String fullNameData = fullName.getText().toString().trim();
        if (caseDescriptionData.isEmpty() || phoneNoData.isEmpty() || fullNameData.isEmpty()){
            Toast.makeText(this,"MISSING FIELD IS REQUIRED!",Toast.LENGTH_SHORT).show();
        }else {

            sendCaseMail();

//            pickphonenumber=phoneNo.getText().toString().trim();
//            /////add stuff here
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            final AlertDialog paymentDialog=new AlertDialog.Builder(this).create();
//            final View payview= inflater.inflate(R.layout.mobile_money_pay, null, false);
//
//            paymentDialog.setView(payview);
//
//            final Button paybtn=(Button)payview.findViewById(R.id.btnPay);
//
//            paybtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    paymentDialog.dismiss();
//                    //check Network Connection
//                    new collectMoney().execute();
//                    //collect Money
//                    //sendCaseMail();
//                }
//            });
//
//            paymentDialog.show();
//            // paymentDialog.setCancelable(false);
//            //////add stuff here

            caseDescription.setText("");
            phoneNo.setText("");
            fullName.setText("");
            selectedDoctorPrice.setText("FEE");
            painLevel.setText("");
            selectedDoctor =  "Select type of doctor";
            capturedCaseImage.setImageResource(R.drawable.ic_image_black_24dp);
        }

    }


    private class collectMoney extends AsyncTask<String,String,String>{

        private String outputdata="";
        HttpURLConnection con;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Processing....Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String urlParameters = "merchantId=SPTM0002&pin=0002&phone="+pickphonenumber+"&amount="+pickamount;

                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                URL myurl = new URL("https://apis.springpesa.com/transact/mm_pull");
                con = (HttpURLConnection) myurl.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Java client");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                    wr.write(postData);
                }
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {
                    String line="";

                    while ((line = in.readLine()) != null) {
                        outputdata+=line;
                    }
                }
                System.out.println(outputdata);
            }catch (Exception e){e.printStackTrace();}
            return outputdata;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                String check=object.getString("status");

                if(check.equalsIgnoreCase("SUCCESS")){
                    final AlertDialog dsuccess=new AlertDialog.Builder(ConsultationActivity.this).create();
                    dsuccess.setCancelable(false);
                    dsuccess.setMessage(object.getString("information")+"\n You can Tap OK after Entering mobile money PIN.");
                    dsuccess.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dsuccess.dismiss();

                            sendCaseMail();
                        }
                    });

                    dsuccess.show();
                    /*
                    caseDescription.setText("");
                    phoneNo.setText("");
                    fullName.setText("");
                    painLevel.setText("");
                    capturedCaseImage.setImageResource(R.drawable.ic_image_black_24dp);*/

                }else {
                    Toast.makeText(ConsultationActivity.this,object.getString("message"),Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){e.printStackTrace();}

        }
    }


}
