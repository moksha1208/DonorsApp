package com.d.donorsapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {


    public static final String prefname = "MySaredPreferenceFile";
    EditText phoneno, pass;
    String phonenumber;
    String result, password;
    Button verify, login;
    Random r = new Random();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(this);
//        mAuth = FirebaseAuth.getInstance();
        SharedPreferences settings = getSharedPreferences(MainActivity.prefname,0);
        boolean hasloggedin = settings.getBoolean("hasloggedin",false);
        if(hasloggedin)
        {
            Intent intent = new Intent(getApplicationContext(),DonorLocation.class);
            intent.putExtra("key", "0");
            intent.putExtra("phonenumber", settings.getString("phonenumber", ""));
            intent.putExtra("donorname", settings.getString("donorname", ""));
            intent.putExtra("address", settings.getString("address", ""));
            intent.putExtra("password", settings.getString("password", ""));
            intent.putExtra("latitude", settings.getString("latitude", ""));
            intent.putExtra("longitude", settings.getString("longitude", ""));
            startActivity(intent);
            MainActivity.this.finish();

        }

        phoneno = findViewById(R.id.donor_phone_no);
        pass = findViewById(R.id.pass);
        verify = findViewById(R.id.verify);
        login = findViewById(R.id.login);
        pass.setVisibility(View.INVISIBLE);
        login.setVisibility(View.INVISIBLE);
        populateAutoComplete();
        phoneno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int p = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                if(p== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 666);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int p = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                if(p== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 666);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int p = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                if(p== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 666);
                }
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(),DonorLocation.class);
//            startActivity(intent);
//            MainActivity.this.finish();
//        }
//    }

    private void populateAutoComplete() {
        if (!mayRequestSendSMS()) {
            return;
        }
    }

    private boolean mayRequestSendSMS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 666);
        }
        return false;
    }

    public void verify(View view) {
        phonenumber = phoneno.getText().toString();
        getLocationPermission();
        if(phoneno.getText().length() < 10) {
            phoneno.setError("Minimum 10 characters");
        }
        if(phoneno.getText().length() == 10) {
            System.out.println("Phone "+phonenumber);
            BackgroundWorker1 backgroundWorker1 = new BackgroundWorker1(getApplicationContext());
            backgroundWorker1.execute("checkphonenumber", phonenumber);
            try {
                result = backgroundWorker1.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(result);
            if (result == null) {
                System.out.println("Not found");
                int random = r.nextInt(1000000);
                int p = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                if(p== PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 666);
                }
                String msg = String.format("%06d", random);
                System.out.println("msg" + msg);
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(phonenumber, null, msg, null, null);

                Intent intent = new Intent(getApplicationContext(), Verification.class);
                intent.putExtra("phonenumber", phonenumber);
                intent.putExtra("random", msg);
                startActivity(intent);

            } else {
                System.out.println("Found");
                pass.setVisibility(View.VISIBLE);
                verify.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
            }
        }
    }

    public void login(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 666);

        }
        password = pass.getText().toString();
        String[] donor_details = result.split(":");
        System.out.println(donor_details[3]);
        if(donor_details[3].equals(password)) {
            System.out.println("Done");
            SharedPreferences settings = getSharedPreferences(MainActivity.prefname, 0);
            SharedPreferences.Editor editor = settings.edit();
            //Set "hasloggedin" to true
            editor.putBoolean("hasloggedin", true);
            editor.putString("phonenumber", phonenumber);
            editor.putString("donorname", donor_details[1]);
            editor.putString("address", donor_details[2]);
            editor.putString("password", password);
            editor.putString("latitude", donor_details[4]);
            editor.putString("longitude", donor_details[5]);
            //Commit the edits
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
            intent.putExtra("key", "0");
            intent.putExtra("phonenumber", phonenumber);
            intent.putExtra("donorname", donor_details[1]);
            intent.putExtra("address", donor_details[2]);
            intent.putExtra("password", password);
            intent.putExtra("latitude", donor_details[4]);
            intent.putExtra("longitde", donor_details[5]);
            startActivity(intent);

        } else {
            pass.setError("Incorrect Password");
        }
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
