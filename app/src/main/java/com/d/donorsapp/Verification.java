package com.d.donorsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class Verification extends AppCompatActivity {

    String phonenumber, random;
    EditText ran;
    Random r = new Random();
    Button resend;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        phonenumber = getIntent().getStringExtra("phonenumber");
        random = getIntent().getStringExtra("random");
        ran = findViewById(R.id.random);
        resend = findViewById(R.id.resend);
        resend.setVisibility(View.INVISIBLE);
    }

    public void verify(View view) {
        getLocationPermission();
        if (random.equals(ran.getText().toString())) {
            System.out.println("given" + random);
            System.out.println("writtem" +ran.getText().toString());
            Intent intent = new Intent(getApplicationContext(), DonorDetails.class);
            intent.putExtra("phonenumber", phonenumber);
            startActivity(intent);
        }
        else {
            ran.setError("Wrong code");
            resend.setVisibility(View.VISIBLE);
        }
    }

    public void resend(View view) {
        int random = r.nextInt(1000000);
        String msg = String.valueOf(random);
        System.out.println("msg" + msg);
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phonenumber, null, msg, null, null);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                mLocationPermissionGranted = true;
//                System.out.println("Permission granted");
//                Log.e("Permission", "Gotpermission");
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
