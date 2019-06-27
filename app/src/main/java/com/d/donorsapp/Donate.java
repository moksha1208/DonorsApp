package com.d.donorsapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Donate extends AppCompatActivity {

    EditText quantity;
    String qty, msg, result;
    String phonenumber, donorname, address, password, latitude, longitude;
    Spinner ngos;
    String[] list;
    String[] ngoname, ngonumber;
    String number, nameofngo;
    int indexBody, indexAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        quantity = findViewById(R.id.quantity);
        ngos = findViewById(R.id.ngos);
        phonenumber = getIntent().getStringExtra("phonenumber");
        donorname = getIntent().getStringExtra("donorname");
        address = getIntent().getStringExtra("address");
        password = getIntent().getStringExtra("password");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        BackgroundWorker1 backgroundWorker3 = new BackgroundWorker1(this);
        backgroundWorker3.execute("sendmessage");
        try {
            result = backgroundWorker3.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list = result.split(":");
        for ( int k = 0 ; k < list.length; k++) {
            System.out.println(list[k]);
        }
        ngoname = new String[list.length/4];
        ngonumber = new String[list.length/4];
        for ( int i = 0 , j = 0; i < (list.length) ; i = i + 4, j = j + 1) {
            ngoname[j] = list[i];
            ngonumber[j] = list[i+2];

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ngoname);
        ngos.setAdapter(dataAdapter);

//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText, String messageNumber) {
//                if (messageNumber.equals(number) && (messageText.contains("Acknowledgement") || messageText.contains("Start Tracking"))) {
//                    Intent intent = new Intent(getApplicationContext(), PopActivity.class);
//                    intent.putExtra("indexaddress", messageNumber);
//                    intent.putExtra("indexbody", messageText);
//                    intent.putExtra("phonenumber", phonenumber);
//                    intent.putExtra("donorname", donorname);
//                    intent.putExtra("address", address);
//                    intent.putExtra("password", password);
//                    intent.putExtra("latitude", latitude);
//                    intent.putExtra("longitude", longitude);
//                    startActivity(intent);
//                }
//            }
//        });

    }

    public void sendmessage(View view) {
        qty = quantity.getText().toString();
        msg = "Donor " + donorname + " at " + address +" wants to donate food for " + qty + " people. Please accept.";
        String selectedngo = ngos.getSelectedItem().toString();
        for ( int m = 0 ; m < list.length; m++) {
            if(list[m].equals(selectedngo)) {
                nameofngo = list[m];
                System.out.println("name of ngo"+nameofngo);
                number = list[m+2];
                break;
            }
        }
//        System.out.println(list[2] + list[5] + list [8]);
        SmsManager sm = SmsManager.getDefault();
//        for (int i = 2; i< 9; i= i+3) {
        number ="+91" + number;
        System.out.println(number);
        ArrayList<String> parts =sm.divideMessage(msg);
        sm.sendMultipartTextMessage(number, null, parts, null, null);
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), DonorLocation.class);
        intent.putExtra("key", "1");
        intent.putExtra("ngonumber", number);
        intent.putExtra("ngoname", nameofngo);
        intent.putExtra("phonenumber", phonenumber);
        intent.putExtra("donorname", donorname);
        intent.putExtra("address", address);
        intent.putExtra("password", password);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
        this.finish();
//        sm.sendTextMessage(number, null, msg, null, null);
//            sm.sendTextMessage(number, null, msg, null, null);
//            System.out.println("Message sent");
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    refreshSmsInbox();
//                }
//            }
//        }).start();


    }

    public  void refreshSmsInbox() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        indexBody = cursor.getColumnIndex("body");
        indexAddress = cursor.getColumnIndex("address");
        if (indexBody < 0 || !cursor.moveToFirst()) return;
        do {
//                    System.out.println("in do");
//            System.out.println("index" + cursor.getString(indexAddress));
//                    System.out.println("no" + phonenumberlist[0]);
//            for (int i = 0; i < (ngonumber.length); i++) {
//                String a = "+91" + ngonumber[i];
//                System.out.println("noo" + a);
//                System.out.println("index" + cursor.getString(indexAddress));
                if (cursor.getString(indexAddress).equals(number) && ( cursor.getString(indexBody).contains("Acknowlegment") || cursor.getString(indexBody).contains("Start Tracking") )) {
                    Intent intent = new Intent(getApplicationContext(), PopActivity.class);
                    intent.putExtra("indexaddress", cursor.getString(indexAddress));
                    intent.putExtra("indexbody", cursor.getString(indexBody));
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("donorname", donorname);
                    intent.putExtra("address", address);
                    intent.putExtra("password", password);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
//                    String str = "SMS From: " + cursor.getString(indexAddress) + "\n" + cursor.getString(indexBody) + "\n";
//                    arrayAdapter.add(str);
                }
//            }
        } while (cursor.moveToNext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
