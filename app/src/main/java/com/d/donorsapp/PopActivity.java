package com.d.donorsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PopActivity extends AppCompatActivity {

    String indexAddress, indexBody, phonenumber, address, donorname, password, latitude, longitude, ngoname;
    TextView tv;
    Button okay, starttracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        tv = findViewById(R.id.tv);
        okay = findViewById(R.id.okay);
        starttracking = findViewById(R.id.starttracking);
        starttracking.setVisibility(View.INVISIBLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        ngoname = getIntent().getStringExtra("ngoname");
        indexAddress = getIntent().getStringExtra("indexaddress");
        indexBody = getIntent().getStringExtra("indexbody");
        phonenumber = getIntent().getStringExtra("phonenumber");
        donorname = getIntent().getStringExtra("donorname");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");

        tv.setText("SMS FRom " + indexAddress + "\n" + indexBody + "\n");
        if(indexBody.contains("Start Tracking")) {
            starttracking.setVisibility(View.VISIBLE);
            okay.setVisibility(View.INVISIBLE);
        }


    }

    public void okay(View view) {
        finish();
    }

    public void starttracking(View view) {
        Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
        intent.putExtra("key", "2");
        intent.putExtra("ngoname", ngoname);
        intent.putExtra("indexAddress", indexAddress);
        intent.putExtra("phonenumber", phonenumber);
        intent.putExtra("donorname", donorname);
        intent.putExtra("address", address);
        intent.putExtra("password", password);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("connection", "isConnected");
        startActivity(intent);
    }
}
