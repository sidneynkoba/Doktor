package com.thesidtech.doktor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class ConsultationActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView painLevel;
    private ImageView capturedCaseImage;
    private ImageButton caseImageBtn;
    private EditText fullName, phoneNo, caseDescription;
    private Button sendCase;
    String newPainLevel;
    private Spinner doctorType;
    String doctorNames[] = {"Select type of doctor","General physician","Specialist"};
    ArrayAdapter <String> types;
    private TextView selectedDoctorPrice;
    private String selectedDoctor;

    int maxPain = 10, minPain = 0;
    int currentProgress;

    String attachmentFile;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    int columnIndex;


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

        doctorType = findViewById(R.id.doctor_of_type);
        types = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,doctorNames);
        doctorType.setAdapter(types);
        doctorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        selectedDoctor=  "Select type of doctor";
                        selectedDoctorPrice.setText("");
                        break;
                    case 1:
                        selectedDoctor=  "General physician";
                        selectedDoctorPrice.setText("UGX 30,000");
                        break;
                    case 2:
                        selectedDoctor =  "Specialist";
                        selectedDoctorPrice.setText("UGX 50,000");
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
                newPainLevel = painLevel.toString();
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
        }
    }

    public void sendCaseMail(){

        String mail = "info@hsvug.com";
        String message = "Full Name: "+fullName.getText().toString().trim()+
                "\n\nPhone Number: "+phoneNo.getText().toString().trim()+
                "\n\nConsult type: "+selectedDoctor+" @ "+selectedDoctorPrice+
                "\n\nPain Level: "+newPainLevel+
                "\n\nCase Description:\n"+ caseDescription.getText().toString().trim();
        String subject = fullName.getText().toString().trim()+"'s Doktor Consultation";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();

    }

    public void checkIfEmpty(){
//        String painLevelData = painLevel.getText().toString().trim();
        String caseDescriptionData = caseDescription.getText().toString().trim();
        String phoneNoData = phoneNo.getText().toString().trim();
        String fullNameData = fullName.getText().toString().trim();
        if (caseDescriptionData.isEmpty() || phoneNoData.isEmpty() || fullNameData.isEmpty()){
            Toast.makeText(this,"MISSING FIELD IS REQUIRED!",Toast.LENGTH_SHORT).show();
        }else {
            sendCaseMail();
            caseDescription.setText("");
            phoneNo.setText("");
            fullName.setText("");
            painLevel.setText("");
            capturedCaseImage.setImageResource(R.drawable.ic_image_black_24dp);
        }

    }

}
